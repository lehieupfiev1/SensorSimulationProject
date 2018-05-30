/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import common.SensorUtility;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import iterface.frameMain;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import model.Curve;
import model.DoublePoint;
import model.IntersectionPoint;
import model.NodeItem;

/**
 *
 * @author dauto98
 */
public class MyAlgorithm4 {
    /**
     * the starting point to run the algorithm, called from UI class
     * @param sensorsThreshold: during the execution of the algorithm, if the number
     *                          of sensors left is lest than the threshold, then the algorithm terminate,
     *                          this is used to avoid the situation such that 1 set has only 1 independent
     *                          sensor and all the others are shared with other sets
     * @return a Map contains the result
     */
    public Map<String, Object> run(int sensorsThreshold) {
        Map<String, Object> data = readData();
        data.put("sensorsThreshold", sensorsThreshold);
        
        Set<NodeItem> test = new HashSet<>(SensorUtility.mListSensorNodes);
        System.out.println(test.size());
//        Arc2D.double test = new Arc2D.double(50, 50, 30, 30, (double)Math.PI/4, (double)Math.PI, Arc2D.OPEN);
//        ((Graphics2D)frameMain.coordinatePanel.getGraphics()).draw(test);
//        frameMain.coordinatePanel.refresh();

        return runAlgorithm(data);
//        System.out.println(SensorUtility.mListSensorNodes.size());
//        return null;
    }
    
    /**
     * Read data used in the algorithm from SensorUtility class
     * This function deep clone all the data to prevent accidental modification
     * @return a Map contains data
     */
    private Map<String, Object> readData() {
        Map<String,Object> data = new HashMap<>();
        data.put("sensorRadius", (double)SensorUtility.mRsValue);
        data.put("sensorLifeTime", SensorUtility.LifeTimeOfSensor);
        // since saved sensor list doesn't assign id for each sensor, so that the below function assign each sensor with
        // an id equals to its index in the array in order to distinguish sensors located at the same coordinate
        ArrayList<NodeItem> test = (new HashSet<>(SensorUtility.mListSensorNodes)).stream().collect(Collectors.toCollection(ArrayList::new));
        data.put("sensorList", IntStream.range(0, test.size()).mapToObj(i -> {
            NodeItem node = test.get(i);
            return new NodeItem(i, node.getX(), node.getY(), 2, 0, 0);
        }).collect(Collectors.toCollection(ArrayList::new)));
        data.put("UpLeftCornerPoint", new DoublePoint(0, 0));
        data.put("DownRightCornerPoint", new DoublePoint(SensorUtility.numberOfColumn - 1, SensorUtility.numberOfRow - 1));
        
        return data;
    }
    
    /**
     * run the algorithm
     * @param data: the map from string to Object which is data use in the algorithm
     * @return a map contains the result, or null if no sensor set can cover the area
     */
    private Map<String, Object> runAlgorithm(Map<String, Object> data) {
        ArrayList<ArrayList<NodeItem>> listOfSensorSets = getListOfSensorSet(data);
        
        if (listOfSensorSets == null ) {
            return null;
        } else {
            ArrayList<Double> listOfSensorSetsOnTime = getListOfOnTime(listOfSensorSets, (ArrayList<NodeItem>)data.get("sensorList"), (int)data.get("sensorLifeTime"));
            
            Map<String, Object> result = new HashMap<>();
            result.put("sensorSets", listOfSensorSets);
            result.put("onTime", listOfSensorSetsOnTime);
            
            return result;
        }
    }
    
    /**
     * Find the ArrayList, each element is the array containing the sensor that cover the area
     * @param data the map contains the data
     * @return List of array, each of them is the array containing the sensor covering the area
     */
    private ArrayList<ArrayList<NodeItem>> getListOfSensorSet(Map<String, Object> data) {
        // deep copy sensor list to ensure immutability
        ArrayList<NodeItem> sensorList = ((ArrayList<NodeItem>)data.get("sensorList")).stream().map(sensor -> new NodeItem(sensor)).collect(Collectors.toCollection(ArrayList::new));
        double sensorRadius = (double) data.get("sensorRadius");
        
        ArrayList<NodeItem> usedSensors = new ArrayList<>();
        ArrayList<ArrayList<NodeItem>> listOfSensorSets = new ArrayList<>();
        
        int minPossibleSensors = (int)data.get("sensorsThreshold");
        
        System.out.println("Start find set with min: " + minPossibleSensors);
        
        /**
         * run the algorithm until sensors list is empty (all sensors have been used)
         * terminate when there are minPossibleSensors/2 unused sensors left, this is used to reduce some sets that reused to many sensor from other set
         */
        while (sensorList.size() > minPossibleSensors) {
            // Initialize uncovered edges/arcs (4 edges of the rectangle)
            ArrayList<Curve> uncoveredCurve = new ArrayList<>();
            uncoveredCurve.add(new Curve((DoublePoint)data.get("DownRightCornerPoint"), new DoublePoint(0, SensorUtility.numberOfRow - 1), Curve.EdgeId.BOTTOM));
            uncoveredCurve.add(new Curve(new DoublePoint(0, SensorUtility.numberOfRow - 1), (DoublePoint)data.get("UpLeftCornerPoint"), Curve.EdgeId.LEFT));
            uncoveredCurve.add(new Curve((DoublePoint)data.get("UpLeftCornerPoint"), new DoublePoint(SensorUtility.numberOfColumn - 1, 0), Curve.EdgeId.TOP));
            uncoveredCurve.add(new Curve(new DoublePoint(SensorUtility.numberOfColumn - 1, 0), (DoublePoint)data.get("DownRightCornerPoint"), Curve.EdgeId.RIGHT));

            HashSet<NodeItem> currentContructingSensorSet = new HashSet<>();
            
            System.out.println("____Number of sensor left: " + sensorList.size() + "___________");
//            SensorUtility.mListSensorNodes.forEach(node -> node.setStatus(0));
//            frameMain.coordinatePanel.refresh();
            
            // run until all arcs is covered
            while (uncoveredCurve.size() > 0) {
                System.out.println(sensorList.size() + ", (" + currentContructingSensorSet.size() + "). Number of curves left: " + uncoveredCurve.size());
                // pick 1st curve, filter out all sensors that don't cover some segment of this curve
                ArrayList<DoublePoint> startPointArray = uncoveredCurve.stream().map(curve -> curve.getStartPoint()).collect(Collectors.toCollection(ArrayList::new));
                ArrayList<NodeItem> nearBySensors = filterByTheNumberOfPointsCovered(sensorList, startPointArray, sensorRadius);
                Optional optionalSensor = nearBySensors.stream().filter(sensor -> !currentContructingSensorSet.contains(sensor)).findAny();

                /**
                 * random sensor from set, if no unused sensor covers the 1st curve, then random from used sensors
                 * multiple sensor can be located at the same location, so that we use hashSet to construct new cover set, since using 2 sensor in 
                 * the same location has no meaning
                 * In the NodeItem hashCode and equals implementation, I only care about the coordinate and the type of node
                 * so that 2 node of the same type and same location, but different id, status will be consider the same one
                 */
                NodeItem chosenSensor;
                if (!optionalSensor.isPresent()) {
                    // get sensor from used sensor list
                    nearBySensors = filterByTheNumberOfPointsCovered(usedSensors, startPointArray, sensorRadius);
                    optionalSensor = nearBySensors.stream().filter(sensor -> !currentContructingSensorSet.contains(sensor)).findAny();
                    // if no sensor cover the next curve, it mean that the provided sensor set doesn't cover the area
                    if (!optionalSensor.isPresent()) {
                        SensorUtility.mListSensorNodes.forEach(node -> node.setStatus(0));
                        currentContructingSensorSet.forEach((node) -> SensorUtility.mListSensorNodes.get(SensorUtility.mListSensorNodes.indexOf(node)).setStatus(1));
                        frameMain.coordinatePanel.refresh();
                        return null;
                    } else {
                        chosenSensor = (NodeItem)optionalSensor.get();
                        usedSensors.remove(chosenSensor);
                        System.out.println("used sensor");   
                    }
                } else {
                    chosenSensor = (NodeItem)optionalSensor.get();
                    sensorList.remove(chosenSensor);
                }
                
                currentContructingSensorSet.add(chosenSensor);
                
//                SensorUtility.mListSensorNodes.get(SensorUtility.mListSensorNodes.indexOf(chosenSensor)).setStatus(1);
//                frameMain.coordinatePanel.refresh();
                
                // filter out curves which cannot intersect with the chosen sensor
                ArrayList<Curve> nearByCurves = getCurvesNearSensor(uncoveredCurve, chosenSensor, sensorRadius);
                
                HashMap<Curve, ArrayList<Curve>> curveArrayModification = getCurveModification(chosenSensor, nearByCurves, sensorRadius);
                
                updateCurveArray(uncoveredCurve, curveArrayModification);
//                // use for debugging
//                if (uncoveredCurve.size() <= 2 && uncoveredCurve.size() > 0) {
//                    SensorUtility.mListSensorNodes.forEach(node -> node.setStatus(0));
//                    currentContructingSensorSet.forEach((node) -> SensorUtility.mListSensorNodes.get(SensorUtility.mListSensorNodes.indexOf(node)).setStatus(1));
//                    frameMain.coordinatePanel.refresh();
//                }
                System.out.println();
            }
            listOfSensorSets.add(currentContructingSensorSet.stream().collect(Collectors.toCollection(ArrayList::new)));
            usedSensors.addAll(currentContructingSensorSet);
        }
        System.out.println("unused sensors: " + sensorList.size());
        return listOfSensorSets;
    }
    
    /**
     * Find the amount of time which each sensor set is turned on using linear programming
     * Let m be the number of sensor sets, then T[1], T[2],...., T[m] is the "on" time of 1st, 2nd, ..., m-th set
     * Let a[i][j] = 1 if sensor i belong to set j -> a[number of sensors][number of sets]
     * Find max: T[1] + T[2] + ... + T[m]
     * Constraint:
     * - T[1], T[2], ..., T[m] >= 0
     * - a[i][j]*T[j] < sensor time (sum of column < time) -> n constraint (n == number of sensors)
     * @param listOfSensorSets: the list of sensor set
     * @return the List of time correspond to the sensor set
     */
    private ArrayList<Double> getListOfOnTime(ArrayList<ArrayList<NodeItem>> listOfSensorSets, ArrayList<NodeItem> sensorList, int sensorLifeTime) {
        ArrayList<Double> listOfOnTime = new ArrayList<>();
        int numberOfSets = listOfSensorSets.size(); // matrix column
        int numberOfSensors = sensorList.size(); // matrix row
        
        // create matrix
        int a[][] = new int[numberOfSensors][numberOfSets];
        for (int i = 0; i < numberOfSets; i++) {
            ArrayList<NodeItem> set = listOfSensorSets.get(i);
            int length = set.size();
            for (int j = 0; j < length; j++) {
                a[set.get(j).getId()][i] = 1; // since the id is also the index of sensor in the original list
            }
        }
        
        try {
            //Define new model
            IloCplex cplex = new IloCplex();
            
            //Create m variable T correspond to m sets of sensor
            IloNumVar[] T = new IloNumVar[numberOfSets];
            for (int i = 0; i < numberOfSets; i++) {
                T[i] = cplex.numVar(0, Double.MAX_VALUE);
            }
            
            // create maximum expression: T[1] + T[2] + ... + T[m]
            IloLinearNumExpr objective = cplex.linearNumExpr();
            for (int i = 0; i < numberOfSets; i++) {
                objective.addTerm(1, T[i]);
            }
            //Define Objective
            cplex.addMaximize(objective);
            
            // Define constraints
            IloLinearNumExpr[] totalTimeOnExpr = new IloLinearNumExpr[numberOfSensors];
            for (int j = 0; j < numberOfSensors; j++) {
                totalTimeOnExpr[j] = cplex.linearNumExpr();
                
                for (int i = 0; i < numberOfSets; i++) {
                    totalTimeOnExpr[j].addTerm(a[j][i], T[i]);
                }
                cplex.addLe(totalTimeOnExpr[j], sensorLifeTime);
            }
            
            cplex.setParam(IloCplex.Param.Simplex.Display, 0);
            
            //Resolve Model
            if (cplex.solve()) {
                for (int i = 0; i < numberOfSets; i++) {
                    listOfOnTime.add(cplex.getValue(T[i]));
                }
                //System.out.println("value: " + cplex.getObjValue());
            } else {
                System.out.println("Problem not solved");
            }
            
            cplex.end();
        } catch (IloException e) {
            System.out.println("Error occur when using cplex");
        }
        
        return listOfOnTime;
    }
    
    /**
     * Find the sensors in the input list that the point lie within
     * @param sensorListL: The input sensor list to filter from
     * @param point: The point that the sensor must cover it
     * @param radius: Sensor radius
     * @return The array of sensor that cover the input point
     */
    private ArrayList<NodeItem> getNearBySensors(ArrayList<NodeItem> sensorList, DoublePoint point, double radius) {
         return sensorList.stream().filter(sensor -> calculateDistance(point, sensor.getCoordinate()) < radius).collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Find the sensors in the input list that cover the most consecutive point from the start of the input point array
     * @param sensorSet: the input sensor list to filter from
     * @param pointArray: The point array, used from the start
     * @param radius: sensor radius
     * @return The array of sensor that cover the most consecutive point from the start of the input point array
     */
    private ArrayList<NodeItem> filterByTheNumberOfPointsCovered(ArrayList<NodeItem> sensorSet, ArrayList<DoublePoint> pointArray, double radius) {
        ArrayList<NodeItem> nextSensorSets = sensorSet.stream().map(node -> new NodeItem(node)).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<NodeItem> result = new ArrayList<>();
        int i = 0;
        while (i < pointArray.size()) {
            nextSensorSets = getNearBySensors(nextSensorSets, pointArray.get(i), radius);
            if (!nextSensorSets.isEmpty()) {
                result = nextSensorSets;
            } else {
                break;
            }
            i++;
        }
        return result;
    }
    
    /**
     * this function is used with usedSensors, which is a hashSet
     * it convert set to arraylist then call the above function
     * @param usedSensors
     * @param pointArray
     * @param radius
     * @return 
     */
    private ArrayList<NodeItem> filterByTheNumberOfPointsCovered(Set<NodeItem> usedSensors, ArrayList<DoublePoint> pointArray, double radius) {
        ArrayList<NodeItem> usedSensorList = new ArrayList<>(usedSensors);
        return filterByTheNumberOfPointsCovered(usedSensorList, pointArray, radius);
    }
    
    /**
     * Find the curves/edges that MAY intersect with the sensor circle
     * The resulting curves/edges is NOT guarantee to intersect with the sensor circle
     * since the calculation is math-heavy and may duplicate with the "find the intersection" function
     * @param curveArray: the array of curves/edges to filter from
     * @param sensor: the sensor
     * @param radius: sensor radius
     * @return The array of curves/edges that MAY intersect with the sensor circle
     */
    private ArrayList<Curve> getCurvesNearSensor(ArrayList<Curve> curveArray, NodeItem sensor, double radius) {
        return curveArray.stream().filter(curve -> {
            DoublePoint curveCenter = curve.getCenter();
            if (curveCenter == null) {
                // since find out whether the line segment intersect with the sensor circle involve lots of math computation
                // and may duplicate when finding the intersection point later, I just accept it
                return true;
            } else {
                return calculateDistance(curveCenter, sensor.getCoordinate()) <= 2*radius;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Get random element in the input array
     * @param array: the array to get random element
     * @return The random element from the array
     */
    private NodeItem randomElement(ArrayList<NodeItem> array) {
        Random rand = new Random();
        return array.get(rand.nextInt(array.size()));
    }
    
    /**
     * Calculate distance between 2 point in the Oxy plane
     * @param point1
     * @param point2
     * @return The distance
     */
    private double calculateDistance(DoublePoint point1, DoublePoint point2) {
        return Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
    }
    
    /**
     * Calculate the Modification Map show how to update the uncoveredCurves array, nothing has been mutated yet
     * The Map has the following interface:
     * - Key: curve: the curve to replaced by the curves array value, if null mean append the array value to the end
     * - Value: ArrayList<Curve> all of this curve will be put in the place of the curve key
     * @param sensor: the sensor that cut the curves
     * @param nearByCurves: List of curves that may be cut the sensor circle
     * @param sensorRadius: sensor radius
     * @return The Modification map
     */
    private HashMap<Curve, ArrayList<Curve>> getCurveModification(NodeItem sensor, ArrayList<Curve> nearByCurves, double sensorRadius) {
        HashMap<Curve, ArrayList<Curve>> modification = new HashMap<>();
        ArrayList<IntersectionPoint> intersectionPointsArray = new ArrayList<>();
        
        nearByCurves.forEach(curve -> {
            if (curve.getCenter() == null) { // if curve is an edge
                ArrayList<DoublePoint> intersectionPoints = getIntersectionLineCircle(curve.getStartPoint(), curve.getEndPoint(), sensor.getCoordinate(), sensorRadius);
                ArrayList<Curve> newLine = new ArrayList<>();
                switch (intersectionPoints.size()) {
                    case 0: {
                        if (calculateDistance(curve.getStartPoint(), sensor.getCoordinate()) < sensorRadius) {
                            // the edge is covered entirely by the sensor, so remove it, null mean remove
                            modification.put(curve, null);
                        } // else, the edge is outside of the sensor, we do nothing
                        break;
                    }
                    case 1: {
                        // the circle cut the line segment at the start point
                        if (curve.getStartPoint().equals(intersectionPoints.get(0))) {
                            if (calculateDistance(curve.getEndPoint(), sensor.getCoordinate()) < sensorRadius) {
                                modification.put(curve, null);
                                // check start point (entry)
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                            } else {
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                            }
                            break;
                        }
                        // the circle cut the line segment at the end point
                        if (curve.getEndPoint().equals(intersectionPoints.get(0))) {
                            if (calculateDistance(curve.getStartPoint(), sensor.getCoordinate()) < sensorRadius) {
                                modification.put(curve, null);
                            }
                            break;
                        }
                        // the circle cut the line segment at the middle
                        newLine.clear();
                        // find out what part of the line is covered
                        double startPointDistance = calculateDistance(curve.getStartPoint(), sensor.getCoordinate());
                        double endPointDistance = calculateDistance(curve.getEndPoint(), sensor.getCoordinate());
                        
                        if (startPointDistance > sensorRadius && endPointDistance < sensorRadius) {
                            newLine.add(new Curve(curve.getStartPoint(), intersectionPoints.get(0), curve.getEdgeId()));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                        }
                        if (startPointDistance < sensorRadius && endPointDistance > sensorRadius) {
                            newLine.add(new Curve(intersectionPoints.get(0), curve.getEndPoint(), curve.getEdgeId()));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                        }
                        if (startPointDistance > sensorRadius && endPointDistance > sensorRadius) {
                            newLine.add(new Curve(curve.getStartPoint(), intersectionPoints.get(0), curve.getEdgeId()));
                            newLine.add(new Curve(intersectionPoints.get(0), curve.getEndPoint(), curve.getEdgeId()));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                        }
                        modification.put(curve, newLine);
                        break;
                    }
                    case 2: {
                        newLine.clear();
                        double startPointDistance = calculateDistance(curve.getStartPoint(), sensor.getCoordinate());
                        double endPointDistance = calculateDistance(curve.getEndPoint(), sensor.getCoordinate());
                        
                        if ((new Double(startPointDistance)).equals(sensorRadius)) {
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                            if ((new Double(endPointDistance)).equals(sensorRadius)) {
                                modification.put(curve, null);
                            } else if (endPointDistance > sensorRadius) {
                                newLine.add(new Curve(intersectionPoints.get(1), curve.getEndPoint(), curve.getEdgeId()));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(1), "exit"));
                            }
                        } else {
                            newLine.add(new Curve(curve.getStartPoint(), intersectionPoints.get(0), curve.getEdgeId()));
                            if ((new Double(endPointDistance)).equals(sensorRadius)) {
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                            } else {
                                newLine.add(new Curve(intersectionPoints.get(1), curve.getEndPoint(), curve.getEdgeId()));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(1), "exit"));
                            }
                        }
                        modification.put(curve, newLine);
                        break;
                    }
                }
            } else { // if curve is a curve
                ArrayList<DoublePoint> intersectionPoints = getIntersectionArcCircle(curve, sensor.getCoordinate(), sensorRadius);
                ArrayList<Curve> newCurve = new ArrayList<>();
                switch (intersectionPoints.size()) {
                    case 0: {
                        if (calculateDistance(curve.getStartPoint(), sensor.getCoordinate()) < sensorRadius) {
                            // the curve is covered entirely by the sensor, so remove it
                            modification.put(curve, null);
//                            System.out.println("___--intersect: 0, in--___");
                        } // else, the curve is outside of the sensor
//                        System.out.println("___--intersect: 0, out--___");
                        break;
                    }
                    case 1: {
                        // the circle cut the curve at the start point
                        if (curve.getStartPoint().equals(intersectionPoints.get(0))) {
                            if (calculateDistance(curve.getEndPoint(), sensor.getCoordinate()) < sensorRadius) {
                                modification.put(curve, null);
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
//                                System.out.println("___--intersect: 1, in--___");
                            } else {
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
//                                System.out.println("___--intersect: 1, in--___");
                            }
                            break;
                        }
                        // the circle cut the curve at the end point
                        if (curve.getEndPoint().equals(intersectionPoints.get(0))) {
                            if (calculateDistance(curve.getStartPoint(), sensor.getCoordinate()) < sensorRadius) {
                                modification.put(curve, null);
                            }
                            break;
                        }
                        // the circle cut the line segment at the middle
                        newCurve.clear();
                        // find out what part of the line is covered
                        double startPointDistance = calculateDistance(curve.getStartPoint(), sensor.getCoordinate());
                        double endPointDistance = calculateDistance(curve.getEndPoint(), sensor.getCoordinate());
                        
                        if (startPointDistance > sensorRadius && endPointDistance < sensorRadius) {
                            newCurve.add(new Curve(curve.getStartPoint(), intersectionPoints.get(0), curve.getCenter()));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                        }
                        if (startPointDistance < sensorRadius && endPointDistance > sensorRadius) {
                            newCurve.add(new Curve(intersectionPoints.get(0), curve.getEndPoint(), curve.getCenter()));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                        }
                        if (startPointDistance > sensorRadius && endPointDistance > sensorRadius) {
                            newCurve.add(new Curve(curve.getStartPoint(), intersectionPoints.get(0), curve.getCenter()));
                            newCurve.add(new Curve(intersectionPoints.get(0), curve.getEndPoint(), curve.getCenter()));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                        }
                        modification.put(curve, newCurve);
                        break;
                    }
                    case 2: {
                        newCurve.clear();
                        double startPointDistance = calculateDistance(curve.getStartPoint(), sensor.getCoordinate());
                        double endPointDistance = calculateDistance(curve.getEndPoint(), sensor.getCoordinate());
                        
                        if ((new Double(startPointDistance)).equals(sensorRadius)) {
                            if ((new Double(endPointDistance)).equals(sensorRadius)) {
                                modification.put(curve, null);
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                            } else if (endPointDistance > sensorRadius) {
                                newCurve.add(new Curve(intersectionPoints.get(1), curve.getEndPoint(), curve.getCenter()));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(1), "exit"));
                            } else {
                                newCurve.add(new Curve(curve.getStartPoint(), intersectionPoints.get(1), curve.getCenter()));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(1), "entry"));
                            }
                        } else if (startPointDistance < sensorRadius) {
                            newCurve.add(new Curve(intersectionPoints.get(0), intersectionPoints.get(1), curve.getCenter()));
                            if ((new Double(endPointDistance)).equals(sensorRadius)) {
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                            } else {
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(1), "entry"));
                            }
                        } else {
                            newCurve.add(new Curve(curve.getStartPoint(), intersectionPoints.get(0), curve.getCenter()));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                            if (endPointDistance > sensorRadius) {
                                newCurve.add(new Curve(intersectionPoints.get(1), curve.getEndPoint(), curve.getCenter()));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(1), "exit"));
                            }
                        }
                        modification.put(curve, newCurve);
                        break;
                    }
                }
            }
        });
        
        /**
         * compute the curves that lie on the sensor circle
         * 1. Sort all point in clockwise order, keep the starting point at the start
         * 2. Traverse through the array, connect each consecutive "entry", "exit" pair, from "exit" to "entry"
         * 3. Put all of curves into the map with key == null to indicate that append all of them to the end of the array
         */
        if (!intersectionPointsArray.isEmpty()) {
            sortPointClockWise(intersectionPointsArray, sensor.getCoordinate());
            ArrayList<Curve> curvesOnSensorCircle = getCurveOnSensorCircle(intersectionPointsArray, sensor.getCoordinate());
            modification.put(null, curvesOnSensorCircle);   
        }
        
        return modification;
    }
    
    /**
     * Apply the modification map to the original array
     * @param curveArray: the curve array that need to mutate
     * @param curveArrayModification: the modification map
     */
    private void updateCurveArray(ArrayList<Curve> curveArray, HashMap<Curve, ArrayList<Curve>> curveArrayModification) {
        curveArrayModification.entrySet().forEach(entry -> {
            if (entry.getKey() == null) {
                curveArray.addAll(entry.getValue());
            } else {
                Curve oldCurve = entry.getKey();
//                System.out.println("key: " + entry.getKey());
//                System.out.println("value: " + entry.getValue());
                int oldCurveIndex = curveArray.indexOf(oldCurve);
//                System.out.println("old curve index: " + oldCurveIndex);
                if (oldCurveIndex != -1) {
                    curveArray.remove(oldCurveIndex);
                    if (entry.getValue() != null) {
                        curveArray.addAll(oldCurveIndex, entry.getValue());
                    }
                }
            }
        });
    }
    
    /**
     * Calculate the intersection point of the line segment and the sensor circle using the method explained here:
     * https://math.stackexchange.com/questions/311921/get-location-of-vector-circle-intersection
     * All variable names are the same as those in the link
     * the result is sorted from the start to end point direction
     * @param startPoint: The start point of the line
     * @param endPoint: The end point of the line
     * @param center: The center of the circle
     * @param radius: The circle radius
     * @return The arrayList contains the intersection points
     */
    private ArrayList<DoublePoint> getIntersectionLineCircle(DoublePoint startPoint, DoublePoint endPoint, DoublePoint center, double radius) {
        double h = center.getX();
        double k = center.getY();
        double x0 = startPoint.getX();
        double y0 = startPoint.getY();
        double x1 = endPoint.getX();
        double y1 = endPoint.getY();
        
        double a = (x1 - x0)*(x1 - x0) + (y1 - y0)*(y1 - y0);
        double b = 2*(x1 - x0)*(x0 - h) + 2*(y1 - y0)*(y0 - k);
        double c = (x0 - h)*(x0 - h) + (y0 - k)*(y0 - k) - radius*radius;
        
        double d = b*b - 4*a*c;
        if (d < 0) {
            return new ArrayList<>();
        }
        if (d == 0) {
            double t = -b/(2*a);
            if (0 <= t && t <= 1) {
                DoublePoint point = new DoublePoint(x0 + (x1 - x0)*t, y0 + (y1 - y0)*t);
                return Stream.of(point).collect(Collectors.toCollection(ArrayList::new));
            }
        }
        // d > 0
        double delta = (double)Math.sqrt(d);
        
        double t1 = (-b - delta)/(2*a);
        double t2 = (-b + delta)/(2*a);
        
        ArrayList<DoublePoint> result = new ArrayList<>();
        if (0 <= t1 && t1 <= 1) {
            result.add(new DoublePoint(x0 + (x1 - x0)*t1, y0 + (y1 - y0)*t1));
        }
        if (0 <= t2 && t2 <= 1) {
            result.add(new DoublePoint(x0 + (x1 - x0)*t2, y0 + (y1 - y0)*t2));
        }
        return result;
    }
    
    /**
     * Calculate the intersection points between the circle and the curve by find the intersection between 2 circle
     * and then filter out point that lie outside the curve
     * @param curve: the curve to the cut
     * @param center: the circle center
     * @param radius: the radius of the circle
     * @return Array list of intersectionPoint
     */
    private ArrayList<DoublePoint> getIntersectionArcCircle(Curve curve, DoublePoint center, double radius) {
        ArrayList<DoublePoint> intersectionPoints = new ArrayList<>();

        // check if the 2 circle don't intersect
        if (calculateDistance(curve.getCenter(), center) > 2*radius) {
            return new ArrayList<>();
        } else if (calculateDistance(curve.getCenter(), center) == 2*radius) {
            /**
             * This function separately handle this case because calculate this case using normal math (below)
             * will come to delta == 0, but due to the double precision limitation, the delta is hard to be 0
             * (usually < 10^(-9) in value, but never be exact zero) >
             * **************
             * The intersection point is the midpoint of the line segment with 2 center point at 2 end
             */
            double x1 = curve.getCenter().getX();
            double y1 = curve.getCenter().getY();
            double x2 = center.getX();
            double y2 = center.getY();
            
            intersectionPoints.add(new DoublePoint((x1 + x2)/2, (y1 + y2)/2));
        } else {
            /**
             * distance < 2*radius
             */
            //2(x2-x1)*X + 2(y2-y1)*Y = x2^2 - x1^2  + y2^2 - y1^2
            //(X-x1)^2+ (Y-y1)^2 = R^2
            double x1 = curve.getCenter().getX();
            double y1 = curve.getCenter().getY();
            double x2 = center.getX();
            double y2 = center.getY();
            
            /**
             * y2 = y1
             * X = [x2^2 - x1^2]/[2(x2 - x1)]
             * Y = +- sqrt[R^2 - (X - x1)^2] + y1
             */
            if (y2 == y1) {
                double X = (x2*x2 - x1*x1)/(2*(x2 - x1));
                double sqrt = Math.sqrt(radius*radius - (X - x1)*(X - x1));
                double Y1 = sqrt + y1;
                double Y2 = -sqrt + y2;
                if (new Double(Y1).equals(Y2)) {
                    intersectionPoints.add(new DoublePoint(X, Y1));
                } else {
                    intersectionPoints.add(new DoublePoint(X, Y1));
                    intersectionPoints.add(new DoublePoint(X, Y2));
                }
            } else {
                /**
                 * Y = [(x2^2 - x1^2 + y2^2 - y1^2)/2(y2 - y1)] - [(x2 - x1)/(y2 - y1)]*X = a - bX
                 * (X - x1)^2 + (bX - a + y1)^2 = R^2
                 * (1 + b^2)*X^2 + 2[-x1 - b(a - y1)]*X + x1^2 + (a - y1)^2 - R^2 = 0
                 * c*X^2 + 2d*X + e = 0
                 * delta = d^2 - ce
                 * X = [-d +- sqrt(delta)]/c
                 */
                double a = (x2*x2 - x1*x1 + y2*y2 - y1*y1)/(2*(y2 - y1));
                double b = (x2 - x1)/(y2 - y1);
                double c = 1 + b*b;
                double d = -x1 - b*(a - y1);
                double e = x1*x1 + (a - y1)*(a - y1) - radius*radius;
                
                double delta = d*d - c*e;
                
                double X1 = (-d + (double)Math.sqrt(delta))/c;
                double Y1 = a - b*X1;
                
                double X2 = (-d - (double)Math.sqrt(delta))/c;
                double Y2 = a - b*X2;
                
                if (new Double(Y1).equals(Y2) && new Double(X1).equals(X2)) {
                    intersectionPoints.add(new DoublePoint(X1, Y1));
                } else {
                    intersectionPoints.add(new DoublePoint(X1, Y1));
                    intersectionPoints.add(new DoublePoint(X2, Y2));
                }
            }
        }
        
        // remove the point lie outside the curve
        intersectionPoints.add(0, curve.getStartPoint());
        intersectionPoints.add(curve.getEndPoint());
        intersectionPoints = sortPointCounterClockWise(intersectionPoints, curve.getCenter());
        return intersectionPoints.subList(intersectionPoints.indexOf(curve.getStartPoint()) + 1, intersectionPoints.indexOf(curve.getEndPoint())).stream().collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Sort the point in the input array in counter clockwise order relative to the center point
     * The y axis is pointing down, so the result is being reserve compare to normal sense
     * @param pointArray: array to be sorted
     * @param center: center point
     * @return sorted array
     */
    private ArrayList<DoublePoint> sortPointCounterClockWise(ArrayList<DoublePoint> pointArray, DoublePoint center) {
        DoublePoint firstItem = pointArray.get(0);
        pointArray.sort((p1, p2) -> Double.compare(Math.atan2(p2.getY() - center.getY(), p2.getX() - center.getX()), Math.atan2(p1.getY() - center.getY(), p1.getX() - center.getX())));
        for (int i = 0, end = pointArray.indexOf(firstItem); i < end; i++) {
            pointArray.add(pointArray.get(i));
        }
        pointArray.subList(0, pointArray.indexOf(firstItem)).clear();
        return pointArray;
    }
    
    /**
     * Sort the point in the input array in clockwise order relative to the center point
     * @param pointArray: array to be sorted
     * @param center: center point
     * @return sorted array
     */
    private ArrayList<IntersectionPoint> sortPointClockWise(ArrayList<IntersectionPoint> pointArray, DoublePoint center) {
        IntersectionPoint firstItem = pointArray.get(0);
        pointArray.sort((p1, p2) -> Double.compare(Math.atan2(p1.getCoordinate().getY() - center.getY(), p1.getCoordinate().getX() - center.getX()), Math.atan2(p2.getCoordinate().getY() - center.getY(), p2.getCoordinate().getX() - center.getX())));
        for (int i = 0, end = pointArray.indexOf(firstItem); i < end; i++) {
            pointArray.add(pointArray.get(i));
        }
        pointArray.subList(0, pointArray.indexOf(firstItem)).clear();
        return pointArray;
    }
    
    /**
     * Calculate new curve that lie on the sensor circle
     * 2 point of the new curve is: last "exit" point in a row and first "entry" point in a row, the direction is from "entry" to "exit"
     * @param intersectionPointsArray: list of intersection point
     * @param center: sensor coordinate
     * @return arraylist of new curves
     */
    private ArrayList<Curve> getCurveOnSensorCircle(ArrayList<IntersectionPoint> intersectionPointsArray, DoublePoint center) {
        DoublePoint pendingPoint = null;
        ArrayList<Curve> newCurve = new ArrayList<>();
        for (int i = 0, length = intersectionPointsArray.size(); i < length; i++) {
            String direction = intersectionPointsArray.get(i).getDirection();
            if ("entry".equals(direction) && pendingPoint != null) {
                newCurve.add(new Curve(intersectionPointsArray.get(i).getCoordinate(), pendingPoint, center));
                pendingPoint = null;
            } else if ("exit".equals(direction)) {
                pendingPoint = intersectionPointsArray.get(i).getCoordinate();
            }
        }
        return newCurve;
    }
        
    public static void main(String[] args) {
        
    }
}

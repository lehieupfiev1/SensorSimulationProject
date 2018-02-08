/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import common.SensorUtility;
import static common.SensorUtility.mListSensorNodes;
import static common.SensorUtility.mListSinkNodes;
import static common.SensorUtility.mListTargetNodes;
import static common.SensorUtility.mListofListPath;
import static common.SensorUtility.mListofListSensor;
import static common.SensorUtility.mListofListTime;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import static iterface.frameMain.coordinatePanel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FloatPointItem;
import model.HeuristicItem;
import model.NodeItem;
import model.PathItem;

/**
 *
 * @author sev_user
 */
public class MyAlgorithm3 {
    
    public float Distance[][];// Matrix distance between two nodes
    public float MinDistanceSink[];// Matrix distance between two nodes
    public float Target[][];// Target nodes
    public float Point[][];// Total nodes
    public float Sink[][];// Target covering sensors
    
    double mTimeLife;
    float Rs, Rc;// Rs and Rt value
    float R; // Tinh phan chia nho nhat
    int MaxHopper;
    List<Integer> EECCcnt;   
    List<List<Integer>> NDEECCcnt;
    List<Double> listTime;
    List<HeuristicItem> Cov_Heuristic;
    List<HeuristicItem> Connect_Heuristic;
    List<Integer> ListNcs ;// Tap list sensing node
    List<Integer> CurrentHopper;
    List<Integer> ListNcr ; // Tap list relaying node
    
//    List<List<Integer>> resultListX;
//    List<Double> resultListT;
    List<List<PathItem>> resultListY;
    List<List<Double>> resultListTi;
    float ListEnergySensor[];
    int MAX_INTERGER = 100000000;
    float MAX_FLOAT = 10000000000000.0f;
    float TimeStamp ;
    
    float Es, Et,Er,Efs,Emp,Do, bit;
    int cnt;
    
    int K;// Number Sink
    int N;//Number sensor
    int TP; // Total points (Contain Sensor , Sink, Target )
    int T;//Number of Tagert Nodes
    int Anpha; // So lan tang
    
    public MyAlgorithm3() {
    }
    
    public void run() {
        
        init();

        readData();
        //Step 1: Find target-covering Sensor
        //FindTargetCoveringSensor();
        
        //Step 2: 

        runAlgorithm();
        
        CoppyToListSensor();
//        
//        freeData();
//        
//        System.gc();
    }
    
    public void init() {
        resultListY = new ArrayList<>();
        resultListTi = new ArrayList<>();
    }
    
    public  void readData() {
        // Read Rs, Rc
        Rs = SensorUtility.mRsValue;
        Rc = SensorUtility.mRcValue;
        mTimeLife = 0;
        MaxHopper = SensorUtility.mMaxHopper;
        R = Rs + MaxHopper*Rc;
        
        //Read constance Energy : Es, Et,Er,Efs,Emp
        Es = SensorUtility.mEsValue;
        Et = SensorUtility.mEtValue;
        Er = SensorUtility.mErValue;
        Efs = SensorUtility.mEfsValue;
        Emp = SensorUtility.mEmpValue;
        Do = (float)Math.sqrt(Efs/Emp);
        bit = SensorUtility.mBitValue;
        TimeStamp = SensorUtility.mTstamp;
        Anpha = SensorUtility.Lvalue;
                
        //Read Sensor , Sink, Target 
        N = SensorUtility.mListSensorNodes.size();
        T = SensorUtility.mListTargetNodes.size();
        K = SensorUtility.mListSinkNodes.size();
        TP = N+ T+ K;
        
        //Add to Total Point;
        Point = new float[TP+1][2];
        ListEnergySensor = new float[N];

        for (int i =0; i < mListSensorNodes.size();i++) {
            Point[i][0] = mListSensorNodes.get(i).getX();
            Point[i][1] = mListSensorNodes.get(i).getY();
            //Add Energy for every node
            ListEnergySensor[i] = SensorUtility.mEoValue;
        }
        
        for (int i =0; i < mListTargetNodes.size();i++) {
            Point[N+i][0] = mListTargetNodes.get(i).getX();
            Point[N+i][1] = mListTargetNodes.get(i).getY();
        }
        for (int i =0; i < mListSinkNodes.size();i++) {
            Point[N+T+i][0] = mListSinkNodes.get(i).getX();
            Point[N+T+i][1] = mListSinkNodes.get(i).getY();
        }

        // Create matrix distance
        Distance = new float[TP+1][TP+1];
         for (int i =0;i<TP;i++) {
            for (int j =0;j<=i;j++) {
                if (i==j ) {
                    Distance[i][j] = 0;
                } else {
                    Distance[i][j] = Distance[j][i] = calculateDistance(Point[i][0], Point[i][1], Point[j][0], Point[j][1]);
                }
            }
        }
         
        //Caculate Mindistance form sensor to Sink
        MinDistanceSink = new float[N];
        float min;
        for (int i =0; i<N ;i++) {
            min = MAX_FLOAT;
            for (int j =0; j < K; j++) {
                if (Distance[i][N+T+j] < min) {
                    min = Distance[i][N+T+j];
                }
            }
            MinDistanceSink[i] = min;
        }
        
        //Init resultListY and resultListTi 
        
        for (int i = 0 ;i < T; i++) {
            List<PathItem> pathY = new ArrayList<>();
            List<Double> timeY = new ArrayList<>();
            resultListY.add(pathY);
            resultListTi.add(timeY);
        }

    }
    
    
    public  float calculateDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    
    boolean checkSensorConnectSink(int sensor, List<Integer> listSink) {
        for (int i =0; i< listSink.size();i++) {
            if (Distance[sensor][N+T+listSink.get(i)] <= Rc) {
                return true;
            }
        }
        return false;
    }
    void Finding_CCP(List<Integer> listSensor, List<Integer> listTarget, List<Integer> listSink, List<List<List<Integer>>> ListPi) {
        ListPi.clear();
        //Khoi tao danh sach Pi
        for(int i = 0; i< listTarget.size();i++) {
            List<List<Integer>> Pi = new ArrayList<>();
            ListPi.add(Pi);
        }
        List<List<Integer>> ListP = new ArrayList<>();
        //
        for(int i =0; i< listSensor.size();i++) {
            if (checkSensorConnectSink(listSensor.get(i), listSink)) {
                for (int j = 0; j< listTarget.size(); j++) {
                    if (Distance[listSensor.get(i)][N+listTarget.get(j)] <= Rs) {
                        List<Integer> list1 = new ArrayList<>();
                        list1.add(listSensor.get(i));
                        ListPi.get(j).add(list1);
                    }  
                }
                List<Integer> list = new ArrayList<>();
                list.add(listSensor.get(i));
                ListP.add(list);
                
            }
        }
        
        while (!ListP.isEmpty()) {
            List<Integer> headP = ListP.get(0);
            int lastSensor = headP.get(headP.size()-1); // Lay phan tu cuoi cung cua head
            
            for (int i = 0 ; i < listTarget.size(); i++) {
                if (Distance[lastSensor][N+listTarget.get(i)] <= Rs) {
                    ListPi.get(i).add(headP);
                }
            }
            
            if (headP.size() == MaxHopper+1) {
                ListP.remove(0);
            } else {
                for (int i = 0 ; i < listSensor.size(); i++) {
                    if (lastSensor != listSensor.get(i) && Distance[lastSensor][listSensor.get(i)] <= Rc) {
                        
                        if (!checkPointExitInList(listSensor.get(i), headP)) {
                            // Coppy to new Array
                            List<Integer> list = new ArrayList<>();
                            for (int j = 0 ; j< headP.size();j++) {
                                list.add(headP.get(j));
                            }
                            list.add(listSensor.get(i));
                            
                            //Add list to P
                            ListP.add(list);
                        }

                    }
                    
                }
                ListP.remove(0);
                
            }
 
        }
        //Dao chieu cua List
        
    }
    
    void Finding_CCP2(List<Integer> listSensor, List<Integer> listTarget, List<Integer> listSink, List<List<PathItem>> ListPathY) {
        List<List<List<Integer>>> ListPi = new ArrayList<>();
        List<List<Integer>> ListP = new ArrayList<>();
        List<List<Integer>> ListParent = new ArrayList<>();
        
        //Check nulll
        if (listSensor.isEmpty() || listTarget.isEmpty() || listSink.isEmpty()) {
            return;
        }
        //Khoi tao danh sach Pi
        for (int k = 0; k < listTarget.size(); k++) {
            List<List<Integer>> Pi = new ArrayList<>();
            int target = listTarget.get(k);

            ListP.clear();
            ListParent.clear();
            //
            List<Integer> listParent1 = new ArrayList<>();
            int num =0;
            for (int i = 0; i < listSensor.size(); i++) {
                if (checkSensorConnectSink(listSensor.get(i), listSink)) {
                    List<Integer> list = new ArrayList<>();
                    list.add(listSensor.get(i));
                    listParent1.add(listSensor.get(i));
                    num++;
                    if (Distance[listSensor.get(i)][N + target] <= Rs) {
                       Pi.add(list);
                    } else {
                       ListP.add(list);
                       
                    }

                }
            }
            for (int j = 0;j< num;j++) {
               ListParent.add(listParent1);
            }
            

            while (!ListP.isEmpty()) {
                List<Integer> headP = ListP.get(0);
                List<Integer> headParent = ListParent.get(0);
                int lastSensor = headP.get(headP.size() - 1); // Lay phan tu cuoi cung cua head

                if (Distance[lastSensor][N + target] <= Rs) {
                    Pi.add(headP);
                    ListP.remove(0);
                    ListParent.remove(0);
                    continue;
                }

                if (headP.size() == MaxHopper) {
                    ListP.remove(0);
                    ListParent.remove(0);
                } else {
                    List<Integer> listParent = new ArrayList<>();
                    for (int j = 0; j < headParent.size(); j++) {
                        listParent.add(headParent.get(j));
                    }
                    int count =0;
                    for (int i = 0; i < listSensor.size(); i++) {
                        if (lastSensor != listSensor.get(i) && Distance[lastSensor][listSensor.get(i)] <= Rc) {

                            if (!checkPointExitInList(listSensor.get(i), headParent)) {
                                // Coppy to new Array
                                List<Integer> list = new ArrayList<>();
                                for (int j = 0; j < headP.size(); j++) {
                                    list.add(headP.get(j));
                                }
                                list.add(listSensor.get(i));
                                listParent.add(listSensor.get(i));
                                count++;

                                //Add list to P
                                ListP.add(list);
                            }

                        }

                    }
                    for (int j = 0;j< count;j++) {
                       ListParent.add(listParent);
                    }
                    ListP.remove(0);
                    ListParent.remove(0);

                }

            }
            
            ListPi.add(Pi);
        }
        
        //Dao nguoc ListPath
        System.gc();
        ListPathY.clear();
        for (int i =0 ; i< ListPi.size(); i++) {
            List<List<Integer>> Pi = ListPi.get(i);
            List<PathItem> listPath = new ArrayList<>();
            for (int j = 0 ; j< Pi.size(); j++) {
                List<Integer> p = Pi.get(j);
                PathItem path = new PathItem();
                for (int k = p.size()-1; k >= 0 ; k--) {
                    path.addElement(p.get(k));
                }
                listPath.add(path);
            }
            ListPathY.add(listPath);
        }
        
    }
    
    void SinkToHop(List<List<List<Integer>>> lis, int pos , int N, int a[] , List<List<Integer>> Lists) {
        if (pos == N) {
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i<N ;i++){
                result.add(a[i]);
            }
            Lists.add(result);
            
        } else {
            List<List<Integer>> list1 = lis.get(pos);
            for (int i = 0; i< list1.size();i++) {
                a[pos] = i;
                pos++;
                SinkToHop(lis,pos,N,a,Lists);
                pos--;
            }
            
        }
    }
    
    
    boolean checkPointExitInList(int point , List<Integer> listPoint ) {
        for (int i = 0 ; i < listPoint.size(); i++) {
            if (point == listPoint.get(i)) return true;
        }
        return false;
    }
    
    public void FindingPathX(List<Integer> listSensor, List<Integer> listTarget, List<Integer> listSink,List<List<List<Integer>>> listPathX, List<List<Integer>> listX) {
        if (listSensor.isEmpty() || listTarget.isEmpty() || listSink.isEmpty()) {
            return;
        }
        List<List<List<Integer>>> ListPathY = new ArrayList<>();
        //Finding_CCP2(listSensor, listTarget, listSink, ListPathY);
        
        int re =0;
        for (int i =0; i < ListPathY.size();i++) {
            re += ListPathY.get(i).size();
        }
        
        //Sink to hop
        List<List<Integer>> listCombi = new ArrayList<>();
        int a[] = new int[listTarget.size()];
        SinkToHop(ListPathY, 0, listTarget.size(), a, listCombi);
        
        //Caculate result ListPathX;
        for (int i = 0 ; i < listCombi.size();i++) {
            List<Integer> combin = listCombi.get(i);
            List<List<Integer>> pathX = new ArrayList<>();
            for (int j =0 ; j< combin.size();j++) {
                pathX.add(ListPathY.get(j).get(combin.get(j)));
            }
            listPathX.add(pathX);
        }
        
        //Caculate Listx;
        for (int i =0 ; i < listPathX.size();i++) {
            List<List<Integer>> pathX = listPathX.get(i);
            List<Integer> Xi= new ArrayList<>();
            
            for (int j  = 0 ; j< pathX.size(); j++) {
                List<Integer> path = pathX.get(j);
                if (j == 0) {
                    for (int k = 0; k < path.size();k++)
                        Xi.add(path.get(k));
                } else {
                    for (int k = 0; k < path.size();k++)
                        if (!checkPointExitInList(path.get(k), Xi)) {
                            Xi.add(path.get(k));
                        }
                    
                }
                
                
            }
            listX.add(Xi);
            
        }
        int s =8;
        
        
    }
    
       
    public List<Double> LinearProAlgorithm(List<List<List<Integer>>> listPathX, List<List<Integer>> listX, List<Integer> listSenSor, double valueE0) {
        List<Double> time = new ArrayList<>();
        int m = listX.size();
        int n = listSenSor.size();
        //Test
//    if (m == 1) {
//        time.add(valueE0/1000);
//        for (int i = 1; i < listX.size(); i++) {
//            time.add(0.0);
//        }
//    } else if (m == 2) {
//        time.add(valueE0/1000);
//        time.add(valueE0/1000);
//        for (int i = 2; i < listX.size(); i++) {
//            time.add(0.0);
//        }
//    } else if (m > 2){
//        time.add(valueE0/1000);
//        time.add(valueE0/1000);
//        time.add(valueE0/1000);
//        
//        for (int i = 3; i < listX.size(); i++) {
//            time.add(0.0);
//        }
//    }
    if (m == 0 || n == 0) {
        return time;
    }
        
        float [][] a = new float[n][m];

        //Check Input
        for (int i = 0; i < n; i++) {
            int sensor = listSenSor.get(i);
            for (int j = 0; j < m; j++) {
                a[i][j] = 0;
                List<Integer> Xj = listX.get(j);
                List<List<Integer>> pathXj = listPathX.get(j);
                for (int k = 0; k < Xj.size(); k++) {
                    if (sensor == Xj.get(k)) {
                        a[i][j] = CaculateEnergyConsume(pathXj,sensor);
                        break;
                    }
                }
            }
        }

        try {
            //Define new model
            IloCplex cplex = new IloCplex();

            //Variable
            IloNumVar[] T = new IloNumVar[m];
            for (int i = 0; i < m; i++) {
                T[i] = cplex.numVar(0, Double.MAX_VALUE);
            }

            //Expression
            IloLinearNumExpr[] totalTimeOnExpr = new IloLinearNumExpr[n];
            for (int j = 0; j < n; j++) {
                totalTimeOnExpr[j] = cplex.linearNumExpr();

                for (int i = 0; i < m; i++) {
                    totalTimeOnExpr[j].addTerm(a[j][i], T[i]);
                }
            }

            IloLinearNumExpr objective = cplex.linearNumExpr();
            for (int i = 0; i < m; i++) {
                objective.addTerm(1, T[i]);
            }

            //Define Objective
            cplex.addMaximize(objective);

            //Constraints
            for (int i = 0; i < n; i++) {
                cplex.addLe(totalTimeOnExpr[i], valueE0);
            }

            cplex.setParam(IloCplex.Param.Simplex.Display, 0);
            //Resolve Model
            if (cplex.solve()) {
                for (int i = 0; i < m; i++) {
                    time.add(cplex.getValue(T[i]));
                    System.out.println("time: " + cplex.getValue(T[i]));
                }
                System.out.println("value: " + cplex.getObjValue());
            } else {
                System.out.println("Problem not solved");
            }

            cplex.end();

        } catch (IloException ex) {
            Logger.getLogger("LeHieu").log(Level.SEVERE, null, ex);
        }
        return time;
    }
    
    float TranferEnergy(float distance) {
        float result = Et;
        if (distance <Do) {
            result+= (Efs*distance*distance);
        } else {
            result+= (Emp*distance*distance*distance*distance);
        }
        return result;
    }
    float CaculateEnergyConsume(List<List<Integer>> ListPath,int sensor) {
        List<Integer> path;
        float result =0;
        for (int i =0 ; i < ListPath.size(); i++) {
            path = ListPath.get(i);
            for (int j =0; j<path.size();j++) {
                if (j == 0 && sensor == path.get(j)) {
                    //TH sensor la sensing node
                    result += bit*Es;
                    if (path.size()== 1) {
                        result += bit*TranferEnergy(MinDistanceSink[sensor]);
                    } else {
                        result += bit*TranferEnergy(Distance[sensor][path.get(j+1)]);
                    }
                    
                    break;
                } else if (sensor == path.get(j)) {
                    //TH sensor laf relay node
                    result += bit*Er;
                    if (j == path.size()-1) {
                        result += bit*TranferEnergy(MinDistanceSink[sensor]);
                    } else {
                        result += bit*TranferEnergy(Distance[sensor][path.get(j+1)]);
                    }
                    break;
                }
            }
            
        }
        return result;
    }
    
    public List<List<Double>> LinearProAlgorithm2(List<List<PathItem>> listPathY, List<Integer> listSenSor, double valueE0) {
        List<List<Double>> time = new ArrayList<>();
        int n = listPathY.size(); // Number target
        int m = listSenSor.size(); // Number sensor
        int Vmax =0;
        //Test
        
//        for (int i =0 ; i< listPathY.size(); i++) {
//            List<PathItem> pathY = listPathY.get(i);
//            List<Double> Ty = new ArrayList<>();
//            for (int j = 0 ;j < pathY.size();j++) {
//                if (j % 2 == 0) {
//                    Ty.add(1000.0);
//                } else {
//                    Ty.add(0.0);
//                }
//
//            }
//            time.add(Ty);
//            
//            
//        }
        
        

        if (m == 0 || n == 0) {
            return time;
        }
        float[] v = new float[n];
        for (int i = 0; i < listPathY.size(); i++) {
            v[i] = listPathY.get(i).size();
            if (Vmax < listPathY.get(i).size()) {
                Vmax = listPathY.get(i).size();
            }
        }

        //Check Input
        double [][][] b = new double[m][n][Vmax];
        for (int i = 0; i < m; i++) {
            int sensor = listSenSor.get(i);
            for (int j = 0; j < n; j++) {
                List<PathItem> listYj = listPathY.get(j);
                for (int k = 0; k < v[j]; k++) {
                    b[i][j][k] = getEnergyConsumer(listYj.get(k).getPath(), sensor);

                }
            }
        }

        try {
            //Init model
            IloCplex cplex = new IloCplex();

            //Define variable
            IloNumVar[][] t = new IloNumVar[n][Vmax];
            
      
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < Vmax; k++) {
                    if (k < v[j]) {
                        t[j][k] = cplex.numVar(0, Double.MAX_VALUE);
                    } else {
                        t[j][k] = cplex.numVar(-1.0, -1.0);
                    }
                }
            }

            //Define Objective
            IloNumVar object = cplex.numVar(0, Double.MAX_VALUE);
            IloLinearNumExpr objective = cplex.linearNumExpr();
            objective.addTerm(1.0, object);
            
            cplex.addMaximize(objective);
            
            //Contraint
            IloLinearNumExpr[] arrayExpress = new IloLinearNumExpr[m];
            for (int i = 0; i < m; i++) {
                arrayExpress[i] = cplex.linearNumExpr();
                
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < v[j]; k++) {
                        arrayExpress[i].addTerm(b[i][j][k], t[j][k]);
                    }
                }
                cplex.addLe(arrayExpress[i], valueE0);
            }
            
            IloLinearNumExpr[] express = new IloLinearNumExpr[n];
            for (int j = 0; j < n; j++) {
                express[j] = cplex.linearNumExpr();
                for (int k = 0; k < v[j]; k++) {
                    express[j].addTerm(1.0, t[j][k]);
                }
                cplex.addLe(object,express[j]);

            }

            if (cplex.solve()) {

                System.out.println("value: " + cplex.getObjValue());
                double[] Time = new double[n];
                
                for (int j = 0; j < n; j++) {
                    Time[j] =0;
                    List<Double> timeTarget = new ArrayList<>();
                    for (int k = 0; k < v[j]; k++) {
                       Time[j] += cplex.getValue(t[j][k]);
                       System.out.print(" "+cplex.getValue(t[j][k]));
                       timeTarget.add(cplex.getValue(t[j][k]));
                    }
                    System.out.println();
                    time.add(timeTarget);
                }
                
                //Find Min
//                double min = Double.MAX_VALUE;
//                for (int i = 0 ;i <n ;i ++) {
//                    if (min > Time[i]) {
//                        min = Time[i];
//                    }
//                }


                        
                int da=5;

                //return cplex.getValue(objective);        
            } else {
                System.out.println("Problem not solved");
            }

            cplex.end();

        } catch (IloException ex) {
            Logger.getLogger("LeHieu").log(Level.SEVERE, null, ex);
        }
        return time;
    }
    
    double getEnergyConsumer(List<Integer> pathYi, int sensor) {
        double result = 0;
        for (int i =0; i< pathYi.size(); i++) {
            if (i==0 && pathYi.get(i) == sensor ) {
                result += bit * Es;
                if (pathYi.size() == 1) {
                    result += bit * TranferEnergy(MinDistanceSink[sensor]);
                } else {
                    result += bit * TranferEnergy(Distance[sensor][pathYi.get(i + 1)]);
                }
                return result;
            } else if (pathYi.get(i) == sensor) {
                result += bit * Er;
                if (i == pathYi.size() - 1) {
                    result += bit * TranferEnergy(MinDistanceSink[sensor]);
                } else {
                    result += bit * TranferEnergy(Distance[sensor][pathYi.get(i + 1)]);
                }
                return result;
            }
        }
        return 0.0;
    }
    
    
    public void CoppyToListSensor() {
        mListofListPath.clear();
        mListofListPath = resultListY;
        SensorUtility.mListofListPathTime = resultListTi;
    }
    
    public void showViewTest(List<Integer> listSensor) {                                            
        // TODO add your handling code here:
        //Clear data
        for (int j = 0; j < mListSensorNodes.size(); j++) {
            mListSensorNodes.get(j).setStatus(0);
        }

        for (int i =0;i<listSensor.size();i++) {
           //Change Value On foreach Sensor   
            mListSensorNodes.get(listSensor.get(i)).setStatus(1);
            
        }
        coordinatePanel.refresh();
    }    
    
    public void runAlgorithm() {
        List<Integer> listSensor = new ArrayList<>();
        for (int i = 0; i < mListSensorNodes.size(); i++) {
            listSensor.add(i);
        }
        List<Integer> listTarget = new ArrayList<>();
        for (int i = 0; i < mListTargetNodes.size(); i++) {
            listTarget.add(i);
        }
        List<Integer> listSink = new ArrayList<>();
        for (int i = 0; i < mListSinkNodes.size(); i++) {
            listSink.add(i);
        }
//        List<List<List<Integer>>> ListPathX = new ArrayList<>();
//        List<List<Integer>> ListXi = new ArrayList<>();
//        FindingPathX(listSensor, listTarget, listSink, ListPathX, ListXi);
//        List<List<List<Integer>>> ListPi = new ArrayList<>();
//        Finding_CCP2(listSensor,listTarget,listSink,ListPi);
//        int a =6;
//        CaculateEnergyConsume(ListPi.get(1),13);
//        CaculateEnergyConsume(ListPi.get(0),3);
//        CaculateEnergyConsume(ListPi.get(0),0);


        FloatPointItem UpLeftCornerPoint = new FloatPointItem(0,0);
        FloatPointItem DownRightCornerPoint = new FloatPointItem(SensorUtility.numberRow-1,SensorUtility.numberColum-1);
        
        List<List<List<PathItem>>> ListOfListY = new ArrayList<>();
        List<List<List<Double>>> ListOfListTi = new ArrayList<>();

        for (int i =0; i< Anpha ;i++) {
            List<List<PathItem>> tempReturnListY = new ArrayList<>();
            List<List<Double>> tempReturnListTi = new ArrayList<>();
            for (int j = 0; j< T;j++) {
                List<PathItem> pathY = new ArrayList<>();
                List<Double> timeY = new ArrayList<>(); 
                tempReturnListY.add(pathY);
                tempReturnListTi.add(timeY);
            }
            
            
            DiviceNetworkFollowWidth(UpLeftCornerPoint,DownRightCornerPoint,i,tempReturnListY,tempReturnListTi);
            if (!tempReturnListY.isEmpty() && !tempReturnListTi.isEmpty()) {
                ListOfListY.add(tempReturnListY);
                ListOfListTi.add(tempReturnListTi);
            }
        }
        Combining_All_Division(ListOfListY,ListOfListTi,resultListY,resultListTi);
        
        //Free data
        ListOfListY = null;
        ListOfListTi = null;

    }
    
    public  void DiviceNetworkFollowWidth(FloatPointItem UpLeftCornerPoint, FloatPointItem DownRightCornerPoint, int divisons, List<List<PathItem>> returnListY, List<List<Double>> returnListTi) {
        FloatPointItem upPoint = new FloatPointItem();
        FloatPointItem downPoint = new FloatPointItem();
        
        List<List<List<PathItem>>> tempListOfListY = new ArrayList<>();
        List<List<List<Double>>> tempListOfListTi = new ArrayList<>();
        List<List<PathItem>> tempListY;
        List<List<Double>> tempListT;
        
        List<List<List<PathItem>>> temp2ListOfListY = new ArrayList<>();
        List<List<List<Double>>> temp2ListOfListTi = new ArrayList<>();

        
        float PostionX = UpLeftCornerPoint.getX();
        float MaxPostionX = DownRightCornerPoint.getX();
        boolean isFirstBlock = true;
        
        
        while (PostionX < MaxPostionX)  {
            //Set upoint and downpoint of Block
            if (isFirstBlock && divisons != 0) {
                //Start Point of Block independent of offset value
                upPoint.setXY(PostionX, UpLeftCornerPoint.getY());
                PostionX += divisons * 2 * R;
                if (PostionX >= MaxPostionX) PostionX = MaxPostionX;
                downPoint.setXY(PostionX, DownRightCornerPoint.getY());
                isFirstBlock = false;
            } else {
                
                upPoint.setXY(PostionX, UpLeftCornerPoint.getY());
                PostionX += Anpha*2 * R;
                if (PostionX >= MaxPostionX) PostionX = MaxPostionX;
                downPoint.setXY(PostionX, DownRightCornerPoint.getY());
            }
            
            //Caculate result of the Block foreach devisions
            for (int i =0; i< Anpha;i++) {
                List<List<PathItem>> temp2returnListX = new ArrayList<>();
                List<List<Double>> temp2returnListTi = new ArrayList<>();
                for (int j = 0; j< T;j++) {
                    List<PathItem> pathY = new ArrayList<>();
                    List<Double> timeY = new ArrayList<>(); 
                    temp2returnListX.add(pathY);
                    temp2returnListTi.add(timeY);
                }
                DiviceNetWorkFollowHeight(upPoint,downPoint,i,temp2returnListX,temp2returnListTi);
                if (!temp2returnListX.isEmpty() && !temp2returnListTi.isEmpty()) {
                    temp2ListOfListY.add(temp2returnListX);
                    temp2ListOfListTi.add(temp2returnListTi);
                }
            }
            tempListY = new ArrayList<>();
            tempListT = new ArrayList<>();
            
            for (int j = 0; j< T;j++) {
                    List<PathItem> pathY = new ArrayList<>();
                    List<Double> timeY = new ArrayList<>(); 
                    tempListY.add(pathY);
                    tempListT.add(timeY);
            }
            Combining_All_Division(temp2ListOfListY, temp2ListOfListTi, tempListY, tempListT);
            temp2ListOfListY.clear();
            temp2ListOfListTi.clear();
            if (!tempListY.isEmpty() && !tempListT.isEmpty()) {
               tempListOfListY.add(tempListY);
               tempListOfListTi.add(tempListT);
            }
        }
        
        //Combining all strips follow the with of netwwork
        Combining_All_Strips(tempListOfListY, tempListOfListTi, returnListY, returnListTi);
        
        //Free Data
        tempListOfListY = null;
        tempListOfListTi = null;
        tempListY = null;
        tempListT = null;
        temp2ListOfListY = null;
        temp2ListOfListTi = null;
    }
    
    public void DiviceNetWorkFollowHeight(FloatPointItem UpLeftCornerPoint, FloatPointItem DownRightCornerPoint, int division, List<List<PathItem>> returnListY, List<List<Double>> returnListTi) {
        
        FloatPointItem upPoint = new FloatPointItem();
        FloatPointItem downPoint = new FloatPointItem();
        
        List<List<List<PathItem>>> tempListOfListY = new ArrayList<>();
        List<List<List<Double>>> tempListOfListTi = new ArrayList<>();
        List<List<PathItem>> tempListY;
        List<List<Double>> tempListTi;
        

        float PostionY = UpLeftCornerPoint.getY();
        float MaxPostionY = DownRightCornerPoint.getY();
        boolean isFirstBlock = true;
        
        while (PostionY < MaxPostionY)  {
            //Set upoint and downpoint of Block
            if (isFirstBlock && division != 0) {
                //Start Point of Block independent of divison value
                upPoint.setXY(UpLeftCornerPoint.getX(), PostionY);
                PostionY += division * 2 * R;
                if (PostionY >= MaxPostionY) PostionY = MaxPostionY;
                downPoint.setXY(DownRightCornerPoint.getX(), PostionY);
                isFirstBlock = false;
            } else {
                
                upPoint.setXY(UpLeftCornerPoint.getX(), PostionY);
                PostionY += Anpha*2 * R;
                if (PostionY >= MaxPostionY) PostionY = MaxPostionY;
                downPoint.setXY(DownRightCornerPoint.getX(), PostionY);
            }

            //Find ListSensor in Block
            List<Integer> tempListSensor = FindListSensor(upPoint,downPoint);
            List<Integer> tempListTarget = FindListTarget(upPoint, downPoint);
            List<Integer> tempListSink = FindListSink(upPoint, downPoint);
            showViewTest(tempListSensor);
            
            List<List<PathItem>> ListPathY = new ArrayList<>();
            Finding_CCP2(tempListSensor, tempListTarget, tempListSink, ListPathY);

//            try {
//                SensorUtility.writeTestFile("D:\\Le-Hieu\\inputPathTest2.txt",ListPathY);
//            } catch (IOException ex) {
//                Logger.getLogger(MyAlgorithm3.class.getName()).log(Level.SEVERE, null, ex);
//            }
            List<List<Double>> ListTi = LinearProAlgorithm2(ListPathY, tempListSensor, SensorUtility.mEoValue);
            reduceListPathYi(ListPathY,ListTi);
            
            //Init tempListTi and tempListY
            tempListY = new ArrayList<>();
            tempListTi = new ArrayList<>();
            for (int i =0 ; i< T; i++) {
                List<PathItem> Yi = new ArrayList<>();
                tempListY.add(Yi);
                
                //Ti
                List<Double> Ty = new ArrayList<>();
                tempListTi.add(Ty);
            }
            
            //Add data from  ListPathY and List Ti
            if (ListPathY.size() == tempListTarget.size()) {
                for (int i = 0; i < tempListTarget.size(); i++) {
                    int pos = tempListTarget.get(i);
                    tempListY.remove(pos);
                    tempListY.add(pos, ListPathY.get(i));

                    tempListTi.remove(pos);
                    tempListTi.add(pos, ListTi.get(i));
                }
            }
            
            //Find set Time foreach SetX%
            //tempListT = LinearProAlgorithm(tempListPathX,tempListX,tempListSensor,SensorUtility.mEoValue);
            
           
 
            
            
            //Add result of Block
            if (!tempListTarget.isEmpty() && !ListPathY.isEmpty()) {
                tempListOfListY.add(tempListY);
                tempListOfListTi.add(tempListTi);
            } else if (!tempListTarget.isEmpty() && tempListY.isEmpty()) {
                System.err.println("Khong ton tai duong di");
            }
            
            ListPathY = null;
            ListTi = null;

        }
        Combining_All_Strips(tempListOfListY, tempListOfListTi, returnListY, returnListTi);
        
        //Free Data
        tempListOfListY = null;
        tempListOfListTi = null;
        tempListY = null; 
        tempListTi = null;
        upPoint = null;
        downPoint = null;
    }
    
    void reduceListPathYi(List<List<PathItem>> ListPathY, List<List<Double>> ListTimeY) {
        for (int i = 0; i< ListPathY.size(); i++) {
            List<PathItem> pathY = ListPathY.get(i);
            List<Double> timeListY  = ListTimeY.get(i);
            // Giam cac TH TY = 0;
            for (int j =0; j< timeListY.size();) {
                if (timeListY.get(j) < 0.00001d) {
                    timeListY.remove(j);
                    pathY.remove(j);
                } else {
                    j++;
                }
                
            }
            
        }
        
        
    }
    
    public List<Integer> FindListSensor(FloatPointItem UpLeftCornerPoint, FloatPointItem DownRightCornerPoint) {
        List<Integer> resultListSensor = new ArrayList<>();
        float Xmax,Xmin,Ymax,Ymin;
        Xmin = UpLeftCornerPoint.getX() - R;
        Xmax=  DownRightCornerPoint.getX() + R;
        Ymin = UpLeftCornerPoint.getY() - R;
        Ymax = DownRightCornerPoint.getY() +R;
        
        for (int i = 0 ;i<mListSensorNodes.size(); i++) {
            if (mListSensorNodes.get(i).getX() >= Xmin && mListSensorNodes.get(i).getX() < Xmax && mListSensorNodes.get(i).getY() >= Ymin && mListSensorNodes.get(i).getY() < Ymax ) {
                resultListSensor.add(i);
            }
        }
        return resultListSensor;        
    }
    
    public List<Integer> FindListTarget(FloatPointItem UpLeftCornerPoint, FloatPointItem DownRightCornerPoint) {
        List<Integer> resultListTarget = new ArrayList<>();
        float Xmax,Xmin,Ymax,Ymin;
        Xmin = UpLeftCornerPoint.getX();
        Xmax=  DownRightCornerPoint.getX();
        Ymin = UpLeftCornerPoint.getY();
        Ymax = DownRightCornerPoint.getY();
        
        for (int i = 0 ;i<mListTargetNodes.size(); i++) {
            if (mListTargetNodes.get(i).getX() >= Xmin && mListTargetNodes.get(i).getX() < Xmax && mListTargetNodes.get(i).getY() >= Ymin && mListTargetNodes.get(i).getY() < Ymax ) {
                resultListTarget.add(i);
            }
        }
        return resultListTarget;        
    }
    
    public List<Integer> FindListSink(FloatPointItem UpLeftCornerPoint, FloatPointItem DownRightCornerPoint) {
        List<Integer> resultListSink = new ArrayList<>();
        float Xmax,Xmin,Ymax,Ymin;
        Xmin = UpLeftCornerPoint.getX() - R-Rc;
        Xmax=  DownRightCornerPoint.getX() + R+Rc;
        Ymin = UpLeftCornerPoint.getY() - R-Rc;
        Ymax = DownRightCornerPoint.getY() +R+Rc;
        
        for (int i = 0 ;i<mListSinkNodes.size(); i++) {
            if (mListSinkNodes.get(i).getX() >= Xmin && mListSinkNodes.get(i).getX() < Xmax && mListSinkNodes.get(i).getY() >= Ymin && mListSinkNodes.get(i).getY() < Ymax ) {
                resultListSink.add(i);
            }
        }
        return resultListSink;        
    }
    
    public void Combining_All_Strips(List<List<List<PathItem>>> ListOfListY, List<List<List<Double>>> ListOfListTi, List<List<PathItem>> returnListY, List<List<Double>> returnListTi) {
        int Strip = ListOfListY.size();
        double Min = Double.MAX_VALUE;
        //Create ListT Ascending
        
        for(int i =0; i<Strip ; i++){
            List<List<PathItem>> ListPathY = ListOfListY.get(i);
            List<List<Double>> ListTimeTi = ListOfListTi.get(i);
             for (int j = 0; j< ListPathY.size();j++) {
                 List<PathItem> pathY = ListPathY.get(j);
                 List<PathItem> retPathY = returnListY.get(j);
                 List<Double> timeY = ListTimeTi.get(j);
                 List<Double> reTimeY = returnListTi.get(j);
                 
                 unionListY(pathY,timeY,retPathY,reTimeY);
                 
             }
            
            
        }

    }
    
    void unionListY(List<PathItem> inputListY ,List<Double> inputTime, List<PathItem> outputListY, List<Double> outputTime) {
        if (outputListY.isEmpty()) {
            for (int i =0; i< inputListY.size();i++) {
                outputListY.add(inputListY.get(i));
                outputTime.add(inputTime.get(i));
            }
            
        } else {
            for (int i =0; i< inputListY.size();i++) {
                int pos = CheckExitPath(inputListY.get(i), outputListY);
                if ( pos == -1) {
                   outputListY.add(inputListY.get(i));
                   outputTime.add(inputTime.get(i));
                } else {
                   double firstTime = outputTime.get(pos);
                   double second = inputTime.get(i);
                   outputTime.remove(pos);
                   outputTime.add(pos, firstTime+second);
                }
                
            }
            
        }
        
    }
    
    int CheckExitPath(PathItem item , List<PathItem> list) {
        for (int i =0; i < list.size(); i++) {
            if (PathItem.compareSame(item, list.get(i))) {
                return i;
            }
        }
        return -1;
    }

    //Ghep Xik U Xin....
    void unionXi(List<Integer> listX, List<Integer> resultListX) {
        int n = resultListX.size();
        boolean isExit;
        for (int i = 0; i < listX.size(); i++) {
            isExit = false;
            for (int j = 0; j < n; j++) {
                if (Objects.equals(listX.get(i), resultListX.get(j))) {
                    isExit = true;
                    break;
                }
            }
            if (!isExit) {
                resultListX.add(listX.get(i));
            }
        }
    }
    

    //Dua ra vi tri corver segment cua list
    int getPostionOfList(List<Double> listT, double starTime, double endTime) {
        for (int i = 0; i < listT.size(); i++) {
            if (i == 0) {
                if (listT.get(0) >= endTime) {
                    return 0;
                }
            } else {
                if (listT.get(i - 1) <= starTime && listT.get(i) >= endTime) {
                    return i;
                }
            }
        }
        return 0;
    }
    
    public void Combining_All_Division(List<List<List<PathItem>>> ListOfListY, List<List<List<Double>>> ListOfListT, List<List<PathItem>> returnListY, List<List<Double>> returnListTi) {

        for (int i = 0; i< ListOfListY.size();i++) {
          List<List<PathItem>> tempListPathY = ListOfListY.get(i);
          List<List<Double>>  tempListTi  =ListOfListT.get(i);
          for (int j =0 ; j< tempListPathY.size();j++) {
                 List<PathItem> pathY = tempListPathY.get(j);
                 List<PathItem> retPathY = returnListY.get(j);
                 List<Double> timeY = tempListTi.get(j);
                 List<Double> reTimeY = returnListTi.get(j);
                 
                 unionListY(pathY, timeY, retPathY, reTimeY);
              
          }
       }
       // Divice Time to (mLvalue +1)
       for (int i = 0; i < returnListTi.size(); i++) {
           List<Double> reTimeY = returnListTi.get(i);
           for (int j =0; j < reTimeY.size();j++) {
            Double x = reTimeY.get(j)/(Anpha+1);
            reTimeY.remove(j);
            reTimeY.add(j, x);
           }
        }
    }
    
    boolean CheckExitListX(List<List<Integer>> listX, List<Integer> setX, int postionX) { //checked
        Collections.sort(setX);
        for (int i = 0 ;i<listX.size();i++) {
            List<Integer> tempX = listX.get(i);
            if (tempX.size() != setX.size()) {
                continue;
            } else {
                Collections.sort(tempX);
                int count = 0;
                for (int j =0;j<tempX.size();j++) {
                    if (!Objects.equals(setX.get(j), tempX.get(j))) {
                        break;
                    } else {
                        count++;
                    }
                }
                if (count == setX.size()) {
                    postionX = i;
                    return true;
                }
            }
            
             
        }
        return false;
    }
    
    
}

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package model;
//
//import java.util.ArrayList;
//import java.util.stream.Collectors;
//
///**
// *
// * @author dauto98
// */
//public class IntersectionPoint {
//    FloatPointItem coordinate;
//    // the center coordinate of circles that this point sit on their edge, null if it is an edge of the cover area
//    ArrayList<FloatPointItem> circleCoordinates;
//    
//    public IntersectionPoint() {
//        
//    }
//    
//    public IntersectionPoint(FloatPointItem coordinate, ArrayList<FloatPointItem> circleCoordinates) {
//        this.coordinate = coordinate;
//        this.circleCoordinates = circleCoordinates;
//    }
//    
//    public FloatPointItem getCoordinate() {
//        return coordinate;
//    }
//    
//    public FloatPointItem getCircle(int index) {
//        return circleCoordinates.get(index);
//    }
//    
//    public IntersectionPoint addCircle(FloatPointItem newCircle) {
//        if (!circleCoordinates.contains(newCircle)) {
//            circleCoordinates.add(newCircle);
//        }
//        return this;
//    }
//    
//    public IntersectionPoint removeCircle(FloatPointItem circle) {
//        circleCoordinates.remove(circle);
//        return this;
//    }
//    
//    public static FloatPointItem getCommonCircle(IntersectionPoint point1, IntersectionPoint point2) {
//        ArrayList<FloatPointItem> commonCircle = point1.circleCoordinates.stream().filter(point2.circleCoordinates::contains).collect(Collectors.toCollection(ArrayList::new));
//        if (commonCircle.size() > 0) {
//            return commonCircle.get(0); // 2 point usually have only 1 common circle, in case of 2. which we will ensure not going to happen, we only interest in 1 circle
//        } else {
//            return null;
//        }
//    }
//    
//    @Override
//    public boolean equals(Object other) {
//        if (other == null) {
//            return false;
//        }
//        if (other == this) {
//            return true;
//        }
//        if (getClass() != other.getClass()) {
//            return false;
//        }
//        IntersectionPoint otherPoint = (IntersectionPoint)other;
//        return FloatPointItem.compare(this.coordinate, otherPoint.getCoordinate());
//    }
//}

package model;

public class IntersectionPoint {
    FloatPointItem coordinate;
    String direction; // the value can be "entry" or "exit", indicate this point is the exit point or entry point of the curve cut through the sensor circle
    
    public IntersectionPoint(FloatPointItem coordinate, String direction) {
        this.coordinate = coordinate;
        this.direction = direction;
    }
    
    public FloatPointItem getCoordinate() {
        return this.coordinate;
    }
    
    public String getDirection() {
        return this.direction;
    }
}
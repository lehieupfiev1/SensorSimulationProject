/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

public class IntersectionPoint {
    private DoublePoint coordinate;
    private String direction; // the value can be "entry" or "exit", indicate this point is the exit point or entry point of the curve cut through the sensor circle
    
    public IntersectionPoint(DoublePoint coordinate, String direction) {
        this.coordinate = coordinate;
        this.direction = direction;
    }
    
    public DoublePoint getCoordinate() {
        return this.coordinate;
    }
    
    public String getDirection() {
        return this.direction;
    }
    
    @Override
    public int hashCode() {
        return coordinate.hashCode() + direction.length();
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        IntersectionPoint otherPoint = (IntersectionPoint)other;
        return DoublePoint.compare(this.coordinate, otherPoint.coordinate) && this.direction.equals(otherPoint.direction);
    } 
}
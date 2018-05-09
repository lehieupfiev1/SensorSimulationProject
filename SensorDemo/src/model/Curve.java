/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author dauto98
 */
public class Curve {
    /**
     * Use to quickly identify which rectangular edge is edge is sitting on
     */
    public enum EdgeId {
        RIGHT, LEFT, TOP, BOTTOM
    };
    
    /**
     * For an arc, the direction from the start point to the end point relative to the center is always counter-clock wise
     * For an edge, the direction is clockwise
     */
    private final FloatPointItem startPoint;
    private final FloatPointItem endPoint;
    
    // center of the arc 
    private FloatPointItem center = null;
    
    // if this instance is an edge, this is used to quickly identify the position of the edge
    private EdgeId edgeId = null;
    
    public Curve(FloatPointItem startPoint, FloatPointItem endPoint, FloatPointItem center) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.center = center;
    }
    
    public Curve(FloatPointItem startPoint, FloatPointItem endPoint, EdgeId edgeId) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.edgeId = edgeId;
    }
    
    public FloatPointItem getStartPoint() {
        return startPoint;
    }
    
    public FloatPointItem getEndPoint() {
        return endPoint;
    }
    
    public FloatPointItem getCenter() {
        return center;
    }
    
    public EdgeId getEdgeId() {
        return edgeId;
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
        Curve otherPoint = (Curve)other;
        return FloatPointItem.compare(this.startPoint, otherPoint.getStartPoint())
                && FloatPointItem.compare(this.endPoint, otherPoint.getEndPoint()) 
                && ((this.center == null && otherPoint.getCenter() == null && this.edgeId == otherPoint.getEdgeId()) || (FloatPointItem.compare(this.center, otherPoint.getCenter())));
    }    
}

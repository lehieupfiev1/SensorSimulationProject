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
    private final DoublePoint startPoint;
    private final DoublePoint endPoint;
    
    // center of the arc 
    private DoublePoint center = null;
    
    // if this instance is an edge, this is used to quickly identify the position of the edge
    private EdgeId edgeId = null;
    
    public Curve(DoublePoint startPoint, DoublePoint endPoint, DoublePoint center) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.center = center;
    }
    
    public Curve(DoublePoint startPoint, DoublePoint endPoint, EdgeId edgeId) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.edgeId = edgeId;
    }
    
    public DoublePoint getStartPoint() {
        return startPoint;
    }
    
    public DoublePoint getEndPoint() {
        return endPoint;
    }
    
    public DoublePoint getCenter() {
        return center;
    }
    
    public EdgeId getEdgeId() {
        return edgeId;
    }
    
    @Override
    public int hashCode() {
        int h1 = startPoint.hashCode();
        int h2 = endPoint.hashCode();
        int h3 = 1;
        if (center != null) {
            h3 = center.hashCode();
        } else if (edgeId == EdgeId.RIGHT) {
            h3 = 101;
        } else if (edgeId == EdgeId.TOP) {
            h3 = 233;
        } else if (edgeId == EdgeId.LEFT) {
            h3 = 383;
        } else if (edgeId == EdgeId.BOTTOM) {
            h3 = 541;
        }
        int prime = 104879;
        int hash = 1;
        hash = hash*prime + h1;
        hash = hash*prime + h2;
        hash = hash*prime + h3;
        return hash;
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
        return DoublePoint.compare(this.startPoint, otherPoint.getStartPoint())
                && DoublePoint.compare(this.endPoint, otherPoint.getEndPoint()) 
                && ((this.center == null && otherPoint.getCenter() == null && this.edgeId == otherPoint.getEdgeId()) || (DoublePoint.compare(this.center, otherPoint.getCenter())));
    }    
}

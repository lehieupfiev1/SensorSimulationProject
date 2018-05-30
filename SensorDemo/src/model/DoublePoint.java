/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author sev_user
 */
public class DoublePoint {
    double x;
    double y;
    
    public DoublePoint() {
        
    }

    public DoublePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    public void setXY(double x,double y) {
        this.x = x;
        this.y = y;
    }
    
    public static boolean compare(DoublePoint a, DoublePoint b) {
        if(a == null || b == null) {
            return false;
        }
        return (new Double(a.getX())).equals(b.getX()) && (new Double(a.getY())).equals(b.getY());
    }
    
    /**
     * The hash function is taken from: https://stackoverflow.com/questions/36848151/hash-codes-for-floats-in-java
     * @return 
     */
    @Override
    public int hashCode() {
        long h1 = Double.doubleToLongBits(x);
        long h2 = Double.doubleToLongBits(y);
        return (int)(h1 ^ ((h2 >>> 16) | (h2 << 16)));
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
        DoublePoint otherPoint = (DoublePoint)other;
        return (new Double(this.x)).equals(otherPoint.getX()) && (new Double(this.y)).equals(otherPoint.getY());
    }    
}

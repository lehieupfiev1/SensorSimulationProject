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
public class FloatPointItem {
    float x;
    float y;
    
    public FloatPointItem() {
        
    }

    public FloatPointItem(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
    
    public void setXY(float x,float y) {
        this.x = x;
        this.y = y;
    }
    
    public static boolean compare(FloatPointItem a, FloatPointItem b) {
        if(a == null || b == null) {
            return false;
        }
        return (new Float(a.getX())).equals(b.getX()) && (new Float(a.getY())).equals(b.getY());
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
        FloatPointItem otherPoint = (FloatPointItem)other;
        return (new Float(this.x)).equals(otherPoint.getX()) && (new Float(this.y)).equals(otherPoint.getY());
    }    
}

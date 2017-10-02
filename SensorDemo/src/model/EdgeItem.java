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
public class EdgeItem {
    int start;
    int end;
    float distance;

    public EdgeItem() {
    }

    public EdgeItem(int start, int end, float distance) {
        this.start = start;
        this.end = end;
        this.distance = distance;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
    
}

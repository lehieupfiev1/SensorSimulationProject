/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Hieu
 */
public class NodeItem {
    int x;
    int y;
    /*
     type  0 : Target
     type  1 : Robot
     type  2 : Sensor
    */
    int type;
    int group;

    public NodeItem(int x, int y, int type, int group) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.group = group;
    }
    public NodeItem(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.group = 0;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

}

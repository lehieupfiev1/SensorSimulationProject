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
     type  3: Sink
    */
    int id;
    int type;
    int group;
    int status;

    public NodeItem( int id, int x, int y, int type, int group, int status) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.type = type;
        this.group = group;
        this.status = status;
    }
    
    public NodeItem(NodeItem object) {
        this.id = object.id;
        this.x = object.x;
        this.y = object.y;
        this.type = object.type;
        this.group = object.group;
        this.status = object.status;
    }

    public NodeItem(int x, int y, int type, int group, int status) {
        this.id = 0;
        this.x = x;
        this.y = y;
        this.type = type;
        this.group = group;
        this.status = status;
    }
    public NodeItem(int x, int y, int type, int group) {
        this.id = 0;
        this.x = x;
        this.y = y;
        this.type = type;
        this.group = group;
        this.status =0;
    }
    
    public NodeItem(int x, int y, int type) {
        this.id = 0;
        this.x = x;
        this.y = y;
        this.type = type;
        this.group = 0;
        this.status = 0;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public DoublePoint getCoordinate() {
        return new DoublePoint(x, y);
    }

    @Override
    public String toString() {
        return "NodeItem{" + "x=" + x + ", y=" + y + ", type=" + type + ", group=" + group + '}';
    }
    
    /**
     * This hash function only use x, y and type properties of this class
     * The algorithm can be found in: https://stackoverflow.com/questions/11742593/what-is-the-hashcode-for-a-custom-class-having-just-two-int-properties
     * The prime number is taken from: https://www.bigprimes.net/cruncher/104743/
     * @return hashcode
     */
    @Override
    public int hashCode() {
        int prime = 104743 ;
        int hash = 1;
        hash = hash*prime + x;
        hash = hash*prime + y;
        hash = hash*prime + type;
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
        NodeItem otherNode = (NodeItem)other;
        return this.x == otherNode.x && this.y == otherNode.y && this.type == otherNode.type;
    }    
}

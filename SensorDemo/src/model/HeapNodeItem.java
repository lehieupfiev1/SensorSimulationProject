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
public class HeapNodeItem {
    int idSet;
    int stNode;
    int desNode;
    float distance;

    public HeapNodeItem() {
    }

    public HeapNodeItem(int idSet, int stNode, int desNode, float distance) {
        this.idSet = idSet;
        this.stNode = stNode;
        this.desNode = desNode;
        this.distance = distance;
    }

    public int getIdSet() {
        return idSet;
    }

    public void setIdSet(int idSet) {
        this.idSet = idSet;
    }

    public int getStNode() {
        return stNode;
    }

    public void setStNode(int stNode) {
        this.stNode = stNode;
    }

    public int getDesNode() {
        return desNode;
    }

    public void setDesNode(int desNode) {
        this.desNode = desNode;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
    
}

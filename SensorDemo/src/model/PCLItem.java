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
public class PCLItem {
    int id;// Posision of SensorNode in mListSensorNode
    int pclValue;// Perimeter Coverage Level Value of Sensor //muc do bao phu cua sensor

    public PCLItem() {
    }

    public PCLItem(int id, int pclValue) {
        this.id = id;
        this.pclValue = pclValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPclValue() {
        return pclValue;
    }

    public void setPclValue(int pclValue) {
        this.pclValue = pclValue;
    }

}

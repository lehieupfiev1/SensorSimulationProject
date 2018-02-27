/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hieu
 */
public class EnergyItem {
    
    int id ; //Id of Sensor
    float energyUse;
    List<Integer> listTarget; //List target co duong path chua id sensor
    List<Integer> listPosition;//Position cua path tuong ung voi taget
    

    public EnergyItem() {
    }

    public EnergyItem(int id, float energyUse) {
        this.id = id;
        this.energyUse = energyUse;
        listTarget = new ArrayList<>();
        listPosition = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getEnergyUse() {
        return energyUse;
    }

    public void setEnergyUse(float energyUse) {
        this.energyUse = energyUse;
    }
    
    public void addEnergyUse(float energy) {
        this.energyUse += energy;
    }
    
    public void subEnergyUse(float energy) {
        this.energyUse -= energy;
    }
    
    public void addPostion(int target,int postion) {
        listTarget.add(target);
        listPosition.add(postion);
    }
    
    public List<Integer> getTargetList() {
        return listTarget;
    }
    
    public List<Integer> getPosiList() {
        return listPosition;
    }
}

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
public class LoadBalanceItem {
    
    int idS; //Id sensor child
    int idSr;// Id of parent sensor
    float value;

    public LoadBalanceItem() {
    }

    public LoadBalanceItem(int idS, int idSr) {
        this.idS = idS;
        this.idSr = idSr;
    }

    public LoadBalanceItem(int idS, int idSr, float value) {
        this.idS = idS;
        this.idSr = idSr;
        this.value = value;
    }

    public int getIdS() {
        return idS;
    }

    public void setIdS(int idS) {
        this.idS = idS;
    }

    public int getIdSr() {
        return idSr;
    }

    public void setIdSr(int idSr) {
        this.idSr = idSr;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}

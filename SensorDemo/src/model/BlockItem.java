/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;

/**
 *
 * @author Hieu
 */
public class BlockItem {
    int id;
    int postionI;
    int postionJ;
    List<List<Integer>> listResultXi;
    List<Double> listTime;
    double TotalTime;

    public BlockItem() {
    }
    
    public BlockItem(int postionI, int postionJ, List<List<Integer>> listResultXi, List<Double> listTime, double TotalTime) {
        this.id =0;
        this.postionI = postionI;
        this.postionJ = postionJ;
        this.listResultXi = listResultXi;
        this.listTime = listTime;
        this.TotalTime = TotalTime;
    }
    
    
    public BlockItem(int id, int postionI, int postionJ, List<List<Integer>> listResultXi, List<Double> listTime, double TotalTime) {
        this.id = id;
        this.postionI = postionI;
        this.postionJ = postionJ;
        this.listResultXi = listResultXi;
        this.listTime = listTime;
        this.TotalTime = TotalTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostionI() {
        return postionI;
    }

    public void setPostionI(int postionI) {
        this.postionI = postionI;
    }

    public int getPostionJ() {
        return postionJ;
    }

    public void setPostionJ(int postionJ) {
        this.postionJ = postionJ;
    }

    public List<List<Integer>> getListResultX() {
        return listResultXi;
    }

    public void setListResultX(List<List<Integer>> listResultX) {
        this.listResultXi = listResultX;
    }

    public List<Double> getListTime() {
        return listTime;
    }

    public void setListTime(List<Double> listTime) {
        this.listTime = listTime;
    }

    public double getTotalTime() {
        return TotalTime;
    }

    public void setTotalTime(double TotalTime) {
        this.TotalTime = TotalTime;
    }
    
    
    
    
}

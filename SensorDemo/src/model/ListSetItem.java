/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Hieu
 */
public class ListSetItem {
    List<Integer> Xi;
    List<Integer> Yi;
    List<Integer> Ci;// Luu cac node da check khong the loai bo
    int K;

    public ListSetItem() {
        Xi = new ArrayList<>();
        Yi = new ArrayList<>();
        Ci = new ArrayList<>();
    }

    public ListSetItem(List<Integer> Xi, List<Integer> Yi, List<Integer> Ci, int K) {
        this.Xi = Xi;
        this.Yi = Yi;
        this.Ci = Ci;
        this.K = K;
    }

    public ListSetItem(List<Integer> Xi, List<Integer> Yi, List<Integer> Ci) {
        this.Xi = Xi;
        this.Yi = Yi;
        this.Ci = Ci;
    }
    
    public ListSetItem(List<Integer> Xi, List<Integer> Yi) {
        this.Xi = Xi;
        this.Yi = Yi;
    }

    public List<Integer> getXi() {
        return Xi;
    }

    public void setXi(List<Integer> Xi) {
        this.Xi = Xi;
    }
    
    public void setXi(List<Integer> Xi, int except) {
        for (int i = 0; i < Xi.size(); i++) {
            if (i != except) {
                this.Xi.add(Xi.get(i));
            }
        }
    }

    public List<Integer> getYi() {
        return Yi;
    }

    public void setYi(List<Integer> Yi) {
        this.Yi = Yi;
    }
    
    public void setYi(List<Integer> Yi, int added) {
        for (int i = 0; i < Yi.size(); i++) {
           this.Yi.add(Yi.get(i));

        }
        this.Yi.add(added);
    }
    public void setListYi(List<Integer> Yi, List<Integer> Listadd) {
        for (int i = 0; i < Yi.size(); i++) {
           this.Yi.add(Yi.get(i));

        }
        for (int i = 0; i < Listadd.size(); i++) {
           this.Yi.add(Listadd.get(i));

        }
    }

    public List<Integer> getCi() {
        return Ci;
    }

    public void setCi(List<Integer> Ci) {
        for (int i = 0; i < Ci.size(); i++) {
           this.Ci.add(Ci.get(i));
        }
    }
    
    public void addCi(int added) {
        this.Ci.add(added);
    }

    public int getK() {
        return K;
    }

    public void setK(int K) {
        this.K = K;
    }
    
}

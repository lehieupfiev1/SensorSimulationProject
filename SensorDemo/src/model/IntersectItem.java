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
public class IntersectItem {
    FloatPointItem N1;
    FloatPointItem N2;

    public IntersectItem() {
        
    }

    public IntersectItem(FloatPointItem N1, FloatPointItem N2) {
        this.N1 = N1;
        this.N2 = N2;
    }

    public FloatPointItem getN1() {
        return N1;
    }

    public void setN1(FloatPointItem N1) {
        this.N1 = N1;
    }

    public FloatPointItem getN2() {
        return N2;
    }

    public void setN2(FloatPointItem N2) {
        this.N2 = N2;
    }
    
    
}

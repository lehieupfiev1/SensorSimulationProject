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
public class CountItem {
    int count;

    public CountItem(int count) {
        this.count = count;
    }

    public CountItem() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    public void increse(int add) {
        this.count+=add;
    }

    public void descrese(int sub) {
        this.count-=sub;
    }

}

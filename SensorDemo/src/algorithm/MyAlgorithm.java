/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import common.SensorUtility;
import java.awt.Point;

/**
 *
 * @author Hieu
 */
public class MyAlgorithm {
    int maxPoint = SensorUtility.MaxTargetNodes+SensorUtility.MaxCycleRobots*SensorUtility.MaxRobotNodesCycle+1;
    public float Distance[][] = new float[maxPoint][maxPoint];
    public float Target[][] = new  float[SensorUtility.MaxTargetNodes][2];
    public float P[][] = new  float[maxPoint][2];
    public float D[][][]= new float[SensorUtility.MaxCycleRobots][SensorUtility.MaxRobotNodesCycle][2];
    
    //
    int K;
    int N;//Number 
    int TP; // Total points
    int TargetPoint;
    int M[] = new int[SensorUtility.MaxCycleRobots]; // Number Point of every Cycle Robot;
    Point LisPoint[];

    public MyAlgorithm() {
        
        
    }
    public void readData() {
        
    }

    
    
    public static void main(String[] args) {
        
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
               
            }
        });
        thread.start();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import common.SensorUtility;
import static common.SensorUtility.mListSensorNodes;
import static common.SensorUtility.mListofListSensor;
import static common.SensorUtility.mListofListTime;
import java.util.ArrayList;
import java.util.List;
import model.NodeItem;

/**
 *
 * @author sev_user
 */
public class Algorithm2 {
    double mTimeLife;
    float Rs, Rt;// Rs and Rt value
    List<List<Integer>> resultListX;
    List<Double> resultListT;
    public float Distance[][];
    int Num;// Number sensor
    public Algorithm2() {
    }
    public void run() {

        init();

        readData();
      
        runAlgorithm();

        CoppyToListSensor();
        //freeData();
    }

    public void init() {
        resultListX = new ArrayList<>();
        resultListT = new ArrayList<>();
    }

    public void readData() {
        // Read Rs, Rt
        Rs = SensorUtility.mRsValue;
        Rt = SensorUtility.mRtValue;
        mTimeLife = SensorUtility.LifeTimeOfSensor;
        Num = mListSensorNodes.size();
        Distance = new float[Num + 1][Num + 1];

        for (int i = 0; i < Num; i++) {
            for (int j = 0; j <= i; j++) {
                if (i == j) {
                    Distance[i][j] = 0;
                } else {
                    Distance[i][j] = Distance[j][i] = calculateDistance(mListSensorNodes.get(i).getX(), mListSensorNodes.get(i).getY(), mListSensorNodes.get(j).getX(), mListSensorNodes.get(j).getY());
                }
            }
        }
    }
    public  float calculateDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
     public void runAlgorithm() {
         //Code in here
         
         
         
         
     }
     
     public void CoppyToListSensor() {
        mListofListSensor.clear();
        for (int i =0;i<resultListX.size();i++ ) {
            List<Integer> temp = resultListX.get(i);
            List<NodeItem> tempNodeList = new ArrayList<>();
            for (int j =0;j<temp.size();j++) {
               tempNodeList.add(mListSensorNodes.get(temp.get(j)));
            }
            mListofListSensor.add(tempNodeList);
        }
        mListofListTime = resultListT;
    }
    public static void main(String[] args) {
        mListSensorNodes.add(new NodeItem(19, 30, 0));
        mListSensorNodes.add(new NodeItem(3, 4, 0));
        mListSensorNodes.add(new NodeItem(5, 6, 0));
        mListSensorNodes.add(new NodeItem(4, 5, 0));
        mListSensorNodes.add(new NodeItem(1, 2, 0));
        
 
        mListSensorNodes.add(new NodeItem(11, 24, 0));
        mListSensorNodes.add(new NodeItem(1, 1, 0));
        mListSensorNodes.add(new NodeItem(2, 3, 0));

        mListSensorNodes.add(new NodeItem(11, 25, 0));
        
        mListSensorNodes.add(new NodeItem(12, 24, 0));
        mListSensorNodes.add(new NodeItem(29, 30, 0));
        
        List<List<Integer>> lisX = new ArrayList<>();
        List<Integer> x1 = new ArrayList<>();
        x1.add(22);
        x1.add(21);
        x1.add(23);
        List<Integer> x2 = new ArrayList<>();
        x2.add(20);
        x2.add(22);
        x2.add(23);
        List<Integer> x3 = new ArrayList<>();
        x3.add(26);
        x3.add(20);
        x3.add(24);
        lisX.add(x1);
        lisX.add(x2);
        lisX.add(x3);
        List<Integer> sensor = new ArrayList<>();
        sensor.add(20);
        sensor.add(23);
        sensor.add(22);

        sensor.add(24);
        sensor.add(21);
        sensor.add(26);
        List<Integer> childsensor = new ArrayList<>();
        childsensor.add(30);
        childsensor.add(35);
        childsensor.add(33);
        childsensor.add(34);

        //List<Double> a = LinearProAlgorithm(lisX, sensor, 5);
//        
        MyAlgorithm2 m = new MyAlgorithm2();

    }
}

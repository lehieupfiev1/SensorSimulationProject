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
import model.PCLItem;

/**
 *
 * @author Hieu
 */
public class TempAlgorithm {
    double mTimeLife;
    float Rs, Rt;// Rs and Rt value
    List<List<Integer>> resultListX;
    List<Double> resultListT;
    List<PCLItem> mListPCLSensor;
    public float Distance[][];
    int Num;// Number sensor
    public TempAlgorithm() {
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
         //Input : Rs Value, TimeLife , Num ( Soluong sensor) , mListSensorNodes : List cac node sensor (NodeItem)
         // Output : resultListX ( List ket qua cac Xi thong qua position cua mListSensorNodes)
         //          resultListT (Thoi gian on của tap hop Xi) 
         
         
         
         
         
         
         
         
         
         
         
         
         // Code test
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
        resultListX.add(x1);
        resultListX.add(x2);
        resultListX.add(x3);
        resultListT.add(300d);
        resultListT.add(400d);
        resultListT.add(500d);   
         
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
        
    }
}

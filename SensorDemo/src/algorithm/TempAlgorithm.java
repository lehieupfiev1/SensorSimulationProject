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
import model.FloatPointItem;
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
    List<List<Integer>> ListNearBy;
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
        //Tim lan can
        ListNearBy.clear();
        for (int i = 0;i<Num;i++) {
            List<Integer> tempNearBy = new ArrayList<>();
            for (int j =0;j<Num;j++) {
                if (i != j && Distance[i][j] <= 2*Rs) {
                    tempNearBy.add(j);
                }
            }
            ListNearBy.add(tempNearBy);
        }
        
    }
    
    public void findPCLAllPoint(List<List<Integer>> listNearBy, List<PCLItem> mListPCLSensor) {
        for (int i =0;i <listNearBy.size();i++ ) {
            
        }
        
    }
    public void findPCLPoint(List<Integer> listNearBy, NodeItem node) {
        
        
    }
    
    int CheckPoint_Corvering_bySet(FloatPointItem point, List<Integer> Set) {
        int count = 0;
        for (int i = 0; i < Set.size(); i++) {
             if (calculateDistance(point.getX(), point.getY(), mListSensorNodes.get(Set.get(i)).getX(), mListSensorNodes.get(Set.get(i)).getY()) + SensorUtility.mSaiso <  Rs) {
                 count++;
             }
        }
        return count;
    }
    
    //Tim giao diem 2 duong tron
    void findIntersection_TwoCircle(int point_u, int point_v, FloatPointItem mP1, FloatPointItem mP2) {
        //Giai phuong trinh
        //2(x1-x0)*X + 2(y1-y0)*Y = x1^2 -x0^2 +y1^2-y0^2
        //(X-x0)^2+ (Y-y0)^2 = R^2
        float x0 = mListSensorNodes.get(point_u).getX();
        float y0 = mListSensorNodes.get(point_u).getY();
        float x1 = mListSensorNodes.get(point_v).getX();
        float y1 = mListSensorNodes.get(point_v).getY();
        if (x0 == x1) {
            float ny = (x1 * x1 - x0 * x0) / (2 * y1 - 2 * y0) + (y1 + y0) / 2;

            float c = x0 * x0 + (ny - y0) * (ny - y0) - Rs * Rs;
            float Delta = 4 * x0 * x0 - 4 * c;

            // Giai phuong trinh
            // Nghiem 1
            float nx1 = (2 * x0 + (float) Math.sqrt(Delta)) / 2;
            mP1.setX(nx1);
            mP1.setY(ny);

            // Nghiem 2
            float nx2 = (2 * x0 - (float) Math.sqrt(Delta)) / 2;
            mP2.setX(nx2);
            mP2.setY(ny);

        } else {
            float a = (x0 + x1) / 2 + (y1 * y1 - y0 * y0) / (2 * x1 - 2 * x0);
            float b = (y0 - y1) / (x1 - x0);

            float a1 = b * b + 1;
            float a2 = 2 * a * b - 2 * x0 * b - 2 * y0;
            float a3 = a * a - 2 * x0 * a + x0 * x0 + y0 * y0 - Rs * Rs;

            float Delta = a2 * a2 - 4 * a1 * a3;

            // Giai phuong trinh
            // Nghiem 1
            float ny1 = (-a2 + (float) Math.sqrt(Delta)) / (2 * a1);
            float nx1 = a + b * ny1;
            mP1.setX(nx1);
            mP1.setY(ny1);

            // Nghiem 2
            float ny2 = (-a2 - (float) Math.sqrt(Delta)) / (2 * a1);
            float nx2 = a + b * ny2;
            mP2.setX(nx2);
            mP2.setY(ny2);

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

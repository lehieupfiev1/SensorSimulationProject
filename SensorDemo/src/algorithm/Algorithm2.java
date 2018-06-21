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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import model.FloatPointItem;
import model.NodeItem;
import model.PCLItem;

/**
 *
 * @author sev_user
 */
public class Algorithm2 {
    double mTimeLife;
    float Rs, Rt;// Rs and Rt value
    List<List<Integer>> resultListX;
    List<Double> resultListT;
    List<PCLItem> mListPCLSensor;
    List<PCLItem> mListPCLSensorCommon;// ListPCL ban dau theo thu tu listSensor
    List<List<Integer>> ListNearBy;
    FloatPointItem P1,P2,P3,P4; // 4 diem cua 4 goc mang cam bien
    public final static int ADD_MODE =  0; 
    public final static int REMOVE_MODE =  1; 
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
        mListPCLSensor = new ArrayList<>();
        mListPCLSensorCommon = new ArrayList<>();
        ListNearBy = new ArrayList<>();
    }

    public void readData() {
        // Read Rs, Rt
        System.out.println("Read data input");
        Rs = SensorUtility.mRsValue;
        Rt = SensorUtility.mRtValue;
        mTimeLife = SensorUtility.LifeTimeOfSensor;
        Num = mListSensorNodes.size();
        P1 = new FloatPointItem(0.0f, 0.0f);
        P2 = new FloatPointItem(0.0f, SensorUtility.numberOfRow-1);
        P3 = new FloatPointItem(SensorUtility.numberOfColumn-1, 0.0f);
        P4 = new FloatPointItem(SensorUtility.numberOfColumn-1, SensorUtility.numberOfRow-1);
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
    
    public void findPCLAllPoint(List<List<Integer>> listofListNearBy, List<PCLItem> mListPCLSensor) {
        for (int i = 0; i < listofListNearBy.size(); i++) {
            PCLItem mPCLItem = findPCLPoint(listofListNearBy.get(i),i);
            mListPCLSensor.add(mPCLItem);
        }
    }
    
    public PCLItem findPCLPoint(List<Integer> nearByList, int node) {//listNearBy : list lan can cua node , node : vi tri cua node trong ListNodeSensor

        List<FloatPointItem> listI = new ArrayList<>(); // List giao diem cua Sj voi lan can
        PCLItem mPCL_Sj = new PCLItem(node, 100000000);// Khoi tao PCL of node
        // TH1 : Diem Sj nam ben trong khoi hinh 
        if (Math.abs(mListSensorNodes.get(node).getX() - P1.getX()) > Rs
                && ( P4.getX() - mListSensorNodes.get(node).getX() ) > Rs
                && Math.abs(mListSensorNodes.get(node).getY() - P1.getY()) > Rs
                && (P4.getY() -mListSensorNodes.get(node).getY()) > Rs) {

            // Tim giao diem cua Sj voi cac lan can
            listI.clear();
            for (int j = 0; j < nearByList.size(); j++) {
                if (node != nearByList.get(j) && Distance[node][nearByList.get(j)] < 2 * Rs) {
                    if ((mListSensorNodes.get(node).getY() != mListSensorNodes.get(nearByList.get(j)).getY()) || (mListSensorNodes.get(node).getX() != mListSensorNodes.get(nearByList.get(j)).getX())) {
                        FloatPointItem n1 = new FloatPointItem();
                        FloatPointItem n2 = new FloatPointItem();
                        findIntersection_TwoCircle(node, nearByList.get(j), n1, n2);
                        listI.add(n1);
                        listI.add(n2);
                    }
                }
            }
            // Find PCL of Sj

            for (int j = 0; j < listI.size(); j++) {
                int PClPoint = CheckPoint_Corvering_bySet(listI.get(j), nearByList);
                if (PClPoint < mPCL_Sj.getPclValue()) {
                    mPCL_Sj.setPclValue(PClPoint);
                }
            }

        } else { //TH 2 :  Diem Sj giao voi cac duong bien
            // Tim giao diem cua Sj voi cac lan can-----------------------------------------------------------------------
            listI.clear();
            for (int j = 0; j < nearByList.size(); j++) {
                if (node != nearByList.get(j) && Distance[node][nearByList.get(j)] < 2 * Rs) {
                    if ((mListSensorNodes.get(node).getY() != mListSensorNodes.get(nearByList.get(j)).getY()) || (mListSensorNodes.get(node).getX() != mListSensorNodes.get(nearByList.get(j)).getX())) {
                        FloatPointItem n1 = new FloatPointItem();
                        FloatPointItem n2 = new FloatPointItem();
                        findIntersection_TwoCircle(node, nearByList.get(j), n1, n2);
                        //Giao diem voi lan can nam trong mang
                        if (CheckPoint_InRectange(n1, P1.getX(), P1.getY(), P4.getX(), P4.getY())) {
                            listI.add(n1);
                        }
                        if (CheckPoint_InRectange(n2, P1.getX(), P1.getY(), P4.getX(), P4.getY())) {
                            listI.add(n2);
                        }
                    }
                }

            }

            // Tim Giao diem cua Sj voi cac canh duong bien----------------------------------------------------------------
            List<FloatPointItem> ListK = new ArrayList<>(); // Giao diem cua Sj voi cac canh duong bien
            boolean[] intersect = new boolean[5];
            ListK.clear();
            //Canh tren
            intersect[1] = Find_Interaction_FirstEdge(mListSensorNodes.get(node), P1, P3, ListK);
            //Canh dưoi
            intersect[2] = Find_Interaction_FirstEdge(mListSensorNodes.get(node), P2, P4, ListK);
            //Canh trai
            intersect[3] = Find_Interaction_SecondEdge(mListSensorNodes.get(node), P1, P2, ListK);
            //Canh phai
            intersect[4] = Find_Interaction_SecondEdge(mListSensorNodes.get(node), P3, P4, ListK);


            // Find PCL of Sj----------------------------------------------------------------------------------------------------------
            // Xet cac giao diem cua Sj voi lan can
            for (int j = 0; j < listI.size(); j++) {
                int PClPoint = CheckPoint_Corvering_bySet(listI.get(j), nearByList);
                if (PClPoint < mPCL_Sj.getPclValue()) {
                    mPCL_Sj.setPclValue(PClPoint);
                }
            }
            // Xet giao diem cua Sj voi cac duong bien
            for (int j = 0; j < ListK.size(); j++) {
                int PClPoint = CheckPoint_Corvering_bySet(ListK.get(j), nearByList);
                if (PClPoint < mPCL_Sj.getPclValue()) {
                    mPCL_Sj.setPclValue(PClPoint);
                }
            }

        }
        // Return mPCL of node 
        return mPCL_Sj;
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
    
        //Check Point nam trong duong tron tam (Ix,Iy) ban kinh r
    boolean CheckPoint_InCircle(FloatPointItem point,float Ix, float Iy, float R) {
        if (point.getX() <0  || point.getY() <0) return false;
        if (calculateDistance(Ix, Iy, point.getX(), point.getY()) < R) {
            return true;
        }
        return false;
    }
    //Check Point inside Rectange
    boolean CheckPoint_InRectange(FloatPointItem point, float x1, float y1, float x2, float y2) {
        float maxX, minX, maxY, minY;
        if (x1 > x2) {
            maxX = x1;
            minX = x2;
        } else {
            maxX = x2;
            minX = x1;
        }
        if (y1 > y2) {
            maxY = y1;
            minY = y2;
        } else {
            maxY = y2;
            minY = y1;
        }
        if (point.getX() > maxX || point.getX() < minX) {
            return false;
        }
        if (point.getY() > maxY || point.getY() < minY) {
            return false;
        }
        return true;
    }
    
    //Check interraction between round and rectangular edge
    // Canh tren va duoi ( startPoint.getY == endpoint.getY
    boolean Find_Interaction_FirstEdge(NodeItem roundPoint, FloatPointItem startPoint, FloatPointItem endPoint, List<FloatPointItem> resultPoint) {
        if (Math.abs(roundPoint.getY()-startPoint.getY()) >= Rs) {
            return false;
        } else {
            //Ton tai giao diem
            //Phuong trinh
            //X^2 -2*x0*X +x0^2 + (Y-y0)^2 - Rs^2 =0;
            // c = x0^2 + (Y-y0)^2 - Rs^2
            float c = roundPoint.getX()*roundPoint.getX() + (startPoint.getY()-roundPoint.getY())*(startPoint.getY()-roundPoint.getY()) - Rs *Rs;
            float delta = roundPoint.getX()*roundPoint.getX() - c;
            if (delta >= 0) {
                float X1 = roundPoint.getX() + (float)Math.sqrt(delta);
                float X2 = roundPoint.getX() - (float)Math.sqrt(delta);
                if (X1 >= startPoint.getX() && X1 <= endPoint.getX()) {
                    resultPoint.add(new FloatPointItem(X1,startPoint.getY()));
                }
                if (X2 >= startPoint.getX() && X2 <= endPoint.getX()) {
                    resultPoint.add(new FloatPointItem(X2,startPoint.getY()));
                }
            }
            return true;
        }
    }
    // Canh ben trai va ben phai (startPoint.getX == endpoint.getX
    boolean Find_Interaction_SecondEdge(NodeItem roundPoint, FloatPointItem startPoint, FloatPointItem endPoint, List<FloatPointItem> resultPoint) {
        if (Math.abs(roundPoint.getX()-startPoint.getX()) >= Rs) {
            return false;
        } else {
            //Ton tai giao diem
            //Phuong trinh
            //Y^2 -2*y0*Y +y0^2 + (X-x0)^2 - Rs^2 =0;
            // c = y0^2 + (X-x0)^2 - Rs^2
            float c = roundPoint.getY()*roundPoint.getY() + (startPoint.getX()-roundPoint.getX())*(startPoint.getX()-roundPoint.getX()) - Rs *Rs;
            float delta = roundPoint.getY()*roundPoint.getY() - c;
            if (delta >= 0) {
                float Y1 = roundPoint.getY() + (float)Math.sqrt(delta);
                float Y2 = roundPoint.getY() - (float)Math.sqrt(delta);
                if (Y1 >= startPoint.getY() && Y1 <= endPoint.getY()) {
                    resultPoint.add(new FloatPointItem(startPoint.getX(),Y1));
                }
                if (Y2 >= startPoint.getY() && Y2 <= endPoint.getY()) {
                    resultPoint.add(new FloatPointItem(startPoint.getX(),Y2));
                }
            }
            return true;
        }
    }
    //Tim giao diem 2 duong tron
    void findIntersection_TwoCircle(int point_u, int point_v, FloatPointItem mP1, FloatPointItem mP2) {
        //Giai phuong trinh
        //2(x1-x0)*X + 2(y1-y0)*Y = x1^2 -x0^2 +y1^2-y0^2
        //(X-x0)^2+ (Y-y0)^2 = R^2
        double x0 = mListSensorNodes.get(point_u).getX();
        double y0 = mListSensorNodes.get(point_u).getY();
        double x1 = mListSensorNodes.get(point_v).getX();
        double y1 = mListSensorNodes.get(point_v).getY();
        if (x0 == x1) {
            double ny = (x1 * x1 - x0 * x0) / (2 * y1 - 2 * y0) + (y1 + y0) / 2;

            double c = x0 * x0 + (ny - y0) * (ny - y0) - Rs * Rs;
            double Delta = 4 * x0 * x0 - 4 * c;

            // Giai phuong trinh
            // Nghiem 1
            double nx1 = (2 * x0 + Math.sqrt(Delta)) / 2;
            mP1.setX((float)nx1);
            mP1.setY((float)ny);

            // Nghiem 2
            double nx2 = (2 * x0 - Math.sqrt(Delta)) / 2;
            mP2.setX((float)nx2);
            mP2.setY((float)ny);

        } else {
            double a = (x0 + x1) / 2 + (y1 * y1 - y0 * y0) / (2 * x1 - 2 * x0);
            double b = (y0 - y1) / (x1 - x0);

            double a1 = b * b + 1;
            double a2 = (2 * a * b) - (2 * x0 * b )- (2 * y0);
            double a3 = (a * a) - (2 * x0 * a) + (x0 * x0) + (y0 * y0) - (Rs * Rs);

            double Delta = a2 * a2 - 4 * a1 * a3;

            // Giai phuong trinh
            // Nghiem 1
            double ny1 = (-a2 +  Math.sqrt(Delta)) / (2 * a1);
            double nx1 = a + b * ny1;
            mP1.setX((float)nx1);
            mP1.setY((float)ny1);

            // Nghiem 2
            double ny2 = (-a2 - Math.sqrt(Delta)) / (2 * a1);
            double nx2 = a + b * ny2;
            mP2.setX((float)nx2);
            mP2.setY((float)ny2);

        }
    }

    public  float calculateDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    
    public int getCoverageLevel(List<PCLItem> listCi) {
        if (listCi.isEmpty()) return 0;
        int result =  listCi.get(0).getPclValue();
        for (int i =1;i < listCi.size();i++) {
            if(result > listCi.get(i).getPclValue()) result = listCi.get(i).getPclValue();
        }
        return result;
    }
    
    public void updateCoverageLevel(List<PCLItem> listCi , int node, int MODE) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0;i< listCi.size();i++) {
           list.add(listCi.get(i).getId());
        }
        switch (MODE) {
            case ADD_MODE: /// Add new node in listCi
                if (list.isEmpty()) {
                    listCi.add(new PCLItem(node, 0));
                } else {
                    //Update 
                    List<Integer> nearByList = new ArrayList<>();
                    PCLItem tempPCL;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) != node && Distance[list.get(i)][node] <= 2 * Rs) {
                            nearByList.add(list.get(i));
                        }
                    }
                    tempPCL = findPCLPoint(nearByList, node);
                    listCi.add(tempPCL);
                    
                    // Update PCL of other node
                    for (int i = 0; i < nearByList.size(); i++) {
                        List<Integer> tempNearByList = findNearByList(list,nearByList.get(i),node);
                        PCLItem temp = findPCLPoint(tempNearByList, nearByList.get(i));
                        //Change value of other node
                        for (int j =0;j<listCi.size();j++) {
                            if (listCi.get(j).getId() == temp.getId()) {
                                listCi.get(j).setPclValue(temp.getPclValue());
                            }
                        }
                        
                    }
                }
                
                break;
            case REMOVE_MODE: // Remove node in ListCi    
                for (int i =0;i< listCi.size();i++) {
                    if (listCi.get(i).getId()== node) {
                        listCi.remove(i);
                        list.remove(i);
                        break;
                    }
                }
                // Find nearByList of node
                List<Integer> nearByList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) != node && Distance[list.get(i)][node] <= 2 * Rs) {
                        nearByList.add(list.get(i));
                    }
                }
                
                //Update PCL node in nearbyNode
                for (int i = 0; i < nearByList.size(); i++) {
                    List<Integer> tempByList = new ArrayList<>();
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j) != nearByList.get(i) && Distance[list.get(j)][nearByList.get(i)] <= 2 * Rs) {
                            tempByList.add(list.get(j));
                        }
                    }
                    
                    //Update node in nearbyNode
                    PCLItem temp = findPCLPoint(tempByList, nearByList.get(i));
                    for (int j = 0; j < listCi.size(); j++) {
                        if (listCi.get(j).getId() == temp.getId()) {
                            listCi.get(j).setPclValue(temp.getPclValue());
                        }
                    }
                }
                
                
                
                break;
            default: break;
        }

    }
    public List<Integer> findNearByList(List<Integer> listSensor, int node, int nodeAdd) {
        List<Integer> nearByList = new ArrayList<>();
        for (int i = 0; i < listSensor.size(); i++) {
            if (listSensor.get(i) != node && Distance[listSensor.get(i)][node] <= 2 * Rs) {
                nearByList.add(listSensor.get(i));
            }
        }
        //Check with node add
        if (nodeAdd != node && Distance[nodeAdd][node] <= 2 * Rs) {
                nearByList.add(nodeAdd);
        }
        return nearByList;
    }
    
     public void runAlgorithm() {
         //Code in here
         //Input : Rs Value, TimeLife , Num ( Soluong sensor) , mListSensorNodes : List cac node sensor (NodeItem)
         // Output : resultListX ( List ket qua cac Xi thong qua position cua mListSensorNodes)
         //          resultListT (Thoi gian on của tap hop Xi) 
         System.out.println("Run algorithm");
         // Step 1: Find PCL of all sensorList and sort PCL of sensor follow non-descend
         findPCLAllPoint(ListNearBy, mListPCLSensor);
         for (int i = 0; i < mListPCLSensor.size(); i++) {
             mListPCLSensorCommon.add(new PCLItem(mListPCLSensor.get(i).getId(), mListPCLSensor.get(i).getPclValue()));
         }
         Collections.sort(mListPCLSensor, new Comparator<PCLItem>() {
            @Override
            public int compare(PCLItem o1, PCLItem o2) {
                int x1 = o1.getPclValue();
                int x2 = o2.getPclValue();
                return Integer.compare(x1, x2);
            }
        });
         
         //Step 2: Vong lap while
         List<PCLItem> listCi = new ArrayList<>();
         while (!mListPCLSensor.isEmpty()) {
             
             int covi = getCoverageLevel(listCi);
             System.out.println(" Covi ="+covi);
             if (covi < 1) {
                 int node = mListPCLSensor.get(0).getId();
                 //Add node to listCi
                 updateCoverageLevel(listCi, node, ADD_MODE);
                 // Remove node from mListPCLSensor
                 System.out.println("Remove mListPCLSensor :"+mListPCLSensor.get(0).getId() + " size="+mListPCLSensor.size());
                 mListPCLSensor.remove(0);
                 
             } else {
                 //Thuat toan PruneGreedySelection(k,S,Ci)
                 PruneGreedySelection(mListPCLSensor,listCi);
                 //Add listCi to resultListC
                 List<Integer> listX = new ArrayList<>();
                 for (int j = 0; j < listCi.size(); j++) {
                     listX.add(listCi.get(j).getId());
                 }
                 resultListX.add(listX);
                 System.out.println("Found a ListXi : size ="+listX.size());
                 //Khoi tao list Ci tiep theo
                 listCi = new ArrayList<>();

             }
         }
         
         //Xet TH toan bo vua du
         int covi = getCoverageLevel(listCi);
         if (covi == 1) {
             List<Integer> listX = new ArrayList<>();
             for (int j = 0; j < listCi.size(); j++) {
                 listX.add(listCi.get(j).getId());
             }
             resultListX.add(listX);
         }
         
         for (int j = 0; j<resultListX.size();j++) {
             resultListT.add(mTimeLife);
         }

       
     }
     
     
     public void PruneGreedySelection(List<PCLItem> ListPCLSensor, List<PCLItem> listCi ) {
         //Coppy listCi to list
        List<Integer> list = new ArrayList<>();
        for (int i = 0;i< listCi.size();i++) {
           list.add(listCi.get(i).getId());
        }
        // Vong lap voi moi phan tu list
         for (int i = 0; i < list.size(); i++) {
             //Remove phan tu i cua listCi
             int node = list.get(i);
             updateCoverageLevel(listCi,node , REMOVE_MODE);
             System.out.println("PruneGreedySelection update node ="+node);
             //getCovi of listCi
             int covi = getCoverageLevel(listCi);
             
             if (covi >= 1) {
                 //Add Si to mListPCLSensor;
                 PCLItem mPCLItem = new PCLItem(node, mListPCLSensorCommon.get(node).getPclValue());
                 ListPCLSensor.add(mPCLItem);
                 
             } else {
                 //Add Si to back listCi
                 updateCoverageLevel(listCi, node, ADD_MODE);
             }

         }
         //Sort List PCL
          Collections.sort(ListPCLSensor, new Comparator<PCLItem>() {
            @Override
            public int compare(PCLItem o1, PCLItem o2) {
                int x1 = o1.getPclValue();
                int x2 = o2.getPclValue();
                return Integer.compare(x1, x2);
            }
        });
         
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

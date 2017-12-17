/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import common.SensorUtility;
import static common.SensorUtility.*;
import ilog.concert.*;
import ilog.cplex.*;
import static iterface.algorithm.frameMyAlgorithm2.ListSensor;
import static iterface.frameMain.coordinatePanel;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FloatPointItem;
import model.ListSetItem;
import model.NodeItem;

/**
 *
 * @author sev_user
 */
public class MyAlgorithm2 {

    double mTimeLife;
    float Rs, Rt;// Rs and Rt value
    int mLvalue;
    List<List<Integer>> resultListX;
    List<Double> resultListT;
    int countA =0;
    public float Distance[][];
    int Num;// Number sensor
    List<List<Integer>> ListNearBy;
    public MyAlgorithm2() {
    }
    
    public void run() {
        
        init();

        readData();
        //Step 1: Find target-covering Sensor
        //FindTargetCoveringSensor();
        
        //Step 2: 
//        createMatrixDistance();
//        
        countA =0;
        runAlgorithm();
        
        CoppyToListSensor();
        System.out.println("Count "+countA);
       //freeData();
    }
    
    public void init() {
        resultListX = new ArrayList<>();
        resultListT = new ArrayList<>();
        ListNearBy = new ArrayList<>();
        
    }
    public void readData() {
        // Read Rs, Rt
        Rs = SensorUtility.mRsValue;
        Rt = SensorUtility.mRtValue;
        mLvalue = SensorUtility.Lvalue;
        mTimeLife = SensorUtility.LifeTimeOfSensor;
        Num = mListSensorNodes.size();
        Distance = new float[Num+1][Num+1];
        
        for (int i =0;i<Num;i++) {
            for (int j =0;j<=i;j++) {
                if (i==j ) {
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
    
    public void showViewTest(List<Integer> listSensor) {                                            
        // TODO add your handling code here:
        //Clear data
        for (int j = 0; j < mListSensorNodes.size(); j++) {
            mListSensorNodes.get(j).setStatus(0);
        }

        for (int i =0;i<listSensor.size();i++) {
           //Change Value On foreach Sensor   
            mListSensorNodes.get(listSensor.get(i)).setStatus(1);
            
        }
        coordinatePanel.refresh();
    }    
    
    public void runAlgorithm() {
        FloatPointItem UpLeftCornerPoint = new FloatPointItem(0,0);
        FloatPointItem DownRightCornerPoint = new FloatPointItem(SensorUtility.numberRow-1,SensorUtility.numberColum-1);
        
        List<List<List<Integer>>> ListOfListX = new ArrayList<List<List<Integer>>>();
        List<List<Double>> ListOfListT = new ArrayList<>();

        for (int i =0; i< SensorUtility.Lvalue ;i++) {
            List<List<Integer>> tempReturnListX = new ArrayList<>();
            List<Double> tempReturnListT = new ArrayList<>();
            
            DiviceNetworkFollowWidth(UpLeftCornerPoint,DownRightCornerPoint,i,tempReturnListX,tempReturnListT);
            ListOfListX.add(tempReturnListX);
            ListOfListT.add(tempReturnListT);
        }
        Combining_All_Division(ListOfListX,ListOfListT,resultListX,resultListT);
        
        //Free data
        ListOfListX = null;
        ListOfListT = null;
    }
    
    public  void DiviceNetworkFollowWidth(FloatPointItem UpLeftCornerPoint, FloatPointItem DownRightCornerPoint, int divisons, List<List<Integer>> returnListX, List<Double> returnListT) {
        FloatPointItem upPoint = new FloatPointItem();
        FloatPointItem downPoint = new FloatPointItem();
        
        List<List<List<Integer>>> tempListOfListX = new ArrayList<>();
        List<List<Double>> tempListOfListT = new ArrayList<>();
        List<List<Integer>> tempListX;
        List<Double> tempListT;
        
        List<List<List<Integer>>> temp2ListOfListX = new ArrayList<>();
        List<List<Double>> temp2ListOfListT = new ArrayList<>();

        
        float PostionX = UpLeftCornerPoint.getX();
        float MaxPostionX = DownRightCornerPoint.getX();
        boolean isFirstBlock = true;
        
        
        while (PostionX < MaxPostionX)  {
            //Set upoint and downpoint of Block
            if (isFirstBlock && divisons != 0) {
                //Start Point of Block independent of offset value
                upPoint.setXY(PostionX, UpLeftCornerPoint.getY());
                PostionX += divisons * 2 * Rs;
                if (PostionX >= MaxPostionX) PostionX = MaxPostionX;
                downPoint.setXY(PostionX, DownRightCornerPoint.getY());
                isFirstBlock = false;
            } else {
                
                upPoint.setXY(PostionX, UpLeftCornerPoint.getY());
                PostionX += mLvalue*2 * Rs;
                if (PostionX >= MaxPostionX) PostionX = MaxPostionX;
                downPoint.setXY(PostionX, DownRightCornerPoint.getY());
            }
            
            //Caculate result of the Block foreach devisions
            for (int i =0; i<mLvalue;i++) {
                List<List<Integer>> temp2returnListX = new ArrayList<>();
                List<Double> temp2returnListT = new ArrayList<>();
                DiviceNetWorkFollowHeight(upPoint,downPoint,i,temp2returnListX,temp2returnListT);
                temp2ListOfListX.add(temp2returnListX);
                temp2ListOfListT.add(temp2returnListT);
            }
            tempListX = new ArrayList<>();
            tempListT = new ArrayList<>();
            Combining_All_Division(temp2ListOfListX, temp2ListOfListT, tempListX, tempListT);
            temp2ListOfListX.clear();
            temp2ListOfListT.clear();
            tempListOfListX.add(tempListX);
            tempListOfListT.add(tempListT);
        }
        
        //Combining all strips follow the with of netwwork
        Combining_All_Strips(tempListOfListX, tempListOfListT, returnListX, returnListT);
        
        //Free Data
        tempListOfListX = null;
        tempListOfListT = null;
        tempListX = null;
        tempListT = null;
        temp2ListOfListX = null;
        temp2ListOfListT = null;
    }
    
    public void DiviceNetWorkFollowHeight(FloatPointItem UpLeftCornerPoint, FloatPointItem DownRightCornerPoint, int division, List<List<Integer>> returnListX, List<Double> returnListT) {
        
        FloatPointItem upPoint = new FloatPointItem();
        FloatPointItem downPoint = new FloatPointItem();
        
        List<List<List<Integer>>> tempListOfListX = new ArrayList<>();
        List<List<Double>> tempListOfListT = new ArrayList<>();
        List<List<Integer>> tempListX;
        List<Double> tempListT;

        float PostionY = UpLeftCornerPoint.getY();
        float MaxPostionY = DownRightCornerPoint.getY();
        boolean isFirstBlock = true;
        
        while (PostionY < MaxPostionY)  {
            //Set upoint and downpoint of Block
            if (isFirstBlock && division != 0) {
                //Start Point of Block independent of divison value
                upPoint.setXY(UpLeftCornerPoint.getX(), PostionY);
                PostionY += division * 2 * Rs;
                if (PostionY >= MaxPostionY) PostionY = MaxPostionY;
                downPoint.setXY(DownRightCornerPoint.getX(), PostionY);
                isFirstBlock = false;
            } else {
                
                upPoint.setXY(UpLeftCornerPoint.getX(), PostionY);
                PostionY += mLvalue*2 * Rs;
                if (PostionY >= MaxPostionY) PostionY = MaxPostionY;
                downPoint.setXY(DownRightCornerPoint.getX(), PostionY);
            }

            //Find ListSensor in Block
            List<Integer> tempListSensor = FindListSensor(upPoint,downPoint);
            
            //Find set X in Block
            tempListX= new ArrayList<>();
            FindSetX(tempListSensor, upPoint,downPoint,tempListX);
            
            //Find set Time foreach SetX%
            tempListT = LinearProAlgorithm(tempListX,tempListSensor,mTimeLife);
            
            //Add result of Block
            tempListOfListX.add(tempListX);
            tempListOfListT.add(tempListT);
        }
        Combining_All_Strips(tempListOfListX, tempListOfListT, returnListX, returnListT);
        
        //Free Data
        tempListOfListX = null;
        tempListOfListT = null;        
        upPoint = null;
        downPoint = null;
    }
    
    public List<Integer> FindListSensor(FloatPointItem UpLeftCornerPoint, FloatPointItem DownRightCornerPoint) {
        List<Integer> resultListSensor = new ArrayList<>();
        float Xmax,Xmin,Ymax,Ymin;
        Xmin = UpLeftCornerPoint.getX() - Rs;
        Xmax=  DownRightCornerPoint.getX() + Rs;
        Ymin = UpLeftCornerPoint.getY() - Rs;
        Ymax = DownRightCornerPoint.getY() +Rs;
        
        for (int i = 0 ;i<mListSensorNodes.size(); i++) {
            if (mListSensorNodes.get(i).getX() >= Xmin && mListSensorNodes.get(i).getX() < Xmax && mListSensorNodes.get(i).getY() >= Ymin && mListSensorNodes.get(i).getY() < Ymax ) {
                resultListSensor.add(i);
            }
        }
        return resultListSensor;        
    }
    
public List<Double> LinearProAlgorithm(List<List<Integer>> listX, List<Integer> listSenSor, double valueT) {
    List<Double> time = new ArrayList<>();
    int m = listX.size();
    int n = listSenSor.size();
    //Test
//    if (m == 1) {
//        time.add(valueT);
//    } else if (m == 2) {
//        time.add(valueT);
//        time.add(valueT);
//    } else {
//        time.add(valueT);
//        time.add(valueT);
//        time.add(valueT);
//    }
        int[][] a = new int[n][m];

        //Check Input
        for (int i = 0; i < n; i++) {
            int sensor = listSenSor.get(i);
            for (int j = 0; j < m; j++) {
                a[i][j] = 0;
                List<Integer> Xj = listX.get(j);
                for (int k = 0; k < Xj.size(); k++) {
                    if (sensor == Xj.get(k)) {
                        a[i][j] = 1;
                        break;
                    }
                }
            }
        }

        try {
            //Define new model
            IloCplex cplex = new IloCplex();

            //Variable
            IloNumVar[] T = new IloNumVar[m];
            for (int i = 0; i < m; i++) {
                T[i] = cplex.numVar(0, Double.MAX_VALUE);
            }

            //Expression
            IloLinearNumExpr[] totalTimeOnExpr = new IloLinearNumExpr[n];
            for (int j = 0; j < n; j++) {
                totalTimeOnExpr[j] = cplex.linearNumExpr();

                for (int i = 0; i < m; i++) {
                    totalTimeOnExpr[j].addTerm(a[j][i], T[i]);
                }
            }

            IloLinearNumExpr objective = cplex.linearNumExpr();
            for (int i = 0; i < m; i++) {
                objective.addTerm(1, T[i]);
            }

            //Define Objective
            cplex.addMaximize(objective);

            //Constraints
            for (int i = 0; i < n; i++) {
                cplex.addLe(totalTimeOnExpr[i], valueT);
            }

            cplex.setParam(IloCplex.Param.Simplex.Display, 0);
            //Resolve Model
            if (cplex.solve()) {
                for (int i = 0; i < m; i++) {
                    time.add(cplex.getValue(T[i]));
                    System.out.println("time: " + cplex.getValue(T[i]));
                }
                System.out.println("value: " + cplex.getObjValue());
            } else {
                System.out.println("Problem not solved");
            }

            cplex.end();

        } catch (IloException ex) {
            Logger.getLogger("LeHieu").log(Level.SEVERE, null, ex);
        }
        return time;
    }
    
    public void FindSetX(List<Integer> listSensor, FloatPointItem UpLeftCornerPoint, FloatPointItem DownRightCornerPoint, List<List<Integer>> returListX) {
        //Check trong vung dang xet
        if (UpLeftCornerPoint.getX() > SensorUtility.MaxColum) {
            UpLeftCornerPoint.setX(SensorUtility.MaxColum);
        }
        if (DownRightCornerPoint.getX() > SensorUtility.MaxColum) {
            DownRightCornerPoint.setX( SensorUtility.MaxColum);
        }
        if (UpLeftCornerPoint.getY() > SensorUtility.MaxRow) {
            UpLeftCornerPoint.setY(SensorUtility.MaxRow);
        }
        if (DownRightCornerPoint.getY() > SensorUtility.MaxRow) {
            DownRightCornerPoint.setY(SensorUtility.MaxRow);
        }
        //Calculate Rectangular area
        float SA = Math.abs((UpLeftCornerPoint.getX() - DownRightCornerPoint.getX()) * (UpLeftCornerPoint.getY() - DownRightCornerPoint.getY()));
        float Str = (float) Math.PI * Rs*Rs;
        int minSensor = (int) (SA / Str) + 1 ;
        returListX.clear();
        int MaxSet = 0;

        //Cach 1: Dung to hop
//        for (int i = minSensor; i <= listSensor.size(); i++) {
//            //Tinh to hop chap i cua ListSize
//            Combination(listSensor, listSensor.size(), i, UpLeftCornerPoint, DownRightCornerPoint, returListX, MaxSet);
//            MaxSet = returListX.size();
//
//        }
        //Cach 2 :
        List<List<Integer>> retuListX = new ArrayList<>();
        FindSet(listSensor,UpLeftCornerPoint,DownRightCornerPoint,returListX);
        Collections.sort(returListX, new Comparator<List<Integer>>(){
            @Override
               public int compare(List<Integer> o1, List<Integer> o2) {
                   int size1 = o1.size();
                   int size2 = o2.size();
                   
                   return Integer.compare(size1, size2);
               }
            
        });
        int a=2;
    }

    //=========-Suw dung cach 2====================================
    List<ListSetItem> mListSet = new ArrayList<>();
    void FindSet(List<Integer> listSensor,FloatPointItem P1, FloatPointItem P4, List<List<Integer>> returListX ) {
        //Init data
        int N = listSensor.size();
        mListSet.clear();
        List<Integer> X0 = new ArrayList<>();
        List<Integer> Y0 = new ArrayList<>();
        List<Integer> C0 = new ArrayList<>();
        for (int i =0;i<listSensor.size();i++) {
            X0.add(listSensor.get(i));
        }
        ListSetItem headSetItem = new ListSetItem(X0, Y0, C0);
        mListSet.add(headSetItem);
        //
        while(!mListSet.isEmpty()){
            ListSetItem headItem = mListSet.get(0);
            List<Integer> tempXi = headItem.getXi();
            List<Integer> tempYi = headItem.getYi();
            List<Integer> tempCi = headItem.getCi();
            
            //Test
            showViewTest(tempXi);
            boolean[] status = new boolean[tempXi.size()];
            int count =0;
            for (int i =0;i <tempXi.size();i++) {
                //Check tinh phu khi loai bo cac phan tu i 
                if (!CheckPointExitInCi(tempCi,tempXi.get(i)) && CheckPointCorveringBySet(tempXi, tempXi.get(i), P1, P4)) {
                    status[i] = true;
                    count++;
                }
            }
            if(count==0) {
                //Khong the loai bo phan tu nao
               
                returListX.add(tempXi);
                mListSet.remove(0);
                
            } else {
                //Ton tai phan tu co the loai bo
                for(int j =0;j <tempXi.size();j++) {
                    if (status[j]) {
                        //Add them phan tu vao List
                        ListSetItem setItem = new ListSetItem();
                        setItem.setXi(tempXi,j);
                        setItem.setYi(tempYi,tempXi.get(j));
                        //Nen Kiem tra xem da ton tai chua
                       if(!CheckExitSet(mListSet,setItem)) {
                           setItem.setCi(tempCi);
                           for (int k =0;k<tempXi.size();k++) {
                               if (!status[k]) setItem.addCi(tempXi.get(k));
                           }
                           mListSet.add(setItem);
                        }
                    }
                } 
                mListSet.remove(0);
            }
        }
    }
    
    boolean CheckPointExitInCi(List<Integer> listSensorCi, int point){
        for (int i = 0; i < listSensorCi.size(); i++) {
            if (listSensorCi.get(i)== point) {
                return true;
            }
        }
        return false;
    }
    boolean CheckPointCorveringBySet(List<Integer> listSensor, int exception, FloatPointItem P1, FloatPointItem P4) {
        FloatPointItem P2 = new FloatPointItem(P1.getX(), P4.getY());
        FloatPointItem P3 = new FloatPointItem(P4.getX(), P1.getY());
        //Tim lan can cua exception
        List<Integer> nearByList = new ArrayList<>();
        List<FloatPointItem> listI = new ArrayList<>();// List giao diem duong trong voi duong tron lan can
        List<FloatPointItem> listY = new ArrayList<>();// List giao diem giua cac duong tron lan can voi nhau
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < listSensor.size(); i++) {
            if (listSensor.get(i) != exception) {
                list.add(listSensor.get(i));
                if (Distance[listSensor.get(i)][exception] < 2 * Rs) {
                    nearByList.add(listSensor.get(i));
                }
            }
        }
        
        if (Math.abs(mListSensorNodes.get(exception).getX() - P1.getX()) > Rs
                && Math.abs(mListSensorNodes.get(exception).getX() - P3.getX()) > Rs
                && Math.abs(mListSensorNodes.get(exception).getY() - P1.getY()) > Rs
                && Math.abs(mListSensorNodes.get(exception).getY() - P2.getY()) > Rs) {
            //Point nam ben trong 
            // Tim giao diem cua lan can
            for (int i = 0; i < nearByList.size(); i++) {
                for (int j =0;j<i;j++) {
                    if (i != j ) {
                        if (Distance[nearByList.get(i)][nearByList.get(j)] < 2*Rs) {
                             FloatPointItem n1 = new FloatPointItem();
                             FloatPointItem n2 = new FloatPointItem();
                             findIntersection_TwoCircle(nearByList.get(j), nearByList.get(i), n1, n2);
                             if (CheckPoint_InCircle(n1,mListSensorNodes.get(exception).getX(), mListSensorNodes.get(exception).getY(),Rs)) {
                                 listY.add(n1);
                             }
                             if (CheckPoint_InCircle(n2,mListSensorNodes.get(exception).getX(), mListSensorNodes.get(exception).getY(), Rs)) {
                                 listY.add(n2);
                             }
                        }
                    }
                }
            }
            
            //Check cac giao diem co phu khong
            for (int i =0;i<listY.size();i++) {
                if (!CheckPoint_Corvering_bySetX(listY.get(i),nearByList)) {
                   return false; 
                }
            }
            
            
            
            // Tim giao diem cua Sj voi cac lan can
//            for (int i = 0; i < nearByList.size(); i++) {
//                FloatPointItem n1 = new FloatPointItem();
//                FloatPointItem n2 = new FloatPointItem();
//                findIntersection_TwoCircle(exception, nearByList.get(i), n1, n2);
//                listI.add(n1);
//                listI.add(n2);
//            }
//            // Tim giao diem cua Sj voi cac lan can
//            for (int i =0;i<listI.size();i++) {
//                if (!CheckPoint_Corvering_bySetX(listI.get(i),nearByList)) {
//                   return false; 
//                }
//            }
            
        } else {

            
           // Giao diem cua cac lan can
           for (int i = 0; i < nearByList.size(); i++) {
                for (int j =0;j<i;j++) {
                    if (i != j ) {
                        if (Distance[nearByList.get(i)][nearByList.get(j)] < 2*Rs) {
                             FloatPointItem n1 = new FloatPointItem();
                             FloatPointItem n2 = new FloatPointItem();
                             findIntersection_TwoCircle(nearByList.get(j), nearByList.get(i), n1, n2);
                             if (CheckPoint_InCircle(n1,mListSensorNodes.get(exception).getX(), mListSensorNodes.get(exception).getY(),Rs) && CheckPoint_InRectange(n1,P1.getX(),P1.getY(),P4.getX(),P4.getY())) {
                                 listY.add(n1);
                             }
                             if (CheckPoint_InCircle(n2,mListSensorNodes.get(exception).getX(), mListSensorNodes.get(exception).getY(), Rs)&& CheckPoint_InRectange(n2,P1.getX(),P1.getY(),P4.getX(),P4.getY())) {
                                 listY.add(n2);
                             }
                        }
                    }
                }
            }
           //Check cac giao diem lan can co phu khong
            for (int i =0;i<listY.size();i++) {
                if (!CheckPoint_Corvering_bySetX(listY.get(i),nearByList)) {
                   return false; 
                }
            }
            //---------------------------------------///
            // Tim giao diem cua Sj voi cac lan can
//            for (int i = 0; i < nearByList.size(); i++) {
//                FloatPointItem n1 = new FloatPointItem();
//                FloatPointItem n2 = new FloatPointItem();
//                findIntersection_TwoCircle(exception, nearByList.get(i), n1, n2);
//                if (CheckPoint_InRectange(n1,P1.getX(),P1.getY(),P4.getX(),P4.getY())) {
//                   listI.add(n1);
//                }
//                if (CheckPoint_InRectange(n2,P1.getX(),P1.getY(),P4.getX(),P4.getY())) {
//                   listI.add(n2);
//                }
//
//            }
//            // Check giao diem cua Sj voi cac lan can
//            for (int i =0;i<listI.size();i++) {
//                if (!CheckPoint_Corvering_bySetX(listI.get(i),nearByList)) {
//                   return false; 
//                }
//            }
           //---------------------------------------///
           //point giao nhau
           List<FloatPointItem> Edge = new ArrayList<>();
           boolean[] intersect = new boolean[5];
           //Canh tren
           intersect[1] = Find_Interaction_FirstEdge(mListSensorNodes.get(exception),P1,P3,Edge);
           //Canh dưoi
           intersect[2] = Find_Interaction_FirstEdge(mListSensorNodes.get(exception),P2,P4,Edge);
           //Canh trai
           intersect[3] = Find_Interaction_SecondEdge(mListSensorNodes.get(exception), P1, P2, Edge);
           //Canh phai
           intersect[4] = Find_Interaction_SecondEdge(mListSensorNodes.get(exception), P3, P4, Edge);
            // Check tinh phu cua truong tron va cac canh
            for (int i =0;i<Edge.size();i++) {
                if (!CheckPoint_Corvering_bySetX(Edge.get(i),nearByList)) {
                   return false; 
                }
            }
            //---------------------------------------///
            //Check giao diem cua cac lan can voi cac canh cau khoi
            List<FloatPointItem> nearByEdge = new ArrayList<>();
            FloatPointItem tempPoint;
            for (int i = 0; i < nearByList.size(); i++) {
                //Giao voi canh tren
                if (intersect[1]) {
                    nearByEdge.clear();
                    Find_Interaction_FirstEdge(mListSensorNodes.get(nearByList.get(i)), P1, P3, nearByEdge);
                    for (int j = 0; j < nearByEdge.size(); j++) {
                        tempPoint = nearByEdge.get(j);
                        if (CheckPoint_InCircle(tempPoint,mListSensorNodes.get(exception).getX(), mListSensorNodes.get(exception).getY(),Rs) && !CheckPoint_Corvering_bySetX(tempPoint, nearByList)) {
                            return false;
                        }
                    }
                }
                //Giao voi canh duoi
                if (intersect[2]) {
                    nearByEdge.clear();
                    Find_Interaction_FirstEdge(mListSensorNodes.get(nearByList.get(i)),P2,P4,nearByEdge);
                    for (int j = 0; j < nearByEdge.size(); j++) {
                        tempPoint = nearByEdge.get(j);
                        if (CheckPoint_InCircle(tempPoint,mListSensorNodes.get(exception).getX(), mListSensorNodes.get(exception).getY(),Rs) && !CheckPoint_Corvering_bySetX(tempPoint, nearByList)) {
                            return false;
                        }
                    }
                }
                
                //Giao voi canh trai
                    if (intersect[3]) {
                    nearByEdge.clear();
                    Find_Interaction_SecondEdge(mListSensorNodes.get(nearByList.get(i)), P1, P2, nearByEdge);
                    for (int j = 0; j < nearByEdge.size(); j++) {
                        tempPoint = nearByEdge.get(j);
                        if (CheckPoint_InCircle(tempPoint,mListSensorNodes.get(exception).getX(), mListSensorNodes.get(exception).getY(),Rs) && !CheckPoint_Corvering_bySetX(tempPoint, nearByList)) {
                            return false;
                        }
                    }
                }
                //Giao voi canh phai
                if (intersect[4]) {
                    nearByEdge.clear();
                    Find_Interaction_SecondEdge(mListSensorNodes.get(nearByList.get(i)), P3, P4, nearByEdge);
                    for (int j = 0; j < nearByEdge.size(); j++) {
                        tempPoint = nearByEdge.get(j);
                        if (CheckPoint_InCircle(tempPoint,mListSensorNodes.get(exception).getX(), mListSensorNodes.get(exception).getY(),Rs) && !CheckPoint_Corvering_bySetX(tempPoint, nearByList)) {
                            return false;
                        }
                    }
                }

            }
           

        }

        return true;
    }
    //Check Point in duong tron tam (Ix,Iy) ban kinh r
    boolean CheckPoint_InCircle(FloatPointItem point,float Ix, float Iy, float R) {
        if (point.getX() <0  || point.getY() <0) return false;
        if (calculateDistance(Ix, Iy, point.getX(), point.getY()) < R) {
            return true;
        }
        return false;
    }
    boolean CheckExitSet(List<ListSetItem> listSet, ListSetItem Set) {
        ListSetItem tempList;
        for (int i =0;i < listSet.size();i++) {
            tempList = listSet.get(i);
            if(tempList.getYi().size() == Set.getYi().size()) {
                Collections.sort(tempList.getYi());
                Collections.sort(Set.getYi());
                int count =0;
                for(int j = 0;j<Set.getYi().size();j++) {
                    if (Objects.equals(tempList.getYi().get(j), Set.getYi().get(j))) {
                        count++;
                    } else break;
                }
                if (count == Set.getYi().size()) return true;
                
            } else if (tempList.getYi().size() > Set.getYi().size()) {
                return false;
            }
        }
        return false;
    }
    
    
    ///////////////////---------------------------------------------------///////////////////////////////////////
    public void Combination(List<Integer> listSensor, int N, int K, FloatPointItem P1, FloatPointItem P2, List<List<Integer>> returListX , int MaxSet) {
        int a[] = new int[N + 1];
        a[0] = 0;
        int i = 1;
        CombCallBack(listSensor, a, N, K, i, P1, P2, returListX,MaxSet);
    }

    void CombCallBack(List<Integer> listSensor, int a[], int N, int K, int i, FloatPointItem P1, FloatPointItem P2, List<List<Integer>> returListX,int MaxSet) {
        for (int j = a[i - 1] + 1; j <= N - K + i; j++) {
            a[i] = j;
            if (i == K) {
                //Print result
                ResultCombination(listSensor, a, K, P1, P2, returListX, MaxSet);
            } else {
                CombCallBack(listSensor, a, N, K, i + 1, P1, P2, returListX, MaxSet);
            }
        }
    }

    void ResultCombination(List<Integer> listSensor, int a[], int K, FloatPointItem P1, FloatPointItem P2, List<List<Integer>> returListX, int MaxSet) {
        List<Integer> listElement_Combination = new ArrayList<>();
        for (int i = 1; i <= K; i++) {
            listElement_Combination.add(listSensor.get(a[i] - 1));
        }
                //Check 4 canh nam ben trong 
     
        if (CheckPoint_Corvering_bySetX(P1, listElement_Combination) && CheckPoint_Corvering_bySetX(P2, listElement_Combination) && CheckPoint_Corvering_bySetX(new FloatPointItem(P1.getX(), P2.getY()), listElement_Combination) && CheckPoint_Corvering_bySetX(new FloatPointItem(P2.getX(), P1.getY()), listElement_Combination)) {

            if (!CheckExitSubSet(returListX, listElement_Combination, MaxSet)) {
                if (CheckSetXCorvering(listElement_Combination, P1.getX(), P1.getY(), P2.getX(), P2.getY())) {
                    //Add Xi vao List and check TH tap hop cha

                    returListX.add(listElement_Combination);
                    showViewTest(listElement_Combination);
                    int s = 2;
                }
            }
        }
    }
    
    boolean CheckExitSubSet(List<List<Integer>> childList, List<Integer> checkListX, int Max) {
        for (int i = 0; i < Max; i++) {
            List<Integer> tempList = childList.get(i);
            if (CheckIsSubSet(tempList, checkListX)) {
                return true;
            }
        }
        return false;
    }
     boolean CheckIsSubSet(List<Integer> childX, List<Integer> parentX ) {
         if (parentX.size() < childX.size()) return false;
         Collections.sort(parentX);
         Collections.sort(childX);

         int mP,mC;
         mP =0; mC =0;
         int count =0;
         while (mP < parentX.size() && mC < childX.size()) {
             if (Objects.equals(parentX.get(mP), childX.get(mC))) {
                 mP++;
                 mC++;
                 count++;
             } else if (parentX.get(mP) > childX.get(mC)) {
                 return false;
             } else {
                 mP++;
             }
         }
         
         if (count == childX.size()) return true;
         
         return false;
     }

    boolean CheckSetXCorvering(List<Integer> SetX, float x1, float y1, float x2, float y2) {
        FloatPointItem nX1 = new FloatPointItem();
        FloatPointItem nX2 = new FloatPointItem();
        List<Integer> SetI = null;
        int NumberIntersInside =0;

        for (int i = 0; i < SetX.size(); i++) {
            SetI = findList_MaxDistance2R(SetX, i);
            if (SetI.isEmpty()) return false;
            for (int j = 0; j < SetI.size(); j++) {
                findIntersection_TwoCircle(SetX.get(i), SetI.get(j), nX1, nX2);
                if (CheckPoint_InRectange(nX1, x1, y1, x2, y2)) {
                    NumberIntersInside++;
                    if (!CheckPoint_Corvering_bySetI(nX1, SetI)) {
                        return false;
                    }
                }
                if (CheckPoint_InRectange(nX2, x1, y1, x2, y2)) {
                    NumberIntersInside++;
                    if (!CheckPoint_Corvering_bySetI(nX2, SetI)) {
                        return false;
                    }
                }

            }
        }
        if (SetX.size() == 2) {
            System.out.println("SetX==2"+SetX.get(0) +" - "+SetX.get(1) + " x1="+x1+" y1="+y1+" x2="+ x2+ " y2="+y2);
        } else if (SetX.size() == 1) {
            System.out.println("SetX==1"+SetX.get(0) + " x1="+x1+" y1="+y1+" x2="+ x2+ " y2="+y2);
        }
        if (Math.abs(x1-x2)>= 2*Rs && Math.abs(y1-y2)>= 2*Rs) {
           if (NumberIntersInside == 0) return false;
        }
        FloatPointItem p1 = new FloatPointItem(x1, y1);
        FloatPointItem p2 = new FloatPointItem(x2, y1);
        FloatPointItem p3 = new FloatPointItem(x1, y2);
        FloatPointItem p4 = new FloatPointItem(x2, y2);
        if (!CheckInteractionPoint_Corvering_bySetX(p1, p4, SetX))  return false;  
        
        //Check 4 canh nam ben trong 
//        if (CheckPoint_Corvering_bySetX(p1,SetX) && CheckPoint_Corvering_bySetX(p2,SetX) && CheckPoint_Corvering_bySetX(p3,SetX)&&CheckPoint_Corvering_bySetX(p4,SetX)) {
//            return true;
//        }


        return true;
    }
    
    boolean CheckInteractionPoint_Corvering_bySetX(FloatPointItem P1,FloatPointItem P4, List<Integer> SetX) {
        FloatPointItem P2 = new FloatPointItem(P1.getX(), P4.getY());
        FloatPointItem P3 = new FloatPointItem(P4.getX(), P1.getY());
        List<FloatPointItem> Edge1 = new ArrayList<>();
        Edge1.add(P1);
        Edge1.add(P3);
        List<FloatPointItem> Edge2 = new ArrayList<>();
        Edge2.add(P2);
        Edge2.add(P4);
        List<FloatPointItem> Edge3 = new ArrayList<>();
        Edge3.add(P1);
        Edge3.add(P2);
        List<FloatPointItem> Edge4 = new ArrayList<>();
        Edge4.add(P3);
        Edge4.add(P4);
        for (int i = 0; i < SetX.size(); i++) {
           NodeItem  node = mListSensorNodes.get(SetX.get(i));
           //Canh tren
           Find_Interaction_FirstEdge(node,P1,P3,Edge1);
           //Canh dưoi
           Find_Interaction_FirstEdge(node,P2,P4,Edge2);
           //Canh trai
            Find_Interaction_SecondEdge(node, P1, P2, Edge3);
           //Canh phai
            Find_Interaction_SecondEdge(node, P3, P4, Edge4);
        }
        
        //Sort edges in increasing order on basis of cos
        Collections.sort(Edge1, new Comparator<FloatPointItem>() {
            @Override
            public int compare(FloatPointItem o1, FloatPointItem o2) {
                float x1 = o1.getX();
                float x2 = o2.getX();
                return Float.compare(x1, x2);
            }
        });
        
        Collections.sort(Edge2, new Comparator<FloatPointItem>() {
            @Override
            public int compare(FloatPointItem o1, FloatPointItem o2) {
                float x1 = o1.getX();
                float x2 = o2.getX();
                return Float.compare(x1, x2);
            }
        });
        Collections.sort(Edge3, new Comparator<FloatPointItem>() {
            @Override
            public int compare(FloatPointItem o1, FloatPointItem o2) {
                float y1 = o1.getY();
                float y2 = o2.getY();
                return Float.compare(y1, y2);
            }
        });
        Collections.sort(Edge4, new Comparator<FloatPointItem>() {
            @Override
            public int compare(FloatPointItem o1, FloatPointItem o2) {
                float y1 = o1.getY();
                float y2 = o2.getY();
                return Float.compare(y1, y2);
            }
        });
        
        for (int i = 1; i < Edge1.size(); i++) {
            if (!CheckPoint_Corvering_bySetX(new FloatPointItem((Edge1.get(i - 1).getX() + Edge1.get(i).getX()) / 2, Edge1.get(i).getY()), SetX)) {
                return false;
            }
        }
        for (int i = 1; i < Edge2.size(); i++) {
            if (!CheckPoint_Corvering_bySetX(new FloatPointItem((Edge2.get(i - 1).getX() + Edge2.get(i).getX()) / 2, Edge2.get(i).getY()), SetX)) {
                return false;
            }
        }
        for (int i = 1; i < Edge3.size(); i++) {
            if (!CheckPoint_Corvering_bySetX(new FloatPointItem(Edge3.get(i).getX(), (Edge3.get(i-1).getY()+Edge3.get(i).getY())/2), SetX)) {
                return false;
            }
        }
        for (int i = 1; i < Edge4.size(); i++) {
            if (!CheckPoint_Corvering_bySetX(new FloatPointItem(Edge4.get(i).getX(), (Edge4.get(i-1).getY()+Edge4.get(i).getY())/2), SetX)) {
                return false;
            }
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
    
    
    //boolean Check Point Have Exit 
    boolean CheckPoint_Corvering_bySetX(FloatPointItem point, List<Integer> SetX) {
        for (int i = 0; i < SetX.size(); i++) {
             if (calculateDistance(point.getX(), point.getY(), mListSensorNodes.get(SetX.get(i)).getX(), mListSensorNodes.get(SetX.get(i)).getY()) + SensorUtility.mSaiso <  Rs) {
                 return true;
             }
        }
        return false;
    }

    //Check All Point of SetI have larger distance 2*Rs with result of equation
    boolean CheckPoint_Corvering_bySetI(FloatPointItem point, List<Integer> SetI) {
        int count = 0;
        if (SetI.size() <= 1) return false;
        for (int i = 0; i < SetI.size(); i++) {
            if (calculateDistance(point.getX(), point.getY(), mListSensorNodes.get(SetI.get(i)).getX(), mListSensorNodes.get(SetI.get(i)).getY()) + SensorUtility.mSaiso <  Rs) {
                count++;
            }
            if (count >= 1) return true;

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
        if (point.getX() >= maxX || point.getX() <= minX) {
            return false;
        }
        if (point.getY() >= maxY || point.getY() <= minY) {
            return false;
        }
        return true;
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

    List<Integer> findList_MaxDistance2R(List<Integer> SetX, int point) {
        NodeItem mNode = mListSensorNodes.get(SetX.get(point));
        List<Integer> setI = new ArrayList<>();
        for (int i = 0; i < SetX.size(); i++) {
            float distance = calculateDistance(mNode.getX(), mNode.getY(), mListSensorNodes.get(SetX.get(i)).getX(), mListSensorNodes.get(SetX.get(i)).getY());
            if (!Objects.equals(SetX.get(point), SetX.get(i)) && distance > 0 && distance <= (2 * Rs)) {
                setI.add(SetX.get(i));
            }
        }
        return setI;
    }

    public  float calculateDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    
    public void Combining_All_Strips(List<List<List<Integer>>> ListOfListX, List<List<Double>> ListOfListT, List<List<Integer>> returnListX, List<Double> returnListT) {
        int K = ListOfListX.size();
        double Min = Double.MAX_VALUE;
        //Create ListT Ascending
        List<List<Double>> ListOfSortListT = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            List<Double> SortListT = new ArrayList<>();
            List<Double> ListT = ListOfListT.get(i);
            if (ListT.size() == 0) {
                System.out.println("ListX is null id:"+i);
                return;
            }
            for (int j = 0; j < ListT.size(); j++) {
                if (j == 0) {
                    SortListT.add(ListT.get(0));
                } else {
                    SortListT.add(SortListT.get(j - 1) + ListT.get(j));
                }
            }
            if (Min > SortListT.get(ListT.size() - 1)) {
                Min = SortListT.get(ListT.size() - 1);
            }
            // Add in ListOfSortListT
            ListOfSortListT.add(SortListT);
        }

        //Tao duong thoi gian chieu
        List<Double> ListTotalT = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            List<Double> SortListT = ListOfSortListT.get(i);
            for (int j = 0; j < SortListT.size(); j++) {
                if (SortListT.get(j) <= Min) {
                    ListTotalT.add(SortListT.get(j));
                }
            }
        }
        Collections.sort(ListTotalT);

        //Find List T return
        int start;
        double startValue = 0;
        for (start = 0; start < ListTotalT.size(); start++) {
            if (ListTotalT.get(start) > 0) {
                returnListT.add(ListTotalT.get(start));
                startValue = ListTotalT.get(start);
                break;
            }
        }

        //Ghep phan tu dau tien cua X
        List<Integer> retlistX = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            List<Double> ListT = ListOfSortListT.get(i);
            int pos = getPostionOfList(ListT, 0, startValue);
            unionXi(ListOfListX.get(i).get(pos), retlistX);
        }
        returnListX.add(retlistX);

        for (int i = start; i < ListTotalT.size(); i++) {
            if (ListTotalT.get(i) > startValue) {
                returnListT.add(ListTotalT.get(i) - startValue);
                //Ghep ptu cua Xi
                retlistX = new ArrayList<>();
                for (int j = 0; j < K; j++) {
                    List<Double> ListT = ListOfSortListT.get(j);
                    int pos = getPostionOfList(ListT, startValue, ListTotalT.get(i));
                    unionXi(ListOfListX.get(j).get(pos), retlistX);
                }
                returnListX.add(retlistX);

                startValue = ListTotalT.get(i);

            }
        }

    }

    //Ghep Xik U Xin....
    void unionXi(List<Integer> listX, List<Integer> resultListX) {
        int n = resultListX.size();
        boolean isExit;
        for (int i = 0; i < listX.size(); i++) {
            isExit = false;
            for (int j = 0; j < n; j++) {
                if (Objects.equals(listX.get(i), resultListX.get(j))) {
                    isExit = true;
                    break;
                }
            }
            if (!isExit) {
                resultListX.add(listX.get(i));
            }
        }
    }
    

    //Dua ra vi tri corver segment cua list
    int getPostionOfList(List<Double> listT, double starTime, double endTime) {
        for (int i = 0; i < listT.size(); i++) {
            if (i == 0) {
                if (listT.get(0) >= endTime) {
                    return 0;
                }
            } else {
                if (listT.get(i - 1) <= starTime && listT.get(i) >= endTime) {
                    return i;
                }
            }
        }
        return 0;
    }
    
    public void Combining_All_Division(List<List<List<Integer>>> ListOfListX, List<List<Double>> ListOfListT, List<List<Integer>> returnListX, List<Double> returnListT) {
        int mPos = 0;
        for (int i = 0; i< ListOfListX.size();i++) {
          List<List<Integer>> tempListX = ListOfListX.get(i);
          for (int j =0 ; j< tempListX.size();j++) {
              
              if (CheckExitListX(returnListX, tempListX.get(j),mPos)) {
                  //Add returnListT 
                  Double x = returnListT.get(mPos)+ListOfListT.get(i).get(j);
                  returnListT.remove(mPos);
                  returnListT.add(mPos,x);
                  
              } else {
                  
                  //Them phan tu moi vao ket qua tra ve
                  returnListX.add(tempListX.get(j));
                  returnListT.add(ListOfListT.get(i).get(j));
              }
          }
       }
       // Divice Time to (mLvalue +1)
        for (int i = 0; i < returnListT.size(); i++) {
            Double x = returnListT.get(i)/(mLvalue+1);
            returnListT.remove(i);
            returnListT.add(i, x);
        }
    }
    
    boolean CheckExitListX(List<List<Integer>> listX, List<Integer> setX, int postionX) { //checked
        Collections.sort(setX);
        for (int i = 0 ;i<listX.size();i++) {
            List<Integer> tempX = listX.get(i);
            if (tempX.size() != setX.size()) {
                continue;
            } else {
                Collections.sort(tempX);
                int count = 0;
                for (int j =0;j<tempX.size();j++) {
                    if (!Objects.equals(setX.get(j), tempX.get(j))) {
                        break;
                    } else {
                        count++;
                    }
                }
                if (count == setX.size()) {
                    postionX = i;
                    return true;
                }
            }
            
             
        }
        return false;
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
        //List<Double> a = m.LinearProAlgorithm(lisX, sensor, 5);
        //Double asd = a.get(0);
        boolean a = m.CheckIsSubSet(sensor, childsensor);
        int as= 5;
    }
}

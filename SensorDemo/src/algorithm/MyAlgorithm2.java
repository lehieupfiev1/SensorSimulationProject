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
import static iterface.frameMain.coordinatePanel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import model.CountItem;
import model.Curve;
import model.DoublePoint;
import model.FloatPointItem;
import model.IntersectItem;
import model.IntersectionPoint;
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
    IntersectItem Intersect[][];
    public MyAlgorithm2() {
    }
    
    public void run() {
        
        init();

        readData();
        //Step 1: Find target-covering Sensor
        //FindTargetCoveringSensor();
        
        //Step 2: 

        runAlgorithm();
        
        CoppyToListSensor();
        
        freeData();
        
        System.gc();
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
        Intersect = new IntersectItem[Num+1][Num+1];
        
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
        
        //Tim giao diem
        for (int i = 0;i<Num;i++) {
            for (int j =0;j<=i;j++) {
                if (i != j && Distance[i][j] < 2*Rs) {
                    FloatPointItem n1 = new FloatPointItem();
                    FloatPointItem n2 = new FloatPointItem();
                    findIntersection_TwoCircle(i, j, n1, n2);
                    IntersectItem intersectItem = new IntersectItem(n1, n2);
                    Intersect[i][j] = intersectItem;
                    Intersect[j][i] = intersectItem;
                } else {
                    IntersectItem intersectItem = new IntersectItem();
                    Intersect[i][j] = intersectItem;
                    Intersect[j][i] = intersectItem;
                }
            }

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
        FloatPointItem DownRightCornerPoint = new FloatPointItem(SensorUtility.numberOfColumn-1,SensorUtility.numberOfRow-1);
        
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
        tempListX = null; 
        tempListT = null;
        upPoint = null;
        downPoint = null;
    }
    
    public List<Integer> FindListSensor(FloatPointItem UpLeftCornerPoint, FloatPointItem DownRightCornerPoint) {
        List<Integer> resultListSensor = new ArrayList<>();
        float Xmax,Xmin,Ymax,Ymin;
        Xmin = UpLeftCornerPoint.getX() - SensorUtility.mRsValue;
        Xmax = DownRightCornerPoint.getX() + SensorUtility.mRsValue;
        Ymin = UpLeftCornerPoint.getY() - SensorUtility.mRsValue;
        Ymax = DownRightCornerPoint.getY() + SensorUtility.mRsValue;
        
        for (int i = 0; i < SensorUtility.mListSensorNodes.size(); i++) {
            float X = SensorUtility.mListSensorNodes.get(i).getX();
            float Y = SensorUtility.mListSensorNodes.get(i).getY();
            if (X > Xmin && X < Xmax && Y > Ymin && Y < Ymax ) {
                if ((X < UpLeftCornerPoint.getX() && Y < UpLeftCornerPoint.getY() && calculateDistance(new DoublePoint(X, Y), new DoublePoint(UpLeftCornerPoint)) >= SensorUtility.mRsValue) ||
                        (X < UpLeftCornerPoint.getX() && Y > DownRightCornerPoint.getY() && calculateDistance(new DoublePoint(X, Y), new DoublePoint(UpLeftCornerPoint.getX(), DownRightCornerPoint.getY())) >= SensorUtility.mRsValue) ||
                        (X > DownRightCornerPoint.getX() && Y > DownRightCornerPoint.getY() && calculateDistance(new DoublePoint(X, Y), new DoublePoint(DownRightCornerPoint)) >= SensorUtility.mRsValue) ||
                        (X > DownRightCornerPoint.getX() && Y < UpLeftCornerPoint.getY()) && calculateDistance(new DoublePoint(X, Y), new DoublePoint(DownRightCornerPoint.getX(), UpLeftCornerPoint.getY())) >= SensorUtility.mRsValue) {
                    // do nothing here
                } else {
                    resultListSensor.add(i);
                }
            }
        }
        return resultListSensor;
    }
    
    public List<Double> LinearProAlgorithm(List<List<Integer>> listX, List<Integer> listSenSor, double valueT) {
        List<Double> time = new ArrayList<>();
        int m = listX.size();
        int n = listSenSor.size();
        
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
        System.out.println("numberSize"+listSensor.size() + " - minsensor "+minSensor);

        //Cach 1: Dung to hop
//        for (int i = minSensor; i <= listSensor.size(); i++) {
//            //Tinh to hop chap i cua ListSize
//            Combination(listSensor, listSensor.size(), i, UpLeftCornerPoint, DownRightCornerPoint, returListX, MaxSet);
//            MaxSet = returListX.size();
//
//        }
        //Cach 2 :
        List<List<Integer>> retuListX = new ArrayList<>();
        FindSetV2(listSensor,UpLeftCornerPoint,DownRightCornerPoint,returListX);
        Collections.sort(returListX, new Comparator<List<Integer>>(){
            @Override
               public int compare(List<Integer> o1, List<Integer> o2) {
                   int size1 = o1.size();
                   int size2 = o2.size();
                   
                   return Integer.compare(size1, size2);
               }
            
        });
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
                int pos = 0;
                if (!CheckExitListX(returListX,tempXi,pos)) {
                   returListX.add(tempXi);
                }
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
                        } else {
                           setItem = null;
                        }
                    }
                } 
                mListSet.remove(0);
            }
        }
    }
    //Code tinnh to hop---------------------
    
    void FindSetV2(List<Integer> listSensor,FloatPointItem P1, FloatPointItem P4, List<List<Integer>> returListX ) {
        //Init data
        int N = listSensor.size();
        mListSet.clear();
        List<Integer> X0 = new ArrayList<>();
        List<Integer> Y0 = new ArrayList<>();
        List<Integer> C0 = new ArrayList<>();
        
        for (int i =0;i<listSensor.size();i++) {
            X0.add(listSensor.get(i));
        }
        ListSetItem headSetItem = new ListSetItem(X0, Y0, C0,2);
        mListSet.add(headSetItem);
        //
        ListSetItem headItem;
        List<Integer> tempXi;
        List<Integer> tempYi;
        List<Integer> tempCi;
        while(!mListSet.isEmpty()){
             headItem = mListSet.get(0);
             tempXi = headItem.getXi();
             tempYi = headItem.getYi();
             tempCi = headItem.getCi();
            int K = headItem.getK();
            int pos =0;
            if (CheckExitListX(returListX,tempXi,pos)) {
                mListSet.remove(0);
            }
            System.out.println("mlistSet Size" +mListSet.size());
            //Test
            showViewTest(tempXi);
            boolean[] status = new boolean[tempXi.size()];
            CountItem count = new CountItem(0);
            if (K > 1) {
                Combi(tempXi, tempYi,tempCi, tempXi.size(), K, P1, P4, count);
            } else if (K==1) {
                for (int i = 0; i < tempXi.size(); i++) {
                    //Check tinh phu khi loai bo cac phan tu i 
                    if (!CheckPointExitInCi(tempCi, tempXi.get(i)) && CheckPointCorveringBySet(tempXi, tempXi.get(i), P1, P4)) {
                        status[i] = true;
                        count.increse(1);
                    }
                }
            }
            if(count.getCount()==0 && K == 1) {
                //Khong the loai bo phan tu nao
                System.out.println("------------returListX Size" +returListX.size());
                returListX.add(tempXi);
                mListSet.remove(0);

            } else if (K == 1 && count.getCount() != 0) {
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

                           setItem.setK(K+1);
                           mListSet.add(setItem);
                        } else {
                            //freedata
                           setItem = null;
                       }
                    } 
                } 
                mListSet.remove(0);
            } else if (count.getCount()==0 && K > 1) {
                mListSet.get(0).setK(K-1);
            } else {
                mListSet.remove(0);
            }

        }
    }
     public void Combi(List<Integer> listSensor,List<Integer> listYi,List<Integer> listCi, int N, int K, FloatPointItem P1, FloatPointItem P4,CountItem count) {
        int a[] = new int[N + 1];
        a[0] = 0;
        int i = 1;
        ComCallBack(listSensor,listYi,listCi, a, N, K, i, P1, P4,count);
    }

    void ComCallBack(List<Integer> listSensor,List<Integer> listYi,List<Integer> listCi, int a[], int N, int K, int i, FloatPointItem P1, FloatPointItem P4,CountItem count) {
        for (int j = a[i - 1] + 1; j <= N - K + i; j++) {
            a[i] = j;
            if (i == K) {
                //Print result
                ResultCombi(listSensor,listYi,listCi, a, K, P1, P4,count);
            } else {
                ComCallBack(listSensor,listYi,listCi, a, N, K, i + 1, P1, P4, count);
            }
        }
    }

    void ResultCombi(List<Integer> listSensor,List<Integer> listYi,List<Integer> listCi, int a[], int K, FloatPointItem P1, FloatPointItem P4 ,CountItem count) {
        List<Integer> listElement_Combination = new ArrayList<>();
        for (int i = 1; i <= K; i++) {
            listElement_Combination.add(listSensor.get(a[i] - 1));
        }
        // Thuc hien thuat toan
        List<Integer> list = new ArrayList<>();
        list.clear();
        boolean found = false;
        for (int i= 0;i<listSensor.size();i++) {
            for (int j =0; j < listElement_Combination.size();j++) {
                found = false;
                if (listSensor.get(i) == listElement_Combination.get(j)) {
                    found = true;
                    break;
                }

            }
            if (!found) list.add(listSensor.get(i));
        }
        //Kiem tra co phu khong
        for (int i =0;i<listElement_Combination.size();i++) {
            if (!CheckPointCorveringBySet(list, listElement_Combination.get(i), P1, P4)) {
                
                return;
            }
                
        }
        
        ///Add ket qua
        count.increse(1);
        ListSetItem setItem = new ListSetItem();
        setItem.setXi(list);
        setItem.setListYi(listYi, listElement_Combination);

        //Check Exit
        if(!CheckExitSet(mListSet,setItem)) {
            
            setItem.setK(K);
            setItem.setCi(listCi);
            mListSet.add(setItem);
        } else {
            //freedata
            setItem = null;
        }

    }
    //------------------------------------
    
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
//                             FloatPointItem n1 = new FloatPointItem();
//                             FloatPointItem n2 = new FloatPointItem();
//                             findIntersection_TwoCircle(nearByList.get(j), nearByList.get(i), n1, n2);
                             FloatPointItem n1 = Intersect[nearByList.get(i)][nearByList.get(j)].getN1();
                             FloatPointItem n2 = Intersect[nearByList.get(i)][nearByList.get(j)].getN2();
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
                if (!CheckPoint_Corvering_bySetX(listY.get(i), nearByList)) {
                    listY = null;
                    nearByList = null;
                    list = null;
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
//                             FloatPointItem n1 = new FloatPointItem();
//                             FloatPointItem n2 = new FloatPointItem();
//                             findIntersection_TwoCircle(nearByList.get(j), nearByList.get(i), n1, n2);
                             FloatPointItem n1 = Intersect[nearByList.get(i)][nearByList.get(j)].getN1();
                             FloatPointItem n2 = Intersect[nearByList.get(i)][nearByList.get(j)].getN2();
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
                   listY = null;
                   nearByList = null;
                   list = null;
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
                    //Free data
                   Edge = null;
                   nearByList = null;
                   list = null;
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
                            //Free data
                            intersect = null;
                            nearByList = null;
                            list = null;
                            nearByEdge = null;
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
                            //Free data
                            intersect = null;
                            nearByList = null;
                            list = null;
                            nearByEdge = null;
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
                            //Free data
                            intersect = null;
                            nearByList = null;
                            list = null;
                            nearByEdge = null;
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
                            //Free data
                            intersect = null;
                            nearByList = null;
                            list = null;
                            nearByEdge = null;
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
                    //showViewTest(listElement_Combination);
                    int s = 2;
                }
            }
        }
        listElement_Combination = null;
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

 
        //Free Data 
        SetI = null;
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
            if (ListT.isEmpty()) {
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
    
    public void freeData() {
        Distance = null;
        Intersect = null;
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
    
    public List<List<Integer>> FindSetXi(List<Integer> ListSensor,FloatPointItem UpLeftCornerPoint, FloatPointItem DownRightCornerPoint) {
        // convert data to be compatible with the function parameters, for more infor
        // on what does each key-value pairs mean, looking at the comment in MyAlgorithm4
        Map<String,Object> data = new HashMap<>();
        data.put("sensorRadius", (double)SensorUtility.mRsValue);
        data.put("sensorLifeTime", SensorUtility.LifeTimeOfSensor);
        data.put("sensorList", ListSensor.stream().map(i -> {
            NodeItem node = SensorUtility.mListSensorNodes.get(i);
            return new NodeItem(i, node.getX(), node.getY(), 2, 0, 0);
        }).collect(Collectors.toCollection(ArrayList::new)));
        data.put("UpLeftCornerPoint", new DoublePoint(UpLeftCornerPoint.getX(), UpLeftCornerPoint.getY()));
        data.put("DownRightCornerPoint", new DoublePoint(DownRightCornerPoint.getX(), DownRightCornerPoint.getY()));
        
        float rectangleArea = Math.abs((DownRightCornerPoint.getX() - UpLeftCornerPoint.getX())*(DownRightCornerPoint.getY() - UpLeftCornerPoint.getY()));
        float sensorArea = SensorUtility.mRsValue*SensorUtility.mRsValue*(float)Math.PI;
        data.put("sensorsThreshold", (int)Math.ceil(rectangleArea/(2*sensorArea)));
        
        ArrayList<ArrayList<NodeItem>> ListSensorSets = getListOfSensorSet(data);
        if (ListSensorSets == null) {
            return null;
        } else {
            // convert back to integer
            return ListSensorSets.stream().map(set -> set.stream().map(sensor -> sensor.getId()).collect(Collectors.toCollection(ArrayList::new))).collect(Collectors.toCollection(ArrayList::new));
        }
    }
    
    /**
     * Find the ArrayList, each element is the array containing the sensor that cover the area
     * @param data the map contains the data
     * @return List of array, each of them is the array containing the sensor covering the area
     */
    private ArrayList<ArrayList<NodeItem>> getListOfSensorSet(Map<String, Object> data) {
        // deep copy sensor list to ensure immutability
        ArrayList<NodeItem> sensorList = ((ArrayList<NodeItem>)data.get("sensorList")).stream().map(sensor -> new NodeItem(sensor)).collect(Collectors.toCollection(ArrayList::new));
        double sensorRadius = (double) data.get("sensorRadius");
        
        ArrayList<NodeItem> usedSensors = new ArrayList<>();
        ArrayList<ArrayList<NodeItem>> listOfSensorSets = new ArrayList<>();
        
        int minPossibleSensors = (int)data.get("sensorsThreshold");
        
        System.out.println("Start find set with min: " + minPossibleSensors);
        
        /**
         * run the algorithm until sensors list is empty (all sensors have been used)
         * terminate when there are minPossibleSensors/2 unused sensors left, this is used to reduce some sets that reused to many sensor from other set
         */
        while (sensorList.size() > minPossibleSensors) {
            int oldLength = sensorList.size();
            
            // Initialize uncovered edges/arcs (4 edges of the rectangle)
            DoublePoint DownRight = (DoublePoint)data.get("DownRightCornerPoint");
            DoublePoint UpLeft = (DoublePoint)data.get("UpLeftCornerPoint");
            ArrayList<Curve> uncoveredCurve = new ArrayList<>();
            uncoveredCurve.add(new Curve(DownRight, new DoublePoint(UpLeft.getX(), DownRight.getY()), Curve.EdgeId.BOTTOM));
            uncoveredCurve.add(new Curve(new DoublePoint(UpLeft.getX(), DownRight.getY()), UpLeft, Curve.EdgeId.LEFT));
            uncoveredCurve.add(new Curve(UpLeft, new DoublePoint(DownRight.getX(), UpLeft.getY()), Curve.EdgeId.TOP));
            uncoveredCurve.add(new Curve(new DoublePoint(DownRight.getX(), UpLeft.getY()), DownRight, Curve.EdgeId.RIGHT));

            System.out.println("____Number of sensor left: " + sensorList.size() + "___________");

            ArrayList<NodeItem> currentConstructingSensorSet = new ArrayList<>();
            currentConstructingSensorSet = getSensorSet(uncoveredCurve, sensorList, usedSensors, sensorRadius);

            if (currentConstructingSensorSet == null) {
                return null;
            }
            
            int newLength = sensorList.size();
            if (oldLength == newLength) {
                break;
            }
            listOfSensorSets.add(currentConstructingSensorSet);
        }
        System.out.println("unused sensors: " + sensorList.size());
        return listOfSensorSets;
    }
    
    /**
     * compute ONE sensor sets that cover the area described by "uncoveredCurve", using sensor in "sensorList",
     * if some area is not covered by any sensor in "sensorList" then the function looking at "usedSensor",
     * if no sensor satisfied, null is returned, otherwise, a arraylist of sensor is returned
     * @param uncoveredCurve an array of cure describing the covering area
     * @param sensorList the first list to looking for sensor, in this algorithm, those are unused sensor
     * @param usedSensors the second list to looking for sensor, those are used sensor
     * @param sensorRadius the sensing radius
     * @return
     */
    private ArrayList<NodeItem> getSensorSet(ArrayList<Curve> uncoveredCurve, ArrayList<NodeItem> sensorList, ArrayList<NodeItem> usedSensors, double sensorRadius) {
        HashSet<NodeItem> currentConstructingSensorSet = new HashSet<>();
        ArrayList<NodeItem> duplicatedSensor = new ArrayList<>();
        ArrayList<NodeItem> duplicatedUsedSensor = new ArrayList<>();
        
        // run until all arcs is covered
        while (uncoveredCurve.size() > 0) {
            System.out.println(sensorList.size() + ", (" + currentConstructingSensorSet.size() + "). Number of curves left: " + uncoveredCurve.size());
            // pick 1st curve, filter out all sensors that don't cover some segment of this curve
            ArrayList<DoublePoint> startPointArray = uncoveredCurve.stream().map(curve -> curve.getStartPoint()).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<NodeItem> nearBySensors = filterByTheNumberOfPointsCovered(sensorList, startPointArray, sensorRadius);
            Optional optionalSensor = nearBySensors.stream().filter(sensor -> !currentConstructingSensorSet.contains(sensor)).findAny();
            
            /**
             * random sensor from set, if no unused sensor covers the 1st curve, then random from used sensors
             * multiple sensor located in the same coordinate could break the below code, so the list is uniquified before hand
             * In the NodeItem hashCode and equals implementation, I only care about the coordinate and the type of node
             * so that 2 node of the same type and same location, but different id, status will be consider the same one
             */
            NodeItem chosenSensor;
            if (!optionalSensor.isPresent()) {
                // get sensor from used sensor list
                nearBySensors = filterByTheNumberOfPointsCovered(usedSensors, startPointArray, sensorRadius);
                optionalSensor = nearBySensors.stream().filter(sensor -> !currentConstructingSensorSet.contains(sensor)).findAny();
                // if no sensor cover the next curve, it mean that the provided sensor set doesn't cover the area
                if (!optionalSensor.isPresent()) {
                    return null;
                } else {
                    chosenSensor = (NodeItem)optionalSensor.get();
                    usedSensors.removeIf(sensor -> sensor.getId() == chosenSensor.getId());
                    int duplicatedIndex;
                    while ((duplicatedIndex = usedSensors.indexOf(chosenSensor)) != -1) {
                        duplicatedUsedSensor.add(usedSensors.remove(duplicatedIndex));
                    }
                    System.out.println("used sensor");
                }
            } else {
                chosenSensor = (NodeItem)optionalSensor.get();
                sensorList.removeIf(sensor -> sensor.getId() == chosenSensor.getId());
                int duplicatedIndex;
                while ((duplicatedIndex = sensorList.indexOf(chosenSensor)) != -1) {
                    duplicatedSensor.add(sensorList.remove(duplicatedIndex));
                }
                while ((duplicatedIndex = usedSensors.indexOf(chosenSensor)) != -1) {
                    duplicatedUsedSensor.add(usedSensors.remove(duplicatedIndex));
                }
            }
            
            currentConstructingSensorSet.add(chosenSensor);
            
            // filter out curves which cannot intersect with the chosen sensor
            ArrayList<Curve> nearByCurves = getCurvesNearSensor(uncoveredCurve, chosenSensor, sensorRadius);
            
            HashMap<Curve, ArrayList<Curve>> curveArrayModification = getCurveModification(chosenSensor, nearByCurves, sensorRadius);
            
            updateCurveArray(uncoveredCurve, curveArrayModification);
        }
        
        usedSensors.addAll(currentConstructingSensorSet);
        usedSensors.addAll(duplicatedUsedSensor);
        sensorList.addAll(duplicatedSensor);
        
        return currentConstructingSensorSet.stream().collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Find the sensors in the input list that the point lie within (remove the == radius case to simplify the algorithm)
     * @param sensorListL: The input sensor list to filter from
     * @param point: The point that the sensor must cover it
     * @param radius: Sensor radius
     * @return The array of sensor that cover the input point
     */
    private ArrayList<NodeItem> getNearBySensors(ArrayList<NodeItem> sensorList, DoublePoint point, double radius) {
         return sensorList.stream().filter(sensor -> calculateDistance(point, sensor.getCoordinate()) < radius).collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Find the sensors in the input list that cover the most consecutive point from the start of the input point array
     * @param sensorSet: the input sensor list to filter from
     * @param pointArray: The point array, used from the start
     * @param radius: sensor radius
     * @return The array of sensor that cover the most consecutive point from the start of the input point array
     */
    private ArrayList<NodeItem> filterByTheNumberOfPointsCovered(ArrayList<NodeItem> sensorSet, ArrayList<DoublePoint> pointArray, double radius) {
        ArrayList<NodeItem> nextSensorSets = sensorSet.stream().map(node -> new NodeItem(node)).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<NodeItem> result = new ArrayList<>();
        int i = 0;
        while (i < pointArray.size()) {
            nextSensorSets = getNearBySensors(nextSensorSets, pointArray.get(i), radius);
            if (!nextSensorSets.isEmpty()) {
                result = nextSensorSets;
            } else {
                break;
            }
            i++;
        }
        return result;
    }
    
    /**
     * Find the curves/edges that MAY intersect with the sensor circle
     * The resulting curves/edges is NOT guarantee to intersect with the sensor circle
     * since the calculation is math-heavy and may duplicate with the "find the intersection" function
     * @param curveArray: the array of curves/edges to filter from
     * @param sensor: the sensor
     * @param radius: sensor radius
     * @return The array of curves/edges that MAY intersect with the sensor circle
     */
    private ArrayList<Curve> getCurvesNearSensor(ArrayList<Curve> curveArray, NodeItem sensor, double radius) {
        return curveArray.stream().filter(curve -> {
            DoublePoint curveCenter = curve.getCenter();
            if (curveCenter == null) {
                // since find out whether the line segment intersect with the sensor circle involve lots of math computation
                // and may duplicate when finding the intersection point later, I just accept it
                return true;
            } else {
                return calculateDistance(curveCenter, sensor.getCoordinate()) <= 2*radius;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Calculate distance between 2 point in the Oxy plane
     * @param point1
     * @param point2
     * @return The distance
     */
    private double calculateDistance(DoublePoint point1, DoublePoint point2) {
        return Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
    }
    
    /**
     * Calculate the Modification Map show how to update the uncoveredCurves array, nothing has been mutated yet
     * The Map has the following interface:
     * - Key: curve: the curve to replaced by the curves array value, if null mean append the array value to the end
     * - Value: ArrayList<Curve> all of this curve will be put in the place of the curve key
     * @param sensor: the sensor that cut the curves
     * @param nearByCurves: List of curves that may be cut the sensor circle
     * @param sensorRadius: sensor radius
     * @return The Modification map
     */
    private HashMap<Curve, ArrayList<Curve>> getCurveModification(NodeItem sensor, ArrayList<Curve> nearByCurves, double sensorRadius) {
        HashMap<Curve, ArrayList<Curve>> modification = new HashMap<>();
        ArrayList<IntersectionPoint> intersectionPointsArray = new ArrayList<>();
        
        nearByCurves.forEach(curve -> {
            if (curve.getCenter() == null) { // if curve is an edge
                ArrayList<DoublePoint> intersectionPoints = getIntersectionLineCircle(curve.getStartPoint(), curve.getEndPoint(), sensor.getCoordinate(), sensorRadius);
                ArrayList<Curve> newLine = new ArrayList<>();
                switch (intersectionPoints.size()) {
                    case 0: {
                        if (calculateDistance(curve.getStartPoint(), sensor.getCoordinate()) < sensorRadius) {
                            // the edge is covered entirely by the sensor, so remove it, null mean remove
                            modification.put(curve, null);
                        } // else, the edge is outside of the sensor, we do nothing
                        break;
                    }
                    case 1: {
                        // the circle cut the line segment at the start point
                        if (curve.getStartPoint().equals(intersectionPoints.get(0))) {
                            if (calculateDistance(curve.getEndPoint(), sensor.getCoordinate()) < sensorRadius) {
                                modification.put(curve, null);
                                // check start point (entry)
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                            } else {
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                            }
                            break;
                        }
                        // the circle cut the line segment at the end point
                        if (curve.getEndPoint().equals(intersectionPoints.get(0))) {
                            if (calculateDistance(curve.getStartPoint(), sensor.getCoordinate()) < sensorRadius) {
                                modification.put(curve, null);
                            }
                            break;
                        }
                        // the circle cut the line segment at the middle
                        newLine.clear();
                        // find out what part of the line is covered
                        double startPointDistance = calculateDistance(curve.getStartPoint(), sensor.getCoordinate());
                        double endPointDistance = calculateDistance(curve.getEndPoint(), sensor.getCoordinate());
                        
                        if (startPointDistance > sensorRadius && endPointDistance < sensorRadius) {
                            newLine.add(new Curve(curve.getStartPoint(), intersectionPoints.get(0), curve.getEdgeId()));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                        }
                        if (startPointDistance < sensorRadius && endPointDistance > sensorRadius) {
                            newLine.add(new Curve(intersectionPoints.get(0), curve.getEndPoint(), curve.getEdgeId()));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                        }
                        if (startPointDistance > sensorRadius && endPointDistance > sensorRadius) {
                            newLine.add(new Curve(curve.getStartPoint(), intersectionPoints.get(0), curve.getEdgeId()));
                            newLine.add(new Curve(intersectionPoints.get(0), curve.getEndPoint(), curve.getEdgeId()));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                        }
                        modification.put(curve, newLine);
                        break;
                    }
                    case 2: {
                        newLine.clear();
                        double startPointDistance = calculateDistance(curve.getStartPoint(), sensor.getCoordinate());
                        double endPointDistance = calculateDistance(curve.getEndPoint(), sensor.getCoordinate());
                        
                        if ((new Double(startPointDistance)).equals(sensorRadius)) {
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                            if ((new Double(endPointDistance)).equals(sensorRadius)) {
                                modification.put(curve, null);
                            } else if (endPointDistance > sensorRadius) {
                                newLine.add(new Curve(intersectionPoints.get(1), curve.getEndPoint(), curve.getEdgeId()));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(1), "exit"));
                            }
                        } else {
                            newLine.add(new Curve(curve.getStartPoint(), intersectionPoints.get(0), curve.getEdgeId()));
                            if ((new Double(endPointDistance)).equals(sensorRadius)) {
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                            } else {
                                newLine.add(new Curve(intersectionPoints.get(1), curve.getEndPoint(), curve.getEdgeId()));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(1), "exit"));
                            }
                        }
                        modification.put(curve, newLine);
                        break;
                    }
                }
            } else { // if curve is a curve
                ArrayList<DoublePoint> intersectionPoints = getIntersectionArcCircle(curve, sensor.getCoordinate(), sensorRadius);
                ArrayList<Curve> newCurve = new ArrayList<>();
                switch (intersectionPoints.size()) {
                    case 0: {
                        if (calculateDistance(curve.getStartPoint(), sensor.getCoordinate()) < sensorRadius) {
                            // the curve is covered entirely by the sensor, so remove it
                            modification.put(curve, null);
//                            System.out.println("___--intersect: 0, in--___");
                        } // else, the curve is outside of the sensor
//                        System.out.println("___--intersect: 0, out--___");
                        break;
                    }
                    case 1: {
                        // the circle cut the curve at the start point
                        if (curve.getStartPoint().equals(intersectionPoints.get(0))) {
                            if (calculateDistance(curve.getEndPoint(), sensor.getCoordinate()) < sensorRadius) {
                                modification.put(curve, null);
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
//                                System.out.println("___--intersect: 1, in--___");
                            } else {
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
//                                System.out.println("___--intersect: 1, in--___");
                            }
                            break;
                        }
                        // the circle cut the curve at the end point
                        if (curve.getEndPoint().equals(intersectionPoints.get(0))) {
                            if (calculateDistance(curve.getStartPoint(), sensor.getCoordinate()) < sensorRadius) {
                                modification.put(curve, null);
                            }
                            break;
                        }
                        // the circle cut the line segment at the middle
                        newCurve.clear();
                        // find out what part of the line is covered
                        double startPointDistance = calculateDistance(curve.getStartPoint(), sensor.getCoordinate());
                        double endPointDistance = calculateDistance(curve.getEndPoint(), sensor.getCoordinate());
                        
                        if (startPointDistance > sensorRadius && endPointDistance < sensorRadius) {
                            newCurve.add(new Curve(curve.getStartPoint(), intersectionPoints.get(0), curve.getCenter()));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                        }
                        if (startPointDistance < sensorRadius && endPointDistance > sensorRadius) {
                            newCurve.add(new Curve(intersectionPoints.get(0), curve.getEndPoint(), curve.getCenter()));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                        }
                        if (startPointDistance > sensorRadius && endPointDistance > sensorRadius) {
                            newCurve.add(new Curve(curve.getStartPoint(), intersectionPoints.get(0), curve.getCenter()));
                            newCurve.add(new Curve(intersectionPoints.get(0), curve.getEndPoint(), curve.getCenter()));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                        }
                        modification.put(curve, newCurve);
                        break;
                    }
                    case 2: {
                        newCurve.clear();
                        double startPointDistance = calculateDistance(curve.getStartPoint(), sensor.getCoordinate());
                        double endPointDistance = calculateDistance(curve.getEndPoint(), sensor.getCoordinate());
                        
                        if ((new Double(startPointDistance)).equals(sensorRadius)) {
                            if ((new Double(endPointDistance)).equals(sensorRadius)) {
                                modification.put(curve, null);
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                            } else if (endPointDistance > sensorRadius) {
                                newCurve.add(new Curve(intersectionPoints.get(1), curve.getEndPoint(), curve.getCenter()));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(1), "exit"));
                            } else {
                                newCurve.add(new Curve(curve.getStartPoint(), intersectionPoints.get(1), curve.getCenter()));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(1), "entry"));
                            }
                        } else if (startPointDistance < sensorRadius) {
                            newCurve.add(new Curve(intersectionPoints.get(0), intersectionPoints.get(1), curve.getCenter()));
                            if ((new Double(endPointDistance)).equals(sensorRadius)) {
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                            } else {
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "exit"));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(1), "entry"));
                            }
                        } else {
                            newCurve.add(new Curve(curve.getStartPoint(), intersectionPoints.get(0), curve.getCenter()));
                            intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(0), "entry"));
                            if (endPointDistance > sensorRadius) {
                                newCurve.add(new Curve(intersectionPoints.get(1), curve.getEndPoint(), curve.getCenter()));
                                intersectionPointsArray.add(new IntersectionPoint(intersectionPoints.get(1), "exit"));
                            }
                        }
                        modification.put(curve, newCurve);
                        break;
                    }
                }
            }
        });
        
        /**
         * compute the curves that lie on the sensor circle
         * 1. Sort all point in clockwise order, keep the starting point at the start
         * 2. Traverse through the array, connect each consecutive "entry", "exit" pair, from "exit" to "entry"
         * 3. Put all of curves into the map with key == null to indicate that append all of them to the end of the array
         */
        if (!intersectionPointsArray.isEmpty()) {
            sortPointClockWise(intersectionPointsArray, sensor.getCoordinate());
            ArrayList<Curve> curvesOnSensorCircle = getCurveOnSensorCircle(intersectionPointsArray, sensor.getCoordinate());
            modification.put(null, curvesOnSensorCircle);   
        }
        
        return modification;
    }
    
    /**
     * Apply the modification map to the original array
     * @param curveArray: the curve array that need to mutate
     * @param curveArrayModification: the modification map
     */
    private void updateCurveArray(ArrayList<Curve> curveArray, HashMap<Curve, ArrayList<Curve>> curveArrayModification) {
        curveArrayModification.entrySet().forEach(entry -> {
            if (entry.getKey() == null) {
                curveArray.addAll(entry.getValue());
            } else {
                Curve oldCurve = entry.getKey();
//                System.out.println("key: " + entry.getKey());
//                System.out.println("value: " + entry.getValue());
                int oldCurveIndex = curveArray.indexOf(oldCurve);
//                System.out.println("old curve index: " + oldCurveIndex);
                if (oldCurveIndex != -1) {
                    curveArray.remove(oldCurveIndex);
                    if (entry.getValue() != null) {
                        curveArray.addAll(oldCurveIndex, entry.getValue());
                    }
                }
            }
        });
    }
    
    /**
     * Calculate the intersection point of the line segment and the sensor circle using the method explained here:
     * https://math.stackexchange.com/questions/311921/get-location-of-vector-circle-intersection
     * All variable names are the same as those in the link
     * the result is sorted from the start to end point direction
     * @param startPoint: The start point of the line
     * @param endPoint: The end point of the line
     * @param center: The center of the circle
     * @param radius: The circle radius
     * @return The arrayList contains the intersection points
     */
    private ArrayList<DoublePoint> getIntersectionLineCircle(DoublePoint startPoint, DoublePoint endPoint, DoublePoint center, double radius) {
        double h = center.getX();
        double k = center.getY();
        double x0 = startPoint.getX();
        double y0 = startPoint.getY();
        double x1 = endPoint.getX();
        double y1 = endPoint.getY();
        
        double a = (x1 - x0)*(x1 - x0) + (y1 - y0)*(y1 - y0);
        double b = 2*(x1 - x0)*(x0 - h) + 2*(y1 - y0)*(y0 - k);
        double c = (x0 - h)*(x0 - h) + (y0 - k)*(y0 - k) - radius*radius;
        
        double d = b*b - 4*a*c;
        if (d < 0) {
            return new ArrayList<>();
        }
        if (d == 0) {
            double t = -b/(2*a);
            if (0 <= t && t <= 1) {
                DoublePoint point = new DoublePoint(x0 + (x1 - x0)*t, y0 + (y1 - y0)*t);
                return Stream.of(point).collect(Collectors.toCollection(ArrayList::new));
            }
        }
        // d > 0
        double delta = (double)Math.sqrt(d);
        
        double t1 = (-b - delta)/(2*a);
        double t2 = (-b + delta)/(2*a);
        
        ArrayList<DoublePoint> result = new ArrayList<>();
        if (0 <= t1 && t1 <= 1) {
            result.add(new DoublePoint(x0 + (x1 - x0)*t1, y0 + (y1 - y0)*t1));
        }
        if (0 <= t2 && t2 <= 1) {
            result.add(new DoublePoint(x0 + (x1 - x0)*t2, y0 + (y1 - y0)*t2));
        }
        return result;
    }
    
    /**
     * Calculate the intersection points between the circle and the curve by find the intersection between 2 circle
     * and then filter out point that lie outside the curve
     * @param curve: the curve to the cut
     * @param center: the circle center
     * @param radius: the radius of the circle
     * @return Array list of intersectionPoint
     */
    private ArrayList<DoublePoint> getIntersectionArcCircle(Curve curve, DoublePoint center, double radius) {
        ArrayList<DoublePoint> intersectionPoints = new ArrayList<>();

        // check if the 2 circle don't intersect
        if (calculateDistance(curve.getCenter(), center) > 2*radius) {
            return new ArrayList<>();
        } else if (calculateDistance(curve.getCenter(), center) == 2*radius) {
            /**
             * This function separately handle this case because calculate this case using normal math (below)
             * will come to delta == 0, but due to the double precision limitation, the delta is hard to be 0
             * (usually < 10^(-9) in value, but never be exact zero) >
             * **************
             * The intersection point is the midpoint of the line segment with 2 center point at 2 end
             */
            double x1 = curve.getCenter().getX();
            double y1 = curve.getCenter().getY();
            double x2 = center.getX();
            double y2 = center.getY();
            
            intersectionPoints.add(new DoublePoint((x1 + x2)/2, (y1 + y2)/2));
        } else {
            /**
             * distance < 2*radius
             */
            //2(x2-x1)*X + 2(y2-y1)*Y = x2^2 - x1^2  + y2^2 - y1^2
            //(X-x1)^2+ (Y-y1)^2 = R^2
            double x1 = curve.getCenter().getX();
            double y1 = curve.getCenter().getY();
            double x2 = center.getX();
            double y2 = center.getY();
            
            /**
             * y2 = y1
             * X = [x2^2 - x1^2]/[2(x2 - x1)]
             * Y = +- sqrt[R^2 - (X - x1)^2] + y1
             */
            if (y2 == y1) {
                double X = (x2*x2 - x1*x1)/(2*(x2 - x1));
                double sqrt = Math.sqrt(radius*radius - (X - x1)*(X - x1));
                double Y1 = sqrt + y1;
                double Y2 = -sqrt + y2;
                if (new Double(Y1).equals(Y2)) {
                    intersectionPoints.add(new DoublePoint(X, Y1));
                } else {
                    intersectionPoints.add(new DoublePoint(X, Y1));
                    intersectionPoints.add(new DoublePoint(X, Y2));
                }
            } else {
                /**
                 * Y = [(x2^2 - x1^2 + y2^2 - y1^2)/2(y2 - y1)] - [(x2 - x1)/(y2 - y1)]*X = a - bX
                 * (X - x1)^2 + (bX - a + y1)^2 = R^2
                 * (1 + b^2)*X^2 + 2[-x1 - b(a - y1)]*X + x1^2 + (a - y1)^2 - R^2 = 0
                 * c*X^2 + 2d*X + e = 0
                 * delta = d^2 - ce
                 * X = [-d +- sqrt(delta)]/c
                 */
                double a = (x2*x2 - x1*x1 + y2*y2 - y1*y1)/(2*(y2 - y1));
                double b = (x2 - x1)/(y2 - y1);
                double c = 1 + b*b;
                double d = -x1 - b*(a - y1);
                double e = x1*x1 + (a - y1)*(a - y1) - radius*radius;
                
                double delta = d*d - c*e;
                
                double X1 = (-d + (double)Math.sqrt(delta))/c;
                double Y1 = a - b*X1;
                
                double X2 = (-d - (double)Math.sqrt(delta))/c;
                double Y2 = a - b*X2;
                
                if (new Double(Y1).equals(Y2) && new Double(X1).equals(X2)) {
                    intersectionPoints.add(new DoublePoint(X1, Y1));
                } else {
                    intersectionPoints.add(new DoublePoint(X1, Y1));
                    intersectionPoints.add(new DoublePoint(X2, Y2));
                }
            }
        }
        
        // remove the point lie outside the curve
        intersectionPoints.add(0, curve.getStartPoint());
        intersectionPoints.add(curve.getEndPoint());
        intersectionPoints = sortPointCounterClockWise(intersectionPoints, curve.getCenter());
        return intersectionPoints.subList(intersectionPoints.indexOf(curve.getStartPoint()) + 1, intersectionPoints.indexOf(curve.getEndPoint())).stream().collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Sort the point in the input array in counter clockwise order relative to the center point
     * The y axis is pointing down, so the result is being reserve compare to normal sense
     * @param pointArray: array to be sorted
     * @param center: center point
     * @return sorted array
     */
    private ArrayList<DoublePoint> sortPointCounterClockWise(ArrayList<DoublePoint> pointArray, DoublePoint center) {
        DoublePoint firstItem = pointArray.get(0);
        pointArray.sort((p1, p2) -> compareAngle(Math.atan2(p2.getY() - center.getY(), p2.getX() - center.getX()), Math.atan2(p1.getY() - center.getY(), p1.getX() - center.getX())));
        for (int i = 0, end = pointArray.indexOf(firstItem); i < end; i++) {
            pointArray.add(pointArray.get(i));
        }
        pointArray.subList(0, pointArray.indexOf(firstItem)).clear();
        return pointArray;
    }
    
    /**
     * Sort the point in the input array in clockwise order relative to the center point
     * The first point in the output array is always the "exit" point, or the getCurveOnSensorCircle function will behave incorrectly
     * @param pointArray: array to be sorted
     * @param center: center point
     * @return sorted array
     */
    private ArrayList<IntersectionPoint> sortPointClockWise(ArrayList<IntersectionPoint> pointArray, DoublePoint center) {
        IntersectionPoint firstItem = pointArray.get(0);
        int j = 0;
        while (!firstItem.getDirection().equals("exit") && j < pointArray.size()) {
            firstItem = pointArray.get(j);
            j++;
        }
        pointArray.sort((p1, p2) -> compareAngle(Math.atan2(p1.getCoordinate().getY() - center.getY(), p1.getCoordinate().getX() - center.getX()), Math.atan2(p2.getCoordinate().getY() - center.getY(), p2.getCoordinate().getX() - center.getX())));
        for (int i = 0, end = pointArray.indexOf(firstItem); i < end; i++) {
            pointArray.add(pointArray.get(i));
        }
        pointArray.subList(0, pointArray.indexOf(firstItem)).clear();
        return pointArray;
    }
    
    /**
     * Calculate new curve that lie on the sensor circle
     * 2 point of the new curve is: last "exit" point in a row and first "entry" point in a row, the direction is from "entry" to "exit"
     * @param intersectionPointsArray: list of intersection point
     * @param center: sensor coordinate
     * @return arraylist of new curves
     */
    private ArrayList<Curve> getCurveOnSensorCircle(ArrayList<IntersectionPoint> intersectionPointsArray, DoublePoint center) {
        DoublePoint pendingPoint = null;
        ArrayList<Curve> newCurve = new ArrayList<>();
        for (int i = 0, length = intersectionPointsArray.size(); i < length; i++) {
            String direction = intersectionPointsArray.get(i).getDirection();
            if ("entry".equals(direction) && pendingPoint != null) {
                newCurve.add(new Curve(intersectionPointsArray.get(i).getCoordinate(), pendingPoint, center));
                pendingPoint = null;
            } else if ("exit".equals(direction)) {
                pendingPoint = intersectionPointsArray.get(i).getCoordinate();
            }
        }
        return newCurve;
    }
    
    /**
     * compare 2 angle in double value using epsilon to avoid precision problem
     * @param angle1
     * @param angle2
     * @return 
     */
    private int compareAngle(double angle1, double angle2) {
        double EPSILON = 0.00001d;
        double diff = Math.abs(angle1 - angle2);
        if (diff < EPSILON || (diff < 2*Math.PI + EPSILON/2 && diff > 2*Math.PI - EPSILON/2)) {
            return 0;
        } else {
            return Double.compare(angle1, angle2);
        }
    }
}

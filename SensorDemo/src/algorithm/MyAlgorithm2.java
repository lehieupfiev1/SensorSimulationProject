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
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FloatPointItem;
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
        mLvalue = SensorUtility.Lvalue;
        mTimeLife = SensorUtility.LifeTimeOfSensor;
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
            
            int a =2;
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
//    //Test
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

        for (int i = minSensor; i <= listSensor.size(); i++) {
            //Tinh to hop chap i cua ListSize
            Combination(listSensor, listSensor.size(), i, UpLeftCornerPoint, DownRightCornerPoint, returListX, MaxSet);
            MaxSet = returListX.size();

        }

    }

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
        
        if (!CheckExitSubSet(returListX, listElement_Combination, MaxSet)) {
            if (CheckSetXCorvering(listElement_Combination, P1.getX(), P1.getY(), P2.getX(), P2.getY())) {
                //Add Xi vao List and check TH tap hop cha

                returListX.add(listElement_Combination);
            }
        }
    }
    
    boolean CheckExitSubSet(List<List<Integer>> parentListX,List<Integer> childList, int Max) {
        int count = 0;
        boolean isFound = false;
        for (int i = 0; i<Max;i++) {
            List<Integer> tempList = parentListX.get(i);
            count = 0;
            for (int j = 0;j < tempList.size();j++) {
                isFound = false; 
                for (int k = 0;k < childList.size(); k++) {
                    if (Objects.equals(tempList.get(j), childList.get(k))) {
                        count++;
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) break;
            }
            if (count == tempList.size()) return true;
        }
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
                    if (CheckPoint_Larger2Rs_withSetI(nX1, SetI)) {
                        return false;
                    }
                }
                if (CheckPoint_InRectange(nX2, x1, y1, x2, y2)) {
                    NumberIntersInside++;
                    if (CheckPoint_Larger2Rs_withSetI(nX2, SetI)) {
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
        if (NumberIntersInside == 0) return false;

        return true;
    }

    //Check All Point of SetI have larger distance 2*Rs with result of equation
    boolean CheckPoint_Larger2Rs_withSetI(FloatPointItem point, List<Integer> SetI) {
        int count = 0;
        for (int i = 0; i < SetI.size(); i++) {
            if (calculateDistance(point.getX(), point.getY(), mListSensorNodes.get(SetI.get(i)).getX(), mListSensorNodes.get(SetI.get(i)).getY()) - SensorUtility.mSaiso >  Rs) {
                count++;
            }

        }
        if (count == SetI.size()-1) {
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
        sensor.add(21);
        sensor.add(22);
        sensor.add(23);
        sensor.add(24);
        sensor.add(26);
        //List<Double> a = LinearProAlgorithm(lisX, sensor, 5);
//        
        MyAlgorithm2 m = new MyAlgorithm2();
        List<Double> a = m.LinearProAlgorithm(lisX, sensor, 5);
        Double asd = a.get(0);
        int as= 5;
    }
}

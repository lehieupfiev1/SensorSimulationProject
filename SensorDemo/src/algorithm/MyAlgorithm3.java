/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import common.SensorUtility;
import static common.SensorUtility.mListSensorNodes;
import static common.SensorUtility.mListSinkNodes;
import static common.SensorUtility.mListTargetNodes;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FloatPointItem;
import model.HeuristicItem;

/**
 *
 * @author sev_user
 */
public class MyAlgorithm3 {
    
    public float Distance[][];// Matrix distance between two nodes
    public float MinDistanceSink[];// Matrix distance between two nodes
    public float Target[][];// Target nodes
    public float Point[][];// Total nodes
    public float Sink[][];// Target covering sensors
    
    double mTimeLife;
    float Rs, Rc;// Rs and Rt value
    float R; // Tinh phan chia nho nhat
    int MaxHopper;
    List<Integer> EECCcnt;   
    List<List<Integer>> NDEECCcnt;
    List<Double> listTime;
    List<HeuristicItem> Cov_Heuristic;
    List<HeuristicItem> Connect_Heuristic;
    List<Integer> ListNcs ;// Tap list sensing node
    List<Integer> CurrentHopper;
    List<Integer> ListNcr ; // Tap list relaying node
    float ListEnergySensor[];
    int MAX_INTERGER = 100000000;
    float MAX_FLOAT = 10000000000000.0f;
    float TimeStamp ;
    
    float Es, Et,Er,Efs,Emp,Do, bit;
    int cnt;
    
    int K;// Number Sink
    int N;//Number sensor
    int TP; // Total points (Contain Sensor , Sink, Target )
    int T;//Number of Tagert Nodes
    int anpha; // So lan tang
    
    public MyAlgorithm3() {
    }
    
    public void run() {
        
//        init();

        readData();
        //Step 1: Find target-covering Sensor
        //FindTargetCoveringSensor();
        
        //Step 2: 

        runAlgorithm();
        
//        CoppyToListSensor();
//        
//        freeData();
//        
//        System.gc();
    }
    
    
    public  void readData() {
        // Read Rs, Rc
        Rs = SensorUtility.mRsValue;
        Rc = SensorUtility.mRcValue;
        mTimeLife = 0;
        MaxHopper = SensorUtility.mMaxHopper;
        R = Rs + MaxHopper*Rc;
        
        //Read constance Energy : Es, Et,Er,Efs,Emp
        Es = SensorUtility.mEsValue;
        Et = SensorUtility.mEtValue;
        Er = SensorUtility.mErValue;
        Efs = SensorUtility.mEfsValue;
        Emp = SensorUtility.mEmpValue;
        Do = (float)Math.sqrt(Efs/Emp);
        bit = SensorUtility.mBitValue;
        TimeStamp = SensorUtility.mTstamp;
        
        //Read Sensor , Sink, Target 
        N = SensorUtility.mListSensorNodes.size();
        T = SensorUtility.mListTargetNodes.size();
        K = SensorUtility.mListSinkNodes.size();
        TP = N+ T+ K;
        
        //Add to Total Point;
        Point = new float[TP+1][2];
        ListEnergySensor = new float[N];

        for (int i =0; i < mListSensorNodes.size();i++) {
            Point[i][0] = mListSensorNodes.get(i).getX();
            Point[i][1] = mListSensorNodes.get(i).getY();
            //Add Energy for every node
            ListEnergySensor[i] = SensorUtility.mEoValue;
        }
        
        for (int i =0; i < mListTargetNodes.size();i++) {
            Point[N+i][0] = mListTargetNodes.get(i).getX();
            Point[N+i][1] = mListTargetNodes.get(i).getY();
        }
        for (int i =0; i < mListSinkNodes.size();i++) {
            Point[N+T+i][0] = mListSinkNodes.get(i).getX();
            Point[N+T+i][1] = mListSinkNodes.get(i).getY();
        }

        // Create matrix distance
        Distance = new float[TP+1][TP+1];
         for (int i =0;i<TP;i++) {
            for (int j =0;j<=i;j++) {
                if (i==j ) {
                    Distance[i][j] = 0;
                } else {
                    Distance[i][j] = Distance[j][i] = calculateDistance(Point[i][0], Point[i][1], Point[j][0], Point[j][1]);
                }
            }
        }
         
        //Caculate Mindistance form sensor to Sink
        MinDistanceSink = new float[N];
        float min;
        for (int i =0; i<N ;i++) {
            min = MAX_FLOAT;
            for (int j =0; j < K; j++) {
                if (Distance[i][N+T+j] < min) {
                    min = Distance[i][N+T+j];
                }
            }
            MinDistanceSink[i] = min;
        }

    }
    
    
    public  float calculateDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    
    boolean checkSensorConnectSink(int sensor, List<Integer> listSink) {
        for (int i =0; i< listSink.size();i++) {
            if (Distance[sensor][N+T+listSink.get(i)] <= Rc) {
                return true;
            }
        }
        return false;
    }
    void Finding_CCP(List<Integer> listSensor, List<Integer> listTarget, List<Integer> listSink, List<List<List<Integer>>> ListPi) {
        ListPi.clear();
        //Khoi tao danh sach Pi
        for(int i = 0; i< listTarget.size();i++) {
            List<List<Integer>> Pi = new ArrayList<>();
            ListPi.add(Pi);
        }
        List<List<Integer>> ListP = new ArrayList<>();
        //
        for(int i =0; i< listSensor.size();i++) {
            if (checkSensorConnectSink(listSensor.get(i), listSink)) {
                for (int j = 0; j< listTarget.size(); j++) {
                    if (Distance[listSensor.get(i)][N+listTarget.get(j)] <= Rs) {
                        List<Integer> list1 = new ArrayList<>();
                        list1.add(listSensor.get(i));
                        ListPi.get(j).add(list1);
                    }  
                }
                List<Integer> list = new ArrayList<>();
                list.add(listSensor.get(i));
                ListP.add(list);
                
            }
        }
        
        while (!ListP.isEmpty()) {
            List<Integer> headP = ListP.get(0);
            int lastSensor = headP.get(headP.size()-1); // Lay phan tu cuoi cung cua head
            
            for (int i = 0 ; i < listTarget.size(); i++) {
                if (Distance[lastSensor][N+listTarget.get(i)] <= Rs) {
                    ListPi.get(i).add(headP);
                }
            }
            
            if (headP.size() == MaxHopper+1) {
                ListP.remove(0);
            } else {
                for (int i = 0 ; i < listSensor.size(); i++) {
                    if (lastSensor != listSensor.get(i) && Distance[lastSensor][listSensor.get(i)] <= Rc) {
                        
                        if (!checkPointExitInList(listSensor.get(i), headP)) {
                            // Coppy to new Array
                            List<Integer> list = new ArrayList<>();
                            for (int j = 0 ; j< headP.size();j++) {
                                list.add(headP.get(j));
                            }
                            list.add(listSensor.get(i));
                            
                            //Add list to P
                            ListP.add(list);
                        }

                    }
                    
                }
                ListP.remove(0);
                
            }
 
        }
        //Dao chieu cua List
        
    }
    
    void Finding_CCP2(List<Integer> listSensor, List<Integer> listTarget, List<Integer> listSink, List<List<List<Integer>>> ListPathX) {
        List<List<List<Integer>>> ListPi = new ArrayList<>();
        List<List<Integer>> ListP = new ArrayList<>();
        List<List<Integer>> ListParent = new ArrayList<>();
        //Khoi tao danh sach Pi
        for (int k = 0; k < listTarget.size(); k++) {
            List<List<Integer>> Pi = new ArrayList<>();
            int target = listTarget.get(k);

            ListP.clear();
            ListParent.clear();
            //
            List<Integer> listParent1 = new ArrayList<>();
            int num =0;
            for (int i = 0; i < listSensor.size(); i++) {
                if (checkSensorConnectSink(listSensor.get(i), listSink)) {
                    List<Integer> list = new ArrayList<>();
                    list.add(listSensor.get(i));
                    listParent1.add(listSensor.get(i));
                    num++;
                    if (Distance[listSensor.get(i)][N + target] <= Rs) {
                       Pi.add(list);
                    } else {
                       ListP.add(list);
                       
                    }

                }
            }
            for (int j = 0;j< num;j++) {
               ListParent.add(listParent1);
            }
            

            while (!ListP.isEmpty()) {
                List<Integer> headP = ListP.get(0);
                List<Integer> headParent = ListParent.get(0);
                int lastSensor = headP.get(headP.size() - 1); // Lay phan tu cuoi cung cua head

                if (Distance[lastSensor][N + target] <= Rs) {
                    Pi.add(headP);
                    ListP.remove(0);
                    ListParent.remove(0);
                    continue;
                }

                if (headP.size() == MaxHopper) {
                    ListP.remove(0);
                    ListParent.remove(0);
                } else {
                    List<Integer> listParent = new ArrayList<>();
                    for (int j = 0; j < headParent.size(); j++) {
                        listParent.add(headParent.get(j));
                    }
                    int count =0;
                    for (int i = 0; i < listSensor.size(); i++) {
                        if (lastSensor != listSensor.get(i) && Distance[lastSensor][listSensor.get(i)] <= Rc) {

                            if (!checkPointExitInList(listSensor.get(i), headParent)) {
                                // Coppy to new Array
                                List<Integer> list = new ArrayList<>();
                                for (int j = 0; j < headP.size(); j++) {
                                    list.add(headP.get(j));
                                }
                                list.add(listSensor.get(i));
                                listParent.add(listSensor.get(i));
                                count++;

                                //Add list to P
                                ListP.add(list);
                            }

                        }

                    }
                    for (int j = 0;j< count;j++) {
                       ListParent.add(listParent);
                    }
                    ListP.remove(0);
                    ListParent.remove(0);

                }

            }
            
            ListPi.add(Pi);
        }
        
        //Dao nguoc ListPath
        ListPathX.clear();
        for (int i =0 ; i< ListPi.size(); i++) {
            List<List<Integer>> Pi = ListPi.get(i);
            List<List<Integer>> listPath = new ArrayList<>();
            for (int j = 0 ; j< Pi.size(); j++) {
                List<Integer> p = Pi.get(j);
                List<Integer> path = new ArrayList<>();
                for (int k = p.size()-1; k >= 0 ; k--) {
                    path.add(p.get(k));
                }
                listPath.add(path);
            }
            ListPathX.add(listPath);
        }
        int a =5;

    }
    
    
    
    
    boolean checkPointExitInList(int point , List<Integer> listPoint ) {
        for (int i = 0 ; i < listPoint.size(); i++) {
            if (point == listPoint.get(i)) return true;
        }
        return false;
    }
    
//    public List<Double> LinearProAlgorithm(List<List<List<Integer>>> listPathX, List<List<Integer>> listX, List<Integer> listSenSor, double valueT) {
//        List<Double> time = new ArrayList<>();
//        int m = listX.size();
//        int n = listSenSor.size();
//        //Test
////    if (m == 1) {
////        time.add(valueT);
////    } else if (m == 2) {
////        time.add(valueT);
////        time.add(valueT);
////    } else {
////        time.add(valueT);
////        time.add(valueT);
////        time.add(valueT);
////    }
//        float [][] a = new float[n][m];
//
//        //Check Input
//        for (int i = 0; i < n; i++) {
//            int sensor = listSenSor.get(i);
//            for (int j = 0; j < m; j++) {
//                a[i][j] = 0;
//                List<Integer> Xj = listX.get(j);
//                List<List<Integer>> pathXj = listPathX.get(j);
//                for (int k = 0; k < Xj.size(); k++) {
//                    if (sensor == Xj.get(k)) {
//                        a[i][j] = CaculateEnergyConsume(pathXj,sensor);
//                        break;
//                    }
//                }
//            }
//        }
//
//        try {
//            //Define new model
//            IloCplex cplex = new IloCplex();
//
//            //Variable
//            IloNumVar[] T = new IloNumVar[m];
//            for (int i = 0; i < m; i++) {
//                T[i] = cplex.numVar(0, Double.MAX_VALUE);
//            }
//
//            //Expression
//            IloLinearNumExpr[] totalTimeOnExpr = new IloLinearNumExpr[n];
//            for (int j = 0; j < n; j++) {
//                totalTimeOnExpr[j] = cplex.linearNumExpr();
//
//                for (int i = 0; i < m; i++) {
//                    totalTimeOnExpr[j].addTerm(a[j][i], T[i]);
//                }
//            }
//
//            IloLinearNumExpr objective = cplex.linearNumExpr();
//            for (int i = 0; i < m; i++) {
//                objective.addTerm(1, T[i]);
//            }
//
//            //Define Objective
//            cplex.addMaximize(objective);
//
//            //Constraints
//            for (int i = 0; i < n; i++) {
//                cplex.addLe(totalTimeOnExpr[i], valueT);
//            }
//
//            cplex.setParam(IloCplex.Param.Simplex.Display, 0);
//            //Resolve Model
//            if (cplex.solve()) {
//                for (int i = 0; i < m; i++) {
//                    time.add(cplex.getValue(T[i]));
//                    System.out.println("time: " + cplex.getValue(T[i]));
//                }
//                System.out.println("value: " + cplex.getObjValue());
//            } else {
//                System.out.println("Problem not solved");
//            }
//
//            cplex.end();
//
//        } catch (IloException ex) {
//            Logger.getLogger("LeHieu").log(Level.SEVERE, null, ex);
//        }
//        return time;
//    }
//    
    float TranferEnergy(float distance) {
        float result = Et;
        if (distance <Do) {
            result+= (Efs*distance*distance);
        } else {
            result+= (Emp*distance*distance*distance*distance);
        }
        return result;
    }
    float CaculateEnergyConsume(List<List<Integer>> ListPath,int sensor) {
        List<Integer> path;
        float result =0;
        for (int i =0 ; i < ListPath.size(); i++) {
            path = ListPath.get(i);
            for (int j =0; j<path.size();j++) {
                if (j == 0 && sensor == path.get(j)) {
                    //TH sensor la sensing node
                    result += bit*Es;
                    if (path.size()== 1) {
                        result += bit*TranferEnergy(MinDistanceSink[sensor]);
                    } else {
                        result += bit*TranferEnergy(Distance[sensor][path.get(j+1)]);
                    }
                    
                    break;
                } else if (sensor == path.get(j)) {
                    //TH sensor laf relay node
                    result += bit*Er;
                    if (j == path.size()-1) {
                        result += bit*TranferEnergy(MinDistanceSink[sensor]);
                    } else {
                        result += bit*TranferEnergy(Distance[sensor][path.get(j+1)]);
                    }
                    break;
                }
            }
            
        }
        return result;
    }
    
    //Ham sinh to hop 
    
    
    
    public void runAlgorithm() {
//        List<Integer> listSensor = new ArrayList<>();
//        for (int i = 0; i < mListSensorNodes.size(); i++) {
//            listSensor.add(i);
//        }
//        List<Integer> listTarget = new ArrayList<>();
//        for (int i = 0; i < mListTargetNodes.size(); i++) {
//            listTarget.add(i);
//        }
//        List<Integer> listSink = new ArrayList<>();
//        for (int i = 0; i < mListSinkNodes.size(); i++) {
//            listSink.add(i);
//        }
//        
//        List<List<List<Integer>>> ListPi = new ArrayList<>();
//        Finding_CCP2(listSensor,listTarget,listSink,ListPi);
//        int a =6;
//        CaculateEnergyConsume(ListPi.get(1),13);
//        CaculateEnergyConsume(ListPi.get(0),3);
//        CaculateEnergyConsume(ListPi.get(0),0);
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
        tempListX = null; 
        tempListT = null;
        upPoint = null;
        downPoint = null;
    }
    
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import common.SensorUtility;
import static common.SensorUtility.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import model.HeuristicItem;
import model.NodeItem;

/**
 *
 * @author sev_user
 */
public class EECCAlgorithm {
     public float Distance[][];// Matrix distance between two nodes
    public float MinDistanceSink[];// Matrix distance between two nodes
    public float Target[][];// Target nodes
    public float Point[][];// Total nodes
    public float Sink[][];// Target covering sensors
    
    double mTimeLife;
    float Rs, Rc;// Rs and Rt value
    int MaxHopper;
    List<Integer> EECCcnt;   
    List<List<Integer>> NDEECCcnt;
    List<Double> listTime;
    List<HeuristicItem> Cov_Heuristic;
    List<HeuristicItem> Connect_Heuristic;
    List<Integer> ListNcs ;// Tap list sensing node
    List<Integer> CurrentHopper;
    List<Integer> temp_hopper;
    List<Integer> previous_hopper;
    List<Integer> ListNcr ; // Tap list relaying node
    List<Integer> listTarget;
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
    
    
    public EECCAlgorithm() {
        
    }
    
    public void run() {

        init();

        readData();
      
        runAlgorithm();

        CoppyToListSensor();
        
        freeData();
    }
    
    
    public  void init() {

        NDEECCcnt = new ArrayList<List<Integer>>();
        Cov_Heuristic = new ArrayList<>();
        Connect_Heuristic = new ArrayList<>();
        ListNcs = new ArrayList<>();
        ListNcr = new ArrayList<>();
        CurrentHopper = new ArrayList<>();
        listTime = new ArrayList<>();
        temp_hopper = new ArrayList<>();
        previous_hopper = new ArrayList<>();
        
        
    }

    public  void readData() {
        // Read Rs, Rc
        Rs = SensorUtility.mRsValue;
        Rc = SensorUtility.mRcValue;
        mTimeLife = 0;
        MaxHopper = SensorUtility.mMaxHopper;
        
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
    
    
    public void runAlgorithm() {

        // Calculate Covering Heuristic
        List<Integer> listRelaySensor = new ArrayList<>();
        List<Integer> listSensingSensor = new ArrayList<>();
        List<Integer> listSensor = new ArrayList<>();
        for (int i = 0; i < mListSensorNodes.size(); i++) {
            listSensor.add(i);
        }
        listTarget = new ArrayList<>();
        for (int i = 0; i < mListTargetNodes.size(); i++) {
            listTarget.add(i);
        }
        List<Integer> listSink = new ArrayList<>();
        for (int i = 0; i < mListSinkNodes.size(); i++) {
            listSink.add(i);
        }

        //Bat dau vong lap
        boolean isTerminal = false;
        do {
            //Update listRelaySensor and sensing node
            listRelaySensor.clear();
            listSensingSensor.clear();
            for (int i = 0; i < listSensor.size(); i++) {
               listRelaySensor.add(listSensor.get(i));
            }
            for (int i =0; i < listSensor.size();i++) {
                for (int j = 0 ; j < listTarget.size(); j++) {
                    if (Distance[listSensor.get(i)][N+listTarget.get(j)] <= Rs) {
                        listSensingSensor.add(listSensor.get(i));
                        break;
                    }
                }
            }
            
            // ------------------Tinh list sensor cover Target------------------------------------
            Calculate_Cov_Heuristic(listTarget, listSensingSensor, Cov_Heuristic);
            Collections.sort(Cov_Heuristic, new Comparator<HeuristicItem>() {
                @Override
                public int compare(HeuristicItem o1, HeuristicItem o2) {
                    float distance1 = o1.getValue();
                    float distance2 = o2.getValue();
                    return Float.compare(distance2, distance1);
                }
            });

            //Calculate EECCcnt set
            boolean isCorvering = false;
            int hasCover = 0;
            EECCcnt = new ArrayList<>();
            while (!isCorvering && !Cov_Heuristic.isEmpty()) {
                int maxSvalue = Cov_Heuristic.get(0).getId();
                EECCcnt.add(maxSvalue);
                int ratio = Calculate_Coverage_Ratio(EECCcnt, listTarget);
                if (ratio > hasCover) {
                    hasCover = ratio;
                } else {
                    EECCcnt.remove(EECCcnt.size() - 1);
                }
                Cov_Heuristic.remove(0);
                if (ratio == listTarget.size()) {
                    isCorvering = true;
                }

            }

            //Check is not covering 
            if (!isCorvering) {
                return;
            }

            
            //Test 

            List<Integer> listStart = new ArrayList<>();
            for (int i = 0; i < EECCcnt.size(); i++) {
                listStart.add(EECCcnt.get(i));
            }
            //showViewTest(listStart);
            
            ListNcs.clear();
            int count;
            for (int i = 0; i < EECCcnt.size(); i++) {
                count = 0;
                for (int j = 0; j < listSink.size(); j++) {
                    if (Distance[EECCcnt.get(i)][N + T + listSink.get(j)] <= Rc) {
                        break;
                    } else {
                        count++;
                    }
                }
                if (count == listSink.size()) {
                    ListNcs.add(EECCcnt.get(i));
                }
                
            }

            //showViewTest(ListNcs);
            //-----------------------Tinh list Sensor den cac sink--------------------------------------------

            // Vong lap tim Connect Node
            boolean isConectivity = false;
//            List<Integer> listNearSink = new ArrayList<>();
//            listNearSink.clear();

            
            boolean isFirstConectivity = false;
            if (ListNcs.size() == 0) {
                isConectivity = true;
                isFirstConectivity = true;
            } else {

                Find_Current_Hoppers(listRelaySensor, listSink, ListNcr, CurrentHopper,ListNcs);
                //Them temp_hopper
                temp_hopper.clear();
                for (int i =0; i< ListNcs.size();i++) {
                    temp_hopper.add(ListNcs.get(i));
                }
                showViewTest(CurrentHopper);
                
                //Tao list Check Covering LisNCS
                int numberOfIterations = 0;
                boolean IsCover[] = new boolean[listStart.size()];
                for (int j = 0; j < listStart.size();j++) IsCover[j] = false;
                
                
                while (!isConectivity  && !CurrentHopper.isEmpty() && numberOfIterations < MaxHopper) {
                    previous_hopper.clear();
                    for (int i =0; i< temp_hopper.size();i++) {
                        previous_hopper.add(temp_hopper.get(i));
                    }
                    Calculate_Connect_Heuristic(listRelaySensor, listSink, CurrentHopper, ListNcs, ListNcr, Connect_Heuristic,previous_hopper);

                    temp_hopper.clear();
                    
                    //Sort Connect Heuristic
                    Collections.sort(Connect_Heuristic, new Comparator<HeuristicItem>() {
                        @Override
                        public int compare(HeuristicItem o1, HeuristicItem o2) {
                            float distance1 = o1.getValue();
                            float distance2 = o2.getValue();
                            return Float.compare(distance2, distance1);
                        }
                    });

                    //test
                    List<Integer> testSensor = new ArrayList<>();
                    for (int i = 0; i < Connect_Heuristic.size(); i++) {
                        testSensor.add(Connect_Heuristic.get(i).getId());
                    }
                    //showViewTest(testSensor);

                    //Lay tung phan tu cua Connect Heuristic 
                    while (!Connect_Heuristic.isEmpty()) {
                        int selected_sensor = Connect_Heuristic.get(0).getId();
                        
                        if (!CheckNodeExit(EECCcnt, selected_sensor)) {
                            temp_hopper.add(selected_sensor);
                            EECCcnt.add(selected_sensor);
//                            if (numberOfIterations == 0) {
//                                listNearSink.add(selected_sensor);
//                            }
                            //Add check
                            if (CheckConectivity(listStart, IsCover, EECCcnt)) {
                                //showViewTest(listNearSink);
                                isConectivity = true;
                                break;
                            } else {
                                isConectivity = false;
                            }

                        }
                        //Remove max heurius
                        Connect_Heuristic.remove(0);
                        int a = 5;

                    }
                    //Check lai
                    if (!isConectivity) {
                        if (CurrentHopper.isEmpty() || ListNcr.isEmpty()) {
                            return;

                        } else {
                            // Khoi tao next Hopper
                            numberOfIterations++;
                            List<Integer> next_hopper = Hop_Finder(ListNcr, temp_hopper);

                            CurrentHopper = next_hopper;
                            //showViewTest(CurrentHopper);
                            int a = 5;
                        }
                    }

                }
            }
            
            if (isConectivity) {
                //Calculate Eneergy
                NDEECCcnt.add(EECCcnt);
                //showViewTest(EECCcnt);
                //Create ListECRi : nang luong tieu hao
                float listEcri[] = new float[N];
                for (int i = 0;i <listEcri.length;i++ ) {
                    listEcri[i] = 0.0f;
                }
                //Add ListNearsink
//                if (isFirstConectivity) {
//                    for (int i = 0; i< listStart.size() ;i++) {
//                        for (int j = 0 ;j < listSink.size();j++) {
//                            if (Distance[listStart.get(i)][N+T+listSink.get(j)] <= Rc) {
//                                listNearSink.add(listStart.get(i));
//                                break;
//                            }
//                        }
//                        
//                    }
 //               }
                
                float L = LifeCycle(EECCcnt,ListEnergySensor,listEcri,listStart,listTarget,listSink);
                
                mTimeLife+= L*TimeStamp;
                listTime.add((double)L*TimeStamp);
                
                
                
                Update_Energy_Sensor(EECCcnt,ListEnergySensor,listEcri,listSensor,L);
                
                //showViewTest(EECCcnt);
                int a = 2;
                
                
            } else {
                isTerminal = true;
            }

        } while (!isTerminal);
        

    }
    
    //Update energy
    public void Update_Energy_Sensor(List<Integer> listEECCcnt,float listEnergySensor[], float listEcri[], List<Integer> listSensor , float L) {
        
        for (int i = 0; i < listEECCcnt.size(); ) {
            float energyUsing = L * listEcri[listEECCcnt.get(i)];
            if (energyUsing == 0) {
                listEECCcnt.remove(i);
            } else {
                listEnergySensor[listEECCcnt.get(i)] -= energyUsing;
                //Check TH het nang luong
                if (listEnergySensor[listEECCcnt.get(i)] < listEcri[listEECCcnt.get(i)]) {

                    for (int j = 0; j < listSensor.size(); j++) {
                        if (Objects.equals(listSensor.get(j), listEECCcnt.get(i))) {
                            listSensor.remove(j);
                            break;
                        }
                    }

                }
                i++;
            }
        }
        
        
    }
    
    // Check energy Sensor
    public float LifeCycle (List<Integer> listEECCcnt, float listEnergySensor[], float listEcr[],List<Integer> listStart,List<Integer> listTarget, List<Integer> listSink) {
        //Tinh toan duong di ngan nhat toi sink
        List<List<Integer>> ListPathSensor = new ArrayList<>();
        Calculate_Path_ToSink(listStart,listTarget,listEECCcnt,ListPathSensor);
        
        //Tinh toan nang luong tieu thu qua cac sensor;

        Calculate_Energy_Consumption(listSink,ListPathSensor,listEcr);
        
        //Tinh nang luong tieu thu voi bit
        for (int i =0;i < listEcr.length;i++) {
            listEcr[i] *= bit;
        }
        
        //Find gia tri nho nhat L
        float L = 1000000000000.0f;
        for (int i = 0;i < listEECCcnt.size();i++) {
            int sensor = listEECCcnt.get(i);
            if (L > (listEnergySensor[sensor]/listEcr[sensor])) {
                L = (listEnergySensor[sensor]/listEcr[sensor]);
            }
        }
        return L;

    }
    // Tinh toan nang luong tieu hao
    public void Calculate_Path_ToSink(List<Integer> listStart, List<Integer> listTarget, List<Integer> listEECCcnt, List<List<Integer>> listPathResult) {
       //Create matrix Distance from ListEECCcnt
       int N1 = listEECCcnt.size();
       int Matrix[][] = new int[N1+1][N1+1];
       for (int i = 0; i<listEECCcnt.size();i++) {
           for (int j =0; j <= i;j++) {
               if (i == j) {
                   Matrix[i][j] = 0;
               } else if (Distance[listEECCcnt.get(i)][listEECCcnt.get(j)] <= Rc) {
                   Matrix[i][j] = Matrix[j][i] = 1;
               } else {
                   Matrix[i][j] = Matrix[j][i] = 0;
               }
           }
       }
       //Khoang cacah den Sink
       Matrix[N1][N1] = 0;
       for (int i = 0; i<listEECCcnt.size();i++) {
           if (MinDistanceSink[listEECCcnt.get(i)] <= Rc) {
               Matrix[i][N1] = Matrix[N1][i] = 1;
           } else {
               Matrix[i][N1] = Matrix[N1][i] = 0;
           }
       }
       
       //Using DijtraAlgorithm
        int back[] = new int[N1+1]; //luu dinh cha
        int weight[] = new int[N1+1];//luu trong so
        List<Integer> path;
        listPathResult.clear();
        int time[] = new int[listStart.size()];
        for (int i =0; i< listStart.size();i++) {
            time[i] = 0;
        }
        //Check node start duoc bao nhieu target di qua
        for (int i = 0; i<listTarget.size();i++) {
            float distance = Rs;
            int pos = 0;
            for (int j =0; j<listStart.size();j++) {
                if (Distance[listStart.get(j)][this.N+listTarget.get(i)] <= distance) {
                    distance = Distance[listStart.get(j)][this.N+listTarget.get(i)];
                    pos = j;
                }
            }
            time[pos]++;
            
        }
        
        for (int i = 0; i < listStart.size(); i++) {
            //Check TH start point is not cover
            int result;
            int minWeith = MaxHopper + 1;
            path = new ArrayList<>();
            int posEnd = N1;

            result = DijkstraAlgorithm(i, posEnd, Matrix, N1+1, back, weight);
            if (result != -1 && minWeith > weight[posEnd]) { // Ton tai duong di
                minWeith = weight[posEnd];
                path.clear();
                FindListPath(i, posEnd, back, listEECCcnt, path,N1);
                showViewTest(path);
            }

            //Add to list
            for (int j = 0; j < time[i]; j++) {
                listPathResult.add(path);
            }
            int a = 4;
        }
    }
    
//Calculate Energy Comsumpation
    public void Calculate_Energy_Consumption(List<Integer> listSink, List<List<Integer>> listPath, float ListEcr[]) {
        int numberListPath[] = new int[listPath.size()];
        for (int i =0; i < numberListPath.length;i++) {
            numberListPath[i] = 0;
        }
        boolean checkTarget[] = new boolean[listTarget.size()];
        int count =0;
        for (int i =0; i < listPath.size(); i++) {
            List<Integer> path = listPath.get(i);
            int startPoint = path.get(0);
            for (int j = 0; j < listTarget.size(); j++) {
                if (!checkTarget[j] && Distance[startPoint][N+j] <= Rs) {
                    checkTarget[j] = true;
                    int num = numberListPath[i];
                    num++;
                    numberListPath[i] = num;
                    count++;
                }
            }

        }
        System.out.println("Size target = "+listTarget.size() + "---- Size do phu count ="+count);
        
        for (int i = 0; i < listPath.size(); i++) {
            List<Integer> path = listPath.get(i);
            int num = numberListPath[i];
            if (path.size() == 1) {
                //Chi co 1 node trung gian
                //Tieu hoa = recive + sending + tranfer

                float minDistane = 100000000000000.0f;
//                for(int j =0;j<listSink.size();j++) {
//                    if (Distance[path.get(0)][N+T+listSink.get(j)] < minDistane) {
//                        minDistane = Distance[path.get(0)][N+T+listSink.get(j)];
//                    }
//                }
                minDistane = MinDistanceSink[path.get(0)]; 
                ListEcr[path.get(0)] += (Es +Er+ TranferEnergy(minDistane))* num;

            } else {
                //Tinh diem dau tien
                int start = path.get(0);
                ListEcr[start] += (Es+ TranferEnergy(Distance[start][path.get(1)]))*num;
                
                //Tinh diem trung gian
                for (int j = 1; j < path.size()-1; j++) {
                    ListEcr[path.get(j)] += (Er + TranferEnergy(Distance[path.get(j)][path.get(j+1)]))*num;
                }
                
                //Tinh diem cuoi
                int end = path.get(path.size()-1);
                float minDistane = 100000000000000.0f;
//                for(int j =0;j<listSink.size();j++) {
//                    if (Distance[end][N+T+listSink.get(j)] < minDistane) {
//                        minDistane = Distance[end][N+T+listSink.get(j)];
//                    }
//                }
                minDistane = MinDistanceSink[end];
                ListEcr[end] += (Er + TranferEnergy(minDistane))*num;
            }
        }

    }
    float TranferEnergy(float distance) {
        float result = Et;
        if (distance <Do) {
            result+= (Efs*distance*distance);
        } else {
            result+= (Emp*distance*distance*distance*distance);
        }
        return result;
    }
    
    void FindListPath(int start, int end ,int back[],List<Integer> listEECCcnt, List<Integer> listPath,int N1) {
        
        if (start == end) {
            if (end != N1) listPath.add(listEECCcnt.get(end));
            System.out.print("sink"+ "-");
        } else {
            FindListPath(start, back[end], back,listEECCcnt,listPath,N1);
            if (end != N1) listPath.add(listEECCcnt.get(end));
            System.out.print(end+ "-");
        }
    }
    //Check conect with only ListNcs
    public boolean CheckFirstConectivity(List<Integer> listNcs, List<Integer> listSink) {
        int count = 0;
        for (int i = 0; i < listNcs.size(); i++) {
            count =0;
            for (int j = 0; j < listSink.size(); j++) {
                if (Distance[listNcs.get(i)][N + T + listSink.get(j)] <= Rc) {
                    break;
                } else {
                   count ++; 
                }
            }
            if (count == listSink.size()) {
                return false;
            }
        }
        return true;
    }
    
    // Check ham xem 
    public boolean CheckConectivity(List<Integer> listStart, boolean IsCover[], List<Integer> listEECCcnt) {
        //Create matrix Distance from ListEECCcnt
       int N1 = listEECCcnt.size();
       int Matrix[][] = new int[N1+1][N1+1];// Them 1 bien lu khoang cach nhỏ nhat đen bien 
       for (int i = 0; i<listEECCcnt.size();i++) {
           for (int j =0; j <= i;j++) {
               if (i == j) {
                   Matrix[i][j] = 0;
               } else if (Distance[listEECCcnt.get(i)][listEECCcnt.get(j)] <= Rc) {
                   Matrix[i][j] = Matrix[j][i] = 1;
               } else {
                   Matrix[i][j] = Matrix[j][i] = 0;
               }
           }
       }
       //Khoang cacah den Sink
       Matrix[N1][N1] = 0;
       for (int i = 0; i<listEECCcnt.size();i++) {
           if (MinDistanceSink[listEECCcnt.get(i)] <= Rc) {
               Matrix[i][N1] = Matrix[N1][i] = 1;
           } else {
               Matrix[i][N1] = Matrix[N1][i] = 0;
           }
       }
        
        
        //Using DijtraAlgorithm
        for (int i = 0; i < listStart.size(); i++) {
            //Check TH start point is not cover
            if (!IsCover[i]) {
                int posEnd = N1;
                int back[] = new int[N1 + 1]; //luu dinh cha
                int weight[] = new int[N1 + 1];//luu trong so
                int result = DijkstraAlgorithm(i, posEnd, Matrix, N1 + 1, back, weight);
                if (result != -1) { // Ton tai duong di
                    IsCover[i] = true;
                    break;
                }
                back = null;
                weight = null;
            }
        }
        
        // Check is connectivity
        int count = 0;
        for (int i = 0; i < listStart.size(); i++) {
            if (IsCover[i]) {
                count++;
            } else {
                break;
            }
        }
        Matrix = null;
        if (count == listStart.size()) return true;
        return false;
        
    }
    int findPostion(List<Integer> list , int value) {
        for (int i =0; i< list.size();i++) {
            if (value == list.get(i)) {
                return i;
            }
        }
        return 0;
    }
     
    int DijkstraAlgorithm(int start, int end, int Matrix[][],int N, int back[], int weight[]) {

        int mark[] = new int[N];//danh dau dinh
        
        int st = start;
        
        //Khoi tao gia tri
        for (int i = 0; i<N;i++) {
            back[i] = -1;
            weight[i] = MAX_INTERGER;
            mark[i] = 0;
        }
        
        //Xuat phat tu dinh dau tien
        back[start] = 0;
        weight[start] = 0;
        
        //Kiem tra do thi co lien thong ko
        int connect;
        do {
            //Gan connect = -1;
            connect = -1;
            int min = MAX_INTERGER;
            //Lan luot duyet qua tat ca ca diem cau do thi
            for (int j =0; j <N;j++) {
                if (mark[j] == 0) { //Dinh chua duoc danh dau
                    if (Matrix[start][j] != 0 && weight[j] > weight[start] + Matrix[j][start]) {
                        //Update weight and back
                        weight[j] = weight[start] + Matrix[j][start];
                        //Luu dinh cah
                        back[j] = start;
                    }
                    
                    //Dua vao mang weight tim duong di ngan nhat hien tai
                    if (min > weight[j]) {
                        min = weight[j];
                        //Dua vao bien connect t co quyet dinh di tiep hay ko
                        connect = j;
                    }
                }
                
                
            }
            if (connect != -1) {
                start = connect;
                mark[start] = 1;
            }
        } while (connect != -1 && start != end);
        
        
        //In ket qua
        if (connect != -1 && weight[end] <= MaxHopper) {
            System.out.println("Weight :" + weight[end]);
            printPath(st, end, back);
            return weight[end]+1;
        } else {
            System.out.println("Ko ket noi");
            return -1;
        }

    }
    void printPath(int start, int end ,int back[]) {
        if (start == end) {
            System.out.print(end+ "->");
        } else {
            printPath(start, back[end], back);
            System.out.print(end+ "->");
        }
    }
    
    boolean CheckNodeExit(List<Integer> list, int node) {
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i) == node) return true;
        }
        return false;
    }
    // Calculate Covering heuristic
     public void Calculate_Cov_Heuristic(List<Integer> listTaget, List<Integer> listSensor,List<HeuristicItem> listCovHeuristic) {
         List<Integer> listTotalCoverTarget = new ArrayList<>();
         listTotalCoverTarget.clear();
         for (int i = 0;i<listTaget.size();i++) {
             int result = Total_CoverTaget(listSensor,listTaget.get(i));
             listTotalCoverTarget.add(result);
         }
         
         //Calculate Cov Heuristic
         listCovHeuristic.clear();
         for (int i = 0;i < listSensor.size(); i++) {
             HeuristicItem heuristicItem = new HeuristicItem();
             heuristicItem.setId(listSensor.get(i));
             float kq = 0;
             for(int j = 0; j < listTaget.size();j++) {
                 if (Distance[listSensor.get(i)][N+listTaget.get(j)] <= Rs) {
                     kq += (1.0/(listTotalCoverTarget.get(j)));
                 }
             }
             heuristicItem.setValue(kq);
             listCovHeuristic.add(heuristicItem);
         }
     }
     public int Total_CoverTaget(List<Integer> listSensor, int target) {
         int result = 0;
         for (int i = 0; i< listSensor.size(); i++ ) {
             if (Distance[listSensor.get(i)][N+target] <= Rs) {
                result++; 
             }
         }
         return result;
     }
    
     // Calculate Coverager Ratio 
     public int Calculate_Coverage_Ratio(List<Integer> listEECCcnt, List<Integer> listTarget) {
         int result  = 0;
         for (int i  = 0; i< listTarget.size();i++) {
             for (int j =0;j < listEECCcnt.size();j++) {
                 if (Distance[listEECCcnt.get(j)][N+listTarget.get(i)] <= Rs) {
                     result++;
                     break;
                 }
             }
         }
         return result;
     }
     
     //Find current_hoppper //Chua check
     public void Find_Current_Hoppers(List<Integer> listRelaySensor,List<Integer> listSink, List<Integer> listNCR, List<Integer> listCurrenHopper,List<Integer> listNCS) {
         listNCR.clear();
         listCurrenHopper.clear();
         for (int i =0; i< listRelaySensor.size();) {
            // int countSink =0;
             int countNcs =0;
//             for (int j = 0;j < listSink.size();j++) {
//                 if (Distance[listRelaySensor.get(i)][N+T+listSink.get(j)] <= Rc) {
//
//                     break;
//                 } else {
//                     countSink++;
//                 }
//             }
             
             for (int j=0; j < listNCS.size();j++) {
                 if (Distance[listRelaySensor.get(i)][listNCS.get(j)] <= Rc) {
                     break;
                 } else {
                     countNcs++;
                 }
             }
                     
             if (countNcs == listNCS.size()) {
                 listNCR.add(listRelaySensor.get(i));
                 listRelaySensor.remove(i);
             } else {
                 listCurrenHopper.add(listRelaySensor.get(i));
                 i++;
             }
         }
         
     }
    
     // Calcualate Connect Heuristic 
     public void Calculate_Connect_Heuristic(List<Integer> listSensor, List<Integer> listSink, List<Integer> listCurrentHopper, List<Integer> listNCS, List<Integer> listNCR ,List<HeuristicItem> listConnectHeuristic,List<Integer> previous_hopper) {
         List<Integer> listTotalCoverNCS = new ArrayList<>();
         listTotalCoverNCS.clear();
         for (int i = 0;i<previous_hopper.size();i++) {
             int result = Total_CoverNCS(listSensor,previous_hopper.get(i));
             listTotalCoverNCS.add(result);
         }
         
         //Calculate Connect Heuristic
         listConnectHeuristic.clear();
         for (int i = 0; i < listCurrentHopper.size(); i++){
             HeuristicItem heuristicItem = new HeuristicItem();
             heuristicItem.setId(listCurrentHopper.get(i));
             float kq = 0;
             for(int j = 0; j < previous_hopper.size();j++) {
                 if (Distance[listCurrentHopper.get(i)][previous_hopper.get(j)] <= Rc) {
                     kq += (1.0/(listTotalCoverNCS.get(j)));
                 }
             }
             
             //Devider for min || Srm- Sink||
             float minValue  = Float.MAX_VALUE;
             for (int j =0; j < listSink.size(); j++) {
                 if (minValue > Distance[listCurrentHopper.get(i)][N+T+listSink.get(j)]) {
                     minValue = Distance[listCurrentHopper.get(i)][N+T+listSink.get(j)];
                 }
             }
             //Check case minValue == 0
             if (minValue <= 0.1) {
                 minValue = 0.1f;
             }
             kq = kq/minValue;
             heuristicItem.setValue(kq);
             listConnectHeuristic.add(heuristicItem);
         }
         
         //Delete listConnectHeuristic == 0;
         for (int i = 0; i < listConnectHeuristic.size();) {
             if (listConnectHeuristic.get(i).getValue() == 0.000000000f) {
                 listConnectHeuristic.remove(i);
       //          listNCR.add(listCurrentHopper.get(i));
                 listCurrentHopper.remove(i);
                 
             } else i++;

         }
         
     }
     
     public int Total_CoverNCS(List<Integer> listSensor, int ncs) {
         int result = 0;
         for (int i = 0; i< listSensor.size(); i++ ) {
             if (Distance[listSensor.get(i)][ncs] <= Rc) {
                result++; 
             }
         }
         return result;
     }
     
    public List<Integer> Hop_Finder (List<Integer> listNCR, List<Integer> listCurrentHopper) {
        List<Integer> listNextHopper = new ArrayList<>();
        
         for (int i = 0; i< listCurrentHopper.size();i++) {
             for (int j = 0; j < listNCR.size(); ) {
                 if (Distance[listCurrentHopper.get(i)][listNCR.get(j)] <= Rc) {
                     listNextHopper.add(listNCR.get(j));
                     listNCR.remove(j);
                 } else j++;
             }
         }
         
         return listNextHopper;
    }
     
    public void CoppyToListSensor() {
        mListofListSensor.clear();
        for (int i =0;i<NDEECCcnt.size();i++ ) {
            List<Integer> temp = NDEECCcnt.get(i);
            List<NodeItem> tempNodeList = new ArrayList<>();
            for (int j =0;j<temp.size();j++) {
               tempNodeList.add(mListSensorNodes.get(temp.get(j)));
            }
            mListofListSensor.add(tempNodeList);
        }
        mListofListTime = listTime;
        
         //Show log Energy ofsensor
        System.out.println();
        System.out.println("Nang luong tieu thu cua sensor");
        for (int i =0; i < ListEnergySensor.length; i++) {
            float energy = SensorUtility.mEoValue - ListEnergySensor[i];
            System.out.print(""+energy/1000000000+" ");        
            
        }
        System.out.println();
    }
    
    public void showViewTest(List<Integer> listSensor) {                                            
//        // TODO add your handling code here:
//        //Clear data
//        for (int j = 0; j < mListSensorNodes.size(); j++) {
//            mListSensorNodes.get(j).setStatus(0);
//        }
//
//        for (int i =0;i<listSensor.size();i++) {
//           //Change Value On foreach Sensor   
//            mListSensorNodes.get(listSensor.get(i)).setStatus(1);
//            
//        }
//        coordinatePanel.refresh();
    }    
    
    public void freeData() {
        NDEECCcnt = null;
        Cov_Heuristic = null;
        Connect_Heuristic = null;
        ListNcs = null;
        ListNcr = null;
        CurrentHopper = null;
        listTime = null;
        Point = null;
        Distance = null;
        MinDistanceSink = null;
        ListEnergySensor = null;
    }

}
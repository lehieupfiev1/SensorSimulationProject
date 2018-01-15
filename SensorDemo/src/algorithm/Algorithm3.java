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
public class Algorithm3 {

    public static float Distance[][];// Matrix distance between two nodes
    public static float Target[][];// Target nodes
    public static float Point[][];// Total nodes
    public static float Sink[][];// Target covering sensors
    
    static double mTimeLife;
    static float Rs, Rt, Rc;// Rs and Rt value
    static int MaxHopper;
    static List<Integer> EECCcnt;   
    static List<List<Integer>> NDEECCcnt;
    static List<HeuristicItem> Cov_Heuristic;
    static List<HeuristicItem> Connect_Heuristic;
    static List<Integer> ListNcs ;// Tap list sensing node
    static List<Integer> CurrentHopper;
    static List<Integer> ListNcr ; // Tap list relaying node
    static float ListEnergySensor[];
    int MAX_INTERGER = 100000000;
    int TimeStamp = 100;
    
    static float Es, Et,Er,Efs,Emp,Do, bit;
    static int cnt;
    
    static int K;// Number Sink
    static int N;//Number sensor
    static int TP; // Total points (Contain Sensor , Sink, Target )
    static int T;//Number of Tagert Nodes
    
    
    public Algorithm3() {
        
    }
    
    public void run() {

        init();

        readData();
      
        runAlgorithm();

        CoppyToListSensor();
        //freeData();
    }
    
    
    public  void init() {

        NDEECCcnt = new ArrayList<List<Integer>>();
        Cov_Heuristic = new ArrayList<>();
        Connect_Heuristic = new ArrayList<>();
        ListNcs = new ArrayList<>();
        ListNcr = new ArrayList<>();
        CurrentHopper = new ArrayList<>();
        
        
    }

    public static void readData() {
        // Read Rs, Rt
        Rs = SensorUtility.mRsValue;
        Rt = SensorUtility.mRtValue;
        Rc = SensorUtility.mRtValue;
        mTimeLife = 0;
        MaxHopper = 3;
        
        //Read constance Energy : Es, Et,Er,Efs,Emp
        Es = 1;
        Et = 2.0F;
        Er = 2;
        Efs = 0.5F;
        Emp = 0.4f;
        Do = (float)Math.sqrt(Efs/Emp);
        bit = 2;
        
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
            ListEnergySensor[i] = 10000.0f;
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

    }
    
    
    public  static float calculateDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    
    
    public void runAlgorithm() {

        // Calculate Covering Heuristic
        List<Integer> listSensor = new ArrayList<>();
        for (int i = 0; i < mListSensorNodes.size(); i++) {
            listSensor.add(i);
        }
        List<Integer> listTarget = new ArrayList<>();
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
            // ------------------Tinh list sensor cover Target---------------------------
            Calculate_Cov_Heuristic(listTarget, listSensor, Cov_Heuristic);
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
            do {
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

            } while (!isCorvering && !Cov_Heuristic.isEmpty());

            //Check is not covering 
            if (!isCorvering) {
                return;
            }

            //Find ListNCS : listSensing Node // Can xem xet lai
            ListNcs.clear();
            for (int i = 0; i < EECCcnt.size(); i++) {
                ListNcs.add(EECCcnt.get(i));
            }

            //-----------------------Tinh list Sensor den cac sink--------------------------------------------
            Find_Current_Hoppers(listSensor, listSink, ListNcr, CurrentHopper);

            // Vong lap tim Connect Node
            int numberOfIterations = 0;
            boolean isConectivity = false;
            List<Integer> listNearSink = new ArrayList<>();
            listNearSink.clear();
            //Tao list Check Covering LisNCS
            boolean IsCover[] = new boolean[ListNcs.size()];
            for (int j = 0; j < ListNcs.size();j++) IsCover[j] = false;
            
            while (!isConectivity && !ListNcr.isEmpty() && !CurrentHopper.isEmpty() && numberOfIterations < MaxHopper) {
                Calculate_Connect_Heuristic(listSensor, listSink, CurrentHopper, ListNcs, Connect_Heuristic);

                //Sort Connect Heuristic
                Collections.sort(Connect_Heuristic, new Comparator<HeuristicItem>() {
                    @Override
                    public int compare(HeuristicItem o1, HeuristicItem o2) {
                        float distance1 = o1.getValue();
                        float distance2 = o2.getValue();
                        return Float.compare(distance2, distance1);
                    }
                });

                //Lay tung phan tu cua Connect Heuristic 
                while (!Connect_Heuristic.isEmpty()) {
                    int selected_sensor = Connect_Heuristic.get(0).getId();
                    if (!CheckNodeExit(EECCcnt, selected_sensor)) {
                        EECCcnt.add(selected_sensor);
                        if (numberOfIterations == 0) {
                            listNearSink.add(selected_sensor);
                        }
                        //Add check
                        if (CheckConectivity(ListNcs, IsCover, EECCcnt, listNearSink)) {
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
                        List<Integer> next_hopper = Hop_Finder(ListNcr, CurrentHopper);

                        CurrentHopper = next_hopper;
                        int a = 5;
                    }
                }

            }
            
            if (isConectivity) {
                //Calculate Eneergy
                NDEECCcnt.add(EECCcnt);
                //Create ListECRi : nang luong tieu hao
                float listEcri[] = new float[N];
                for (int i = 0;i <listEcri.length;i++ ) {
                    listEcri[i] = 0.0f;
                }
                float L = LifeCycle(EECCcnt,ListEnergySensor,listEcri,ListNcs,listNearSink,listTarget,listSink);
                
                mTimeLife+= L*TimeStamp;
                Update_Energy_Sensor(EECCcnt,ListEnergySensor,listEcri,listSensor,L);
                
                int a = 2;
                
                
            } else {
                isTerminal = true;
            }

        } while (!isTerminal);
        

    }
    
    //Update energy
    public void Update_Energy_Sensor(List<Integer> listEECCcnt,float listEnergySensor[], float listEcri[], List<Integer> listSensor , float L) {
        
        for(int i= 0 ; i < listEECCcnt.size();i++) {
            listEnergySensor[listEECCcnt.get(i)] -= L*listEcri[listEECCcnt.get(i)];
            //Check TH het nang luong
            if (listEnergySensor[listEECCcnt.get(i)] < listEcri[listEECCcnt.get(i)]) {
                 
                for (int j = 0; j<listSensor.size();j++) {
                    if (Objects.equals(listSensor.get(j), listEECCcnt.get(i))) {
                        listSensor.remove(j);
                        break;
                    }
                }
                
                
            }
        }
        
        
    }
    
    // Check energy Sensor
    public float LifeCycle (List<Integer> listEECCcnt, float listEnergySensor[], float listEcr[],List<Integer> listStart, List<Integer> listEnd,List<Integer> listTarget, List<Integer> listSink) {
        //Tinh toan duong di ngan nhat toi sink
        List<List<Integer>> ListPathSensor = new ArrayList<>();
        Calculate_Path_ToSink(listStart,listEnd,listTarget,listEECCcnt,ListPathSensor);
        
        //Tinh toan nang luong tieu thu qua cac sensor;

        Calculate_Energy_Consumption(listSink,ListPathSensor,listEcr);
        
        //Tinh nang luong tieu thu voi bit
        for (int i =0;i < listEcr.length;i++) {
            listEcr[i] *= bit;
        }
        
        //Find gia tri nho nhat L
        float L = 1000000.0f;
        for (int i = 0;i < listEECCcnt.size();i++) {
            int sensor = listEECCcnt.get(i);
            if (L > (listEnergySensor[sensor]/listEcr[sensor])) {
                L = (listEnergySensor[sensor]/listEcr[sensor]);
            }
        }
        return L;

    }
    // Tinh toan nang luong tieu hao
    public void Calculate_Path_ToSink(List<Integer> listStart,List<Integer> listEnd, List<Integer> listTarget, List<Integer> listEECCcnt, List<List<Integer>> listPathResult) {
       //Create matrix Distance from ListEECCcnt
       int N = listEECCcnt.size();
       int Matrix[][] = new int[N][N];
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
       
       //Using DijtraAlgorithm
        int back[] = new int[N]; //luu dinh cha
        int weight[] = new int[N];//luu trong so
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
                if (Distance[listStart.get(j)][N+listTarget.get(i)] <= distance) {
                    distance = Distance[listStart.get(j)][N+listTarget.get(i)];
                    pos = j;
                }
            }
            time[pos]++;
            
        }
        
        for (int i= 0;i < listStart.size();i++) {
            //Check TH start point is not cover
            int result;
            int minWeith = MaxHopper+1;
            path = new ArrayList<>();
            for (int j = 0; j < listEnd.size(); j++) {
                int posEnd = findPostion(listEECCcnt, listEnd.get(j));

                result = DijkstraAlgorithm(i, posEnd, Matrix, N, back, weight);
                if (result != -1 && minWeith > weight[posEnd]) { // Ton tai duong di
                    minWeith = weight[posEnd];
                    path.clear();
                    FindListPath(i,posEnd,back,listEECCcnt,path);
                }

            }
            
            //Add to list
            for (int j =0 ;j <time[i];j++) {
               listPathResult.add(path);
            }
            int a = 4;
        }
    }
    
//Calculate Energy Comsumpation
    public void Calculate_Energy_Consumption(List<Integer> listSink, List<List<Integer>> listPath, float ListEcr[]) {
        for (int i = 0; i < listPath.size(); i++) {
            List<Integer> path = listPath.get(i);
            if (path.size() == 1) {
                //Chi co 1 node trung gian
                //Tieu hoa = recive + sending + tranfer

                float minDistane = 1000000000.0f;
                for(int j =0;j<listSink.size();j++) {
                    if (Distance[path.get(0)][N+T+listSink.get(j)] < minDistane) {
                        minDistane = Distance[path.get(0)][N+T+listSink.get(j)];
                    }
                }
                ListEcr[path.get(0)] += Es+Er + TranferEnergy(minDistane);

            } else {
                //Tinh diem dau tien
                int start = path.get(0);
                ListEcr[start] += Es+ TranferEnergy(Distance[start][path.get(1)]);
                
                //Tinh diem trung gian
                for (int j = 1; j < path.size()-1; j++) {
                    ListEcr[path.get(j)] += Er + TranferEnergy(Distance[path.get(j)][path.get(j+1)]);
                }
                
                //Tinh diem cuoi
                int end = path.get(path.size()-1);
                float minDistane = 1000000000.0f;
                for(int j =0;j<listSink.size();j++) {
                    if (Distance[end][N+T+listSink.get(j)] < minDistane) {
                        minDistane = Distance[end][N+T+listSink.get(j)];
                    }
                }
                ListEcr[end] += Er + TranferEnergy(minDistane);
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
    
    void FindListPath(int start, int end ,int back[],List<Integer> listEECCcnt, List<Integer> listPath) {
        
        if (start == end) {
            listPath.add(listEECCcnt.get(end));
            System.out.print(end+ "-");
        } else {
            FindListPath(start, back[end], back,listEECCcnt,listPath);
            listPath.add(listEECCcnt.get(end));
            System.out.print(end+ "-");
        }
    }
    // Check ham xem 
    public boolean CheckConectivity(List<Integer> listStart, boolean IsCover[], List<Integer> listEECCcnt, List<Integer> listEnd) {
        //Create matrix Distance from ListEECCcnt
       int N = listEECCcnt.size();
       int Matrix[][] = new int[N][N];
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
        
        
        //Using DijtraAlgorithm
        for (int i= 0;i < listStart.size();i++) {
            //Check TH start point is not cover
            if (!IsCover[i]) {
                for (int j = 0; j < listEnd.size();j++) {
                    int posEnd = findPostion(listEECCcnt, listEnd.get(j));
                    int back[] = new int[N]; //luu dinh cha
                    int weight[] = new int[N];//luu trong so
                    int result = DijkstraAlgorithm(i,posEnd,Matrix,N,back,weight);
                    if (result != -1) { // Ton tai duong di
                        IsCover[i] = true;
                        break;
                    }
                }
                
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
        if (connect != -1 && weight[end] <= MaxHopper-1) {
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
     public void Find_Current_Hoppers(List<Integer> listSensor,List<Integer> listSink, List<Integer> listNCR, List<Integer> listCurrenHopper) {
         listNCR.clear();
         listCurrenHopper.clear();
         for (int i =0; i< listSensor.size();i++) {
             int count =0;
             for (int j = 0;j < listSink.size();j++) {
                 if (Distance[listSensor.get(i)][N+T+listSink.get(j)] <= Rc) {
                     listCurrenHopper.add(listSensor.get(i));
                     break;
                 } else {
                     count++;
                 }
             }
             if (count == listSink.size()) listNCR.add(listSensor.get(i));
         }
         
     }
    
     // Calcualate Connect Heuristic 
     public void Calculate_Connect_Heuristic(List<Integer> listSensor, List<Integer> listSink, List<Integer> listCurrentHopper, List<Integer> listNCS ,List<HeuristicItem> listConnectHeuristic) {
         List<Integer> listTotalCoverNCS = new ArrayList<>();
         listTotalCoverNCS.clear();
         for (int i = 0;i<listNCS.size();i++) {
             int result = Total_CoverNCS(listSensor,listNCS.get(i));
             listTotalCoverNCS.add(result);
         }
         
         //Calculate Connect Heuristic
         listConnectHeuristic.clear();
         for (int i = 0; i < listCurrentHopper.size(); i++){
             HeuristicItem heuristicItem = new HeuristicItem();
             heuristicItem.setId(listCurrentHopper.get(i));
             float kq = 0;
             for(int j = 0; j < listNCS.size();j++) {
                 if (Distance[listCurrentHopper.get(i)][listNCS.get(j)] <= Rc) {
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
        //Coppy to display

    }
    
    public static void main(String[] args) {
        mListSensorNodes.add(new NodeItem(10, 9, 2));
        mListSensorNodes.add(new NodeItem(8, 7, 2));
        mListSensorNodes.add(new NodeItem(3, 4, 2));
        mListSensorNodes.add(new NodeItem(5, 6, 2));
        mListSensorNodes.add(new NodeItem(4, 5, 2));
        mListSensorNodes.add(new NodeItem(1, 2, 2));
        
 
        mListTargetNodes.add(new NodeItem(7, 6, 0));
        mListTargetNodes.add(new NodeItem(1, 1, 0));
        mListTargetNodes.add(new NodeItem(2, 3, 0));
        
        mListSinkNodes.add(new NodeItem(6, 8, 3));
        mListSinkNodes.add(new NodeItem(4, 3, 3));
        

        Algorithm3 m = new Algorithm3();
        m.run();
//        List<Integer> listSensor = new ArrayList<>();
//        listSensor.add(0);
//        listSensor.add(1);
//        listSensor.add(2);
//        listSensor.add(3);
//        listSensor.add(4);
//        List<Integer> listTarget = new ArrayList<>();
//        listTarget.add(0);
//        listTarget.add(1);
//        listTarget.add(2);
        //m.Calculate_Cov_Heuristic(listTarget, listSensor,Cov_Heuristic);
        int a =3;

    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import common.SensorUtility;
import static common.SensorUtility.mListRobotNodes;
import static common.SensorUtility.mListSensorNodes;
import static common.SensorUtility.mListTargetNodes;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import model.EdgeItem;
import model.NodeItem;
import model.SetItem;

/**
 *
 * @author Hieu
 */
public class Algorithm1 {
    public float Distance[][];// matrix distance between two nodes
    public float Target[][];// Target nodes
    public float P[][];// Total nodes
    public float S[][];// Target covering sensors
    public float D[][][];//Robots nodes
    
    //
    int K;// Number cycle Robot
    int N;//Number of target corvering Sensor Nodes
    int TP; // Total points
    int T;//Number of Tagert Nodes
    int M[]; // Number Point of every Cycle Robot;
    List<Point> ListSPoint;
    
    float Rs, Rt;// Rs and Rt value
    
    SetItem SetS, SetD[];
    
    
    public void run() {


        //
        init();
        readData();
        //Step 1: Find target-covering Sensor
        FindTargetCoveringSensor();

        //Step 2: 
        createMatrixDistance();

        runAlgorithm();

        CoppyToListSensor();
    }

    public void init() {
        int MaxRobotInCycle = 1;
        for (int i = 0;i < mListRobotNodes.size(); i++ ){
            if (mListRobotNodes.get(i).size() > MaxRobotInCycle) {
                MaxRobotInCycle = mListRobotNodes.get(i).size();
            }
        }
        
        ListSPoint = new ArrayList<Point>();
        T = SensorUtility.mListTargetNodes.size();
        K = SensorUtility.mListRobotNodes.size();
        Distance = new float[T + K * MaxRobotInCycle + 1][T + K * MaxRobotInCycle + 1];
        Target = new float[T][2];
        P = new float[T + K * MaxRobotInCycle + 1][2];
        D = new float[K][MaxRobotInCycle][2];
        M = new int[K];
        SetD = new SetItem[K];
        SetS = new SetItem();
    }

    public void readData() {
        // Read Target
        for (int i = 0; i < T; i++) {
            NodeItem next = mListTargetNodes.get(i);
            Target[i][0] = next.getX();
            Target[i][1] = next.getY();
        }
        // Read K cycle of Robots
        for (int i = 0; i < K; i++) {
            List<NodeItem> listRobot = mListRobotNodes.get(i);
            M[i] = listRobot.size();
            for (int j = 0; j < M[i]; j++) {
                D[i][j][0] = listRobot.get(j).getX();
                D[i][j][1] = listRobot.get(j).getY();
            }
        }
        // Read Rs, Rt
        Rs = SensorUtility.mRsValue;
        Rt = SensorUtility.mRtValue;
    }

    public void FindTargetCoveringSensor() {
        // Example :
        N = T; // Number corvering sensor == Number Target
        S = new float[N][2];
        for (int i = 0; i < N; i++) {
            S[i][0] = Target[i][0];
            S[i][1] = Target[i][1];
        }

        // Code algorithm in here
        //
        //----Move total nodse to P---//
        int pi = 0;
        //Add List S to List P
        for (int i = 0; i < N; i++) {
            P[i][0] = S[i][0];
            P[i][1] = S[i][1];
            pi++;
        }
        SetS.setId(-1);
        SetS.setStart(0);
        SetS.setEnd(N - 1);

        // Add List D to List P;
        for (int i = 0; i < K; i++) {
            SetD[i] = new SetItem(i, pi, pi + M[i] - 1);
            for (int j = 0; j < M[i]; j++) {
                P[pi][0] = D[i][j][0];
                P[pi][1] = D[i][j][1];
                pi++;
            }
        }

    }

    public float calculateDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    public void createMatrixDistance() {
        //Calculate Total Nodes
        TP = N;
        for (int i = 0; i < K; i++) {
            TP += M[i];
        }
        //Calcuate Distance
        for (int i = 0; i < TP; i++) {
            for (int j = 0; j <= i; j++) {
                if (i == j) {
                    Distance[i][j] = 0;
                } else {
                    Distance[i][j] = Distance[j][i] = calculateDistance(P[i][0], P[i][1], P[j][0], P[j][1]);
                }
            }
        }
    }
    //Minimum distance between two Set (D(i) and S
    public int findMinConnectFromDToSetS(SetItem setD) {
        int startD = setD.getStart();
        int endD = setD.getEnd();
        int point = -1;
        float distance =  SensorUtility.MAX_DISTANCE;
        for (int i =0;i <N;i++) {
            for (int j =startD;j<= endD;j++) {
                if (distance > Distance[i][j]) {
                    distance = Distance[i][j];
                    point = j;
                }
            }
        }
        return point;
    }
    
// Create matrix N+1 of S and representative of the setD(i)
    float Matrix[][];
    void createMatrix(int point) {
        Matrix = new float[N+1][N+1];
        for (int i = 0;i<N;i++) {
            for (int j =0; j<=i;j++) {
                Matrix[i][j] = Matrix[j][i] = Distance[i][j];
            }
        }
        for (int i = 0;i <N;i++) {
            Matrix[i][N] = Matrix[N][i] = Distance[i][point];
        }
        Matrix[N][N] = 0;
    }
    
    ///-----------KRUKAL Algorithm---------//
    List<EdgeItem>  nEdgeList; // List Edge of N+1 Matrix 
    List<List<EdgeItem>>  ListEdgeResult;// List Egde is egde result after using Krukal Algorithm with N+1 matrix 
    List<EdgeItem>  ListTotalResult;// List egde of algorithm
    int nEdge , V;
    int parent[];
    
    void initData() {
        nEdgeList = new ArrayList<>();
        ListEdgeResult = new ArrayList<List<EdgeItem>>();
        ListTotalResult = new ArrayList<>();
        parent = new int[N+2];
    }
    
     void createListEdges() {
         nEdgeList.clear();
         for(int i = 0; i <=N;i++) {
             for (int j =0;j <i;j++) {
                 if (i != j) {
                     nEdgeList.add(new EdgeItem(i, j, Matrix[i][j]));
                 }
            }
        }
    }

    //Find parent node
    int Find_Set(int x) {
        while (parent[x] > -1) {
            x = parent[x];
        }
        return x;
    }

// Union two node
    void Union(int u, int v) {
        if (parent[u] > parent[v]) {
            parent[v] += parent[u];
            parent[u] = v;
        } else {
            parent[u] += parent[v];
            parent[v] = u;
        }
    }
    
    void Kruskal(int Di, int point) {
        V = N+1;
        nEdge = V*(V-1)/2;
        // Add edge to nEdgeList
        createListEdges();
        
        for (int i =0 ; i < V;i++) {
            parent[i] = -1;
        }
        
        //Sort edges in increasing order on basis of cost
        
        Collections.sort(nEdgeList, new Comparator<EdgeItem>() {
            @Override
            public int compare(EdgeItem o1, EdgeItem o2) {
                float distance1 = o1.getDistance();
                float distance2 = o2.getDistance();
                if (distance1 > distance2) return 1;
                if (distance1 == distance2) return 0;
                return -1;
            }
        });
        //
        List<EdgeItem> edgeResult = new ArrayList<>();
        for (int i = 0; i < nEdge; i++) {
            // u and v are vertices of edge i
            // find parent of u,v
            int u = Find_Set(nEdgeList.get(i).getStart());
            int v = Find_Set(nEdgeList.get(i).getEnd());

            // Check u and v have the same parent
            if (u != v) {
                EdgeItem edgeItem = new EdgeItem();
                if (nEdgeList.get(i).getStart() == N) {
                    edgeItem.setStart(point);
                } else {
                    edgeItem.setStart(nEdgeList.get(i).getStart());
                }

                if (nEdgeList.get(i).getEnd() == N) {
                    edgeItem.setEnd(point);
                } else {
                    edgeItem.setEnd(nEdgeList.get(i).getEnd());
                }

                edgeItem.setDistance(nEdgeList.get(i).getDistance());
                edgeResult.add(edgeItem);
                //Union v and v
                Union(u, v);
            }
            if (edgeResult.size() == (V-1)) break;
        }
        ListEdgeResult.add(edgeResult);
       
    }
    void printList(int Di) {
        List<EdgeItem> edgeResult = ListEdgeResult.get(Di);
	for (int i =0;i<N;i++) {
	    System.out.print(edgeResult.get(i).getStart()+"-"+edgeResult.get(i).getEnd()+" ");
	}
	System.out.println();
    }
    
    //
    boolean checkEdgeExit(int x, int y, int N1) {
        for (int i = 0; i < N1; i++) {
            if ((ListTotalResult.get(i).getStart() == x && ListTotalResult.get(i).getEnd()== y) || (ListTotalResult.get(i).getStart() == y && ListTotalResult.get(i).getEnd()== x)) {
                return true;
            }
        }
        return false;
    }

    void printLisEdgetResult() {
        for (Iterator<EdgeItem> iterator = ListTotalResult.iterator(); iterator.hasNext();) {
            EdgeItem next = iterator.next();
            System.out.print(next.getStart()+"-"+next.getEnd()+" ");
        }
        System.out.println();
    }
    public void runAlgorithm() {
        //Init List
        initData();
        // Minimun Spaning Tree for N+1 Points ( N points of S and a representative point of the D(i)
        for (int i = 0; i < K; i++) {
            int point = findMinConnectFromDToSetS(SetD[i]);
            createMatrix(point);
            //Call Krukal Algorithm
            Kruskal(i, point);
            //printList(i);
        }

        // Calculates the sum of the spanning trees
        float Total = 0;
        int k1 = 0;
        for (int i = 0; i < K; i++) {
            List<EdgeItem> edgeList = ListEdgeResult.get(i);
            for (int j = 0; j < N; j++) {
                if (!checkEdgeExit(edgeList.get(j).getStart(), edgeList.get(j).getEnd(), k1)) {
                    // Add Egde to List Total Result
                    EdgeItem edgeItem = new EdgeItem();
                    edgeItem.setStart(edgeList.get(j).getStart());
                    edgeItem.setEnd(edgeList.get(j).getEnd());
                    edgeItem.setDistance(edgeList.get(j).getDistance());
                    k1++;
                    Total += edgeList.get(j).getDistance();
                    ListTotalResult.add(edgeItem);
                }
            }
        }
        printLisEdgetResult();
        //Calculate Sensor
        CalculateSensor();
        System.out.println("Number Sensor = "+ListSPoint.size());
        printListSensor();
        
    }

    void CalculateSensor() {
        // Add target corvering Sensor in ListSPoint
        for (int i = 0; i < N; i++) {
            ListSPoint.add(new Point((int) S[i][0], (int) S[i][1]));
        }

        // Add point between a segment
        int numberEdge = ListTotalResult.size();
        for (int i = 0; i < numberEdge; i++) {
            EdgeItem edgeItem = ListTotalResult.get(i);
            addSensorInSegment(P[edgeItem.getStart()][0], P[edgeItem.getStart()][1], P[edgeItem.getEnd()][0], P[edgeItem.getEnd()][1]);
        }
    }

    // Calculate nuber of sensor in a segment
    void addSensorInSegment(float x1, float y1, float x2, float y2) {
        float distance = calculateDistance(x1, y1, x2, y2);
        if (distance <= 2 * Rt && distance > 0) {
            float x = (x1 + x2) / 2;
            float y = (y1 + y2) / 2;
            ListSPoint.add(new Point((int) x, (int) y));
        } else {
            int k = (int) (distance / Rt);
            float tempx = x2 - x1;
            float tempy = y2 - y1;
            float temp1 = x1 * x1 + y1 * y1 - (x2 * x2 + y2 * y2);
            float temp2 = x2 * y1 - x1 * y2;
            //He pt 2 an
            //  2tempx*X + 2tempy*Y + temp1 - (kRt)^2 + (distance - k*Rt)^2 = 0;
            //  (tempy/tempx)* X + temp2/tempx = Y;
            for (int i = 1; i <= k; i++) {
                if (tempx != 0) {
                    float temp3 = (distance - i * Rt) * (distance - i * Rt) - (i * Rt) * (i * Rt);
                    float x = -(2 * tempy * temp2 + tempx * temp1 + tempx * temp3) / (2 * tempx * tempx + 2 * tempy * tempy);
                    float y = tempy * x / tempx + temp2 / tempx;
                    ListSPoint.add(new Point((int) x, (int) y));
                } else {
                    float temp3 = (distance - i * Rt) * (distance - i * Rt) - (i * Rt) * (i * Rt);
                    float x = x1;
                    float y = -(temp1 + temp3) / (2 * tempy);
                    ListSPoint.add(new Point((int) x, (int) y));
                }
            }

        }
    }
    public void printListSensor() {
        for (Iterator<Point> iterator = ListSPoint.iterator(); iterator.hasNext();) {
            Point next = iterator.next();
            System.out.println("("+next.x+","+next.y+") ");
        }
    }
    public void CoppyToListSensor() {
        mListSensorNodes.clear();
        for (Iterator<Point> iterator = ListSPoint.iterator(); iterator.hasNext();) {
            Point next = iterator.next();
            mListSensorNodes.add(new NodeItem(next.x, next.y, 2));
        }
    }
    
        public static void main(String[] args) {
                //Add temp
        mListTargetNodes.add(new NodeItem(1, 2, 0));
        mListTargetNodes.add(new NodeItem(5, 6, 0));
        mListTargetNodes.add(new NodeItem(11, 24, 0));
        List<NodeItem> node1 = new ArrayList<>();
        node1.add(new NodeItem(1, 3, 1, 0));
        node1.add(new NodeItem(12, 3, 1, 0));

        List<NodeItem> node2 = new ArrayList<>();
        node2.add(new NodeItem(1, 30, 1, 1));
        node2.add(new NodeItem(14, 31, 1, 1));

        List<NodeItem> node3 = new ArrayList<>();
        node3.add(new NodeItem(15, 32, 1, 2));
        node3.add(new NodeItem(12, 37, 1, 2));

        mListRobotNodes.add(node1);
        mListRobotNodes.add(node2);
        mListRobotNodes.add(node3);
        
        Algorithm1 m = new Algorithm1();
        m.run();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//               this
//            }
//        });
//        thread.start();
    }
}

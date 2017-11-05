/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import common.SensorUtility;
import static common.SensorUtility.castInt;
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
import model.FloatPointItem;
import model.HeapNodeItem;
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

//      PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
//      System.setOut(out);
        //
        init();
        readData();
        //Step 1: Find target-covering Sensor
        FindTargetCoveringSensor();

        //Step 2: 
        createMatrixDistance();

        runAlgorithm();

        CoppyToListSensor();
        
        freeData();
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
        // Code algorithm in here
        initDataGroup();
        
        for (int i = 0; i < T; i++) {
            if (!targetVisited[i]) {
                divideGroup(i, Rs);

                //Add vao tong list
                mListGroupTemp.add(mListElement);
            }
        }
        
        //Sort edges in increasing order on basis of cost
        Collections.sort(mListGroupTemp, new Comparator<List<Integer>>(){
            @Override
               public int compare(List<Integer> o1, List<Integer> o2) {
                   int size1 = o1.size();
                   int size2 = o2.size();
                   
                   return Integer.compare(size2, size1);
               }
            
        });
        
        CalculateCoveringSensor(mListGroupTemp);
 
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
    
      //Part 1 Algorithm
    boolean targetVisited[];
    List<List<Integer>> mListGroup = new ArrayList<List<Integer>>();
    List<List<Integer>> mListGroupTemp;
    void initDataGroup() {
        targetVisited = new boolean[T];
        mListGroupTemp = new ArrayList<>();
        for (int i= 0;i<T;i++) {
            targetVisited[i] = false;
        }
    }
    
    int status[];
    float Matrix[][];
    ArrayList<Integer> mListElement;
    ArrayList<Integer> mListTempElement;
    private void divideGroup(int startPoint, float Rs) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(startPoint);
        for(int i =0 ;i<T;i++) {
            if (!targetVisited[i] && i != startPoint && calculateDistance(Target[i][0], Target[i][1], Target[startPoint][0], Target[startPoint][1])<= 2*Rs) {
                list.add(i);
            }
        }
        
        mListElement = new ArrayList<>();
        //Check cac case
        if(list.size() > 2) {
            // crete matrix khoan cach
            status = new int[list.size()];
            Matrix = new float[list.size()][list.size()];
            for (int i =1;i<list.size();i++) {
                status[i] = 0;
                for(int j = 1;j<=i;j++) {
                    Matrix[i][j] = Matrix[j][i] = calculateDistance(Target[list.get(i)][0], Target[list.get(i)][1], Target[list.get(j)][0], Target[list.get(j)][1]);
                }
            }
            
            mListTempElement = new ArrayList<>();
            mListTempElement.clear();
            RecursiveMaxElement(1,list.size(),Rs);
            
            //Add element
            mListElement.add(startPoint);
            targetVisited[startPoint] = true;
            for (Iterator<Integer> iterator = mListTempElement.iterator(); iterator.hasNext();) {
                Integer next = iterator.next();
                mListElement.add(list.get(next)); 
                targetVisited[list.get(next)] = true;
            }
            
        } else {
            for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
                Integer next = iterator.next();
                mListElement.add(next); 
                targetVisited[next] = true;
            }
        }  
    }
    
    void RecursiveMaxElement(int position, int N1, float Rs) {
        boolean finish = true;
        for (int i = position; i < N1; i++) {
            if (status[i] == 0) {
                finish = false;
                status[i] = 1;
                ArrayList<Integer> templist = new ArrayList<>();
                for (int j = i + 1; j < N1; j++) {
                    if (status[j] != 2 && Matrix[i][j] > 2 * Rs) {
                        status[j] = 2;
                        templist.add(j);
                    }
                }

                //De quy
                RecursiveMaxElement(i + 1, N1, Rs);
                status[i] = 2;
                for (int k = 0; k < templist.size(); k++) {
                    status[templist.get(k)] = 0;
                }
                //templist = null;

                RecursiveMaxElement(i + 1, N1, Rs);
                status[i] = 0;
            }
        }
        // Luu ket qua
        if (finish && checkList(N1) > mListTempElement.size()) {
            mListTempElement.clear();
            for (int i = 1; i < N1; i++) {
                if (status[i] == 1) {
                    mListTempElement.add(i);
                }
            }

        }

    }

    int checkList(int N1) {
        int result = 0;
        for (int i =1;i<N1;i++) {
            if (status[i] == 1) {
                result++;
            }
        }
        return result;
    }
    
    void CalculateCoveringSensor(List<List<Integer>> listGroup) {
        int sizegroup = listGroup.size();
        List<FloatPointItem> listPoint = new ArrayList<FloatPointItem>();
        listPoint.clear();
        //Tinh vitri listsize > 2
        int position =0;
        for (Iterator<List<Integer>> iterator = listGroup.iterator(); iterator.hasNext();) {
            List<Integer> next = iterator.next();
            if (next.size() >= 2) {
                position++;
            } else {
                break;
            }
        }
        //TH next size  >=2
        for(int i =0;i<position;i++) {
            List<Integer> next = listGroup.get(i);
            if (next.size() == 2) {
                float x = (Target[next.get(0)][0] + Target[next.get(1)][0])/2 ;
                float y = (Target[next.get(0)][1] + Target[next.get(1)][1])/2 ;
                listPoint.add(new FloatPointItem(x, y));
            } else if (next.size() > 2) {
                boolean found = false;
                FloatPointItem point1 = new FloatPointItem();
                for(int j = 0;j < next.size();j++) {
                    for (int k =j+1;k< next.size();k++) {
                        if (k != j) {
                            //Giai phuong trinh
                            //2(x1-x0)*X + 2(y1-y0)*Y = x1^2 -x0^2 +y1^2-y0^2
                            //(X-x0)^2+ (Y-y0)^2 = R^2
                            float x0 = Target[next.get(j)][0];
                            float y0 = Target[next.get(j)][1];
                            float x1 = Target[next.get(k)][0];
                            float y1 = Target[next.get(k)][1];
                            if (x0 == x1) {
                                float ny = (x1*x1-x0*x0)/(2*y1-2*y0) +(y1+y0)/2;
                                
                                float c = x0*x0 + (ny-y0)*(ny-y0) - Rs*Rs;
                                float Delta = 4*x0*x0 - 4*c;
                                
                                // Giai phuong trinh
                                // Nghiem 1
                                float nx1 = (2*x0 + (float)Math.sqrt(Delta))/2;
                                point1.setX(nx1);
                                point1.setY(ny);
                                if (checkCoveringPoint(point1,next)) {
                                    listPoint.add(point1);
                                    found = true; 
                                    break;
                                }
                                
                                // Nghiem 2
                                
                                float nx2 = (2*x0 - (float)Math.sqrt(Delta))/2;
                                point1.setX(nx2);
                                point1.setY(ny);
                                if (checkCoveringPoint(point1,next)) {
                                    listPoint.add(point1);
                                    found = true; 
                                    break;
                                }
                                
                            } else {
                                float a = (x0+x1)/2 + (y1*y1-y0*y0)/(2*x1-2*x0);
                                float b = (y0-y1)/(x1-x0);
                                
                                float a1 = b*b+1;
                                float a2 = 2*a*b - 2*x0*b-2*y0;
                                float a3 = a*a - 2*x0*a + x0*x0 + y0*y0 - Rs*Rs;
                                
                                float Delta = a2*a2 - 4*a1*a3;

                                // Giai phuong trinh
                                // Nghiem 1
                                float ny1 = (-a2 + (float)Math.sqrt(Delta))/(2*a1);
                                float nx1 = a + b*ny1;
                                point1.setX(nx1);
                                point1.setY(ny1);
                                if (checkCoveringPoint(point1,next)) {
                                    listPoint.add(point1);
                                    found = true; 
                                    break;
                                }
                                
                                // Nghiem 2
                                float ny2 = (-a2 - (float)Math.sqrt(Delta))/(2*a1);
                                float nx2 = a + b*ny2;
                                point1.setX(nx2);
                                point1.setY(ny2);
                                if (checkCoveringPoint(point1,next)) {
                                    listPoint.add(point1);
                                    found = true; 
                                    break;
                                }
                                
                            }
                            
                            
                        }
                        if (found) break;
                    }
                    if (found) break;
                }
            }
        }
        
        //TH next size  == 1
        if (position == 0) {
            listPoint.add(new FloatPointItem(Target[listGroup.get(0).get(0)][0], Target[listGroup.get(0).get(0)][1]));
            position++;
        }
        int numberSensorFind ;
        if (sizegroup>position) {
            numberSensorFind = (sizegroup-position)*100;
        } else numberSensorFind = 1;
        
        HeapFindSensor = new HeapNodeItem[numberSensorFind+1];
        sizeTagetHeap = 0;
        float mindistance;
        float distance;
        boolean checked[]= new boolean[T];
        float TagetMinDistance[] = new float[T];
        for (int i =0; i<T;i++ ) {
            checked[i] = false;
            TagetMinDistance[i] = SensorUtility.MAX_DISTANCE;
        }
        
       //Put vao lan dau
        for (int i = position;i<sizegroup;i++) {
            //Tinh khoan cach nho nhat
            mindistance = SensorUtility.MAX_DISTANCE;
            List<Integer> next = listGroup.get(i);
            HeapNodeItem node = new HeapNodeItem();
            node.setIdSet(0);
            node.setStNode(next.get(0));
            for(int j=0; j<listPoint.size();j++) {
                distance = calculateDistance(listPoint.get(j).getX(), listPoint.get(j).getY(), Target[next.get(0)][0], Target[next.get(0)][1]);
                if ( distance < mindistance) {
                    mindistance = distance;
                    node.setDesNode(j);
                    node.setDistance(mindistance);
                }
            }
            pushTarget(node);
            TagetMinDistance[node.getStNode()] = node.getDistance();
            
        }
        

        //pop phan tu ra
        for (int i = 0; i < sizegroup - position; i++) {
            HeapNodeItem headnode = popTarget();
            while (checked[headnode.getStNode()]) {
                headnode = popTarget();
            }
            checked[headnode.getStNode()] = true;
            FloatPointItem point = findMinPointConnecFromTagertToS(Target[headnode.getStNode()][0], Target[headnode.getStNode()][1], listPoint.get(headnode.getDesNode()).getX(), listPoint.get(headnode.getDesNode()).getY(), headnode.getDistance());
            listPoint.add(point);

            // Update head node thieu
            for (int j = position; j < sizegroup; j++) {
                List<Integer> next = listGroup.get(j);
                if (!checked[next.get(0)]) {
                    float dis = calculateDistance(Target[next.get(0)][0], Target[next.get(0)][1], point.getX(), point.getY());
                    if (dis < TagetMinDistance[next.get(0)]) {
                        HeapNodeItem node1 = new HeapNodeItem(next.get(0), listPoint.size()-1, dis);
                        pushTarget(node1);
                        //Update Distance
                        TagetMinDistance[next.get(0)] = dis;
                    }
                }
            }
        }

        /// Coppy to S
        
        N = listPoint.size(); // Number corvering sensor == Number Target
        S = new float[N][2];
        for (int i =0;i <N;i++) {
            S[i][0] = listPoint.get(i).getX();
            S[i][1] = listPoint.get(i).getY();
        }
        //int s= 1;
        // Test code 
//        for(int j =0; j <N; j++) {
//            ListSPoint.add(new Point(castInt(S[j][0]),castInt(S[j][1])));
//        }
        
    }
    
    boolean checkCoveringPoint(FloatPointItem point, List<Integer> list) {
        for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();
            if (calculateDistance(point.getX(), point.getY(), Target[next][0], Target[next][1]) > Rs) {
                return false;
            }
        }
        return true;
    }
    
    FloatPointItem findMinPointConnecFromTagertToS(float targetX, float targetY, float minSx, float minSy, float distance) {
        FloatPointItem point = new FloatPointItem();

        if (distance <= Rs && distance > 0) {
            float x = (targetX + minSx) / 2;
            float y = (targetY + minSy) / 2;
            point.setX(x);
            point.setY(y);
        } else if (distance > Rs) {
            float tempx = minSx - targetX;
            float tempy = minSy - targetY;
            float temp1 = targetX * targetX + targetY * targetY - (minSx * minSx + minSy * minSy);
            float temp2 = minSx * targetY - targetX * minSy;
            //He pt 2 an
            //  2tempx*X + 2tempy*Y + temp1 - (kRt)^2 + (distance - k*Rt)^2 = 0;
            //  (tempy/tempx)* X + temp2/tempx = Y;

            if (tempx != 0) {
                float temp3 = (distance - Rs) * (distance - Rs) - Rs * Rs;
                float x = -(2 * tempy * temp2 + tempx * temp1 + tempx * temp3) / (2 * tempx * tempx + 2 * tempy * tempy);
                float y = tempy * x / tempx + temp2 / tempx;
                point.setX(x);
                point.setY(y);
            } else {
                float temp3 = (distance - Rs) * (distance - Rs) - Rs * Rs;
                float x = targetX;
                float y = -(temp1 + temp3) / (2 * tempy);
                point.setX(x);
                point.setY(y);
            }

        }

        return point;
    }
    
    HeapNodeItem HeapFindSensor[];
    int sizeTagetHeap;
    void swapTagetNode(int x, int y) {
        int temp ;
	float temp2;
	// swap id Set
	temp = HeapFindSensor[x].getIdSet();
	HeapFindSensor[x].setIdSet(HeapFindSensor[y].getIdSet());
	HeapFindSensor[y].setIdSet(temp);
	// swap start Node
	temp = HeapFindSensor[x].getStNode();
	HeapFindSensor[x].setStNode(HeapFindSensor[y].getStNode());
	HeapFindSensor[y].setStNode(temp);
	// swap destination Node
	temp = HeapFindSensor[x].getDesNode();
	HeapFindSensor[x].setDesNode(HeapFindSensor[y].getDesNode());
	HeapFindSensor[y].setDesNode(temp);

	temp2 = HeapFindSensor[x].getDistance();
	HeapFindSensor[x].setDistance(HeapFindSensor[y].getDistance());
	HeapFindSensor[y].setDistance(temp2);
    }
    
    int findTargetMin(int x,int y) {
        if (HeapFindSensor[x].getDistance() < HeapFindSensor[y].getDistance()) return x;
        return y;
    }
    
    // Di chuyen xuong duoi
    void sinkTaget(int p) {
        int x = p * 2;
        int y = p * 2 + 1;
        int min;
        if (p * 2 + 1 <= sizeTagetHeap) {
            min = findTargetMin(x, y);
            if (HeapFindSensor[p].getDistance() > HeapFindSensor[min].getDistance()) {
                swapTagetNode(min, p);
                sinkTaget(min);
            }
        } else if (p * 2 == sizeTagetHeap) {
            min = x;
            if (HeapFindSensor[p].getDistance() > HeapFindSensor[min].getDistance()) {
                swapTagetNode(min, p);
            }

        }

    }
    //Di chuyen len
    void swimTarget(int p) {
        if (p > 1) {
            if (HeapFindSensor[p].getDistance() < HeapFindSensor[p/2].getDistance()) {
                swapTagetNode(p, p/2);
                swimTarget(p/2);
            }
        }
    }
     void pushTarget(HeapNodeItem node) {
         if (sizeTagetHeap == 0) {
             sizeTagetHeap++;
             HeapFindSensor[sizeTagetHeap] = node;
         } else {
             sizeTagetHeap++;
             HeapFindSensor[sizeTagetHeap] = node;
             swimTarget(sizeTagetHeap);
             
         }
     }
     
     HeapNodeItem popTarget() {
         HeapNodeItem node = null;
         if (sizeTagetHeap > 0) {
             node = new HeapNodeItem(HeapFindSensor[1].getIdSet(), HeapFindSensor[1].getStNode(), HeapFindSensor[1].getDesNode(), HeapFindSensor[1].getDistance());
             swapTagetNode(1, sizeTagetHeap);
             sizeTagetHeap--;
             sinkTaget(1);
         }
         return  node;
     }
    
    ///-------------------------------------////
    

    

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
                return  Float.compare(distance1, distance2);
//                if (distance1 > distance2) return 1;
//                if (distance1 == distance2) return 0;
//                return -1 ;
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
     ///-----------ENd KRUKAL Algorithm---------//
    
    
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
            for (int j = 0; j < edgeList.size(); j++) {
                if (!checkEdgeExit(edgeList.get(j).getStart(), edgeList.get(j).getEnd(), ListTotalResult.size()-1)) {
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
        System.out.println("Algorithm1 "+Total);
        System.out.println("Number Edge = "+ListTotalResult.size());
        printLisEdgetResult();
        //Calculate Sensor
        CalculateSensor();
        System.out.println("Number Sensor = "+ListSPoint.size());
        printListSensor();
        
    }

    void CalculateSensor() {
        // Add target corvering Sensor in ListSPoint
        for (int i = 0; i < N; i++) {
            ListSPoint.add(new Point(castInt(S[i][0]), castInt(S[i][1])));
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
        if (distance <= 2 * Rt && distance > Rt) {
            float x = (x1 + x2) / 2;
            float y = (y1 + y2) / 2;
            ListSPoint.add(new Point(castInt(x), castInt(y)));
        } else if (distance > 2*Rt){
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
                    ListSPoint.add(new Point(castInt(x), castInt(y)));
                } else {
                    float temp3 = (distance - i * Rt) * (distance - i * Rt) - (i * Rt) * (i * Rt);
                    float x = x1;
                    float y = -(temp1 + temp3) / (2 * tempy);
                    ListSPoint.add(new Point( castInt(x), castInt(y)));
                }
            }

        }
    }
    public void printListSensor() {
        for (Iterator<Point> iterator = ListSPoint.iterator(); iterator.hasNext();) {
            Point next = iterator.next();
            System.out.print("("+next.x+","+next.y+") ");
        }
        System.out.println();
    }
    public void CoppyToListSensor() {
        mListSensorNodes.clear();
        for (Iterator<Point> iterator = ListSPoint.iterator(); iterator.hasNext();) {
            Point next = iterator.next();
            mListSensorNodes.add(new NodeItem(next.x, next.y, 2));
        }
    }
    
    public void freeData() {
        Distance = null;
        Target = null;
        D = null;
        S = null;
        P = null;
        parent = null;
        Matrix = null;
        ListEdgeResult = null;
        ListTotalResult = null;
        nEdgeList = null;
        ListSPoint = null;
        mListElement = null;
        mListTempElement = null;
        mListGroupTemp = null;
        mListGroup = null;
        HeapFindSensor = null;
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

    }
}

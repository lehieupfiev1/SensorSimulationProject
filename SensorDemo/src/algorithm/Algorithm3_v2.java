package algorithm;

import common.SensorUtility;
import static common.SensorUtility.mListSensorNodes;
import static common.SensorUtility.mListSinkNodes;
import static common.SensorUtility.mListTargetNodes;
import static common.SensorUtility.mListofListSensor;
import static common.SensorUtility.mListofListTime;
import static iterface.frameMain.coordinatePanel;
import java.util.ArrayList;
import java.util.List;
import model.CoverageItem;
import model.EnergyItem;
import model.HeuristicItem;
import model.LoadBalanceItem;
import model.NodeItem;

/**
 *
 * @author sev_user
 */
public class Algorithm3_v2 {
    public float Distance[][];// Matrix distance between two nodes
    public float MinDistanceSink[];// Matrix distance between two nodes
    public float Target[][];// Target nodes
    public float Point[][];// Total nodes
    public float Sink[][];// Target covering sensors
    
    double mTimeLife;
    float Rs, Rc;// Rs and Rt value
    int MaxHopper;
    List<Double> listTime;
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

    public Algorithm3_v2() {
        
    }
     
    
    
    
    public void run() {

        init();

        readData();
      
        runAlgorithm();

        CoppyToListSensor();
        
        freeData();
    }
    
    public  void init() {

        listTime = new ArrayList<>();

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
        //Calculate ListIu and listSa
        
        List<CoverageItem> mListIu = CalculateListIu(listSensor,listTarget);
        
        List<Integer> mListSa = findListSa(listSensor, mListIu);
        showViewTest(mListSa);
        List<List<Integer>> mListDs = Coverage_Optimizing_RecursiveHeuristic(mListSa, listTarget, mListIu);
        
        //Only test
        for (int i= 0; i< mListDs.size();i++) {
            List<Integer> tempList = mListDs.get(i);
            showViewTest(tempList);
            int a =0;
            
        }
        List<Integer> mListSrsd = findListSrsd(mListDs, listSensor);
        
        //Vong lap lay tung Ci
        
        for (int i = 0 ; i < mListDs.size(); i++) {
            //Get list Ci
            List<Integer> mListCi = mListDs.get(i);
            List<List<Integer>> mListPath = CalculatePathConnection(mListCi, mListSrsd);
            
            
            
            
            
            
            
            
        }
        
    }
    
    public List<List<Integer>> CalculatePathConnection(List<Integer> mListCi,List<Integer> mListSrsd) {
        List<List<Integer>> ListPath = new ArrayList<>();
        List<EnergyItem> listEi = new ArrayList<>();
        List<Integer> listRetrict = new ArrayList<>();

        for (int i =0 ;i < mListCi.size(); i++) {
            List<Integer> path = new ArrayList<>();
            listRetrict.clear();
            int senSing = mListCi.get(i);
            path.add(senSing);
            //TH sensing near Sink
            if (MinDistanceSink[senSing] <= Rc) {
                ListPath.add(path);
                continue;
            }

            List<LoadBalanceItem> mListNeighborSensing = FindLoadBalanceSensing(senSing,mListSrsd);
            LoadBalanceItem item = getRanDomItem(mListNeighborSensing);
            
            if(item == null) return ListPath;
            int nextNode = item.getIdSr();
            float loadNextNode = 1;
            path.add(nextNode);
            //Add list node retrict
            for (int j =0 ; j < mListNeighborSensing.size(); j++) {
                LoadBalanceItem TempItem = mListNeighborSensing.get(j);
                listRetrict.add(TempItem.getIdSr());
            }
            
            //TH relay node near Sink
            if (MinDistanceSink[nextNode] <= Rc) {
                ListPath.add(path);
                continue;
            }
            int counHop = 2;
            while ((MaxHopper-counHop)>0){
                List<LoadBalanceItem> mListNeighborRelaying = FindLoadBalanceRelaying(nextNode,loadNextNode,mListSrsd, listRetrict);
                
                LoadBalanceItem item2 = getRanDomItem(mListNeighborRelaying);
                nextNode = item2.getIdSr();
                loadNextNode /= mListNeighborRelaying.size();
                path.add(nextNode);
                if (MinDistanceSink[nextNode] <= Rc) {
                    ListPath.add(path);
                    break;
                }
                counHop++;
            }
            
        }
        
        return ListPath;
    }
    
    LoadBalanceItem getRanDomItem(List<LoadBalanceItem> mListNeighbor) {
        if (mListNeighbor.isEmpty()) return null;
        double p = Math.random();
        double cumulativeProbability = 0.0;
        for (LoadBalanceItem item : mListNeighbor) {
            cumulativeProbability += item.getValue();
            if (p <= cumulativeProbability) {
                return item;
            }
        }
        return mListNeighbor.get(0);
    }
    
    List<HeuristicItem> CalculateWeighList(float loadRelayNode, int sizeNei, List<Integer> listSnei) {
        List<HeuristicItem> ListWeigh = new ArrayList<>();
        for (int i =0; i < listSnei.size(); i++ ) {
            HeuristicItem WeightItem = new HeuristicItem();
            WeightItem.setId(listSnei.get(i));
            double temp = ListEnergySensor[listSnei.get(i)]/((loadRelayNode/sizeNei) + 1);
            float result = (float) Math.pow(temp, 4);
            WeightItem.setValue(result);
            ListWeigh.add(WeightItem);
        }
        return ListWeigh;
    }
    
    public List<LoadBalanceItem> FindLoadBalanceRelaying(int relayNode,float loadRelayNode , List<Integer> mListSrsd, List<Integer> mListRetrict) {
        List<Integer> ListSnei = new ArrayList<>();
        List<LoadBalanceItem> listBalanceLoad = new ArrayList<>();
        //Find neghbour
        for (int i =0; i < ListSnei.size(); i++) {
            if (Distance[relayNode][mListSrsd.get(i)] <= Rc) {
                for (int j =0 ; j < mListRetrict.size(); j++) {
                    if (mListSrsd.get(i) != mListRetrict.get(j))
                       ListSnei.add(mListSrsd.get(i));
                }
            }
        }
        if (ListSnei.isEmpty()) return listBalanceLoad;
        int sizeNei = ListSnei.size();
        
        List<HeuristicItem> mListWeight = CalculateWeighList(loadRelayNode, sizeNei, ListSnei);
        
        //Tim gia tri Trung binh va min cua weight
        float minWeight = MAX_FLOAT;
        float AverageWeight;
        float TotalWeitht =0;
        for (int i = 0; i< mListWeight.size(); i++) {
            HeuristicItem item  = mListWeight.get(i);
            if (minWeight > item.getValue()) minWeight = item.getValue();
            TotalWeitht += item.getValue();
        }
        AverageWeight = TotalWeitht/(mListWeight.size());
        
        //Tinh total Dev weight
        float TotalDevWeight = 0;
        for (int i =0; i < mListWeight.size(); i++) {
            HeuristicItem item  = mListWeight.get(i);
            TotalDevWeight += (item.getValue() - AverageWeight - 2*minWeight);
        }
        // Tinh Pi
        for (int i =0; i < mListWeight.size(); i++) {
            HeuristicItem item  = mListWeight.get(i);
            LoadBalanceItem loadBalanceItem = new LoadBalanceItem(relayNode, item.getId());
            float values = (item.getValue() - AverageWeight - 2*minWeight) / TotalDevWeight;
            loadBalanceItem.setValue(values);
            listBalanceLoad.add(loadBalanceItem);
        }
        
        return listBalanceLoad;
    }
    
    public List<LoadBalanceItem> FindLoadBalanceSensing(int sensing, List<Integer> mListSrsd) {
        List<Integer> ListSnei = new ArrayList<>();
        List<LoadBalanceItem> listLoad = new ArrayList<>();
        //Find neghbour
        for (int i =0; i < ListSnei.size(); i++) {
            if (Distance[sensing][mListSrsd.get(i)] <= Rc) {
                ListSnei.add(mListSrsd.get(i));
            }
        }
        
        //FindTotal
        float Total = 0;
        for (int i = 0; i< ListSnei.size();i++) {
            int idSr = ListSnei.get(i);
            Total += ((ListEnergySensor[idSr]/Distance[sensing][idSr]) *(ListEnergySensor[idSr]/Distance[sensing][idSr]));
        }
        
        for (int i = 0; i< ListSnei.size();i++) {
            int idSr = ListSnei.get(i);
            float weigh = ((ListEnergySensor[idSr]/Distance[sensing][idSr]) *(ListEnergySensor[idSr]/Distance[sensing][idSr]));
            LoadBalanceItem loadBalanceItem = new LoadBalanceItem(sensing, idSr);
            loadBalanceItem.setValue(weigh/Total);
            listLoad.add(loadBalanceItem);
        }
        
        return listLoad;
    }
    

    public List<Integer> findCOR_Heuristic_Utilized_CovRecovery(List<Integer> listSrsd, List<CoverageItem> listIu , List<Integer> listPloss, List<Integer> listSensorLoss) {

        List<Integer> listSnei = new ArrayList<>();
        
        for (int i =0; i < listSrsd.size(); i++) {
            for (int j =0; j<listSensorLoss.size(); j++) {
                if (Distance[listSrsd.get(i)][listSensorLoss.get(j)] <= Rc) {
                    listSnei.add(listSrsd.get(i));
                    break;
                }
            }
        }
        
        CoverageItem prebst_Iu = new CoverageItem();
        List<Integer> listW = new ArrayList<>();
        int prebst_node = -1;
        while (!CheckListCoverAllTarget(listW, listIu, listPloss)) {
            boolean enhance_flag = false;
            prebst_node = -1;
            for (int i = 0; i < listSnei.size(); i++) {
                int sensor = listSnei.get(i);
                int numCover = calculateCombineCover(listW, sensor, listIu, listPloss);
                int numPrebst = calculateCover(prebst_Iu.getListCoverage(), listIu, listPloss);
                //TH number coverTarget increase
                if (numCover > numPrebst) {

                    enhance_flag = true;

                    //Remove prebst_node
                    for (int j = 0; j < listW.size();) {
                        int node = listW.get(j);
                        if (node == prebst_node) {
                            listW.remove(j);
                        } else {
                            j++;
                        }
                    }

                    //
                    prebst_node = sensor;

                    //Gan listCi them node sensor
                    boolean exitSensor = false;
                    for (int j = 0; j < listW.size(); j++) {
                        int node = listW.get(j);
                        if (node == sensor) {
                            exitSensor = true;
                        }
                    }
                    if (!exitSensor) {
                        listW.add(sensor);
                    }

                    prebst_Iu.getListCoverage().clear();
                    for (int j = 0; j < listW.size(); j++) {
                        prebst_Iu.addIdCover(listW.get(j));
                    }

                }
            }

            if (enhance_flag) {
                //remove prebst_node
                for (int i = 0; i < listSnei.size();) {
                    int point = listSnei.get(i);
                    if (point == prebst_node) {
                        listSnei.remove(i);
                    } else {
                        i++;
                    }
                }

            } else {
                break;
            }

        }
        return listW;
    }
    
    public List<List<Integer>> Coverage_Optimizing_RecursiveHeuristic(List<Integer> listSa, List<Integer> listP, List<CoverageItem> listIu) {
        List<List<Integer>> listDs = new ArrayList<>();
        
        while (CheckListCoverAllTarget(listSa, listIu, listP)) {
            //Khoi tao cac bien prebst_Iu, prebst_node, listCi
            CoverageItem prebst_Iu = new CoverageItem();
            List<Integer> listCi = new ArrayList<>();
            int prebst_node = -1;
            
            //Kiem tra xem listCi co phu toan bo target
            while (!CheckListCoverAllTarget(listCi, listIu, listP)) {
                boolean enhance_flag = false;
                prebst_node = -1;
                for (int i =0; i< listSa.size(); i++) {
                    int sensor = listSa.get(i);
                    int numCover = calculateCombineCover(listCi,sensor,listIu,listP);
                    int numPrebst = calculateCover(prebst_Iu.getListCoverage(),listIu,listP);
                    //TH number coverTarget increase
                    if (numCover > numPrebst) {
                        
                        enhance_flag = true;
                        
                        //Remove prebst_node
                        for (int j =0; j< listCi.size(); ) {
                            int node = listCi.get(j);
                            if (node == prebst_node) {
                                listCi.remove(j);
                            } else {
                                j++;
                            }
                        }
                        
                        //
                        prebst_node = sensor;
                        
                        //Gan listCi them node sensor
                        boolean exitSensor = false;
                        for (int j =0; j< listCi.size();j++ ) {
                            int node = listCi.get(j);
                            if (node == sensor) {
                                exitSensor = true;
                            }
                        }
                        if (!exitSensor) listCi.add(sensor);
                        
                        prebst_Iu.getListCoverage().clear();
                        for (int j =0; j< listCi.size();j++) {
                            prebst_Iu.addIdCover(listCi.get(j));
                        }
                        
                    }
                }
                
                if (enhance_flag) {
                    //remove prebst_node
                    for (int i =0; i < listSa.size(); ) {
                        int point = listSa.get(i);
                        if (point == prebst_node) {
                            listSa.remove(i);
                        } else {
                            i++;
                        }
                    }
                    
                } else {
                    break;
                }
            }
            
            //Check ListCi == P
            if (CheckListCoverAllTarget(listCi, listIu, listP)) {
                //Add to list Ds
                listDs.add(listCi);
            } else {
                //Coppy list Ci to Sa
                for (int i =0; i < listCi.size(); i++) {
                    boolean check = false;
                    int sensor1 = listCi.get(i);
                    for (int j=0 ; j < listSa.size(); j++) {
                        if (sensor1 == listSa.get(j)) {
                            check = true;
                            break;
                        }
                    }
                    if (!check) listSa.add(sensor1);
                }
                
            }
            
        }
        return listDs;
    } 
    
    int calculateCombineCover(List<Integer> listCi,int sensor, List<CoverageItem> listIu,List<Integer> listP) {
        int result =0;
        boolean Check[] = new boolean[T];
        for (int i=0; i < T;i++) {
            Check[i] = false;
        }
        //Check ListCi
        for (int i =0; i< listCi.size(); i++) {
            int sensorId = listCi.get(i);
            CoverageItem coverageItem = listIu.get(sensorId);
            List<Integer> listId = coverageItem.getListCoverage();
            for (int j =0 ; j<listId.size();j++ ) {
                int id = listId.get(j);
                Check[id] = true;
            }
        }
        //Check Add sensor
        List<Integer> listIds = listIu.get(sensor).getListCoverage();
        for (int j =0 ; j<listIds.size();j++ ) {
                int id = listIds.get(j);
                Check[id] = true;
        }
        
        for (int i =0; i< listP.size(); i++) {
            int idtarget = listP.get(i);
            if (Check[idtarget]) result++;
        }
        return result;
        
    }
    
    int calculateCover(List<Integer> listCi, List<CoverageItem> listIu,List<Integer> listP) {
        int result =0;
        boolean Check[] = new boolean[T];
        for (int i=0; i < T;i++) {
            Check[i] = false;
        }
        //Check ListCi
        for (int i =0; i< listCi.size(); i++) {
            int sensorId = listCi.get(i);
            CoverageItem coverageItem = listIu.get(sensorId);
            List<Integer> listId = coverageItem.getListCoverage();
            for (int j =0 ; j<listId.size();j++ ) {
                int id = listId.get(j);
                Check[id] = true;
            }
        }
        
        for (int i =0; i< listP.size(); i++) {
            int idtarget = listP.get(i);
            if (Check[idtarget]) result++;
        }
        return result;
        
    }
    
    boolean CheckListCoverAllTarget(List<Integer> listSa,List<CoverageItem> listIu, List<Integer> listP) {
        boolean Check[] = new boolean[T];
        for (int i=0; i < T;i++) {
            Check[i] = false;
        }
        for (int i =0; i< listSa.size(); i++) {
            int sensorId = listSa.get(i);
            CoverageItem coverageItem = listIu.get(sensorId);
            List<Integer> listId = coverageItem.getListCoverage();
            for (int j =0 ; j<listId.size();j++ ) {
                int id = listId.get(j);
                Check[id] = true;
            }
        }
        
        for (int i =0; i< listP.size(); i++) {
            int idtarget = listP.get(i);
            if (!Check[idtarget]) return false;
        }
        return true;
    }
    
    
    public List<CoverageItem> CalculateListIu(List<Integer> listSensor, List<Integer> listTarget) {
        List<CoverageItem> listIu = new ArrayList<>();
        for (int i =0 ; i< listSensor.size(); i++) {
            CoverageItem coverageItem = new CoverageItem(i);
            for (int j =0; j < listTarget.size(); j++) {
                if (Distance[listSensor.get(i)][N+listTarget.get(j)] <= Rs) {
                    coverageItem.addIdCover(listTarget.get(j));
                }
            }
            listIu.add(coverageItem);
        }
        return listIu;
    }
    
    public List<Integer> findListSa(List<Integer> listSensor, List<CoverageItem> listIu) {
        List<Integer> listSa = new ArrayList<>();
        for (int i =0; i < listIu.size(); i++) {
            if (listIu.get(i).sizeCoverage() != 0) {
                listSa.add(listIu.get(i).getId());
            }
        }
        return listSa;
    }
    
    public List<Integer> findListSrsd (List<List<Integer>> listDs, List<Integer> listSensor) {
        List<Integer> listSrsd = new ArrayList<>();
        int N1 = listSensor.size();
        boolean Check[] = new boolean[N1];
        for (int i=0; i < N1 ;i++) {
            Check[i] = false;
        }
        
        for (int i =0; i< listDs.size(); i++) {
            List<Integer> listCi = listDs.get(i);
            for (int j =0; j < listCi.size(); j++) {
                int sensor = listCi.get(j);
                Check[sensor] = true;
            }
        }
        
        for (int i =0; i < listSensor.size(); i++) {
            int id = listSensor.get(i);
            if (!Check[id]) listSrsd.add(id);
        }
        
        return listSrsd;
    }
    
    public void CoppyToListSensor() {
//        mListofListSensor.clear();
//        for (int i =0;i<NDEECCcnt.size();i++ ) {
//            List<Integer> temp = NDEECCcnt.get(i);
//            List<NodeItem> tempNodeList = new ArrayList<>();
//            for (int j =0;j<temp.size();j++) {
//               tempNodeList.add(mListSensorNodes.get(temp.get(j)));
//            }
//            mListofListSensor.add(tempNodeList);
//        }
//        mListofListTime = listTime;
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
    
    public void freeData() {

        listTime = null;
        Point = null;
        Distance = null;
        ListEnergySensor = null;
    }

    
}
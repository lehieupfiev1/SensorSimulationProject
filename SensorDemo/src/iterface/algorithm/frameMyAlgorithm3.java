/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iterface.algorithm;

import algorithm.MyAlgorithm3;
import common.SensorUtility;
import static common.SensorUtility.*;
import static iterface.algorithm.frameAlgorithm3.ListSensor;
import iterface.frameCoordinateSystemPanel;
import static iterface.frameMain.coordinatePanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.NodeItem;
import model.PathItem;

/**
 *
 * @author sev_user
 */
public class frameMyAlgorithm3 extends javax.swing.JFrame {

    /**
     * Creates new form frameMyAlgorithm3
     */
    
    public DefaultListModel dataPathModel;
    public DefaultListModel dataListTargetModel;
    public ArrayList<Double> listTotalTime;
    public int mListTargetIndex = -1;
    public int mListPathIndex = -1;
    float timeRun;
    public static List<NodeItem> ListSensor = new ArrayList<>();
    public ListSelectionListener mListTargetSelectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
                if (!dataListTargetModel.isEmpty()) {
                    mListTargetIndex = mJListTarget.getSelectedIndex();
                    if (mListTargetIndex >= 0 && mListTargetIndex < mListofListPath.size()) {
                        updateListPath(mListTargetIndex);
                    }
                }
        }
    };
    
    public ListSelectionListener mListPathSelectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!dataPathModel.isEmpty()) {
                mListPathIndex = mJListPath.getSelectedIndex();
                mListTargetIndex = mJListTarget.getSelectedIndex();
                if (mListTargetIndex >= 0 && mListTargetIndex < mListofListPath.size()) {
                    
                    if (mListPathIndex >= 0 && mListPathIndex < mListofListPath.get(mListTargetIndex).size()) {
                        
                        //Do any thing

                    }
                }
            }
        }
    };
    
    
    public frameMyAlgorithm3() {
        initComponents();
        initOtherComponent();
        displayInput();
        clearData();
        this.setTitle("MyAlgorithm 3");
    }

    private void initOtherComponent() {
        //Init ListX ScrollPannel
        dataListTargetModel = new DefaultListModel();
        mJListTarget = new JList(dataListTargetModel);

       
        mJListTarget.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listEECCScrollPane.setViewportView(mJListTarget);
        mJListTarget.addListSelectionListener(mListTargetSelectionListener);
        
        
        //Init ListSensor ScrollPannel
        dataPathModel = new DefaultListModel();
        mJListPath = new JList(dataPathModel);
        dataPathModel.clear();
        mJListPath.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSensorScrollPane.setViewportView(mJListPath);
        mJListPath.addListSelectionListener(mListPathSelectionListener);
        
        listTotalTime = new ArrayList<>();
        
    }
    
    void displayInput() {
        NumberSensorLabel.setText("Number Sensor : "+mListSensorNodes.size());
        NumberTargetLabel.setText("Number Target : "+mListTargetNodes.size());
        NumberSinkLabel.setText("Number Sink : "+mListSinkNodes.size());
        RcTextfiled.setText(""+mRcValue);
        MaxHopperTextField.setText(""+mMaxHopper);
        EsValueTextField.setText(""+mEsValue);
        EtValueTextField.setText(""+mEtValue);
        EfsValueTextField.setText(""+mEfsValue);
        EmpValueTextField.setText(""+mEmpValue);
        BitValueTextField.setText(""+mBitValue);
        ErValueTextField.setText(""+mErValue);
        numberAlphaTextField.setText(""+Lvalue);
        float value = (float)(mEoValue/1000000000.0);
        EoValueTextField.setText(""+value);
        
        //Result
        TimeRunningLabel.setText("Time Running : 0");
        TimeLifeLabel.setText("Total time life : 0");
        ListEECCLabel.setText("List EECCcnt :0");
        ListSensorResultLabel.setText("List Sensor : 0");
     
    }
    
    void clearData(){
        TimeRunningLabel.setText("Time Running : 0");
        TimeLifeLabel.setText("Total time life : 0");
        ListEECCLabel.setText("List EECCcnt :0");
        ListSensorResultLabel.setText("List Sensor : 0");
        
        
        dataPathModel.clear();
        dataListTargetModel.clear();
     

    }
    
    void displayResult() {
        TimeRunningLabel.setText("Time Running : "+timeRun);
        
        double minimumTime = Double.MAX_VALUE;
        for (int i = 0; i < listTotalTime.size(); i++) {
            if (minimumTime > listTotalTime.get(i).doubleValue())
            minimumTime = listTotalTime.get(i).doubleValue();
        }
        TimeLifeLabel.setText("Minimize time life :  "+minimumTime);
    }

    public void updateListPath(int index) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                dataPathModel.clear();
                ListSensorResultLabel.setText("List Path : 0");
                if (index >= 0 && index < mListofListPath.size()) {
                    List<PathItem> ListPath = mListofListPath.get(index);
                    List<Double>  ListTime = mListofListPathTime.get(index);
                    for (int i = 0; i < ListPath.size(); i++) {
                        PathItem next = ListPath.get(i);
                        Double time = ListTime.get(i);
                        ((DefaultListModel) mJListPath.getModel()).addElement(i + ". " + next.getString()+ " "+ "(Time : "+time.doubleValue()+")");
                    }
                    ListSensorResultLabel.setText("List Path : " + ListPath.size());
                }
            }
        });
    }
    
    public void updateListTarget() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                dataListTargetModel.clear();
                
                for (int i = 0; i < mListofListPath.size(); i++) {
                    NodeItem targetNode = mListTargetNodes.get(i);
                    if (mListofListPath.get(i).size() > 0) {
                        ((DefaultListModel) mJListTarget.getModel()).addElement(i + ". id = " + i +"( "+targetNode.getX()+" , "+targetNode.getY()+" )"+"(NoPath: "+mListofListPath.get(i).size() +", TotalTime: " + listTotalTime.get(i) + ")");
                    } else {
                        ((DefaultListModel) mJListTarget.getModel()).addElement(i + ". id = "+i+"( "+targetNode.getX()+" , "+targetNode.getY()+" )" +"path=null");
                    }
                }
                ListEECCLabel.setText("List Target :"+ mListofListPath.size());
            }
        });

    }
    
    double calculateTotal(List<Double> list) {
        double result =0;
        for (int i =0; i< list.size();i++) {
            result += list.get(i);
        }
        return result;
    }
    
    void calculateTotalTime() {
        listTotalTime.clear();
        for (int i = 0; i < mListofListPathTime.size(); i++) {
            List<Double> next = mListofListPathTime.get(i);
            listTotalTime.add(calculateTotal(next));
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButton1 = new javax.swing.JRadioButton();
        CalculateButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        NumberSensorLabel = new javax.swing.JLabel();
        NumberTargetLabel = new javax.swing.JLabel();
        NumberSinkLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        EsValueTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        EtValueTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        EfsValueTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        EmpValueTextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        BitValueTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        ErValueTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        EoValueTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        RcTextfiled = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        MaxHopperTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        numberAlphaTextField = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        DoneButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        ShowButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        TimeRunningLabel = new javax.swing.JLabel();
        TimeLifeLabel = new javax.swing.JLabel();
        listEECCScrollPane = new javax.swing.JScrollPane();
        ListEECCLabel = new javax.swing.JLabel();
        listSensorScrollPane = new javax.swing.JScrollPane();
        ListSensorResultLabel = new javax.swing.JLabel();

        jRadioButton1.setText("jRadioButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        CalculateButton.setText("Calculate");
        CalculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalculateButtonActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        NumberSensorLabel.setText("Number Sensor : 0");

        NumberTargetLabel.setText("Number Target : 0");

        NumberSinkLabel.setText("Number Sink : 0");

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setText("Parameter of Energy");

        jLabel7.setText("Es :");

        EsValueTextField.setText("0");
        EsValueTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                EsValueTextFieldEsValueKeyReleased(evt);
            }
        });

        jLabel8.setText("Et : ");

        EtValueTextField.setText("0");
        EtValueTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                EtValueTextFieldEtValueKeyReleased(evt);
            }
        });

        jLabel9.setText("Efs :");

        EfsValueTextField.setText("0");
        EfsValueTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                EfsValueTextFieldEfsValueKeyReleased(evt);
            }
        });

        jLabel10.setText("Emp :");

        EmpValueTextField.setText("0");
        EmpValueTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                EmpValueTextFieldEmpValueKeyReleased(evt);
            }
        });

        jLabel11.setText("Bit :");

        BitValueTextField.setText("0");
        BitValueTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BitValueTextFieldBitValueKeyReleased(evt);
            }
        });

        jLabel12.setText("(bit/s)");

        jLabel19.setText("Er:");
        jLabel19.setToolTipText("");

        ErValueTextField.setText("1");
        ErValueTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ErValueTextFieldKeyReleased(evt);
            }
        });

        jLabel4.setText("Eo : ");

        EoValueTextField.setText("0");
        EoValueTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EoValueTextFieldActionPerformed(evt);
            }
        });
        EoValueTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                EoValueKeyReleased(evt);
            }
        });

        jLabel5.setText("(Jun)");

        jLabel15.setText("(nJ/bit)");

        jLabel16.setText("(nJ/bit)");

        jLabel17.setText("(nJ/bit)");

        jLabel18.setText("(nJ/bit/m4)");

        jLabel20.setText("(nJ/bit/m2)");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BitValueTextField))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EsValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EtValueTextField)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel19)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(EfsValueTextField)
                    .addComponent(EmpValueTextField)
                    .addComponent(ErValueTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel20)))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(131, 131, 131)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EoValueTextField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(149, 149, 149))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(EsValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(EfsValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel20))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(EtValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(EmpValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel18))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(BitValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel19)
                    .addComponent(ErValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(EoValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap())
        );

        jLabel13.setText("Rc : ");

        RcTextfiled.setText("0");
        RcTextfiled.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                RcTextfiledRcValueKeyReleased(evt);
            }
        });

        jLabel14.setText("Max Hopper");

        MaxHopperTextField.setText("0");
        MaxHopperTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                MaxHopperTextFieldMaxhopperKeyReleased(evt);
            }
        });

        jLabel3.setText("Alpha");

        numberAlphaTextField.setText("0");
        numberAlphaTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                numberAlphaKeyReleased(evt);
            }
        });

        jLabel21.setText("(m)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RcTextfiled, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel21))
                            .addComponent(NumberSensorLabel))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(6, 6, 6)
                                .addComponent(MaxHopperTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(numberAlphaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(NumberTargetLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(NumberSinkLabel)
                                .addGap(58, 58, 58)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NumberSensorLabel)
                    .addComponent(NumberTargetLabel)
                    .addComponent(NumberSinkLabel))
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(RcTextfiled, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(MaxHopperTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(numberAlphaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        DoneButton.setText("Done");
        DoneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DoneButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Input");

        ShowButton.setText("ShowPath");
        ShowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Output");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        TimeRunningLabel.setText("Time Running : 0");

        TimeLifeLabel.setText("Minimize time life : 0");

        ListEECCLabel.setText("List Target :0");

        ListSensorResultLabel.setText("List Path Y : 0");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(ListEECCLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(listEECCScrollPane)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(TimeRunningLabel)
                                .addGap(181, 181, 181)))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ListSensorResultLabel)
                    .addComponent(TimeLifeLabel)
                    .addComponent(listSensorScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(76, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TimeRunningLabel)
                    .addComponent(TimeLifeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ListEECCLabel)
                    .addComponent(ListSensorResultLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(listSensorScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                    .addComponent(listEECCScrollPane))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(CalculateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addComponent(ShowButton)
                .addGap(76, 76, 76)
                .addComponent(DoneButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CalculateButton)
                    .addComponent(DoneButton)
                    .addComponent(ShowButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CalculateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CalculateButtonActionPerformed
        // TODO add your handling code here:
        if (SensorUtility.mListSensorNodes.isEmpty() && mListTargetNodes.isEmpty() && mListSinkNodes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Insert sensorr nodes , target and sink");
        }  else {
            clearData();
            //Algorithm2 mAlgorithm = new Algorithm2();
            TimeRunningLabel.setText("Time Running : ...");
            TimeLifeLabel.setText("Total time life : ...");
            MyAlgorithm3 mAlgorithm = new MyAlgorithm3();
            Thread thread;
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long begin = System.currentTimeMillis();
                    mAlgorithm.run();
                    long end = System.currentTimeMillis();
                    timeRun = end-begin;
                    JOptionPane.showMessageDialog(null, "Run finished !");
                    calculateTotalTime();
                    updateListTarget();
                    displayResult();
                }
            });
            thread.start();
        }

    }//GEN-LAST:event_CalculateButtonActionPerformed

    private void EsValueTextFieldEsValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EsValueTextFieldEsValueKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mEsValue = Float.parseFloat(EsValueTextField.getText());
        } catch (NumberFormatException nfe) {
            EsValueTextField.setText("");
        }
    }//GEN-LAST:event_EsValueTextFieldEsValueKeyReleased

    private void EtValueTextFieldEtValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EtValueTextFieldEtValueKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mEtValue = Float.parseFloat(EtValueTextField.getText());
        } catch (NumberFormatException nfe) {
            EtValueTextField.setText("");
        }
    }//GEN-LAST:event_EtValueTextFieldEtValueKeyReleased

    private void EfsValueTextFieldEfsValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EfsValueTextFieldEfsValueKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mEfsValue = Float.parseFloat(EfsValueTextField.getText());
        } catch (NumberFormatException nfe) {
            EfsValueTextField.setText("");
        }
    }//GEN-LAST:event_EfsValueTextFieldEfsValueKeyReleased

    private void EmpValueTextFieldEmpValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EmpValueTextFieldEmpValueKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mEmpValue = Float.parseFloat(EmpValueTextField.getText());
        } catch (NumberFormatException nfe) {
            EmpValueTextField.setText("");
        }
    }//GEN-LAST:event_EmpValueTextFieldEmpValueKeyReleased

    private void BitValueTextFieldBitValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BitValueTextFieldBitValueKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mBitValue = Float.parseFloat(BitValueTextField.getText());
        } catch (NumberFormatException nfe) {
            BitValueTextField.setText("");
        }
    }//GEN-LAST:event_BitValueTextFieldBitValueKeyReleased

    private void ErValueTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ErValueTextFieldKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mErValue = Float.parseFloat(ErValueTextField.getText());
        } catch (NumberFormatException nfe) {
            ErValueTextField.setText("");
        }
    }//GEN-LAST:event_ErValueTextFieldKeyReleased

    private void RcTextfiledRcValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RcTextfiledRcValueKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mRcValue = Float.parseFloat(RcTextfiled.getText());
        } catch (NumberFormatException nfe) {
            RcTextfiled.setText("");
        }
    }//GEN-LAST:event_RcTextfiledRcValueKeyReleased

    private void MaxHopperTextFieldMaxhopperKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MaxHopperTextFieldMaxhopperKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mMaxHopper = Integer.parseInt(MaxHopperTextField.getText());
        } catch (NumberFormatException nfe) {
            MaxHopperTextField.setText("");
        }
    }//GEN-LAST:event_MaxHopperTextFieldMaxhopperKeyReleased

    private void DoneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DoneButtonActionPerformed
        // TODO add your handling code here:
        coordinatePanel.refresh();
        this.dispose();
    }//GEN-LAST:event_DoneButtonActionPerformed

    private void ShowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowButtonActionPerformed
        // TODO add your handling code here:
        //Clear data
        for (int j = 0; j < mListSensorNodes.size(); j++) {
            mListSensorNodes.get(j).setStatus(0);
        }
        if (mListTargetIndex >= 0 && mListTargetIndex < mListofListPath.size()) {

            if (mListPathIndex >= 0 && mListPathIndex < mListofListPath.get(mListTargetIndex).size()) {
                
                PathItem path = mListofListPath.get(mListTargetIndex).get(mListPathIndex);
                List<Integer> list = path.getPath();
//                for (int i = 0; i < list.size(); i++) {
//                    //Change Value On foreach Sensor
//                    mListSensorNodes.get(list.get(i)).setStatus(1);
//                   
//                }
                mPathSensor = path.getPath();
                
                
                int lastSensor = list.get(list.size()-1);
                NodeItem sensorNode = mListSensorNodes.get(lastSensor);
                float MinDistane = Float.MAX_VALUE;
                int minSink = -1;
                for (int i = 0 ; i< mListSinkNodes.size();i++) {
                    NodeItem sinkNode = mListSinkNodes.get(i);
                    float distance = SensorUtility.calculateDistance(sensorNode.getX(), sensorNode.getY(), sinkNode.getX(), sinkNode.getY());
                    if (distance < MinDistane) {
                        MinDistane = distance;
                        if (MinDistane <= SensorUtility.mRcValue) minSink = i;
                    }
                }
                
                if (minSink != -1) {
                    frameCoordinateSystemPanel.SinkSelected = minSink;
                    frameCoordinateSystemPanel.isShowPathSelected = true;
                    frameCoordinateSystemPanel.TargetSelected = mListTargetIndex;
                }

            }
        }
        coordinatePanel.refresh();
    }//GEN-LAST:event_ShowButtonActionPerformed
      
    
    private void numberAlphaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numberAlphaKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.Lvalue = Integer.parseInt(numberAlphaTextField.getText());
        } catch (NumberFormatException nfe) {
            numberAlphaTextField.setText("");
        }
    }//GEN-LAST:event_numberAlphaKeyReleased

    private void EoValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EoValueKeyReleased
        // TODO add your handling code here:
        try {
            float value = Float.parseFloat(EoValueTextField.getText());
            SensorUtility.mEoValue = value * 1000000000;
        } catch (NumberFormatException nfe) {
            EoValueTextField.setText("");
        }
    }//GEN-LAST:event_EoValueKeyReleased

    private void EoValueTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EoValueTextFieldActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_EoValueTextFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frameMyAlgorithm3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frameMyAlgorithm3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frameMyAlgorithm3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frameMyAlgorithm3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frameMyAlgorithm3().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BitValueTextField;
    private javax.swing.JButton CalculateButton;
    private javax.swing.JButton DoneButton;
    private javax.swing.JTextField EfsValueTextField;
    private javax.swing.JTextField EmpValueTextField;
    private javax.swing.JTextField EoValueTextField;
    private javax.swing.JTextField ErValueTextField;
    private javax.swing.JTextField EsValueTextField;
    private javax.swing.JTextField EtValueTextField;
    private javax.swing.JLabel ListEECCLabel;
    private javax.swing.JLabel ListSensorResultLabel;
    private javax.swing.JTextField MaxHopperTextField;
    private javax.swing.JLabel NumberSensorLabel;
    private javax.swing.JLabel NumberSinkLabel;
    private javax.swing.JLabel NumberTargetLabel;
    private javax.swing.JTextField RcTextfiled;
    private javax.swing.JButton ShowButton;
    private javax.swing.JLabel TimeLifeLabel;
    private javax.swing.JLabel TimeRunningLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane listEECCScrollPane;
    private javax.swing.JScrollPane listSensorScrollPane;
    private javax.swing.JTextField numberAlphaTextField;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JList mJListPath;
    private javax.swing.JList mJListTarget;
}

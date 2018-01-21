/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iterface.algorithm;

import algorithm.Algorithm3;
import algorithm.TempAlgorithm;
import common.SensorUtility;
import static common.SensorUtility.*;
import static common.SensorUtility.Lvalue;
import static common.SensorUtility.mListSensorNodes;
import static iterface.algorithm.frameAlgorithm2.ListSensor;
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

/**
 *
 * @author Hieu
 */
public class frameAlgorithm3 extends javax.swing.JFrame {

    /**
     * Creates new form frameAlgorithm3
     */
    public DefaultListModel dataSensorModel;
    public DefaultListModel dataListEECCModel;
    public int mListEECCIndex = -1;
    float timeRun;
    public static List<NodeItem> ListSensor = new ArrayList<>();
    public ListSelectionListener mListEECCSelectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
                if (!dataListEECCModel.isEmpty()) {
                    mListEECCIndex = mJListEECC.getSelectedIndex();
                    if (mListEECCIndex >= 0 && mListEECCIndex < mListofListSensor.size()) {
                        updateListSensor(mListEECCIndex);
                    }
                }
        }
    };
    public frameAlgorithm3() {
        initComponents();
        initOtherComponent();
        displayInput();
        clearData();
        this.setTitle("CompareAlgorithm 3");
    }

    
    private void initOtherComponent() {
        //Init ListX ScrollPannel
        dataListEECCModel = new DefaultListModel();
        mJListEECC = new JList(dataListEECCModel);

       
        mJListEECC.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listEECCScrollPane.setViewportView(mJListEECC);
        mJListEECC.addListSelectionListener(mListEECCSelectionListener);
        
        
        //Init ListSensor ScrollPannel
        dataSensorModel = new DefaultListModel();
        mJListSensor = new JList(dataSensorModel);
        dataSensorModel.clear();
        mJListSensor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSensorScrollPane.setViewportView(mJListSensor);
        
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
        
        
        dataSensorModel.clear();
        dataListEECCModel.clear();
     

    }
    
    void displayResult() {
        TimeRunningLabel.setText("Time Running : "+timeRun);
        
        double totalTime =0;
        for (int i = 0; i < mListofListSensor.size(); i++) {
            Double next = mListofListTime.get(i);
            totalTime+=next;
        }
        TimeLifeLabel.setText("Total time life : "+totalTime);
    }
    
    public void updateListSensor(int index) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                dataSensorModel.clear();
                ListSensorResultLabel.setText("List Sensor : 0");
                if (index >= 0 && index < mListofListSensor.size()) {
                    ListSensor = mListofListSensor.get(index);
                    for (int i = 0; i < ListSensor.size(); i++) {
                        NodeItem next = ListSensor.get(i);
                        ((DefaultListModel) mJListSensor.getModel()).addElement(i + ".( X = " + next.getX() + ", Y= " + next.getY() + ")");
                    }
                    ListSensorResultLabel.setText("List sensor : " + ListSensor.size());
                }
            }
        });
    }
    
    public void updateListX() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                dataListEECCModel.clear();
                for (int i = 0; i < mListofListSensor.size(); i++) {
                    Double next = mListofListTime.get(i);
                    if (mListofListSensor.get(i).size() > 0) {
                        ((DefaultListModel) mJListEECC.getModel()).addElement(i + ". id = " + i + "(Time: " + next + ")");
                    } else {
                        ((DefaultListModel) mJListEECC.getModel()).addElement(i + ". id = null");
                    }
                }
                ListEECCLabel.setText("List EECCcnt :"+ mListofListSensor.size());
            }
        });

    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        jLabel13 = new javax.swing.JLabel();
        RcTextfiled = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        MaxHopperTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        TimeRunningLabel = new javax.swing.JLabel();
        TimeLifeLabel = new javax.swing.JLabel();
        listEECCScrollPane = new javax.swing.JScrollPane();
        ListEECCLabel = new javax.swing.JLabel();
        listSensorScrollPane = new javax.swing.JScrollPane();
        ListSensorResultLabel = new javax.swing.JLabel();
        CalculateButton = new javax.swing.JButton();
        DoneButton = new javax.swing.JButton();
        ShowButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
                EsValueKeyReleased(evt);
            }
        });

        jLabel8.setText("Et : ");

        EtValueTextField.setText("0");
        EtValueTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                EtValueKeyReleased(evt);
            }
        });

        jLabel9.setText("Efs :");

        EfsValueTextField.setText("0");
        EfsValueTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                EfsValueKeyReleased(evt);
            }
        });

        jLabel10.setText("Emp :");

        EmpValueTextField.setText("0");
        EmpValueTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                EmpValueKeyReleased(evt);
            }
        });

        jLabel11.setText("Bit :");

        BitValueTextField.setText("0");
        BitValueTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BitValueKeyReleased(evt);
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
                .addComponent(jLabel12)
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
                .addGap(57, 57, 57))
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
                    .addComponent(EfsValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(EtValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(EmpValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(BitValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel19)
                    .addComponent(ErValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 43, Short.MAX_VALUE))
        );

        jLabel13.setText("Rc : ");

        RcTextfiled.setText("0");
        RcTextfiled.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                RcValueKeyReleased(evt);
            }
        });

        jLabel14.setText("Max Hopper");

        MaxHopperTextField.setText("0");
        MaxHopperTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                MaxhopperKeyReleased(evt);
            }
        });

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
                                .addComponent(NumberSensorLabel)
                                .addGap(96, 96, 96)
                                .addComponent(NumberTargetLabel)
                                .addGap(112, 112, 112)
                                .addComponent(NumberSinkLabel))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RcTextfiled, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(133, 133, 133)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(MaxHopperTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 54, Short.MAX_VALUE)))
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
                    .addComponent(MaxHopperTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel1.setText("Input");

        jLabel2.setText("Output");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        TimeRunningLabel.setText("Time Running : 0");

        TimeLifeLabel.setText("Total time life : 0");

        ListEECCLabel.setText("List EECCcnt :0");

        ListSensorResultLabel.setText("List Sensor : 0");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(TimeRunningLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(TimeLifeLabel)
                .addGap(133, 133, 133))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addComponent(listEECCScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addComponent(ListEECCLabel)))
                .addGap(137, 137, 137)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ListSensorResultLabel)
                    .addComponent(listSensorScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        CalculateButton.setText("Calculate");
        CalculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalculateButtonActionPerformed(evt);
            }
        });

        DoneButton.setText("Done");
        DoneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DoneButtonActionPerformed(evt);
            }
        });

        ShowButton.setText("ShowView");
        ShowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(CalculateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ShowButton)
                .addGap(85, 85, 85)
                .addComponent(DoneButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CalculateButton)
                    .addComponent(DoneButton)
                    .addComponent(ShowButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void RcValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RcValueKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mRcValue = Float.parseFloat(RcTextfiled.getText());
        } catch (NumberFormatException nfe) {
            RcTextfiled.setText("");
        }
    }//GEN-LAST:event_RcValueKeyReleased

    private void MaxhopperKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MaxhopperKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mMaxHopper = Integer.parseInt(MaxHopperTextField.getText());
        } catch (NumberFormatException nfe) {
            MaxHopperTextField.setText("");
        }
    }//GEN-LAST:event_MaxhopperKeyReleased

    private void EsValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EsValueKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mEsValue = Float.parseFloat(EsValueTextField.getText());
        } catch (NumberFormatException nfe) {
            EsValueTextField.setText("");
        }
    }//GEN-LAST:event_EsValueKeyReleased

    private void EfsValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EfsValueKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mEfsValue = Float.parseFloat(EfsValueTextField.getText());
        } catch (NumberFormatException nfe) {
            EfsValueTextField.setText("");
        }
    }//GEN-LAST:event_EfsValueKeyReleased

    private void EtValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EtValueKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mEtValue = Float.parseFloat(EtValueTextField.getText());
        } catch (NumberFormatException nfe) {
            EtValueTextField.setText("");
        }
    }//GEN-LAST:event_EtValueKeyReleased

    private void EmpValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EmpValueKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mEmpValue = Float.parseFloat(EmpValueTextField.getText());
        } catch (NumberFormatException nfe) {
            EmpValueTextField.setText("");
        }
    }//GEN-LAST:event_EmpValueKeyReleased

    private void BitValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BitValueKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mBitValue = Float.parseFloat(BitValueTextField.getText());
        } catch (NumberFormatException nfe) {
            BitValueTextField.setText("");
        }
    }//GEN-LAST:event_BitValueKeyReleased

    private void ErValueTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ErValueTextFieldKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.mErValue = Float.parseFloat(ErValueTextField.getText());
        } catch (NumberFormatException nfe) {
            ErValueTextField.setText("");
        }
    }//GEN-LAST:event_ErValueTextFieldKeyReleased

    private void CalculateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CalculateButtonActionPerformed
        // TODO add your handling code here:
        if (SensorUtility.mListSensorNodes.isEmpty() && mListTargetNodes.isEmpty() && mListSinkNodes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Insert sensorr nodes , target and sink");
        }  else {
            clearData();
            //Algorithm2 mAlgorithm = new Algorithm2();
            Algorithm3 mAlgorithm = new Algorithm3();
            Thread thread;
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long begin = System.currentTimeMillis();
                    mAlgorithm.run();
                    long end = System.currentTimeMillis();
                    timeRun = end-begin;
                    updateListX();
                    displayResult();
                }
            });
            thread.start();
        }
        
    }//GEN-LAST:event_CalculateButtonActionPerformed

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
        if (mListEECCIndex >= 0 && mListEECCIndex < mListofListSensor.size()) {
            ListSensor = mListofListSensor.get(mListEECCIndex);
            for (int i =0;i<ListSensor.size();i++) {
                //Change Value On foreach Sensor
                for(int j =0;j<mListSensorNodes.size();j++){
                    if (ListSensor.get(i).getX()== mListSensorNodes.get(j).getX() && ListSensor.get(i).getY()== mListSensorNodes.get(j).getY()) {
                        mListSensorNodes.get(j).setStatus(1);
                        break;
                    }
                }
            }
        }
        coordinatePanel.refresh();
    }//GEN-LAST:event_ShowButtonActionPerformed

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
            java.util.logging.Logger.getLogger(frameAlgorithm3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frameAlgorithm3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frameAlgorithm3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frameAlgorithm3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frameAlgorithm3().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BitValueTextField;
    private javax.swing.JButton CalculateButton;
    private javax.swing.JButton DoneButton;
    private javax.swing.JTextField EfsValueTextField;
    private javax.swing.JTextField EmpValueTextField;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane listEECCScrollPane;
    private javax.swing.JScrollPane listSensorScrollPane;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JList mJListSensor;
    private javax.swing.JList mJListEECC;

}

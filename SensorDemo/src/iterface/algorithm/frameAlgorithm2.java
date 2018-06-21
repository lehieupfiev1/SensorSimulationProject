/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iterface.algorithm;

import algorithm.Algorithm2;
import algorithm.TempAlgorithm;
import common.SensorUtility;
import static common.SensorUtility.LifeTimeOfSensor;
import static common.SensorUtility.Lvalue;
import static common.SensorUtility.mListSensorNodes;
import static common.SensorUtility.mListofListSensor;
import static common.SensorUtility.mListofListTime;

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
 * @author sev_user
 */
public class frameAlgorithm2 extends javax.swing.JFrame {

    /**
     * Creates new form frameAlgorithm2
     */
    public DefaultListModel dataSensorModel;
    public DefaultListModel dataListXModel;
    public int mListXIndex = -1;
    float timeRun;
    public static List<NodeItem> ListSensor = new ArrayList<>();
    public ListSelectionListener mListXSelectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
                if (!dataListXModel.isEmpty()) {
                    mListXIndex = mJListX.getSelectedIndex();
                    if (mListXIndex >= 0 && mListXIndex < mListofListSensor.size()) {
                        updateListSensor(mListXIndex);
                    }
                }
        }
    };
    public frameAlgorithm2() {
        initComponents();
        initOtherComponent();
        displayInput();
        clearData();
        this.setTitle("CompareAlgorithm 2");
    }
public void initOtherComponent() {
        //Init ListX ScrollPannel
        dataListXModel = new DefaultListModel();
        mJListX = new JList(dataListXModel);

       
        mJListX.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listXScrollPane.setViewportView(mJListX);
        mJListX.addListSelectionListener(mListXSelectionListener);
        
        
        //Init ListSensor ScrollPannel
        dataSensorModel = new DefaultListModel();
        mJListSensor = new JList(dataSensorModel);
        dataSensorModel.clear();
        mJListSensor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSensorScrollPane.setViewportView(mJListSensor);
    }
    
    void displayInput() {
        numberSensorLabel.setText("Number of Sensor : "+mListSensorNodes.size());
        LvalueTextField.setText(""+Lvalue);
        TimeSensorTextField.setText(""+LifeTimeOfSensor);
     
    }
    
    void clearData(){
        TimeRunningLabel.setText("TimeRunning : 0 ");
        dataSensorModel.clear();
        dataListXModel.clear();
        ListSensor.clear();
        totalTimeOnLabel.setText("Total Time ON : 0");
    }
    
    void displayResult() {
        TimeRunningLabel.setText("TimeRunning : "+timeRun);
        double totalTime =0;
        for (int i = 0; i < mListofListSensor.size(); i++) {
            Double next = mListofListTime.get(i);
            totalTime+=next;
        }
        totalTimeOnLabel.setText("Total Time ON : "+totalTime);
    }
    
    public void updateListSensor(int index) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                dataSensorModel.clear();
                listSenSorLabel.setText("List robots : 0");
                if (index >= 0 && index < mListofListSensor.size()) {
                    ListSensor = mListofListSensor.get(index);
                    for (int i = 0; i < ListSensor.size(); i++) {
                        NodeItem next = ListSensor.get(i);
                        ((DefaultListModel) mJListSensor.getModel()).addElement(i + ".( X = " + next.getX() + ", Y= " + next.getY() + ")");
                    }
                    listSenSorLabel.setText("List sensor : " + ListSensor.size());
                }
            }
        });
    }
    
    public void updateListX() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                dataListXModel.clear();
                for (int i = 0; i < mListofListSensor.size(); i++) {
                    Double next = mListofListTime.get(i);
                    if (mListofListSensor.get(i).size() > 0) {
                        ((DefaultListModel) mJListX.getModel()).addElement(i + ". id = " + i + "(Time: " + next + ")");
                    } else {
                        ((DefaultListModel) mJListX.getModel()).addElement(i + ". id = null");
                    }
                }
                listXLabel.setText("ListX(time) : " + mListofListSensor.size());
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

        jLabel2 = new javax.swing.JLabel();
        TimeSensorTextField = new javax.swing.JTextField();
        RunButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        TimeRunningLabel = new javax.swing.JLabel();
        listXScrollPane = new javax.swing.JScrollPane();
        listXLabel = new javax.swing.JLabel();
        listSensorScrollPane = new javax.swing.JScrollPane();
        totalTimeOnLabel = new javax.swing.JLabel();
        listSenSorLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        showViewBtn = new javax.swing.JButton();
        DoneBtn = new javax.swing.JButton();
        numberSensorLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        LvalueTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("Time life Sensor (s)");

        TimeSensorTextField.setText("3000");
        TimeSensorTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TimeSensorTextFieldchangeTimeLifeKeyReleased(evt);
            }
        });

        RunButton.setText("Run");
        RunButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RunButtonActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        TimeRunningLabel.setText("TimeRunning");

        listXLabel.setText("ListX(time) : 5");

        totalTimeOnLabel.setText("Total Time ON");

        listSenSorLabel.setText("List Sensor : 0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(TimeRunningLabel)
                        .addGap(228, 228, 228)
                        .addComponent(totalTimeOnLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(listXScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(104, 104, 104)
                                .addComponent(listXLabel)))
                        .addGap(125, 125, 125)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(listSenSorLabel)
                            .addComponent(listSensorScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(85, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TimeRunningLabel)
                    .addComponent(totalTimeOnLabel))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(listXLabel)
                    .addComponent(listSenSorLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listSensorScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(listXScrollPane))
                .addContainerGap())
        );

        jLabel3.setText("Result :");

        showViewBtn.setText("Show View");
        showViewBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showViewBtnActionPerformed(evt);
            }
        });

        DoneBtn.setText("Done");
        DoneBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DoneBtnActionPerformed(evt);
            }
        });

        numberSensorLabel.setText("Number of Sensor : 0");

        jLabel1.setText("L value");

        LvalueTextField.setText("5");
        LvalueTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                LvalueTextFieldchangeLvalueKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addComponent(showViewBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(DoneBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(160, 160, 160))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(numberSensorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addGap(64, 64, 64)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(LvalueTextField)
                                    .addComponent(TimeSensorTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(245, 245, 245)
                        .addComponent(RunButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(numberSensorLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(LvalueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(TimeSensorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(RunButton)
                .addGap(15, 15, 15)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(showViewBtn)
                    .addComponent(DoneBtn))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TimeSensorTextFieldchangeTimeLifeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TimeSensorTextFieldchangeTimeLifeKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.LifeTimeOfSensor = Integer.parseInt(TimeSensorTextField.getText());
        } catch (NumberFormatException nfe) {
            TimeSensorTextField.setText("");
        }
    }//GEN-LAST:event_TimeSensorTextFieldchangeTimeLifeKeyReleased

    private void RunButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RunButtonActionPerformed
        // TODO add your handling code here:
        if (SensorUtility.mListSensorNodes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Insert sensorr nodes");
        }  else {
            clearData();
            //Algorithm2 mAlgorithm = new Algorithm2();
            Algorithm2 mAlgorithm = new Algorithm2();
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
                    JOptionPane.showMessageDialog(null, "Compare Algorithm run finished !");
                }
            });
            thread.start();
        }
    }//GEN-LAST:event_RunButtonActionPerformed

    private void showViewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showViewBtnActionPerformed
        // TODO add your handling code here:
        //Clear data
        for (int j = 0; j < mListSensorNodes.size(); j++) {
            mListSensorNodes.get(j).setStatus(0);
        }
        if (mListXIndex >= 0 && mListXIndex < mListofListSensor.size()) {
            ListSensor = mListofListSensor.get(mListXIndex);
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
    }//GEN-LAST:event_showViewBtnActionPerformed

    private void DoneBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DoneBtnActionPerformed
        // TODO add your handling code here:
        coordinatePanel.refresh();
        this.dispose();
    }//GEN-LAST:event_DoneBtnActionPerformed

    private void LvalueTextFieldchangeLvalueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LvalueTextFieldchangeLvalueKeyReleased
        // TODO add your handling code here:
        try {
            SensorUtility.Lvalue = Integer.parseInt(LvalueTextField.getText());
        } catch (NumberFormatException nfe) {
            LvalueTextField.setText("");
        }
    }//GEN-LAST:event_LvalueTextFieldchangeLvalueKeyReleased

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
            java.util.logging.Logger.getLogger(frameAlgorithm2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frameAlgorithm2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frameAlgorithm2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frameAlgorithm2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        List<NodeItem> listSensor = new ArrayList<>();
        listSensor.add(new NodeItem(19, 30, 0));
        listSensor.add(new NodeItem(3, 4, 0));
        listSensor.add(new NodeItem(5, 6, 0));
        listSensor.add(new NodeItem(4, 5, 0));
        listSensor.add(new NodeItem(1, 2, 0));
        mListofListSensor.add(listSensor);

        listSensor = new ArrayList<>();
        listSensor.add(new NodeItem(11, 24, 0));
        listSensor.add(new NodeItem(1, 1, 0));
        listSensor.add(new NodeItem(2, 3, 0));
        mListofListSensor.add(listSensor);

        listSensor = new ArrayList<>();
        listSensor.add(new NodeItem(11, 25, 0));
        mListofListSensor.add(listSensor);

        listSensor = new ArrayList<>();
        listSensor.add(new NodeItem(12, 24, 0));
        listSensor.add(new NodeItem(29, 30, 0));
        mListofListSensor.add(listSensor);
        
        mListofListTime.add(new Double(2));
        mListofListTime.add(new Double(4));
        mListofListTime.add(new Double(5));
        mListofListTime.add(new Double(6));
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frameAlgorithm2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DoneBtn;
    private javax.swing.JTextField LvalueTextField;
    private javax.swing.JButton RunButton;
    private javax.swing.JLabel TimeRunningLabel;
    private javax.swing.JTextField TimeSensorTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel listSenSorLabel;
    private javax.swing.JScrollPane listSensorScrollPane;
    private javax.swing.JLabel listXLabel;
    private javax.swing.JScrollPane listXScrollPane;
    private javax.swing.JLabel numberSensorLabel;
    private javax.swing.JButton showViewBtn;
    private javax.swing.JLabel totalTimeOnLabel;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JList mJListSensor;
    private javax.swing.JList mJListX;
}

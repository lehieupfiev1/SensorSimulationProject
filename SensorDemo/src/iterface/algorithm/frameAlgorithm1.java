/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iterface.algorithm;

import algorithm.Algorithm1;
import common.SensorUtility;
import static iterface.frameMain.coordinatePanel;
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
public class frameAlgorithm1 extends javax.swing.JFrame {

    /**
     * Creates new form frameAlgorithm1
     */
    boolean isListRobotEmpty = false;
    public DefaultListModel dataModel;
    float timeRun;
    Algorithm1 mAlgorithm1;
    public frameAlgorithm1() {
        initComponents();
        initOtherComponent();
        displayInput();
        clearData();
        this.setTitle("Algorithm 1");
    }
     public void initOtherComponent() {
        dataModel = new DefaultListModel();
        mJListSensor = new JList(dataModel);
        dataModel.clear();
        mJListSensor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSensorScrollPane.setViewportView(mJListSensor);
        mJListSensor.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    void clearData(){
        TimeRunLabel.setText("Time run : "+0+ " mS");
        dataModel.clear();
        NumberSensorLabel.setText("Number of Sensors: ");
    }
    public void updateResultListSensor() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TimeRunLabel.setText("Time run : " + timeRun + " mS");
                dataModel.clear();
                for (int i = 0; i < SensorUtility.mListSensorNodes.size(); i++) {
                    NodeItem next = SensorUtility.mListSensorNodes.get(i);
                    ((DefaultListModel) mJListSensor.getModel()).addElement(i + ".( X = " + next.getX() + ", Y= " + next.getY() + ")");
                }
                NumberSensorLabel.setText("Number of Sensors: " + SensorUtility.mListSensorNodes.size());
            }
        });
    }
    void displayInput() {
        NumberTargetLabel.setText("Number target :"+SensorUtility.mListTargetNodes.size());
        NumberCycleLabel.setText("Number Cycle Robots : "+SensorUtility.mListRobotNodes.size());
        // Find Max robot in cycle
        int max = 0;
        isListRobotEmpty = false;
        for(int i =0; i <SensorUtility.mListRobotNodes.size();i++) {
            List<NodeItem> listRobot = SensorUtility.mListRobotNodes.get(i);
            if (listRobot.size() > max) {
                max = listRobot.size();
            }
            if(listRobot.isEmpty()) isListRobotEmpty= true;
        }
        MaxRobotsLabel.setText("Max Robots Of Cycle :"+max);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DoneBtn = new javax.swing.JButton();
        NumberTargetLabel = new javax.swing.JLabel();
        NumberCycleLabel = new javax.swing.JLabel();
        MaxRobotsLabel = new javax.swing.JLabel();
        RunBtn = new java.awt.Button();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        TimeRunLabel = new javax.swing.JLabel();
        NumberSensorLabel = new javax.swing.JLabel();
        ListSensorScrollPane = new javax.swing.JScrollPane();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        DoneBtn.setText("Done ");
        DoneBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DoneBtnActionPerformed(evt);
            }
        });

        NumberTargetLabel.setText("Number target :0");

        NumberCycleLabel.setText("Number Cycle Robots : ");

        MaxRobotsLabel.setText("Max Robots Of Cycle :");

        RunBtn.setLabel("Run");
        RunBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RunBtnActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("Result :");

        TimeRunLabel.setText("Time run : 0s");

        NumberSensorLabel.setText("Number of Sensor: 0");

        jLabel5.setText("Detailed List of Sensors");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NumberSensorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TimeRunLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ListSensorScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(48, 48, 48))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(TimeRunLabel)
                        .addGap(18, 18, 18)
                        .addComponent(NumberSensorLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ListSensorScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(NumberTargetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(MaxRobotsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(NumberCycleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(187, 187, 187)
                                .addComponent(DoneBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(163, 163, 163)
                                .addComponent(RunBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NumberTargetLabel)
                .addGap(18, 18, 18)
                .addComponent(NumberCycleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MaxRobotsLabel)
                .addGap(18, 18, 18)
                .addComponent(RunBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(DoneBtn)
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DoneBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DoneBtnActionPerformed
        // TODO add your handling code here:
        coordinatePanel.refresh();
        this.dispose();
    }//GEN-LAST:event_DoneBtnActionPerformed

    private void RunBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RunBtnActionPerformed
        // TODO add your handling code here:
        displayInput();
        clearData();
        if (SensorUtility.mListTargetNodes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Insert target nodes");
        } else if (SensorUtility.mListRobotNodes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Insert robot node");
        } else if (isListRobotEmpty) {
            JOptionPane.showMessageDialog(null, "Some cycle don't have any robot");
        } else {
            if (mAlgorithm1 != null) {
                mAlgorithm1 = null;
            }
            mAlgorithm1 = new Algorithm1();
            Thread thread;
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long begin = System.currentTimeMillis();
                    mAlgorithm1.run();
                    long end = System.currentTimeMillis();
                    timeRun = end-begin;
                    updateResultListSensor();
                }
            });
            thread.start();
        }
    }//GEN-LAST:event_RunBtnActionPerformed

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
            java.util.logging.Logger.getLogger(frameAlgorithm1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frameAlgorithm1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frameAlgorithm1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frameAlgorithm1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frameAlgorithm1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DoneBtn;
    private javax.swing.JScrollPane ListSensorScrollPane;
    private javax.swing.JLabel MaxRobotsLabel;
    private javax.swing.JLabel NumberCycleLabel;
    private javax.swing.JLabel NumberSensorLabel;
    private javax.swing.JLabel NumberTargetLabel;
    private java.awt.Button RunBtn;
    private javax.swing.JLabel TimeRunLabel;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JList mJListSensor;
}

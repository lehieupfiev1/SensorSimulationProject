/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iterface.robot;

import common.SensorUtility;
import static common.SensorUtility.mListRobotNodes;

import iterface.frameMain;
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
public class frameModifyRobot extends javax.swing.JFrame {

    /**
     * Creates new form frameModifyRobot
     */
    public DefaultListModel dataCycleModel;
    public DefaultListModel dataRobotModel;
    public int mCycleIndex = -1;
    public int mRobotIndex = -1;
    public int mOldPostionX;
    public int mOldPostionY;
    public int mNewPostionX;
    public int mNewPostionY;
    
    public ListSelectionListener mListRobotSelectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
                if (!dataRobotModel.isEmpty()) {
                    mCycleIndex = mJListCycle.getSelectedIndex();
                    mRobotIndex = mJListRobot.getSelectedIndex();
                    if (mCycleIndex >= 0 && mCycleIndex < mListRobotNodes.size()) {
                        List<NodeItem> listItem = mListRobotNodes.get(mCycleIndex);
                        if (mRobotIndex >=0 && mRobotIndex < listItem.size()) {
                            mOldPostionX = listItem.get(mRobotIndex).getX();
                            mOldPostionY = listItem.get(mRobotIndex).getY();
                            mNewPostionX = mOldPostionX;
                            mNewPostionY = mOldPostionY;
                            positonXTextField.setText("" + listItem.get(mRobotIndex).getX());
                            postionYTextField.setText("" + listItem.get(mRobotIndex).getY());
                            CycleOfRobotLabel.setText("Cycle of Robot : id =" +listItem.get(mRobotIndex).getGroup());
                        }
                    }
                }
        }
    };
    public ListSelectionListener mListCycleSelectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
                if (!dataCycleModel.isEmpty()) {
                    mCycleIndex = mJListCycle.getSelectedIndex();
                    if (mCycleIndex >= 0 && mCycleIndex < mListRobotNodes.size()) {
                        updateListRobot(mCycleIndex);
                        mRobotIndex =-1;
                        CycleOfRobotLabel.setText("Cycle of Robot : " );
                        resetPositionLabel();
                    }
                }
        }
    };
    
    public frameModifyRobot() {
        initComponents();
        initOtherComponents();
        this.setTitle("Modify Robots");
    }

    private void initOtherComponents() {
        // Init Cycle
        dataCycleModel = new DefaultListModel();
        mJListCycle = new JList(dataCycleModel);

        updateListCycle();
        mJListCycle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listCycleRobotScrollPane.setViewportView(mJListCycle);
        mJListCycle.addListSelectionListener(mListCycleSelectionListener);
        
        // Init Robot
        dataRobotModel = new DefaultListModel();
        mJListRobot = new JList(dataRobotModel);

        //updateListRobot(mCycleIndex);
        mJListRobot.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListRobotScrollPane.setViewportView(mJListRobot);
        mJListRobot.addListSelectionListener(mListRobotSelectionListener);
        
    }

    
    public void updateListCycle() {
        dataCycleModel.clear();
        for (int i = 0; i < mListRobotNodes.size(); i++) {
            List<NodeItem> next = mListRobotNodes.get(i);
            if (next.size()>0) {
              ((DefaultListModel) mJListCycle.getModel()).addElement(i + ". id = " + next.get(0).getGroup());
            } else {
              ((DefaultListModel) mJListCycle.getModel()).addElement(i + ". id = null");
            }
        }
        mListCycleLabel.setText("List Cycle : " + mListRobotNodes.size());

    }
    public void resetPositionLabel() {
        positonXTextField.setText("0");
        postionYTextField.setText("0");

    }

    public void updateListRobot(int index) {
        dataRobotModel.clear();
        mListRobotsLabel.setText("List robots : 0");
        if (index >= 0 && index < mListRobotNodes.size()) {
            List<NodeItem> listItem = mListRobotNodes.get(index);
            for (int i = 0; i < listItem.size(); i++) {
                NodeItem next = listItem.get(i);
                ((DefaultListModel) mJListRobot.getModel()).addElement(i + ".( X = " + next.getX() + ", Y= " + next.getY() + ")");
            }
            mListRobotsLabel.setText("List robots : " + listItem.size());
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

        mListCycleLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        mChangeBtn = new javax.swing.JButton();
        mDeleteRobotBtn = new javax.swing.JButton();
        listCycleRobotScrollPane = new javax.swing.JScrollPane();
        mDoneBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        CycleOfRobotLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        positonXTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        postionYTextField = new javax.swing.JTextField();
        ListRobotScrollPane = new javax.swing.JScrollPane();
        mListRobotsLabel = new javax.swing.JLabel();
        mDeleteCycleBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        mListCycleLabel.setText("List Cycle : 0");

        jLabel2.setText("Detail Robot");

        mChangeBtn.setText("Change");
        mChangeBtn.setToolTipText("");
        mChangeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mChangeBtnActionPerformed(evt);
            }
        });

        mDeleteRobotBtn.setText("Delete Robots");
        mDeleteRobotBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mDeleteRobotBtnActionPerformed(evt);
            }
        });

        mDoneBtn.setText("Done");
        mDoneBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mDoneBtnActionPerformed(evt);
            }
        });

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        CycleOfRobotLabel.setText("Cycle of Robot :");

        jLabel4.setText("Position X");

        positonXTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                positonXTextField(evt);
            }
        });

        jLabel5.setText("Position Y");

        postionYTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                postionYTextField(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(positonXTextField)
                    .addComponent(postionYTextField)
                    .addComponent(CycleOfRobotLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(CycleOfRobotLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(positonXTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(postionYTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );

        mListRobotsLabel.setText("List Robots : 0");

        mDeleteCycleBtn.setText("Delete Cycle");
        mDeleteCycleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mDeleteCycleBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(listCycleRobotScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mDeleteCycleBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(ListRobotScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(mDeleteRobotBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                                .addComponent(mChangeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(mDoneBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(33, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(mListCycleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(mListRobotsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(97, 97, 97)
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mListCycleLabel)
                    .addComponent(jLabel2)
                    .addComponent(mListRobotsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ListRobotScrollPane)
                    .addComponent(listCycleRobotScrollPane, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mDeleteCycleBtn)
                    .addComponent(mDeleteRobotBtn)
                    .addComponent(mChangeBtn)
                    .addComponent(mDoneBtn))
                .addGap(33, 33, 33))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mChangeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mChangeBtnActionPerformed
        // TODO add your handling code here:
        if (positonXTextField.getText().equals("") || postionYTextField.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please insert Valid Number Only");
        } else if (mNewPostionX >= SensorUtility.numberRow || mNewPostionY >= SensorUtility.numberColum){
            int maxX = SensorUtility.numberRow-1;
            int maxY = SensorUtility.numberColum-1;
            JOptionPane.showMessageDialog(null, "Maximum positon X =" + maxX +" and Maximum positon Y =" +maxY);
        } else if (mNewPostionX == mOldPostionX && mNewPostionY == mOldPostionY) {
            JOptionPane.showMessageDialog(null, "There are no change ^_^" );
        } else if (mCycleIndex >= 0 && mCycleIndex < mListRobotNodes.size()) {
            List<NodeItem> listItem = mListRobotNodes.get(mCycleIndex);
            if (mRobotIndex >= 0 && mRobotIndex < listItem.size()) {
                mJListRobot.removeListSelectionListener(mListRobotSelectionListener);

                listItem.get(mRobotIndex).setX(mNewPostionX);
                listItem.get(mRobotIndex).setY(mNewPostionY);

                updateListRobot(mCycleIndex);
                mJListRobot.addListSelectionListener(mListRobotSelectionListener);
                mJListRobot.setSelectedIndex(mRobotIndex);
            }
        }
    }//GEN-LAST:event_mChangeBtnActionPerformed

    private void mDeleteRobotBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mDeleteRobotBtnActionPerformed
        // TODO add your handling code here:
        if (mCycleIndex >= 0 && mCycleIndex < mListRobotNodes.size()) {
            List<NodeItem> listItem = mListRobotNodes.get(mCycleIndex);
            if (mRobotIndex >= 0 && mRobotIndex < listItem.size()) {
                mJListRobot.removeListSelectionListener(mListRobotSelectionListener);
                mJListRobot.clearSelection();
                listItem.remove(mRobotIndex);
                mRobotIndex = -1;

                updateListRobot(mCycleIndex);
                resetPositionLabel();
                mJListRobot.addListSelectionListener(mListRobotSelectionListener);
            }
        }

    }//GEN-LAST:event_mDeleteRobotBtnActionPerformed

    private void mDoneBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mDoneBtnActionPerformed
        // TODO add your handling code here:
        try {
            frameMain.coordinatePanel.refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        this.dispose();
    }//GEN-LAST:event_mDoneBtnActionPerformed

    private void positonXTextField(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_positonXTextField
        // TODO add your handling code here:
        try {
            mNewPostionX = Integer.parseInt(positonXTextField.getText());
        } catch (NumberFormatException nfe) {
            positonXTextField.setText("");
        }
    }//GEN-LAST:event_positonXTextField

    private void postionYTextField(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_postionYTextField
        // TODO add your handling code here:
        try {
            mNewPostionY = Integer.parseInt(postionYTextField.getText());
        } catch (NumberFormatException nfe) {
            postionYTextField.setText("");
        }
    }//GEN-LAST:event_postionYTextField

    private void mDeleteCycleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mDeleteCycleBtnActionPerformed
        // TODO add your handling code here:
        if (mCycleIndex >= 0 && mCycleIndex < mListRobotNodes.size()) {
            mJListCycle.removeListSelectionListener(mListCycleSelectionListener);
            mJListCycle.clearSelection();
            mListRobotNodes.remove(mCycleIndex);
            mCycleIndex = -1;
            mRobotIndex = -1;
            
            dataRobotModel.clear();
            mListRobotsLabel.setText("List robots : 0");
            resetPositionLabel();
            updateListCycle();

            mJListCycle.addListSelectionListener(mListCycleSelectionListener);
        }
    }//GEN-LAST:event_mDeleteCycleBtnActionPerformed

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
            java.util.logging.Logger.getLogger(frameModifyRobot.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frameModifyRobot.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frameModifyRobot.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frameModifyRobot.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frameModifyRobot().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CycleOfRobotLabel;
    private javax.swing.JScrollPane ListRobotScrollPane;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane listCycleRobotScrollPane;
    private javax.swing.JButton mChangeBtn;
    private javax.swing.JButton mDeleteCycleBtn;
    private javax.swing.JButton mDeleteRobotBtn;
    private javax.swing.JButton mDoneBtn;
    private javax.swing.JLabel mListCycleLabel;
    private javax.swing.JLabel mListRobotsLabel;
    private javax.swing.JTextField positonXTextField;
    private javax.swing.JTextField postionYTextField;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JList mJListCycle;
    private javax.swing.JList mJListRobot;
}

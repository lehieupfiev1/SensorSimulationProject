/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iterface;

import common.SensorUtility;
import iterface.robot.frameAddRobot;
import iterface.robot.frameModifyRobot;
import iterface.sensor.frameAddSensor;
import iterface.sensor.frameModifySensor;
import iterface.setting.frameCalculateDistance;
import iterface.setting.frameSetNetworkSize;
import iterface.target.frameAddTarget;
import iterface.target.frameModifyTarget;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;


/**
 *
 * @author Hieu
 */
public class frameMain extends javax.swing.JFrame {

    /**
     * Creates new form frameMain
     */
    public frameMain() {
        initComponents();
        initOtherComponents();
        Image image = null;
        try {
            image = ImageIO.read(getClass().getResource("/resource/Logo_wsn.png"));
            this.setIconImage(image);
        } catch (IOException ex) {
            
        }

    }

    public void initOtherComponents() {
        coordinatePanel = new frameCoordinateSystemPanel(1200,1200);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(coordinatePanel);
        coordinatePanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(coordinatePanel);

    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        menuBar = new javax.swing.JMenuBar();
        inputDataMenu = new javax.swing.JMenu();
        NetworkSizeMenuItem = new javax.swing.JMenuItem();
        CalculateDistanceMenuItem = new javax.swing.JMenuItem();
        ShowGridMenuItem = new javax.swing.JCheckBoxMenuItem();
        SaveFileMenuItem = new javax.swing.JMenuItem();
        importFileMenuItem = new javax.swing.JMenuItem();
        captureScreenMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        SensorMenu = new javax.swing.JMenu();
        AddSensorMenuItem = new javax.swing.JMenuItem();
        ModifiySensorMenuItem = new javax.swing.JMenuItem();
        ShowSensorMenuItem = new javax.swing.JCheckBoxMenuItem();
        DeleteAllSenorMenuItem = new javax.swing.JMenuItem();
        TargetMenu = new javax.swing.JMenu();
        AddTargetMenuItem = new javax.swing.JMenuItem();
        ModifyTargetMenuItem = new javax.swing.JMenuItem();
        ShowTargetMenuItem = new javax.swing.JCheckBoxMenuItem();
        DeleteAllTargetMenuItem = new javax.swing.JMenuItem();
        RobotMenu = new javax.swing.JMenu();
        AddRobotMenuItem = new javax.swing.JMenuItem();
        ModifyRobotMenuItem = new javax.swing.JMenuItem();
        ShowRobotMenuItem = new javax.swing.JCheckBoxMenuItem();
        DeleteAllRobotMenuItem = new javax.swing.JMenuItem();
        AlgorithmMenu = new javax.swing.JMenu();
        MyAlgorithmMenuItem = new javax.swing.JMenuItem();
        Algorithm1MenuItem = new javax.swing.JMenuItem();
        Algorithm2MenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        inputDataMenu.setMnemonic('f');
        inputDataMenu.setText("Setting");

        NetworkSizeMenuItem.setMnemonic('o');
        NetworkSizeMenuItem.setText("Network Size");
        NetworkSizeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NetworkSizeMenuItemActionPerformed(evt);
            }
        });
        inputDataMenu.add(NetworkSizeMenuItem);

        CalculateDistanceMenuItem.setMnemonic('s');
        CalculateDistanceMenuItem.setText("Calculate Distance");
        CalculateDistanceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalculateDistanceMenuItemActionPerformed(evt);
            }
        });
        inputDataMenu.add(CalculateDistanceMenuItem);

        ShowGridMenuItem.setSelected(true);
        ShowGridMenuItem.setText("ShowGrid");
        ShowGridMenuItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ShowGridItemStateChangedListener(evt);
            }
        });
        inputDataMenu.add(ShowGridMenuItem);

        SaveFileMenuItem.setText("Save Input Data File");
        SaveFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveFileMenuItemActionPerformed(evt);
            }
        });
        inputDataMenu.add(SaveFileMenuItem);

        importFileMenuItem.setMnemonic('a');
        importFileMenuItem.setText("Import Input Data File");
        importFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importFileMenuItemActionPerformed(evt);
            }
        });
        inputDataMenu.add(importFileMenuItem);

        captureScreenMenuItem.setText("Capture Screen");
        captureScreenMenuItem.setActionCommand("Capture Screen");
        captureScreenMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                captureScreenMenuItemActionPerformed(evt);
            }
        });
        inputDataMenu.add(captureScreenMenuItem);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        inputDataMenu.add(exitMenuItem);

        menuBar.add(inputDataMenu);

        SensorMenu.setMnemonic('e');
        SensorMenu.setText("Sensor");

        AddSensorMenuItem.setMnemonic('t');
        AddSensorMenuItem.setText("Add");
        AddSensorMenuItem.setHideActionText(true);
        AddSensorMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddSensorMenuItemActionPerformed(evt);
            }
        });
        SensorMenu.add(AddSensorMenuItem);

        ModifiySensorMenuItem.setMnemonic('y');
        ModifiySensorMenuItem.setText("Modify");
        ModifiySensorMenuItem.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/roundicon_1.png"))); // NOI18N
        ModifiySensorMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifiySensorMenuItemActionPerformed(evt);
            }
        });
        SensorMenu.add(ModifiySensorMenuItem);

        ShowSensorMenuItem.setSelected(true);
        ShowSensorMenuItem.setText("View");
        ShowSensorMenuItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SensorItemStateChangedListener(evt);
            }
        });
        ShowSensorMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowSensorMenuItemActionPerformed(evt);
            }
        });
        SensorMenu.add(ShowSensorMenuItem);

        DeleteAllSenorMenuItem.setMnemonic('d');
        DeleteAllSenorMenuItem.setText("DeleteAll");
        DeleteAllSenorMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteAllSenorMenuItemActionPerformed(evt);
            }
        });
        SensorMenu.add(DeleteAllSenorMenuItem);

        menuBar.add(SensorMenu);

        TargetMenu.setText("Target");

        AddTargetMenuItem.setText("Add");
        AddTargetMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddTargetMenuItemActionPerformed(evt);
            }
        });
        TargetMenu.add(AddTargetMenuItem);

        ModifyTargetMenuItem.setText("Modify");
        ModifyTargetMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifyTargetMenuItemActionPerformed(evt);
            }
        });
        TargetMenu.add(ModifyTargetMenuItem);

        ShowTargetMenuItem.setSelected(true);
        ShowTargetMenuItem.setText("View");
        ShowTargetMenuItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TargetItemStateChangedListener(evt);
            }
        });
        ShowTargetMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowTargetMenuItemActionPerformed(evt);
            }
        });
        TargetMenu.add(ShowTargetMenuItem);

        DeleteAllTargetMenuItem.setText("DeleteAll");
        DeleteAllTargetMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteAllTargetMenuItemActionPerformed(evt);
            }
        });
        TargetMenu.add(DeleteAllTargetMenuItem);

        menuBar.add(TargetMenu);

        RobotMenu.setText("Robot");

        AddRobotMenuItem.setText("Add");
        AddRobotMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddRobotMenuItemActionPerformed(evt);
            }
        });
        RobotMenu.add(AddRobotMenuItem);

        ModifyRobotMenuItem.setText("Modify");
        ModifyRobotMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifyRobotMenuItemActionPerformed(evt);
            }
        });
        RobotMenu.add(ModifyRobotMenuItem);

        ShowRobotMenuItem.setSelected(true);
        ShowRobotMenuItem.setText("View");
        ShowRobotMenuItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RobotItemStateChangedListener(evt);
            }
        });
        RobotMenu.add(ShowRobotMenuItem);

        DeleteAllRobotMenuItem.setText("DeleteAll");
        DeleteAllRobotMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteAllRobotMenuItemActionPerformed(evt);
            }
        });
        RobotMenu.add(DeleteAllRobotMenuItem);

        menuBar.add(RobotMenu);

        AlgorithmMenu.setText("Algorithm");

        MyAlgorithmMenuItem.setText("MyAlgorithm");
        AlgorithmMenu.add(MyAlgorithmMenuItem);

        Algorithm1MenuItem.setText("Algorithm 1");
        AlgorithmMenu.add(Algorithm1MenuItem);

        Algorithm2MenuItem.setText("Algorithm 2");
        AlgorithmMenu.add(Algorithm2MenuItem);

        menuBar.add(AlgorithmMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        contentsMenuItem.setMnemonic('c');
        contentsMenuItem.setText("Contents");
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1500, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 813, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void NetworkSizeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NetworkSizeMenuItemActionPerformed
        // TODO add your handling code here:
        
        frameSetNetworkSize frameNetworkSize = new frameSetNetworkSize();
        frameNetworkSize.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frameNetworkSize.setVisible(true);
        //coordinatePanel.refresh();
    }//GEN-LAST:event_NetworkSizeMenuItemActionPerformed

    private void ShowSensorMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowSensorMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ShowSensorMenuItemActionPerformed

    private void ShowTargetMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowTargetMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ShowTargetMenuItemActionPerformed

    private void SensorItemStateChangedListener(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SensorItemStateChangedListener
        // TODO add your handling code here:
        int state = evt.getStateChange();
        if (state == ItemEvent.SELECTED) {
            coordinatePanel.setShowSensor(true);
        } else {
            coordinatePanel.setShowSensor(false);
        }
        coordinatePanel.refresh();
    }//GEN-LAST:event_SensorItemStateChangedListener

    private void TargetItemStateChangedListener(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TargetItemStateChangedListener
        // TODO add your handling code here:
        int state = evt.getStateChange();
        if (state == ItemEvent.SELECTED) {
            coordinatePanel.setShowTarget(true);
        } else {
            coordinatePanel.setShowTarget(false);
        }
        coordinatePanel.refresh();
    }//GEN-LAST:event_TargetItemStateChangedListener

    private void RobotItemStateChangedListener(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RobotItemStateChangedListener
        // TODO add your handling code here:
        int state = evt.getStateChange();
        if (state == ItemEvent.SELECTED) {
            coordinatePanel.setShowRobot(true);
        } else {
            coordinatePanel.setShowRobot(false);
        }
        coordinatePanel.refresh();
    }//GEN-LAST:event_RobotItemStateChangedListener

    private void ShowGridItemStateChangedListener(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ShowGridItemStateChangedListener
        // TODO add your handling code here:
        int state = evt.getStateChange();
        if (state == ItemEvent.SELECTED) {
            coordinatePanel.setShowGrid(true);
        } else {
            coordinatePanel.setShowGrid(false);
        }
        coordinatePanel.refresh();
    }//GEN-LAST:event_ShowGridItemStateChangedListener

    private void AddTargetMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddTargetMenuItemActionPerformed
        // TODO add your handling code here:
        frameAddTarget mFrameAddTarget = new frameAddTarget();
        mFrameAddTarget.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mFrameAddTarget.setVisible(true);
    }//GEN-LAST:event_AddTargetMenuItemActionPerformed

    private void CalculateDistanceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CalculateDistanceMenuItemActionPerformed
        // TODO add your handling code here:
        frameCalculateDistance mCalculateDistance = new frameCalculateDistance();
        mCalculateDistance.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mCalculateDistance.setVisible(true);
    }//GEN-LAST:event_CalculateDistanceMenuItemActionPerformed

    private void ModifyTargetMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifyTargetMenuItemActionPerformed
        // TODO add your handling code here:
        frameModifyTarget mModifyTarget = new frameModifyTarget();
        mModifyTarget.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mModifyTarget.setVisible(true);
    }//GEN-LAST:event_ModifyTargetMenuItemActionPerformed

    private void AddSensorMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddSensorMenuItemActionPerformed
        // TODO add your handling code here:
        frameAddSensor mFrameAddSensor = new frameAddSensor();
        mFrameAddSensor.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mFrameAddSensor.setVisible(true);
    }//GEN-LAST:event_AddSensorMenuItemActionPerformed

    private void ModifiySensorMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifiySensorMenuItemActionPerformed
        // TODO add your handling code here:
        frameModifySensor mModifySensor = new frameModifySensor();
        mModifySensor.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mModifySensor.setVisible(true);
    }//GEN-LAST:event_ModifiySensorMenuItemActionPerformed

    private void DeleteAllSenorMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteAllSenorMenuItemActionPerformed
        // TODO add your handling code here:
        SensorUtility.mListSensorNodes.clear();
        coordinatePanel.refresh();
    }//GEN-LAST:event_DeleteAllSenorMenuItemActionPerformed

    private void DeleteAllTargetMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteAllTargetMenuItemActionPerformed
        // TODO add your handling code here:
        SensorUtility.mListTargetNodes.clear();
        coordinatePanel.refresh();
    }//GEN-LAST:event_DeleteAllTargetMenuItemActionPerformed

    private void DeleteAllRobotMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteAllRobotMenuItemActionPerformed
        // TODO add your handling code here:
        SensorUtility.mListRobotNodes.clear();
        coordinatePanel.refresh();
    }//GEN-LAST:event_DeleteAllRobotMenuItemActionPerformed

    private void AddRobotMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddRobotMenuItemActionPerformed
        // TODO add your handling code here:
        frameAddRobot mFrameAddRobot = new frameAddRobot();
        mFrameAddRobot.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mFrameAddRobot.setVisible(true);
    }//GEN-LAST:event_AddRobotMenuItemActionPerformed

    private void ModifyRobotMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifyRobotMenuItemActionPerformed
        // TODO add your handling code here:
        frameModifyRobot mFrameModifyRobot = new frameModifyRobot();
        mFrameModifyRobot.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mFrameModifyRobot.setVisible(true);
    }//GEN-LAST:event_ModifyRobotMenuItemActionPerformed

    private void importFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importFileMenuItemActionPerformed

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else if (f.getName().endsWith(".INP")) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public String getDescription() {
                return "Sensor Input files";
            }
        };
        chooser.setDialogTitle("Open File");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.addChoosableFileFilter(filter);

        chooser.setAcceptAllFileFilterUsed(true);


        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
            try {
                SensorUtility.readFile(chooser.getSelectedFile()+"");
            } catch (Exception ex) {
                Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(" Le Hieu IOException");
                JOptionPane.showMessageDialog(null, "Error open file !");
                SensorUtility.resetSetting();
            } finally {
                coordinatePanel.setCoordinateSize(SensorUtility.numberRow,SensorUtility.numberColum);
                coordinatePanel.refresh();
            }

        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_importFileMenuItemActionPerformed

    private void SaveFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveFileMenuItemActionPerformed
        // TODO add your handling code here:
        saveFile();
    }//GEN-LAST:event_SaveFileMenuItemActionPerformed

    private void captureScreenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_captureScreenMenuItemActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else if (f.getName().endsWith(".png")) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public String getDescription() {
                return "Image files";
            }
        };
        chooser.setDialogTitle("Save screen image");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.addChoosableFileFilter(filter);

        chooser.setAcceptAllFileFilterUsed(true);


        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
            try {
                SensorUtility.captureScreen(coordinatePanel ,chooser.getSelectedFile()+".png");
            } catch (IOException ex) {
                Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_captureScreenMenuItemActionPerformed

    public void saveFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else if (f.getName().endsWith(".INP")) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public String getDescription() {
                return "Sensor Input files";
            }
        };
        chooser.setDialogTitle("Save File");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.addChoosableFileFilter(filter);

        chooser.setAcceptAllFileFilterUsed(true);


        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
            try {
                SensorUtility.writeFile(chooser.getSelectedFile()+".INP");
            } catch (IOException ex) {
                Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("No Selection ");
        }
    }
    
    
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
            java.util.logging.Logger.getLogger(frameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frameMain mFrameMain = new frameMain();
               // mFrameMain.setState(JFrame.MAXIMIZED_BOTH);
                mFrameMain.setExtendedState(JFrame.MAXIMIZED_BOTH);
//                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();  
//                mFrameMain.setMaximizedBounds(env.getMaximumWindowBounds());  
                mFrameMain.setVisible(true);
                
                mFrameMain.setTitle("Sensor Simulation");
                
                coordinatePanel.fillCell(199, 199, 0);
                coordinatePanel.fillCell(0, 199, 0);
                coordinatePanel.fillCell(199, 0, 1);
                coordinatePanel.fillCell(0, 4, 2);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AddRobotMenuItem;
    private javax.swing.JMenuItem AddSensorMenuItem;
    private javax.swing.JMenuItem AddTargetMenuItem;
    private javax.swing.JMenuItem Algorithm1MenuItem;
    private javax.swing.JMenuItem Algorithm2MenuItem;
    private javax.swing.JMenu AlgorithmMenu;
    private javax.swing.JMenuItem CalculateDistanceMenuItem;
    private javax.swing.JMenuItem DeleteAllRobotMenuItem;
    private javax.swing.JMenuItem DeleteAllSenorMenuItem;
    private javax.swing.JMenuItem DeleteAllTargetMenuItem;
    private javax.swing.JMenuItem ModifiySensorMenuItem;
    private javax.swing.JMenuItem ModifyRobotMenuItem;
    private javax.swing.JMenuItem ModifyTargetMenuItem;
    private javax.swing.JMenuItem MyAlgorithmMenuItem;
    private javax.swing.JMenuItem NetworkSizeMenuItem;
    private javax.swing.JMenu RobotMenu;
    private javax.swing.JMenuItem SaveFileMenuItem;
    private javax.swing.JMenu SensorMenu;
    private javax.swing.JCheckBoxMenuItem ShowGridMenuItem;
    private javax.swing.JCheckBoxMenuItem ShowRobotMenuItem;
    private javax.swing.JCheckBoxMenuItem ShowSensorMenuItem;
    private javax.swing.JCheckBoxMenuItem ShowTargetMenuItem;
    private javax.swing.JMenu TargetMenu;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem captureScreenMenuItem;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem importFileMenuItem;
    private javax.swing.JMenu inputDataMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables
    public static frameCoordinateSystemPanel coordinatePanel;
}

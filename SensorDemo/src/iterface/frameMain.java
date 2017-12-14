/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iterface;

import algorithm.Algorithm1;
import algorithm.MyAlgorithm;
import common.SensorUtility;
import iterface.algorithm.frameAlgorithm1;
import iterface.algorithm.frameAlgorithm2;
import iterface.algorithm.frameMyAlgorithm;
import iterface.algorithm.frameMyAlgorithm2;
import iterface.robot.frameAddRobot;
import iterface.robot.frameModifyRobot;
import iterface.sensor.frameAddSensor;
import iterface.sensor.frameModifySensor;
import iterface.setting.frameCalculateDistance;
import iterface.setting.frameSetNetworkSize;
import iterface.target.frameAddTarget;
import iterface.target.frameModifyTarget;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;


/**
 *
 * @author Hieu
 */
public class frameMain extends javax.swing.JFrame {

    /**
     * Creates new form frameMain
     */
    MyAlgorithm myAlgorithm;
    Algorithm1 algorithm1;
    
    public frameMain() {
        //Init ListColor
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            int r = rand.nextInt(255);
            int g = rand.nextInt(255);
            int b = rand.nextInt(255);
            Color randomColor = new Color(r, g, b);
            SensorUtility.mListColor.add(randomColor);
        }
        
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
        JScrollPane jScrollPane1 = new JScrollPane();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1500, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
        );
        



        //Add Coordinate Panel

       // jScrollPane1.setPreferredSize(new Dimension(1500,1500));
        coordinatePanel = new frameCoordinateSystemPanel(1240,1240);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(coordinatePanel);
        coordinatePanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1640, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1640, Short.MAX_VALUE)
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

        menuBar = new javax.swing.JMenuBar();
        inputDataMenu = new javax.swing.JMenu();
        NetworkSizeMenuItem = new javax.swing.JMenuItem();
        CalculateDistanceMenuItem = new javax.swing.JMenuItem();
        SaveFileMenuItem = new javax.swing.JMenuItem();
        importFileMenuItem = new javax.swing.JMenuItem();
        captureScreenMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        SensorMenu = new javax.swing.JMenu();
        AddSensorMenuItem = new javax.swing.JMenuItem();
        ModifiySensorMenuItem = new javax.swing.JMenuItem();
        DeleteAllSenorMenuItem = new javax.swing.JMenuItem();
        TargetMenu = new javax.swing.JMenu();
        AddTargetMenuItem = new javax.swing.JMenuItem();
        ModifyTargetMenuItem = new javax.swing.JMenuItem();
        DeleteAllTargetMenuItem = new javax.swing.JMenuItem();
        RobotMenu = new javax.swing.JMenu();
        AddRobotMenuItem = new javax.swing.JMenuItem();
        ModifyRobotMenuItem = new javax.swing.JMenuItem();
        DeleteAllRobotMenuItem = new javax.swing.JMenuItem();
        ViewMenu = new javax.swing.JMenu();
        showGridMenuItem = new javax.swing.JCheckBoxMenuItem();
        showSensorMenuItem = new javax.swing.JCheckBoxMenuItem();
        showTargetMenuItem = new javax.swing.JCheckBoxMenuItem();
        showRobotMenuItem = new javax.swing.JCheckBoxMenuItem();
        showSensorCorverMenuItem = new javax.swing.JCheckBoxMenuItem();
        AlgorithmMenu = new javax.swing.JMenu();
        MyAlgorithmMenuItem = new javax.swing.JMenuItem();
        Algorithm1MenuItem = new javax.swing.JMenuItem();
        MyAlgorithm2MenuItem = new javax.swing.JMenuItem();
        Algorithm2 = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        inputDataMenu.setMnemonic('f');
        inputDataMenu.setText("Setting");

        NetworkSizeMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/change_icon.png"))); // NOI18N
        NetworkSizeMenuItem.setMnemonic('o');
        NetworkSizeMenuItem.setText("Network Size");
        NetworkSizeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NetworkSizeMenuItemActionPerformed(evt);
            }
        });
        inputDataMenu.add(NetworkSizeMenuItem);

        CalculateDistanceMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/calculate_icon.png"))); // NOI18N
        CalculateDistanceMenuItem.setMnemonic('s');
        CalculateDistanceMenuItem.setText("Calculate Distance");
        CalculateDistanceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalculateDistanceMenuItemActionPerformed(evt);
            }
        });
        inputDataMenu.add(CalculateDistanceMenuItem);

        SaveFileMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save_icon.png"))); // NOI18N
        SaveFileMenuItem.setText("Save Input Data File");
        SaveFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveFileMenuItemActionPerformed(evt);
            }
        });
        inputDataMenu.add(SaveFileMenuItem);

        importFileMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/import_icon.png"))); // NOI18N
        importFileMenuItem.setMnemonic('a');
        importFileMenuItem.setText("Import Input Data File");
        importFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importFileMenuItemActionPerformed(evt);
            }
        });
        inputDataMenu.add(importFileMenuItem);

        captureScreenMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/capture_icon.png"))); // NOI18N
        captureScreenMenuItem.setText("Capture Screen");
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

        AddSensorMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/circle_icon.png"))); // NOI18N
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
        ModifiySensorMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifiySensorMenuItemActionPerformed(evt);
            }
        });
        SensorMenu.add(ModifiySensorMenuItem);

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

        AddTargetMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/square_icon.png"))); // NOI18N
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

        DeleteAllTargetMenuItem.setText("DeleteAll");
        DeleteAllTargetMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteAllTargetMenuItemActionPerformed(evt);
            }
        });
        TargetMenu.add(DeleteAllTargetMenuItem);

        menuBar.add(TargetMenu);

        RobotMenu.setText("Robot");

        AddRobotMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/triangle_icon.png"))); // NOI18N
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

        DeleteAllRobotMenuItem.setText("DeleteAll");
        DeleteAllRobotMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteAllRobotMenuItemActionPerformed(evt);
            }
        });
        RobotMenu.add(DeleteAllRobotMenuItem);

        menuBar.add(RobotMenu);

        ViewMenu.setText("View");

        showGridMenuItem.setSelected(true);
        showGridMenuItem.setText("Show Grid");
        showGridMenuItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                showGridItemStateChangedListener(evt);
            }
        });
        ViewMenu.add(showGridMenuItem);

        showSensorMenuItem.setSelected(true);
        showSensorMenuItem.setText("Show Sensor");
        showSensorMenuItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                showSensorItemStateChangedListener(evt);
            }
        });
        ViewMenu.add(showSensorMenuItem);

        showTargetMenuItem.setSelected(true);
        showTargetMenuItem.setText("Show Target");
        showTargetMenuItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                showTargetItemStateChangedListener(evt);
            }
        });
        ViewMenu.add(showTargetMenuItem);

        showRobotMenuItem.setSelected(true);
        showRobotMenuItem.setText("Show Robot");
        showRobotMenuItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                showRobotItemStateChangedListener(evt);
            }
        });
        ViewMenu.add(showRobotMenuItem);

        showSensorCorverMenuItem.setSelected(true);
        showSensorCorverMenuItem.setText("Show Sensor Corver");
        showSensorCorverMenuItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                showSensorCorverItemStateChanged(evt);
            }
        });
        ViewMenu.add(showSensorCorverMenuItem);

        menuBar.add(ViewMenu);

        AlgorithmMenu.setText("Algorithm");

        MyAlgorithmMenuItem.setText("MyAlgorithm");
        MyAlgorithmMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MyAlgorithmMenuItemActionPerformed(evt);
            }
        });
        AlgorithmMenu.add(MyAlgorithmMenuItem);

        Algorithm1MenuItem.setText("Algorithm 1");
        Algorithm1MenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Algorithm1MenuItemActionPerformed(evt);
            }
        });
        AlgorithmMenu.add(Algorithm1MenuItem);

        MyAlgorithm2MenuItem.setText("MyAlgorithm 2");
        MyAlgorithm2MenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MyAlgorithm2MenuItemActionPerformed(evt);
            }
        });
        AlgorithmMenu.add(MyAlgorithm2MenuItem);

        Algorithm2.setText("Algorithm2");
        Algorithm2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Algorithm2ActionPerformed(evt);
            }
        });
        AlgorithmMenu.add(Algorithm2);

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
            .addGap(0, 1500, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 826, Short.MAX_VALUE)
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
                if (SensorUtility.numberRow < 300 && SensorUtility.numberColum < 300) {
                    coordinatePanel.setPanelScreenReSize(1240, 1240, SensorUtility.numberRow, SensorUtility.numberColum);
                } else if (SensorUtility.numberRow <= 400 && SensorUtility.numberColum <= 400) {
                    if (SensorUtility.numberRow > SensorUtility.numberColum) {
                        int tempScreen = SensorUtility.numberRow * 4 + SensorUtility.marginPanel * 2;
                        coordinatePanel.setPanelScreenReSize(tempScreen, tempScreen, SensorUtility.numberRow, SensorUtility.numberColum);
                    } else {
                        int tempScreen = SensorUtility.numberColum * 4 + SensorUtility.marginPanel * 2;
                        coordinatePanel.setPanelScreenReSize(tempScreen, tempScreen, SensorUtility.numberRow, SensorUtility.numberColum);
                    }
                } else {
                    if (SensorUtility.numberRow > SensorUtility.numberColum) {
                        int tempScreen = SensorUtility.numberRow * 3 + SensorUtility.marginPanel * 2;
                        coordinatePanel.setPanelScreenReSize(tempScreen, tempScreen, SensorUtility.numberRow, SensorUtility.numberColum);
                    } else {
                        int tempScreen = SensorUtility.numberColum * 3 + SensorUtility.marginPanel * 2;
                        coordinatePanel.setPanelScreenReSize(tempScreen, tempScreen, SensorUtility.numberRow, SensorUtility.numberColum);
                    }
                }
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
                SensorUtility.captureScreen(coordinatePanel ,coordinatePanel.sizeWidthCoordinater+SensorUtility.marginPanel,coordinatePanel.sizeHeightCoordianter+SensorUtility.marginPanel,chooser.getSelectedFile()+".png");
            } catch (IOException ex) {
                Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("No Selection ");
        }
    }//GEN-LAST:event_captureScreenMenuItemActionPerformed

    private void showGridItemStateChangedListener(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_showGridItemStateChangedListener
        // TODO add your handling code here:
        int state = evt.getStateChange();
        if (state == ItemEvent.SELECTED) {
            coordinatePanel.setShowGrid(true);
        } else {
            coordinatePanel.setShowGrid(false);
        }
        coordinatePanel.refresh();
    }//GEN-LAST:event_showGridItemStateChangedListener

    private void showSensorItemStateChangedListener(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_showSensorItemStateChangedListener
        // TODO add your handling code here:
        int state = evt.getStateChange();
        if (state == ItemEvent.SELECTED) {
            coordinatePanel.setShowSensor(true);
        } else {
            coordinatePanel.setShowSensor(false);
        }
        coordinatePanel.refresh();
    }//GEN-LAST:event_showSensorItemStateChangedListener

    private void showTargetItemStateChangedListener(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_showTargetItemStateChangedListener
        // TODO add your handling code here:
        int state = evt.getStateChange();
        if (state == ItemEvent.SELECTED) {
            coordinatePanel.setShowTarget(true);
        } else {
            coordinatePanel.setShowTarget(false);
        }
        coordinatePanel.refresh();
    }//GEN-LAST:event_showTargetItemStateChangedListener

    private void showRobotItemStateChangedListener(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_showRobotItemStateChangedListener
        // TODO add your handling code here:
        int state = evt.getStateChange();
        if (state == ItemEvent.SELECTED) {
            coordinatePanel.setShowRobot(true);
        } else {
            coordinatePanel.setShowRobot(false);
        }
        coordinatePanel.refresh();
    }//GEN-LAST:event_showRobotItemStateChangedListener

    private void MyAlgorithmMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MyAlgorithmMenuItemActionPerformed
        // TODO add your handling code here:
//        if (SensorUtility.mListTargetNodes.isEmpty()) {
//            JOptionPane.showMessageDialog(null, "Insert target nodes");
//        } else if (SensorUtility.mListRobotNodes.isEmpty()) {
//            JOptionPane.showMessageDialog(null, "Insert robot node");
//        } else {
//            if (myAlgorithm == null) {
//                myAlgorithm = new MyAlgorithm();
//            }
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    myAlgorithm.run();
//                    coordinatePanel.refresh();
//                }
//            });
//            thread.start();
//        }
        frameMyAlgorithm mFrameMyAlgorithm = new frameMyAlgorithm();
        mFrameMyAlgorithm.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mFrameMyAlgorithm.setVisible(true);
    }//GEN-LAST:event_MyAlgorithmMenuItemActionPerformed

    private void Algorithm1MenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Algorithm1MenuItemActionPerformed
        // TODO add your handling code here:
//        if (SensorUtility.mListTargetNodes.isEmpty()) {
//            JOptionPane.showMessageDialog(null, "Insert target nodes!");
//        } else if (SensorUtility.mListRobotNodes.isEmpty()) {
//            JOptionPane.showMessageDialog(null, "Insert Robot node !");
//        } else {
//            if (algorithm1 == null) {
//                algorithm1 = new Algorithm1();
//            }
//
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    algorithm1.run();
//                    coordinatePanel.refresh();
//                }
//            });
//            thread.start();
//   
//        }
        frameAlgorithm1 mFrameAlgorithm1 = new frameAlgorithm1();
        mFrameAlgorithm1.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mFrameAlgorithm1.setVisible(true);
    }//GEN-LAST:event_Algorithm1MenuItemActionPerformed

    private void MyAlgorithm2MenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MyAlgorithm2MenuItemActionPerformed
        // TODO add your handling code here:
        frameMyAlgorithm2 mFrameAlgorithm = new frameMyAlgorithm2();
        mFrameAlgorithm.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mFrameAlgorithm.setVisible(true);
        
    }//GEN-LAST:event_MyAlgorithm2MenuItemActionPerformed

    private void Algorithm2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Algorithm2ActionPerformed
        // TODO add your handling code here:
        frameAlgorithm2 mFrameAlgorithm = new frameAlgorithm2();
        mFrameAlgorithm.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mFrameAlgorithm.setVisible(true);
    }//GEN-LAST:event_Algorithm2ActionPerformed

    private void showSensorCorverItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_showSensorCorverItemStateChanged
        // TODO add your handling code here:
        int state = evt.getStateChange();
        if (state == ItemEvent.SELECTED) {
            coordinatePanel.setShowCorver(true);
        } else {
            coordinatePanel.setShowCorver(false);
        }
        coordinatePanel.refresh();
    }//GEN-LAST:event_showSensorCorverItemStateChanged

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
    private javax.swing.JMenuItem Algorithm2;
    private javax.swing.JMenu AlgorithmMenu;
    private javax.swing.JMenuItem CalculateDistanceMenuItem;
    private javax.swing.JMenuItem DeleteAllRobotMenuItem;
    private javax.swing.JMenuItem DeleteAllSenorMenuItem;
    private javax.swing.JMenuItem DeleteAllTargetMenuItem;
    private javax.swing.JMenuItem ModifiySensorMenuItem;
    private javax.swing.JMenuItem ModifyRobotMenuItem;
    private javax.swing.JMenuItem ModifyTargetMenuItem;
    private javax.swing.JMenuItem MyAlgorithm2MenuItem;
    private javax.swing.JMenuItem MyAlgorithmMenuItem;
    private javax.swing.JMenuItem NetworkSizeMenuItem;
    private javax.swing.JMenu RobotMenu;
    private javax.swing.JMenuItem SaveFileMenuItem;
    private javax.swing.JMenu SensorMenu;
    private javax.swing.JMenu TargetMenu;
    private javax.swing.JMenu ViewMenu;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem captureScreenMenuItem;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem importFileMenuItem;
    private javax.swing.JMenu inputDataMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JCheckBoxMenuItem showGridMenuItem;
    private javax.swing.JCheckBoxMenuItem showRobotMenuItem;
    private javax.swing.JCheckBoxMenuItem showSensorCorverMenuItem;
    private javax.swing.JCheckBoxMenuItem showSensorMenuItem;
    private javax.swing.JCheckBoxMenuItem showTargetMenuItem;
    // End of variables declaration//GEN-END:variables
    public static frameCoordinateSystemPanel coordinatePanel;
}

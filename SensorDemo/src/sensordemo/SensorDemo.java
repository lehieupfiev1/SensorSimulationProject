/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensordemo;

import common.SensorUtility;
import iterface.frameMain;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Hieu
 */
public class SensorDemo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        frameMain mFrameMain = new frameMain();
        // mFrameMain.setState(JFrame.MAXIMIZED_BOTH);
        mFrameMain.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mFrameMain.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mFrameMain.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (SensorUtility.mListNodes.size() > 0) {
                    int confirmed = JOptionPane.showConfirmDialog(null,
                            "Do you want to save input data before exit?", "Exit Program Message Box",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmed == JOptionPane.YES_OPTION) {
                        mFrameMain.saveFile();
                        System.exit(0);
                    } else if (confirmed == JOptionPane.NO_OPTION) {
                        mFrameMain.dispose();
                    }
                } else {
                    mFrameMain.dispose();
                }
            }
            
        });
//                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();  
//                mFrameMain.setMaximizedBounds(env.getMaximumWindowBounds());  
        mFrameMain.setVisible(true);
        mFrameMain.setTitle("Sensor Simulation");
    }
    
}

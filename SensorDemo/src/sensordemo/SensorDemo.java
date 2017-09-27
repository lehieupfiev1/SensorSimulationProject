/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensordemo;

import iterface.frameMain;
import javax.swing.JFrame;

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
//                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();  
//                mFrameMain.setMaximizedBounds(env.getMaximumWindowBounds());  
        mFrameMain.setVisible(true);
        mFrameMain.setTitle("Sensor Simulation");
    }
    
}

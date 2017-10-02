/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iterface;

import common.SensorUtility;
import static common.SensorUtility.mListNodes;
import static common.SensorUtility.mListRobotNodes;
import static common.SensorUtility.mListSensorNodes;
import static common.SensorUtility.mListTargetNodes;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.NodeItem;

/**
 *
 * @author Hieu
 */
public class frameCoordinateSystemPanel extends JPanel{
    int sizeRect;
    int sizeWidthPanel;
    int sizeHeightPanel;
    int sizeWidthCoordinater;
    int sizeHeightCoordianter;
    static boolean isShowGrid = true;
    static boolean isShowSensor = true;
    static boolean isShowTarget = true;
    static boolean isShowRobot = true;


    public frameCoordinateSystemPanel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public frameCoordinateSystemPanel(int withScreen, int heightScreen) {
       sizeWidthPanel = withScreen-SensorUtility.marginPanel*2;
       sizeHeightPanel = heightScreen -SensorUtility.marginPanel*2;
       setCoordinateSize(SensorUtility.numberRow,SensorUtility.numberColum);
       this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int x = e.getX()-SensorUtility.marginPanel;
                int y = e.getY()-SensorUtility.marginPanel;
                int cellX = x / sizeRect;
                int cellY = y / sizeRect;
                if (cellX < SensorUtility.numberRow && cellX >= 0 && cellY < SensorUtility.numberColum && cellY >=0 )
                   JOptionPane.showMessageDialog(null, "X ="+cellX +" Y =" +cellY );
                //System.out.println("Clicked! X=" +cellX +" Y =" +cellY);
            }
        });
    }
    
    public void setPanelScreenReSize(int withScreen, int heightScreen,int row,int colum) {
       sizeWidthPanel = withScreen-SensorUtility.marginPanel*2;
       sizeHeightPanel = heightScreen -SensorUtility.marginPanel*2;
       setCoordinateSize(row,colum);
    }
    
    public void setCoordinateSize(int  nRow,int nColum) {
        SensorUtility.numberColum = nColum;
        SensorUtility.numberRow = nRow;
        if (sizeWidthPanel/(SensorUtility.numberRow) > sizeHeightPanel/(SensorUtility.numberColum)) {
            sizeRect = (int) sizeHeightPanel/(SensorUtility.numberColum);
        } else {
            sizeRect = (int) sizeWidthPanel/(SensorUtility.numberRow);
        }
        sizeWidthCoordinater = (SensorUtility.numberRow)*sizeRect+SensorUtility.marginPanel;
        sizeHeightCoordianter = (SensorUtility.numberColum)*sizeRect+SensorUtility.marginPanel;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.

        //Draw list nodes
        System.err.println("with"+sizeWidthPanel +" height="+sizeHeightPanel + " sizeRect="+sizeRect+ " sizeWidthCoordinater="+sizeWidthCoordinater+"sizeHeightCoordianter ="+sizeHeightCoordianter);
        for (Iterator<NodeItem> iterator = mListNodes.iterator(); iterator.hasNext();) {
            //Draw node
            NodeItem next = iterator.next();
            int cellX = SensorUtility.marginPanel + (next.getX() * sizeRect);
            int cellY = SensorUtility.marginPanel + (next.getY() * sizeRect);
            if (next.getType() == 0) {
                //target Node
                if (isShowTarget) {
                    g.setColor(Color.ORANGE);
                } else {
                    g.setColor(new Color(0, 0, 0, 0));
                }
                
                g.fillRect(cellX, cellY, sizeRect, sizeRect);

            } else if (next.getType() == 1) {
                // Robot node
                if (isShowRobot) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(new Color(0, 0, 0, 0));
                }
                g.drawOval(cellX, cellY, sizeRect, sizeRect);
            } else if (next.getType() == 2) {
                // Sensor node
                if (isShowSensor) {
                    g.setColor(Color.BLUE);
                } else {
                    g.setColor(new Color(0, 0, 0, 0));
                }
                g.fillArc(cellX - sizeRect, cellY - sizeRect, sizeRect * 3, sizeRect * 2, 244, 53);
            }

        }
        //Draw grid 
        if (isShowGrid) {
            g.setColor(new Color(0, 0, 0, 40));
        } else {
            g.setColor(new Color(0, 0, 0, 0));
        }

        for (int i = SensorUtility.marginPanel; i <= sizeWidthCoordinater; i += sizeRect) {
            g.drawLine(i, SensorUtility.marginPanel, i, sizeHeightCoordianter);
        }

        for (int i = SensorUtility.marginPanel; i <= sizeHeightCoordianter; i += sizeRect) {
            g.drawLine(SensorUtility.marginPanel, i, sizeWidthCoordinater, i);
        }
        // Draw khung toa do
        g.setColor(Color.BLACK);
        g.drawRect(SensorUtility.marginPanel, SensorUtility.marginPanel, sizeWidthCoordinater-SensorUtility.marginPanel, sizeHeightCoordianter-SensorUtility.marginPanel);

        
    }
    public void fillCell(int x, int y, int type) {
        mListNodes.add(new NodeItem(x, y, type));
        repaint();
    }
    
    public void setShowSensor(boolean view) {
        isShowSensor = view;
        refresh();
    }
    public void setShowTarget(boolean view) {
        isShowTarget = view;
        refresh();
    }
    public void setShowRobot(boolean view) {
        isShowRobot = view;
        refresh();
    }
    public void setShowGrid(boolean view) {
        isShowGrid = view;
        refresh();
    }
    
    public void deleteArraryList(){
        mListNodes.clear();
        repaint();
    }
    public void refresh() {
        mListNodes.clear();
        //Add target nodes
        for (Iterator<NodeItem> iterator = mListTargetNodes.iterator(); iterator.hasNext();) {
            NodeItem next = iterator.next();
            mListNodes.add(next);
        }
        //Add robot nodes
        for (Iterator<List<NodeItem>> iterator = mListRobotNodes.iterator(); iterator.hasNext();) {
            List<NodeItem> next = iterator.next();
            for (int i =0;i < next.size();i++) {
               mListNodes.add(next.get(i));
            }
        }
        
        //Add sensor nodes
        for (Iterator<NodeItem> iterator = mListSensorNodes.iterator(); iterator.hasNext();) {
            NodeItem next = iterator.next();
            mListNodes.add(next);
        }
        repaint();
    }
    
    
}

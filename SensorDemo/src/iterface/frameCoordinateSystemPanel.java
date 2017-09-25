/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iterface;

import common.SensorUtility;
import static common.SensorUtility.mListNodes;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
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
       sizeWidthPanel = withScreen;
       sizeHeightPanel = heightScreen -100;
       setCoordinateSize(SensorUtility.numberRow,SensorUtility.numberColum);
       this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int cellX = x / sizeRect - 1;
                int cellY = y / sizeRect - 1;
                if (cellX < SensorUtility.numberRow && cellX >= 0 && cellY < SensorUtility.numberColum && cellY >=0 )
                   JOptionPane.showMessageDialog(null, "X ="+cellX +" Y =" +cellY );
                //System.out.println("Clicked! X=" +cellX +" Y =" +cellY);
            }
        });
    }
    
    public void setCoordinateSize(int  nRow,int nColum) {
        SensorUtility.numberColum = nColum;
        SensorUtility.numberRow = nRow;
        if (sizeWidthPanel/(SensorUtility.numberRow+2) > sizeHeightPanel/(SensorUtility.numberColum+2)) {
            sizeRect = (int) sizeHeightPanel/(SensorUtility.numberColum+2);
        } else {
            sizeRect = (int) sizeWidthPanel/(SensorUtility.numberRow+2);
        }
        sizeWidthCoordinater = (SensorUtility.numberRow)*sizeRect;
        sizeHeightCoordianter = (SensorUtility.numberColum)*sizeRect;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.

        //Draw list nodes
        for (Iterator<NodeItem> iterator = mListNodes.iterator(); iterator.hasNext();) {
            //Draw grid 
            if (isShowGrid) {
               g.setColor(new Color(0, 0, 0, 40));
            } else {
               g.setColor(new Color(0, 0, 0, 0));
            }
            
            for (int i = sizeRect; i <= sizeWidthCoordinater; i += sizeRect) {
                g.drawLine(i, sizeRect, i, sizeHeightCoordianter + sizeRect);
            }

            for (int i = sizeRect; i <= sizeHeightCoordianter; i += sizeRect) {
                g.drawLine(sizeRect, i, sizeWidthCoordinater + sizeRect, i);
            }
            // Draw khung toa do
            g.setColor(Color.BLACK);
            g.drawRect(sizeRect, sizeRect, sizeWidthCoordinater, sizeHeightCoordianter);

            //Draw node
            NodeItem next = iterator.next();
            int cellX = sizeRect + (next.getX() * sizeRect);
            int cellY = sizeRect + (next.getY() * sizeRect);
            if (next.getType() == 0) {
                //target Node
                if (isShowTarget) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(new Color(0, 0, 0, 0));
                }
                
                g.fillRect(cellX, cellY, sizeRect, sizeRect);

            } else if (next.getType() == 1) {
                // Robot node
                if (isShowRobot) {
                    g.setColor(Color.BLUE);
                } else {
                    g.setColor(new Color(0, 0, 0, 0));
                }
                g.fillOval(cellX, cellY, sizeRect, sizeRect);
            } else if (next.getType() == 2) {
                // Sensor node
                if (isShowSensor) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(new Color(0, 0, 0, 0));
                }
                g.fillArc(cellX - sizeRect, cellY - sizeRect, sizeRect * 3, sizeRect * 2, 244, 53);
            }

        }

        
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
        repaint();
    }
    
    
}

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
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
    static boolean isShowCorver = true;
    JPopupMenu popup;
    JMenuItem deleteSensorItem;
    JMenuItem deleteTargetItem;
    int pointX;
    int pointY;


    public frameCoordinateSystemPanel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public frameCoordinateSystemPanel(int withScreen, int heightScreen) {
       sizeWidthPanel = withScreen-SensorUtility.marginPanel*2;
       sizeHeightPanel = heightScreen -SensorUtility.marginPanel*2;
       setCoordinateSize(SensorUtility.numberRow,SensorUtility.numberColum);
       initPopupMenu();
       this.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               if (SwingUtilities.isLeftMouseButton(e)) {
                int x = e.getX()-SensorUtility.marginPanel;
                int y = e.getY()-SensorUtility.marginPanel;
                int cellX = x / sizeRect;
                int cellY = y / sizeRect;
                if (cellX < SensorUtility.numberRow && cellX >= 0 && cellY < SensorUtility.numberColum && cellY >=0 )
                   JOptionPane.showMessageDialog(null, "X ="+cellX +" Y =" +cellY );
                //System.out.println("Clicked! X=" +cellX +" Y =" +cellY);
               }
           }
           
           @Override
           public void mousePressed(MouseEvent e) {
               int x = e.getX() - SensorUtility.marginPanel;
               int y = e.getY() - SensorUtility.marginPanel;
               int cellX = x / sizeRect;
               int cellY = y / sizeRect;
               if (checkPointExit(cellX, cellY) == 3) {
                   deleteSensorItem.setEnabled(true);
                   deleteTargetItem.setEnabled(true);
               } else if (checkPointExit(cellX, cellY) == 1) {
                   deleteSensorItem.setEnabled(true);
                   deleteTargetItem.setEnabled(false);
               } else if (checkPointExit(cellX, cellY) == 2) {
                   deleteSensorItem.setEnabled(false);
                   deleteTargetItem.setEnabled(true);
               } else {
                   deleteSensorItem.setEnabled(false);
                   deleteTargetItem.setEnabled(false);
               }
               showPopup(e);
           }
 
            @Override
            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }
 
            private void showPopup(MouseEvent e) {
                int x = e.getX()-SensorUtility.marginPanel;
                int y = e.getY()-SensorUtility.marginPanel;
                pointX = x / sizeRect;
                pointY = y / sizeRect;
                if (e.isPopupTrigger() && pointX < SensorUtility.numberRow && pointX >= 0 && pointY < SensorUtility.numberColum && pointY >=0 ) {
                    popup.show(e.getComponent(),
                            e.getX(), e.getY());
                }
            }
       });
//       this.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                int x = e.getX()-SensorUtility.marginPanel;
//                int y = e.getY()-SensorUtility.marginPanel;
//                int cellX = x / sizeRect;
//                int cellY = y / sizeRect;
//                if (cellX < SensorUtility.numberRow && cellX >= 0 && cellY < SensorUtility.numberColum && cellY >=0 )
//                   JOptionPane.showMessageDialog(null, "X ="+cellX +" Y =" +cellY );
//                //System.out.println("Clicked! X=" +cellX +" Y =" +cellY);
//            }
//        });
    }
     void initPopupMenu() {
        popup = new JPopupMenu();
        // New sensor
        JMenuItem menuItem = new JMenuItem("Add sensor",
                new ImageIcon(getClass().getResource("/resource/circle_icon.png")));
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription(
                "New Sensor");
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(frame, "New Project clicked!");
                SensorUtility.mListSensorNodes.add(new NodeItem(pointX, pointY, 2));
                refresh();
            }
        });
        popup.add(menuItem);

        // New target
        menuItem = new JMenuItem("Add target",
                new ImageIcon(getClass().getResource("/resource/square_icon.png")));
        menuItem.setMnemonic(KeyEvent.VK_F);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(frame, "New File clicked!");
                SensorUtility.mListTargetNodes.add(new NodeItem(pointX, pointY, 0));
                refresh();
            }
        });
        popup.add(menuItem);

        //Refresg button
        menuItem = new JMenuItem("Refresh");
        menuItem.setMnemonic(KeyEvent.VK_F);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(frame, "New File clicked!");
                refresh();
            }
        });
        popup.add(menuItem);
        
        //DeleteSensor
        deleteSensorItem = new JMenuItem("Delete Sensor");
        deleteSensorItem.setMnemonic(KeyEvent.VK_F);
        deleteSensorItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(frame, "New File clicked!");
                for (int i =0; i< mListSensorNodes.size();) {
                    NodeItem next = mListSensorNodes.get(i);
                    if (next.getX() == pointX && next.getY() == pointY) {
                        mListSensorNodes.remove(i);
                        continue;
                    }
                    i++;
                }

                refresh();
            }
        });
        popup.add(deleteSensorItem);
        
        //Delete Target
        deleteTargetItem = new JMenuItem("Delete Target");
        deleteTargetItem.setMnemonic(KeyEvent.VK_F);
        deleteTargetItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(frame, "New File clicked!");
                for (int i =0; i< mListTargetNodes.size();) {
                    NodeItem next = mListTargetNodes.get(i);
                    if (next.getX() == pointX && next.getY() == pointY) {
                        mListTargetNodes.remove(i);
                        continue;
                    }
                    i++;
                }

                refresh();
            }
        });
        popup.add(deleteTargetItem);
    }
   
   int checkPointExit(int cellX, int cellY) {
        boolean hasSensor = false;
        boolean hasTarget = false;
        for (Iterator<NodeItem> iterator = mListSensorNodes.iterator(); iterator.hasNext();) {
            NodeItem next = iterator.next();
            if (next.getX() == cellX && next.getY() == cellY) {
                hasSensor = true;
                break;
            }
        }
        for (Iterator<NodeItem> iterator = mListTargetNodes.iterator(); iterator.hasNext();) {
            NodeItem next = iterator.next();
            if (next.getX() == cellX && next.getY() == cellY) {
                hasTarget = true;
                break;
            }
        }
        if (hasSensor && hasTarget) {
            return 3;
        } else if (hasSensor) {
            return 1;
        } else if (hasTarget) {
            return 2;
        }
        return 0;
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
        
        for (Iterator<NodeItem> iterator = mListNodes.iterator(); iterator.hasNext();) {
            //Draw node
            NodeItem next = iterator.next();
            int cellX = SensorUtility.marginPanel + (next.getX() * sizeRect);
            int cellY = SensorUtility.marginPanel + (next.getY() * sizeRect);
            if (next.getType() == 0) {
                //target Node
                if (isShowTarget) {
                    g.setColor(Color.BLUE);
                } else {
                    g.setColor(new Color(0, 0, 0, 0));
                }
                
                g.fillRect(cellX, cellY, sizeRect, sizeRect);

            } else if (next.getType() == 1) {
                // Robot node
                if (isShowRobot) {
                    g.setColor(SensorUtility.mListColor.get(next.getGroup()));
                } else {
                    g.setColor(new Color(0, 0, 0, 0));
                }
                g.fillArc(cellX - sizeRect, cellY - sizeRect, sizeRect * 3, sizeRect * 2, 244, 53);

            } else if (next.getType() == 2) {
                // Sensor node
                if (isShowSensor) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(new Color(0, 0, 0, 0));
                }
                if (next.getStatus() == 1) {
                    //Sensor turn on
                    g.fillOval(cellX, cellY, sizeRect, sizeRect);
                    if (isShowCorver) {
                       g.setColor(Color.DARK_GRAY);
                    } else {
                       g.setColor(new Color(0, 0, 0, 0));
                    }
                     g.drawOval(cellX -(int)(sizeRect*(SensorUtility.mRsValue-0.5)), cellY-(int)(sizeRect*(SensorUtility.mRsValue-0.5)), (int)(2*sizeRect*SensorUtility.mRsValue),(int)(2*sizeRect*SensorUtility.mRsValue));
                } else {
                    g.drawOval(cellX, cellY, sizeRect, sizeRect);
                }
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
    
    public void setShowCorver(boolean view) {
        isShowCorver = view;
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

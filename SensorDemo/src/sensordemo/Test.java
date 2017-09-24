/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensordemo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Test extends JPanel
{
    public Test()
    {
        JLabel label = new JLabel( "First Name:" );
        add( label );

        JTextField textField = new JTextField(15);
        add( textField );

        MouseListener ml = new MouseAdapter()
        {
            public void mouseReleased(MouseEvent e)
            {
                JComponent component = (JComponent)e.getSource();
                component.setToolTipText("Mouse released on: " + component.getClass().toString());

                MouseEvent phantom = new MouseEvent(
                    component,
                    MouseEvent.MOUSE_MOVED,
                    System.currentTimeMillis(),
                    0,
                    0,
                    0,
                    0,
                    false);

                ToolTipManager.sharedInstance().mouseMoved(phantom);
            }
        };

        label.addMouseListener( ml );
        textField.addMouseListener( ml );
    }

    private static void createAndShowUI()
    {
        JFrame frame = new JFrame("ToolTipOnRelease");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add( new Test() );
        frame.pack();
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
    }

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowUI();
            }
        });
    }
}

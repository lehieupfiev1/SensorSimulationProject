/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.util.ArrayList;
import java.util.List;
import model.NodeItem;

/**
 *
 * @author Hieu
 */
public final class SensorUtility {
    public static int numberColum =200;
    public static int numberRow = 200;
    public static float mRsValue = 2.0f;
    public static float mRtValue = 3.0f;
    public static List<NodeItem> mListNodes = new ArrayList<>(300000);
    public static List<NodeItem> mListSensorNodes = new ArrayList<>(300000);
    public static List<NodeItem> mListTargetNodes = new ArrayList<>(30000);
    public static List<NodeItem> mListRobotNodes = new ArrayList<>(30000);

}

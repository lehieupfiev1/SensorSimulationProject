/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
import model.NodeItem;

/**
 *
 * @author Hieu
 */
public final class SensorUtility {
    public static int MaxColum =1001;
    public static int MaxRow =1001;
    public static int numberColum =200;
    public static int marginPanel =20;
    public static int numberRow = 200;
    public static float mRsValue = 4.0f;
    public static float mRtValue = 4.0f;
    public static int mNumberRobotCycle = 0;
    public static List<NodeItem> mListNodes = new ArrayList<>();
    public static List<NodeItem> mListSensorNodes = new ArrayList<>();
    public static List<NodeItem> mListTargetNodes = new ArrayList<>();
    public static List<NodeItem> mListSinkNodes = new ArrayList<>();
    public static List<List<NodeItem>> mListRobotNodes = new ArrayList<List<NodeItem>>();
    public static List<List<NodeItem>> mListofListSensor = new ArrayList<List<NodeItem>>();
    public static List<Double> mListofListTime = new ArrayList<>();
    public static List<Color> mListColor = new ArrayList<>();
    public static int MaxTargetNodes = 30000;
    public static int MaxCycleRobots = 1000;
    public static int MaxRobotNodesCycle = 1000;
    public static float MAX_DISTANCE = 1000000000000.0f;
    
    public static int Lvalue = 2;
    public static int LifeTimeOfSensor = 3000;
    public static float mSaiso = 0.001f;
      
    
    public static void writeFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) file.createNewFile();
        BufferedWriter outputWriter;

        outputWriter = new BufferedWriter(new FileWriter(file, false));
        //Write Width and height , Rs, Rt
        outputWriter.write(numberColum + " "+ numberRow+" "+mRsValue+ " "+mRtValue);
        outputWriter.newLine();
        //Write number Target, number Robot Cycle, number Sensor
        outputWriter.write(mListTargetNodes.size() + " "+ mListRobotNodes.size()+" "+mListSensorNodes.size()+ " "+ mListSinkNodes.size());
        outputWriter.newLine();
        //Write List point Target
        for (int i = 0; i < mListTargetNodes.size(); i++) {
            // Maybe:
            outputWriter.write(mListTargetNodes.get(i).getX() + " "+ mListTargetNodes.get(i).getY()+" ");
        }
        outputWriter.newLine();
        //Write Robot Cycle
        for (int i =0;i < mListRobotNodes.size();i++){
            List<NodeItem> temp = mListRobotNodes.get(i);
            outputWriter.write(temp.size()+"");
            outputWriter.newLine();
            for (int j =0;j < temp.size();j++) {
               outputWriter.write(temp.get(j).getX()+" "+temp.get(j).getY()+" "+temp.get(j).getGroup()+" ");
            }
            outputWriter.newLine();
            
        }
        //Write List Sensor 
        for (int i = 0; i < mListSensorNodes.size(); i++) {
            // Maybe:
            outputWriter.write(mListSensorNodes.get(i).getX() + " " + mListSensorNodes.get(i).getY() + " ");
        }
        outputWriter.newLine();
        
        //Write List Sink 
        for (int i = 0; i < mListSinkNodes.size(); i++) {
            // Maybe:
            outputWriter.write(mListSinkNodes.get(i).getX() + " " + mListSinkNodes.get(i).getY() + " ");
        }
        outputWriter.newLine();
        

        outputWriter.flush();
        outputWriter.close();

    }
    public static void readFile(String filename) throws IOException {
        mListSensorNodes.clear();
        mListRobotNodes.clear();
        mListTargetNodes.clear();
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        //Read width , height, RsValue, mRtValue
        numberColum = scanner.nextInt();
        numberRow = scanner.nextInt();
        mRsValue = scanner.nextFloat();
        mRtValue = scanner.nextFloat();

        ////Read number Target, number Robot Cycle, number Sensor
        int sizeTarget = scanner.nextInt();
        int sizeRobotCycle = scanner.nextInt();
        int sizeSensor = scanner.nextInt();
        int sizeSink = scanner.nextInt();
        // Read Target
        for (int i = 0; i < sizeTarget; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            mListTargetNodes.add(new NodeItem(x, y, 0));
        }
        //Read Robot Cycle
        for (int i = 0; i < sizeRobotCycle; i++) {
            mListRobotNodes.add(new ArrayList<>());
            int sizeRobot = scanner.nextInt();
            for (int j = 0; j < sizeRobot; j++) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int group = scanner.nextInt();
                mListRobotNodes.get(i).add(new NodeItem(x, y, 1, group));
            }
        }

        //Read Sensor
        for (int i = 0; i < sizeSensor; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            mListSensorNodes.add(new NodeItem(x, y, 2));
        }
        
        //Read Sink
        for (int i = 0; i < sizeSink; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            mListSinkNodes.add(new NodeItem(x, y, 3));
        }

    }
    
    public static void captureScreen(Component component,int width, int height,String filename) throws IOException {
        BufferedImage img = getScreenShot(component,width,height);
        // write the captured image as a PNG
        ImageIO.write(img, "png", new File(filename));
    }
    
    public static void captureScreen(Component component,String filename) throws IOException {
        BufferedImage img = getScreenShot(component,component.getWidth(),component.getHeight());
        // write the captured image as a PNG
        ImageIO.write(img, "png", new File(filename));
    }

    public static BufferedImage getScreenShot(Component component ,int width, int height) {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // paints into image's Graphics
        component.paint(image.getGraphics());
        return image;
    }
    
    public static int castInt(float value) {
        int result = (int)value;
        if (result +0.5 < value ){
           return result+1;   
        }
        return result;
    }
        
    public static void resetSetting() {
        numberColum =200;
        numberRow = 200;
        mRsValue = 2.0f;
        mRtValue = 3.0f;
        mNumberRobotCycle = 0;
        mListNodes.clear();
        mListSensorNodes.clear();
        mListRobotNodes.clear();
        mListTargetNodes.clear();
    }
    
    public static int giaithua(int N) {
        int result = 1;
        for (int i = 1;i <= N; i++) {
            result = result*i;
        }
        return result;
    }
    
    public static int tohop(int N, int K) {
        if (K == 0 || K == N) {
            return 1;
        } else {
            return (giaithua(N)/(giaithua(N-K)*giaithua(K)));
        }
    }


}

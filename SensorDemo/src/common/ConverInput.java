/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.NodeItem;

/**
 *
 * @author Hieu
 */
public class ConverInput {
    static int numberColum =1000; // Kich thuoc mang (theo cot)
    static int numberRow = 1000;// Kich thuoc mang (theo cot)
    static float mRsValue = 20.0f;//(bán kính sensing)	
    static float mRtValue = 40.0f;
    static int mNumberRobotCycle = 0;
    static List<NodeItem> mListNodes = new ArrayList<>();
    static List<NodeItem> mListSensorNodes = new ArrayList<>();
    static List<NodeItem> mListTargetNodes = new ArrayList<>();
    static List<List<NodeItem>> mListRobotNodes = new ArrayList<List<NodeItem>>();
    static List<List<NodeItem>> mListofListSensor = new ArrayList<List<NodeItem>>();
    static List<NodeItem> mListSinkNodes = new ArrayList<>();
    
    //Cac tham so khac
    
    static float mRcValue = 40f; //(bán kính truyền tin)
    static float mEsValue = 100.0f;//Sensing Energy đối với 1 bit
    static float mErValue = 100.0f;//Receiving Energy đối với 1 bit
    static float mEtValue = 50.0f; //Tranmitting Energy  
    static float mEfsValue = 0.01f;
    static float mEmpValue = 0.0000013f;
    static float mBitValue = 16.0f; //Tần suât sinh gói tin (bit/s)
    static float mTstamp = 1.0f;
    static int mMaxHopper = 5; // Giá trị lớn nhất của hopper count
    static float mEoValue = 20000000000.0f;//Năng lượng sensor
    
    
     public static void main(String[] args) {
         
        try {
            readSensorFile("D:\\sensor.txt"); //Add URL sensor file with format (
            readTargetFile("D:\\target.txt");//Add URL target file
            readSinkFile("D:\\sink.txt");//Add URL Sink file
        } catch (IOException ex) {
            Logger.getLogger(ConverInput.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            writeFile("D:\\inputnew.INP"); //Url luu file input duoc sinh ra
        } catch (IOException ex) {
            Logger.getLogger(ConverInput.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     
     public static void readSensorFile(String filename) throws IOException {
         // Format File  : 
         // So luong sensor 
         // Toa do tung sensor xi yi
        mListSensorNodes.clear();
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        int sizeSensor = scanner.nextInt();
        for (int i = 0; i < sizeSensor; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            mListSensorNodes.add(new NodeItem(x, y, 2));
        }
     }
     public static void readTargetFile(String filename) throws IOException {
        mListTargetNodes.clear();
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        int sizeTarget = scanner.nextInt();
         for (int i = 0; i < sizeTarget; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            mListTargetNodes.add(new NodeItem(x, y, 0));
        }
     }
     public static void readSinkFile(String filename) throws IOException {
        mListSinkNodes.clear();
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        int sizeSink = scanner.nextInt();
        for (int i = 0; i < sizeSink; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            mListSinkNodes.add(new NodeItem(x, y, 3));
        }
     }
     
     
     public static void readFile(String filename) throws IOException {
        mListSensorNodes.clear();
        mListRobotNodes.clear();
        mListTargetNodes.clear();
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        //Read width , height, RsValue, mRtValue
//        numberColum = scanner.nextInt();
//        numberRow = scanner.nextInt();
//        mRsValue = scanner.nextFloat();
//        mRtValue = scanner.nextFloat();
        ////Read number Target, number Robot Cycle, number Sensor
        int sizeTarget = 0;
        int sizeRobotCycle = 0;
        int sizeSensor = 3000;
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
    }
     
     public static void writeFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) file.createNewFile();
        BufferedWriter outputWriter;

        outputWriter = new BufferedWriter(new FileWriter(file, false));
        //Write Width and height , Rs, Rt
        outputWriter.write(numberColum + " "+ numberRow+" "+mRsValue+ " "+mRtValue);
        outputWriter.newLine();
        //Write parameter of energy
        outputWriter.write(mRcValue + " "+ mEsValue+" "+mEtValue+ " "+mErValue+ " "+mEfsValue+" "+mEmpValue+" "+mBitValue+ " "+mMaxHopper);
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
}

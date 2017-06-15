package com.duansky.graph.benchmark.driver;

import com.duansky.graph.benchmark.util.Contract;
import com.duansky.graph.benchmark.util.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by SkyDream on 2017/3/24.
 */
public class SimpleDriver {
    private String inPath;
    private double percent;
    private static Random RND = new Random();

    public static void main(String args[]){
        String inPath = Contract.DATA_FOLDER_GELLY + File.separator + "graph-1234-0.1";
        double percent = 0.01;
        SimpleDriver driver = new SimpleDriver(inPath,percent);
        driver.simple();
        System.out.println("simple done!");
    }

    public SimpleDriver(String path,double percent){
        this.inPath = path;
        this.percent = percent;
    }

    public void simple(){
        BufferedReader reader = Files.asBufferedReader(inPath);
        PrintWriter writer = Files.asPrintWriter(inPath+"-simple");
        String line;
        try {
            while((line = reader.readLine()) != null){
                if(line.startsWith("0,") || RND.nextDouble() < percent) {
                    writer.write(line+"\n");
                    writer.flush();
                }
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

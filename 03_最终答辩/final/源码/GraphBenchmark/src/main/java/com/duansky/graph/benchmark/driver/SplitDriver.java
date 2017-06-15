package com.duansky.graph.benchmark.driver;

import com.duansky.graph.benchmark.util.Contract;
import com.duansky.graph.benchmark.util.Files;

import java.io.*;

/**
 * Created by SkyDream on 2017/3/24.
 */
public class SplitDriver {

    private String inPath;
    private int num;
    private boolean mixMode;

    public SplitDriver(String inPath,int num,boolean mixMode){
        Files.checkNotNull(inPath);
        this.inPath = inPath;
        this.num = num;
        this.mixMode = mixMode;
    }

    public static void main(String args[]){
        String inPath = Contract.DATA_FOLDER_GELLY + File.separator + "graph-1234-0.1";
        int num = 8;
        boolean mixMode = false;
        if(args != null && args.length != 0){
            inPath = Contract.DATA_FOLDER_GELLY + File.separator + args[0];
            num = Integer.parseInt(args[1]);
            mixMode = Boolean.valueOf(args[2]);
        }
        SplitDriver splitDriver = new SplitDriver(inPath,num,mixMode);
        if(splitDriver.mixMode)
            splitDriver.split();
        else splitDriver.split0();
        System.out.println("split done!");
    }

    public void split(){
        PrintWriter[] writers = new PrintWriter[num];
        for(int i = 0; i < num; i++){
            writers[i] = Files.asPrintWriter(inPath+"-"+i);
        }
        int curr = 0;
        BufferedReader reader = Files.asBufferedReader(inPath);
        String line;
        try {
            while((line=reader.readLine())!=null){
                writers[curr].write(line+"\n");
                curr = (curr >= (num - 1) ? 0 : (curr + 1));
                writers[curr].flush();
            }
            for(int i = 0; i < num; i++){
                writers[i].close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void split0(){

        BufferedReader reader = Files.asBufferedReader(inPath);
        int total = Files.getLineNumber(inPath);
        int avg = total / num, currNum = 0, currPart = 0;
        PrintWriter writer = Files.asPrintWriter(inPath+"-"+currPart);
        String line;
        try {
            while((line = reader.readLine()) != null){
                if(currNum < avg || currPart >= num) currNum++;
                else{
                    currNum = 0;
                    currPart++;
                    writer.close();
                    writer = Files.asPrintWriter(inPath+"-"+currPart);
                }
                writer.write(line+"\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

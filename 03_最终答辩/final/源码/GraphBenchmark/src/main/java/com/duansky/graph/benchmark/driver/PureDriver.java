package com.duansky.graph.benchmark.driver;

import com.duansky.graph.benchmark.util.Contract;
import com.duansky.graph.benchmark.util.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by SkyDream on 2017/3/25.
 */
public class PureDriver {
    private String inPath;
    Set<Long> edgeSet = new HashSet<Long>();

    public PureDriver(String inPath){
        this.inPath = inPath;
    }

    public static void main(String[] args) {
        String inPath = Contract.DATA_FOLDER_GELLY + File.separator + "graph-1234-0.1";
        PureDriver driver = new PureDriver(inPath);
        driver.pure();
        System.out.println("pure done!");
    }

    public void pure(){
        BufferedReader reader = Files.asBufferedReader(inPath);
        PrintWriter writer = Files.asPrintWriter(inPath+"-pure");
        String line;
        try {
            while((line=reader.readLine())!=null){
                String data[] = line.split(",");
                int a = Integer.parseInt(data[0]);
                int b = Integer.parseInt(data[1]);
                long hash = hashCode(a,b);
                if(a != b && !edgeSet.contains(hash)){
                    edgeSet.add(hash);
                    writer.write(line+"\n");
                    writer.flush();
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long hashCode(int start,int end){
        if(end < start){
            int tmp = start;
            start = end;
            end = tmp;
        }

        return 5000000l * start + end;
    }
}

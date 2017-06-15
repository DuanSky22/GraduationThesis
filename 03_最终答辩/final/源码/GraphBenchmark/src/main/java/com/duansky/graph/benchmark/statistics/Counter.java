package com.duansky.graph.benchmark.statistics;

import com.duansky.graph.benchmark.util.Contract;
import com.duansky.graph.benchmark.util.Files;
import com.duansky.hazelcast.graphflow.util.Contracts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by SkyDream on 2017/3/20.
 */
public class Counter {

    Map<Integer,Integer> counter = new TreeMap<Integer, Integer>();
    String inPath;
    String outPath;

    public Counter(String inPath,String outPath){
        this.inPath = inPath;
        this.outPath = outPath;
    }

    public static void main(String args[]){
        String in = Contracts.TEST_BASE + File.separator + "pr.txt";
        String out = Contracts.TEST_BASE + File.separator + "f-pr.txt";
        Counter counter = new Counter(in,out);
        counter.count();
        counter.write();
    }

    public void count(){
        BufferedReader reader = Files.asBufferedReader(inPath);
        String line;
        try {
            while((line = reader.readLine())!=null){
                int a = Integer.parseInt(line);
                if(counter.containsKey(a)){
                    counter.put(a,counter.get(a)+1);
                }else
                    counter.put(a,1);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(){
        PrintWriter writer = Files.asPrintWriter(outPath);
        long currSum = 0l;
        for(Map.Entry<Integer,Integer> entry : counter.entrySet()){
            currSum += entry.getValue();
            writer.write(entry.getKey()+"\t"+entry.getValue()+"\t"+currSum+"\n");
        }
        writer.flush();
        writer.close();
    }
}

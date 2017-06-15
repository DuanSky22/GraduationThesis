package com.duansky.graph.benchmark.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by SkyDream on 2017/3/24.
 */
public class VertexConter {

    private String edgePath;
    private Set<Integer> verities = new HashSet<Integer>();

    public VertexConter(String edgePath){
        this.edgePath = edgePath;
    }

    public static void main(String[] args) {
        String edgePath = Contract.DATA_FOLDER_GELLY + File.separator + "graph-1234-0.1";
        VertexConter counter = new VertexConter(edgePath);
        System.out.println(counter.count());

    }

    public int count(){
        BufferedReader reader = Files.asBufferedReader(edgePath);
        String line;
        try {
            while((line=reader.readLine()) != null){
                String data[] = line.split(",");
                verities.add(Integer.parseInt(data[0]));
                verities.add(Integer.parseInt(data[1]));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return verities.size();
    }
}

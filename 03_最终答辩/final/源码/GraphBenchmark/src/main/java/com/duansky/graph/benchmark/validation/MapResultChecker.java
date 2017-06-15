package com.duansky.graph.benchmark.validation;

import com.duansky.graph.benchmark.util.Files;
import com.hazelcast.core.HazelcastInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The result is stored in the form of map.
 * Created by SkyDream on 2017/3/18.
 */
public abstract class MapResultChecker implements Checker{
    /** the final result of batch algorithm. **/
    protected Map<Integer,Integer> resA;

    /** the final result of stream algorithm. **/
    protected Map resB;

    public MapResultChecker(String pathA, HazelcastInstance hi,String name){
        resA = getBatchResultByPath(pathA);
        resB = getStreamResultByNetwork(hi,name);
    }

    private Map<Integer,Integer> getBatchResultByPath(String path){
        Map<Integer,Integer> res = new HashMap<Integer, Integer>();
        BufferedReader reader = Files.asBufferedReader(path);
        String line;
        try {
            while((line = reader.readLine()) != null){
                String[] row = line.split(",");
                int value;
                if(row[1].contains("."))
                    value = (int) Double.parseDouble(row[1]);
                else
                    value = Integer.parseInt(row[1]);
                res.put(Integer.parseInt(row[0]),value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private Map getStreamResultByNetwork(HazelcastInstance hi,String name){
        return hi.getMap(name);
    }
}

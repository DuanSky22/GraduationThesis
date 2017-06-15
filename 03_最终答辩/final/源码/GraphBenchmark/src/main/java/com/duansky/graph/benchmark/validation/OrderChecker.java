package com.duansky.graph.benchmark.validation;

import com.duansky.graph.benchmark.util.Files;
import com.hazelcast.core.HazelcastInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by SkyDream on 2017/3/18.
 */
public class OrderChecker{

    private String pathA;
    private HazelcastInstance hi;
    private String name;

    public OrderChecker(String pathA, HazelcastInstance hi, String name) {
        this.pathA = pathA;
        this.hi = hi;
        this.name = name;
    }


    public double[] calculateScore() {
        Map<Integer,Double> mapA = getBatchResultByPath(pathA);
        Map mapB = getStreamResultByNetwork(hi,name);
        List<Integer> listA = getTop100(mapA);
        List<Integer> listB = getTop100(mapB);
        double[] res = new double[10];
        for(int i = 0; i < 10; i++)
            res[i] = calculateScoreAt(listA,listB,10 * (i+1));
        return res;
    }

    private double calculateScoreAt(List<Integer> listA,List<Integer> listB,int pos){
        int counter = 0;
        Set<Integer> set = new HashSet<Integer>();
        for(int i = 0; i < pos; i++){
            set.add(listA.get(i));
        }
        for(int i = 0; i < pos; i++){
            if(set.contains(listB.get(i)))
                counter++;
        }
        return 1.0 * counter / pos;
    }

    public List<Integer> getTop100(Map map){
        List<Map.Entry<Integer,Double>> list = new LinkedList<Map.Entry<Integer, Double>>(map.entrySet());
        Collections.sort(list,
                new Comparator<Map.Entry<Integer, Double>>() {
                    @Override
                    public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                        return o1.getValue().compareTo(o2.getValue());
                    }
                });
        List<Integer> res = new ArrayList<Integer>(100);
        for(int i = 0; i < 100; i++){
            res.add(list.get(i).getKey());
        }
        return res;
    }

    private Map<Integer,Double> getBatchResultByPath(String path){
        Map<Integer,Double> res = new HashMap<Integer, Double>();
        BufferedReader reader = Files.asBufferedReader(path);
        String line;
        try {
            while((line = reader.readLine()) != null){
                String[] row = line.split(",");
                double value;
                if(row[1].contains("."))
                    value = Double.parseDouble(row[1]);
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

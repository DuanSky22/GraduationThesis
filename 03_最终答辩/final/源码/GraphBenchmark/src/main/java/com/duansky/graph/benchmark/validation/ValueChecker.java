package com.duansky.graph.benchmark.validation;

import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

/**
 * Created by SkyDream on 2017/3/18.
 */
public class ValueChecker extends MapResultChecker {

    public ValueChecker(String pathA, HazelcastInstance hi, String name) {
        super(pathA, hi, name);
    }

    @Override
    public double calculateScore() {
        int total = resA.size();
        int correct = 0;
        for(Map.Entry<Integer,Integer> tuple : resA.entrySet()){
            Integer key = tuple.getKey();
            if(tuple.getValue().equals(Integer.MAX_VALUE))
                correct++;
            else if(resB.containsKey(key)){
                Integer res;
                if(resB.get(key).toString().contains("."))
                    res = (int)(Double.parseDouble(resB.get(key).toString()));
                else
                    res = Integer.parseInt(resB.get(key).toString());
                if(res.equals(tuple.getValue()))
                    correct++;
                else{
                    System.out.println(String.format("%s[%s,%s]", tuple.getKey(),tuple.getValue(),res));
                }
            }else{
                System.out.println(tuple.getValue());
            }
        }
        return ((double) correct) / total;
    }
}

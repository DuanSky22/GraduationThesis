package com.duansky.graph.benchmark.validation;

import com.duansky.graph.benchmark.util.Files;
import com.hazelcast.core.HazelcastInstance;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by SkyDream on 2017/3/18.
 */
public class SingleResultChecker implements Checker{

    /**batch result path**/
    private String pathA;

    /**stream result server**/
    private HazelcastInstance hi;
    private String nameB;

    public SingleResultChecker(String pathA,HazelcastInstance hi,String nameB){
        this.pathA = pathA;
        this.hi = hi;
        this.nameB = nameB;
    }

    @Override
    public double calculateScore() {
        BufferedReader reader = Files.asBufferedReader(pathA);
        try {
            Long a = Long.parseLong(reader.readLine());
            Long b = hi.getAtomicLong(nameB).get();
            System.out.println(a+","+b);
            return 1 - 1.0 * Math.abs(a-b) / a;

        } catch (IOException e) {
            e.printStackTrace();
            return -1d;
        }
    }
}

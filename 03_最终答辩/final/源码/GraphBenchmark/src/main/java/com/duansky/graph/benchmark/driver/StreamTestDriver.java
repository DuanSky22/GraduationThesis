package com.duansky.graph.benchmark.driver;

import com.duansky.graph.benchmark.components.impl.DefaultGraphPathTransformer;
import com.duansky.graph.benchmark.components.impl.DefaultTemplate;
import com.duansky.graph.benchmark.util.Contract;
import com.duansky.graph.benchmark.util.Files;
import com.duansky.hazelcast.graphflow.components.state.AbstractIndividualState;
import com.duansky.hazelcast.graphflow.lib.*;

import java.io.File;

/**
 * Created by SkyDream on 2017/3/18.
 */
public class StreamTestDriver {
    //(test name, algorithm name, vertex number, possibility, part)
    public static void main(String[] args) {
        if(args == null || args.length == 0)
            args = new String[]{"test","dd","1234","0.1","0"};
        String lockConflict = Contract.DATA_FOLDER_GELLY + File.separator + "lock.txt";
//        HazelcastServer.getInstance().getServer();
        if(args != null && args.length == 5){
            String testName = args[0];
            String algorithmName = args[1];
            Integer vertexNumber = Integer.parseInt(args[2]);
            Double possibility = Double.parseDouble(args[3]);
            Integer part = Integer.parseInt(args[4]);
            String path = DefaultGraphPathTransformer.getInstance().getPath(
                    Contract.DATA_FOLDER_GELLY,
                    new DefaultTemplate(vertexNumber,possibility),part);
            if(algorithmName.contains("dd")){
                DegreeDistribution<Integer,Double> dd = new DegreeDistribution<Integer, Double>(testName,path,Integer.class,Double.class);
                long startTime = System.currentTimeMillis();
                dd.run();
                long endTime = System.currentTimeMillis();
                Files.writeAsTxt(lockConflict,
                        String.format("dd:[%s,%s,%s,%s,%s]", testName,algorithmName,vertexNumber,possibility,part)
                                +(endTime-startTime)/1000+"\n",true);
            } else if(algorithmName.contains("tc")){
                TriangleCount<Integer,Double,Double> tc = new TriangleCount<Integer, Double,Double>(testName,path,Integer.class,Double.class);
                long startTime = System.currentTimeMillis();
                tc.run();
                long endTime = System.currentTimeMillis();
                Files.writeAsTxt(lockConflict,
                        String.format("tc:[%s,%s,%s,%s,%s]", testName,algorithmName,vertexNumber,possibility,part)
                                +(endTime-startTime)/1000+"\n",true);
            }else if(algorithmName.contains("sssp")){
                SSSP<Integer,Double> sssp = new SSSP<Integer, Double>(testName,path,0,true,Integer.class,Double.class);
                long startTime = System.currentTimeMillis();
                sssp.run();
                long endTime = System.currentTimeMillis();
                Files.writeAsTxt(lockConflict,
                        String.format("sssp:[%s,%s,%s,%s,%s]", testName,algorithmName,vertexNumber,possibility,part)
                                +(endTime-startTime)/1000+"\n",true);
            }else if(algorithmName.contains("pr")){
                PageRank<Integer,Double> pr = new PageRank<Integer, Double>(testName,path,Integer.class,Double.class,0.2,100);
                long startTime = System.currentTimeMillis();
                pr.run();
                long endTime = System.currentTimeMillis();
                Files.writeAsTxt(lockConflict,
                        String.format("pr:[%s,%s,%s,%s,%s]", testName,algorithmName,vertexNumber,possibility,part)
                                +(endTime-startTime)/1000+"\n",true);
            }
        }else{
            System.out.println("Usage: (test name | algorithm name | vertex number | possibility | part)");
        }
    }
}

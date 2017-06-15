package com.duansky.graph.benchmark.driver;

import com.duansky.graph.benchmark.components.GraphTemplate;
import com.duansky.graph.benchmark.components.ResultPathTransformer;
import com.duansky.graph.benchmark.components.impl.DefaultResultPathTransformer;
import com.duansky.graph.benchmark.components.impl.DefaultTemplate;
import com.duansky.graph.benchmark.scripts.batchgraph.TestDD;
import com.duansky.graph.benchmark.scripts.batchgraph.TestPR;
import com.duansky.graph.benchmark.scripts.batchgraph.TestSSSP;
import com.duansky.graph.benchmark.scripts.batchgraph.TestTC;
import com.duansky.graph.benchmark.util.Contract;
import com.duansky.graph.benchmark.util.Files;
import com.duansky.graph.benchmark.validation.Checker;
import com.duansky.graph.benchmark.validation.OrderChecker;
import com.duansky.graph.benchmark.validation.SingleResultChecker;
import com.duansky.graph.benchmark.validation.ValueChecker;
import com.duansky.hazelcast.graphflow.storage.HazelcastClient;
import com.duansky.hazelcast.graphflow.util.Contracts;

import java.util.Arrays;

/**
 * Created by SkyDream on 2017/3/18.
 */
public class ResultCheckDriver {
    public static final ResultPathTransformer transformer = DefaultResultPathTransformer.getInstance();
    // (algorithmName,vertexNumber,possibility,testName)
    public static void main(String[] args) {
        args = new String[]{"dd","1234","0.1","test"};
       if(args != null || args.length == 4){
           String algorithmName = args[0];
           Integer vertexNumber = Integer.parseInt(args[1]);
           Double possibility = Double.parseDouble(args[2]);
           String testName = args[3];
           GraphTemplate template = new DefaultTemplate(vertexNumber,possibility);
           Checker checker = null;
           if(algorithmName.contains("dd")){
               checker = new ValueChecker(
                       transformer.getPath(TestDD.name,template, Contract.DATA_FOLDER_GELLY),
                       HazelcastClient.getInstance().getClient(),
                       Contracts.DEGREE_DISTRIBUTION_STATE+"-"+testName
               );

           }else if(algorithmName.contains("tc")){
               checker = new SingleResultChecker(
                       transformer.getPath(TestTC.name,template, Contract.DATA_FOLDER_GELLY),
                       HazelcastClient.getInstance().getClient(),
                       Contracts.TRIANGLE_COUNT_STATE + "-"+ testName
               );
           }else if(algorithmName.contains("sssp")){
               checker = new ValueChecker(
                       transformer.getPath(TestSSSP.name,template, Contract.DATA_FOLDER_GELLY),
                       HazelcastClient.getInstance().getClient(),
                       Contracts.SSSP_STATE+"-"+ testName
               );
           }else if(algorithmName.contains("pr")){
               OrderChecker checker1 = new OrderChecker(
                       transformer.getPath(TestPR.name,template,Contract.DATA_FOLDER_GELLY),
                       HazelcastClient.getInstance().getClient(),
                       Contracts.PAGERANK_STATE+"-"+testName
               );
               double[] scores = checker1.calculateScore();
               for(double score : scores){
                   System.out.print(score+" ");
               }
               Files.writeAsTxt(Contract.CHECKER_RESULT,scores+"",false);
               return;
           }
           else{
               throw new IllegalArgumentException("unsupported algorithm test.");
           }
           double score = checker.calculateScore();
           System.out.println(args[0]+":"+score);
           Files.writeAsTxt(Contract.CHECKER_RESULT,score+"",false);
       }
    }
}

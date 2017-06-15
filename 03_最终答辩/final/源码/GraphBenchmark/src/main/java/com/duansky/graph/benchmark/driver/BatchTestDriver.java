package com.duansky.graph.benchmark.driver;

import com.duansky.graph.benchmark.scripts.batchgraph.TestDD;
import com.duansky.graph.benchmark.scripts.batchgraph.TestPR;
import com.duansky.graph.benchmark.scripts.batchgraph.TestSSSP;
import com.duansky.graph.benchmark.scripts.batchgraph.TestTC;

/**
 * Created by SkyDream on 2017/3/17.
 */
public class BatchTestDriver {

    //(algorithmName,vertexNumber,possibility)
    public static void main(String[] args){
        if(args == null || args.length == 0)
            args = new String[]{"sssp","1234","0.1"};
        String script = args[0];
        String tmps[] = new String[args.length-1];
        for(int i = 1; i < args.length; i++){
            tmps[i-1]=args[i];
        }
        try {
            if (script.contains("dd")) TestDD.main(tmps);
            else if (script.contains("tc")) TestTC.main(tmps);
            else if (script.contains("sssp")) TestSSSP.main(tmps);
            else if (script.contains("pr")) TestPR.main(tmps);
            else
                throw new IllegalArgumentException("the script name error!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}

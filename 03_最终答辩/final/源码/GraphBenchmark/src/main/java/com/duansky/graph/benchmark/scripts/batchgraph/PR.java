package com.duansky.graph.benchmark.scripts.batchgraph;

import com.duansky.graph.benchmark.components.impl.DefaultGraphGenerator;
import com.duansky.graph.benchmark.components.impl.DefaultGraphPathTransformer;
import com.duansky.graph.benchmark.components.impl.DefaultTemplate;
import com.duansky.graph.benchmark.util.Contract;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.core.fs.FileSystem;

import java.io.File;

/**
 * Created by SkyDream on 2017/3/23.
 */
public class PR {
    public static void main(String args[]){
        DefaultTemplate template = new DefaultTemplate(4,0.1);
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().disableSysoutLogging();
        String edgePath = DefaultGraphPathTransformer.getInstance().getEdgePath(Contract.DATA_FOLDER_GELLY,template);
        String vertexPath = DefaultGraphPathTransformer.getInstance().getVertexPath(Contract.DATA_FOLDER_GELLY,template);
        PageRank pageRank = new PageRank(0.2,100);
        try {
            pageRank.run(DefaultGraphGenerator.getInstance().generateGraph(env,edgePath,vertexPath))
                    .writeAsCsv(Contract.DATA_FOLDER_GELLY+ File.separator+"pr.txt", FileSystem.WriteMode.OVERWRITE).setParallelism(1);

            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

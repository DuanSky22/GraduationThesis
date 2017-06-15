package com.duansky.graph.benchmark.scripts.batchgraph;

import com.duansky.graph.benchmark.components.GraphTemplate;
import com.duansky.graph.benchmark.components.impl.DefaultTemplate;
import com.duansky.graph.benchmark.scripts.AbstractScript;
import com.duansky.graph.benchmark.util.Contract;
import com.duansky.graph.benchmark.util.Files;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.graph.Graph;
import org.apache.flink.graph.library.clustering.directed.TriangleCount;

import java.io.File;

/**
 * Created by DuanSky on 2016/12/22.
 */
public class TestTC extends AbstractScript {

    public static String resPath = Contract.BASE_FOLD + File.separator + "test-triangle-count.txt";

    public static String name = "test triangle count";

    public TestTC() {
        super();
        setResPath(resPath);
        setScriptName(name);
    }

    public TestTC(String templatePath){
        super(templatePath);
        setResPath(resPath);
        setScriptName(name);
    }

    public TestTC(GraphTemplate template){
        super(new GraphTemplate[]{template});
        setResPath(resPath);
        setScriptName(name);
    }

    @Override
    protected String runInternal(GraphTemplate template) throws Exception {
        try {
            //generate the graph of this template.
            Graph graph = graphGenerator.generateGraph(env,
                    graphPathTransformer.getPath(Contract.DATA_FOLDER_GELLY,template));
            //run algorithm on this graph.
            TriangleCount tc = (TriangleCount) graph.run(new TriangleCount());

            //trigger this algorithm.
            env.execute("triangle count");

            //get the job result and its id.
            JobExecutionResult result = env.getLastJobExecutionResult();
            String jobId = result.getJobID().toString();

            String resultPath = resultPathTransformer.getPath(name,template,Contract.DATA_FOLDER_GELLY);
            Files.writeAsTxt(resultPath,tc.getResult()+"",false);

            return String.format("test for graph(%s,%s)\t%s\t%s\t%s\n",
                    template.getVertexNumber(),
                    template.getProbability(),
                    jobId,
                    tc.getResult(),
                    result.getNetRuntime());

        } catch (Exception e) {
            e.printStackTrace();

            return String.format("test for graph(%s,%s)\t%s\n",
                    template.getVertexNumber(),
                    template.getProbability(),
                    "Error!");
        }
    }

    public static void main(String args[]) throws Exception{
        TestTC test;
        if(args == null || args.length == 0)
            test = new TestTC();
        else if(args.length == 1)
            test = new TestTC(args[0]);
        else if(args.length == 2)
            test = new TestTC(new DefaultTemplate(Integer.parseInt(args[0]),Double.parseDouble(args[1])));
        else
            throw new IllegalArgumentException("args error for degree distribution test.");
        test.run();
    }
}

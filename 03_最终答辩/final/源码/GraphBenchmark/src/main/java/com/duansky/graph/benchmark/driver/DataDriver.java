package com.duansky.graph.benchmark.driver;


import com.duansky.graph.benchmark.components.GraphTemplate;
import com.duansky.graph.benchmark.components.GraphWriter;
import com.duansky.graph.benchmark.components.impl.DefaultGraphWriter;
import com.duansky.graph.benchmark.util.Contract;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Write graph data into files.
 * Created by DuanSky on 2016/10/31.
 */
public class DataDriver {

    private GraphWriter writer = DefaultGraphWriter.getInstance();

    /**
     * generate graphs use {@link GraphTemplateFactory} and write them
     * into files.
     */
    public void generateAndWriteGraphs(int parts,boolean withEdgeValue){
        //get all the templates.
        GraphTemplate[] templates = GraphTemplateFactory.generateTemplates();
        generateAndWriteGraphs(parts,withEdgeValue,templates);
    }

    /**
     * generate graphs use graph-template.properties and write them into files.
     * @param propertiesPath the path of the graph-template.properties
     * @param parts how many parts should this graph split.
     * @param withEdgeValue the edge of the graph has its value?
     */
    public void generateAndWriteGraphs(String propertiesPath,int parts,boolean withEdgeValue){
        //get all the templates.
        GraphTemplate[] templates = GraphTemplateFactory.generateTemplates(propertiesPath);
        generateAndWriteGraphs(parts,withEdgeValue,templates);
    }

    /**
     * generate graphs use {@link GraphTemplate}s you input and write them into
     * files.
     * @param templates the graph templates you want to generate.
     */
    public void generateAndWriteGraphs(int parts,boolean withEdgeValue,GraphTemplate... templates){
        //write the graphs.
        writeGraphs(templates,parts,withEdgeValue);
    }

    private void writeGraphs(GraphTemplate[] templates, final int parts,final boolean withEdgeValue){
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for(final GraphTemplate template : templates){
            service.execute(new Runnable() {
                @Override
                public void run() {
                    if(parts == 1)  writer.writeAsFile(Contract.DATA_FOLDER_GELLY,template);
                    else writer.writeAsFiles(Contract.DATA_FOLDER_GELLY,template,parts,withEdgeValue,true);
                    System.out.println(String.format("write graph(%s,%s) done.",
                            template.getVertexNumber(),
                            template.getProbability()));
                }
            });

        }
        service.shutdown();
    }

    public static void main(String args[]){
        DataDriver driver = new DataDriver();
        int parts = 10;
        boolean withEdgeValue = true;
        if(args != null && args.length == 3)
            driver.generateAndWriteGraphs(
                    System.getProperty("user.dir")+ File.separator+args[0],
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]) == 0 ? false : true);
        else
            driver.generateAndWriteGraphs(parts,withEdgeValue);
    }
}

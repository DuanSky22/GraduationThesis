package com.duansky.graph.benchmark.driver;

import com.duansky.graph.benchmark.components.GraphMerger;
import com.duansky.graph.benchmark.components.GraphTemplate;
import com.duansky.graph.benchmark.components.impl.DefalutGraphMerger;
import com.duansky.graph.benchmark.components.impl.DefaultTemplate;
import com.duansky.graph.benchmark.util.Contract;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by SkyDream on 2017/3/17.
 */
public class MergeDriver {
    private GraphMerger merger = DefalutGraphMerger.getInstance();

    public void merge(GraphTemplate... templates){
        if(templates == null || templates.length == 0)
            templates = GraphTemplateFactory.generateTemplates();
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for(final GraphTemplate template : templates){
            service.execute(new Runnable() {
                @Override
                public void run() {
                    merger.merge(Contract.DATA_FOLDER_GELLY,template);
                }
            });
        }
        service.shutdown();
    }

    public void merge(String propertiesPath){
        GraphTemplate[] templates = GraphTemplateFactory.generateTemplates(propertiesPath);
        merge(templates);
    }

    public static void main(String[] args) {
        MergeDriver driver = new MergeDriver();
        if(args == null || args.length == 0)
            driver.merge();
        else if(args.length == 1)
            driver.merge(args[0]);
        else if(args.length == 2)
            driver.merge(new DefaultTemplate(Integer.parseInt(args[0]),Double.parseDouble(args[1])));
        else
            throw new IllegalArgumentException("input args error!");
    }
}

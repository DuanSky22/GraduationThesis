package com.duansky.graph.benchmark.scripts;


import com.duansky.graph.benchmark.components.GraphGenerator;
import com.duansky.graph.benchmark.components.GraphTemplate;
import com.duansky.graph.benchmark.components.GraphPathTransformer;
import com.duansky.graph.benchmark.components.ResultPathTransformer;
import com.duansky.graph.benchmark.components.impl.DefaultResultPathTransformer;
import com.duansky.graph.benchmark.components.impl.DefaultGraphGenerator;
import com.duansky.graph.benchmark.components.impl.DefaultGraphPathTransformer;
import com.duansky.graph.benchmark.driver.GraphTemplateFactory;
import com.duansky.graph.benchmark.util.Contract;
import org.apache.flink.api.java.ExecutionEnvironment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by DuanSky on 2016/12/22.
 */
public abstract class AbstractScript implements Script{

    /** tools **/
    public static final GraphGenerator graphGenerator = DefaultGraphGenerator.getInstance();
    public static final GraphPathTransformer graphPathTransformer = DefaultGraphPathTransformer.getInstance();
    public static final ResultPathTransformer resultPathTransformer = DefaultResultPathTransformer.getInstance();


    /**environment**/
    protected ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

    {
        env.getConfig().disableSysoutLogging();
    }

    /** input data **/
    protected final GraphTemplate[] templates;

    protected String scriptName = "abstract script";

    /** output result **/
    protected String resPath = Contract.BASE_FOLD + File.separator + scriptName;
    PrintWriter writer;


    public AbstractScript(){
        this.templates = GraphTemplateFactory.generateTemplates();
    }

    public AbstractScript(GraphTemplate[] templates){
        this.templates = templates;
    }

    public AbstractScript(String templatePath){
        this.templates = GraphTemplateFactory.generateTemplates(templatePath);
    }


    @Override
    public void run() throws Exception{
        System.out.println("Start test["+ getScriptName()+"]...");
        String res;
        for(GraphTemplate template : templates) {
            res = runInternal(template);
            if(res != null){
                System.out.println(res);
                writeResult(res);
            }else{
                System.out.println("the result is null.");
            }

        }
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
        this.resPath = Contract.BASE_FOLD + File.separator + scriptName + ".txt";
    }

    public String getResPath() {
        return resPath;
    }

    public void setResPath(String resPath) {
        this.resPath = resPath;
        try {
            writer = new PrintWriter(resPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected abstract String runInternal(GraphTemplate template) throws Exception;

    protected void writeResult(String result) throws Exception{
        writer.write(result);
        writer.flush();
    }
}

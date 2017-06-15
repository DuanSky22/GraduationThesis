package com.duansky.graph.benchmark.driver;

/**
 * Created by SkyDream on 2017/3/18.
 */
public class TestEngine {

    //batch: (algorithmName,vertexNumber,possibility)
    //stream: (test name, algorithm name, vertex number, possibility, part)
    //check: (algorithmName,vertexNumber,possibility,testName)
    public static void main(String args[]){
        String[] batch = new String[]{"tc","1234","0.2"};
        String[] stream = new String[]{"test","tc","1234","0.2"};
        String[] check = new String[]{"tc","1234","0.2","test"};
        //start batch test.
        BatchTestDriver.main(batch);
        //start stream test.
        StreamTestDriver.main(stream);
        //start check
        ResultCheckDriver.main(check);

    }

}

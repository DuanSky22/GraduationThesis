package com.duansky.graph.benchmark.driver;

import com.duansky.graph.benchmark.util.Contract;
import com.duansky.graph.benchmark.util.Graphs;

import java.io.File;

/**
 * Created by SkyDream on 2017/3/23.
 */
public class EdgeToVertex {
    public static void main(String args[]){
        String edgePath = Contract.DATA_FOLDER_GELLY + File.separator + "graph-4-0.1";
        String vertexPath = Contract.DATA_FOLDER_GELLY + File.separator + "graph-4-0.1-verities.txt";
        Graphs.writeVerties(vertexPath,edgePath);
    }
}

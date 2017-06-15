package com.duansky.graph.benchmark.util;

import org.apache.commons.collections.list.TreeList;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.graph.Edge;
import org.apache.flink.graph.Graph;
import org.apache.flink.types.IntValue;
import org.apache.flink.types.NullValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by DuanSky on 2016/10/30.
 */
public class Graphs {

    /**
     * transform the edge array to DataSet type.
     * @param env the execution environment.
     * @param edges the array of edges.txt.
     * @return the dataset of the given edge array.
     */
    public static DataSet<Edge<IntValue,NullValue>> transform(ExecutionEnvironment env, int[][] edges){
        List<Edge<IntValue,NullValue>> list = new LinkedList<Edge<IntValue, NullValue>>();
        for(int[] edge : edges)
            list.add(new Edge<IntValue, NullValue>(new IntValue(edge[0]),new IntValue(edge[1]),NullValue.getInstance()));
        return env.fromCollection(list);
    }

    public static void writeVerties(String vertexPath,String edgePath){
        BufferedReader reader = Files.asBufferedReader(edgePath);
        PrintWriter writer = Files.asPrintWriter(vertexPath);
        Set<Integer> verities = new HashSet<Integer>();
        String line;
        try {
            while((line = reader.readLine())!=null){
                String[] data = line.split(",");
                int a = Integer.parseInt(data[0]);
                int b = Integer.parseInt(data[1]);
                if(!verities.contains(a)) {
                    verities.add(a);
                    writer.write(a + ",1\n");
                }
                if(!verities.contains(b)){
                    verities.add(b);
                    writer.write(b+",1\n");
                }
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.close();
        }
    }

    public static void printGraph(Graph graph){
        try {
            System.out.println("=================graph======================");
            System.out.println("  number of edges.txt:"+graph.numberOfEdges());
            System.out.println("number of verties:"+graph.numberOfVertices());
            System.out.println("   vertie degrees:");
            graph.getDegrees().print();
            System.out.println("       verties id:");
            graph.getVertexIds().print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

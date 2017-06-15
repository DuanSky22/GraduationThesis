package com.duansky.graph.benchmark.components.impl;


import com.duansky.graph.benchmark.components.GraphTemplate;
import com.duansky.graph.benchmark.components.GraphWriter;
import com.duansky.graph.benchmark.components.GraphPathTransformer;
import com.duansky.graph.benchmark.util.Files;
import com.duansky.graph.benchmark.util.Maths;
import com.duansky.hazelcast.graphflow.graph.Graph;

import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by DuanSky on 2016/10/31.
 */
public class DefaultGraphWriter implements GraphWriter {

    private static Random RND = new Random();
    private static int THRESHOLD = 1000;

    private GraphPathTransformer graphPathTransformer = DefaultGraphPathTransformer.getInstance();

    private static DefaultGraphWriter INSTANCE = new DefaultGraphWriter();

    public static DefaultGraphWriter getInstance(){return INSTANCE;}

    private DefaultGraphWriter(){}

    @Override
    public void writeAsFile(String folder, GraphTemplate template) {
        //check folder exists.
        Files.checkAndCreateFolder(folder);

        //write the edge of this graph.
        PrintWriter edgeWriter =
                Files.asPrintWriter(graphPathTransformer.getEdgePath(folder,template));
        if(edgeWriter != null){
            int n = template.getVertexNumber();
            if(n <= THRESHOLD) writeEdgeDirectly(edgeWriter,template);
            else writeEdgeUseProbability(edgeWriter,template);
        }

        //write the vertex of this graph.
        PrintWriter vertexWriter =
                Files.asPrintWriter(graphPathTransformer.getVertexPath(folder,template));
        if(vertexWriter != null){
            writeVertex(vertexWriter,template);
        }
    }

    @Override
    public void writeAsFiles(String folder, GraphTemplate template, int parts,boolean withEdgeValue,boolean mixWrite) {
        //write the edge of this graph.
        if(mixWrite)
            writeEdgeWithPartsMixMode(folder,template,parts,withEdgeValue);
        else
            writeEdgeWithParts(folder,template,parts,withEdgeValue);
        //write the vertex of this graph.
        PrintWriter vertexWriter =
                Files.asPrintWriter(graphPathTransformer.getVertexPath(folder,template));
        if(vertexWriter != null){
            writeVertex(vertexWriter,template);
        }
    }


    private void writeEdgeWithPartsMixMode(String folder, GraphTemplate template,int parts,boolean withEdgeValue){
        //check folder exists.
        Files.checkAndCreateFolder(folder);
        if(parts < 0) throw new IllegalArgumentException("the parts must large than 0!");
        else if(parts == 1) writeAsFile(folder,template);
        else{
            int n = template.getVertexNumber();
            double p = template.getProbability();
            //write the edge of this graph.
            PrintWriter writers[] = new PrintWriter[parts];
            for(int i = 0; i < parts; i++){
                writers[i] = Files.asPrintWriter(graphPathTransformer.getPath(folder,template,i));
            }
            int part = 0;
            for(int i = 0; i < n-1; i++){
                for(int j = i+1; j < n; j++){
                    if(RND.nextDouble() <= p) {
                        if(RND.nextBoolean())
                            writers[part].write(String.format("%s,%s,%s\n", i, j,withEdgeValue?(RND.nextInt(100)+1):""));
                        else
                            writers[part].write(String.format("%s,%s,%s\n", j, i,withEdgeValue?(RND.nextInt(100)+1):""));
                        writers[part].flush();
                        part = (part + 1) % parts;
                    }
                }
            }
            for(int i = 0; i < parts; i++){
                writers[i].close();
            }
        }
    }

    /** write the edge value serializable **/
    private void writeEdgeWithParts(String folder, GraphTemplate template, int parts,boolean withEdgeValue){
        //check folder exists.
        Files.checkAndCreateFolder(folder);
        if(parts < 0) throw new IllegalArgumentException("the parts must large than 0!");
        else if(parts == 1) writeAsFile(folder,template);
        else{
            int n = template.getVertexNumber();
            double p = template.getProbability();
            long total = (long) (n * p * ((n-1)/2)); // the total number of edge.
            long single = total / parts; //the number of edge for every part.
            int part = 1, current = 0;
            //write the edge of this graph.
            PrintWriter writer = Files.asPrintWriter(graphPathTransformer.getPath(folder,template,part));
            for(int i = 0; i < n-1; i++){
                for(int j = i+1; j < n; j++){
                    if(RND.nextDouble() <= p) {
                        if(current < single || part >= parts){
                            current++;
                        }else{
                            current = 0;
                            part++;
                            writer.close();
                            writer = Files.asPrintWriter(graphPathTransformer.getPath(folder,template,part));
                        }
                        if(RND.nextBoolean())
                            writer.write(String.format("%s,%s,%s\n", i, j,withEdgeValue?(RND.nextInt(100)+1):""));
                        else
                            writer.write(String.format("%s,%s,%s\n", j, i,withEdgeValue?(RND.nextInt(100)+1):""));
                        writer.flush();
                    }
                }
            }
        }
    }

    private void writeVertex(PrintWriter writer, GraphTemplate template){
        int n = template.getVertexNumber();
        for(int i = 0; i < n; i++){
            writer.write(i+","+1+"\n");
        }
        writer.close();
    }

    private void writeEdgeUseProbability(PrintWriter writer, GraphTemplate template){
        int n = template.getVertexNumber();
        double p = template.getProbability();
        for(int i = 0; i < n-1; i++){
            for(int j = i+1; j < n; j++){
                if(RND.nextDouble() <= p) {
                    if(RND.nextBoolean())
                        writer.write(String.format("%s,%s\n", i, j));
                    else
                        writer.write(String.format("%s,%s\n", j, i));
                    writer.flush();
                }
            }
        }
        writer.close();
    }

    private void writeEdgeDirectly(PrintWriter writer, GraphTemplate template){
        int n = template.getVertexNumber();
        double p = template.getProbability();
        //the random edge.
        int[][] e = Maths.getRandomUndirectedPairs(n, (int)(Maths.getCombinationsNumber(n,2) * p));
        for(int i = 0; i < e.length; i++){
            writer.write(String.format("%s,%s\n", e[i][0],e[i][1]));
            writer.flush();
        }
        writer.close();
    }


}

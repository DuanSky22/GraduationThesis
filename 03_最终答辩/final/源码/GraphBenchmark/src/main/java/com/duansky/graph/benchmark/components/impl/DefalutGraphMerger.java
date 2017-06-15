package com.duansky.graph.benchmark.components.impl;

import com.duansky.graph.benchmark.components.GraphMerger;
import com.duansky.graph.benchmark.components.GraphTemplate;
import com.duansky.graph.benchmark.components.GraphPathTransformer;
import com.duansky.graph.benchmark.util.Files;

import java.io.*;
import java.util.regex.Pattern;

/**
 * Created by SkyDream on 2017/3/17.
 */
public class DefalutGraphMerger implements GraphMerger {

    private GraphPathTransformer graphPathTransformer = DefaultGraphPathTransformer.getInstance();

    private static final DefalutGraphMerger INSTANCE = new DefalutGraphMerger();
    public static DefalutGraphMerger getInstance(){return INSTANCE;}

    @Override
    public void merge(String folder, GraphTemplate template) {
        //get all the splits prefix,also as the integrated file name.
        final String prefix = graphPathTransformer.getPath(folder,template);
        //find all the splits.
        File base = new File(folder);
        final File[] parts = base.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String[] list = pathname.getName().split("-");
                return pathname.getPath().startsWith(prefix) && Pattern.compile("[0-9]*").matcher(list[list.length-1]).matches();
            }
        });
        PrintWriter writer = Files.asPrintWriter(prefix);
        //merge the splits.
        BufferedReader reader;
        try {
            for(File part : parts){
                reader = Files.asBufferedReader(part);
                String temp = "";
                while(temp != null) {
                    temp = reader.readLine();
                    if(temp != null) writer.write(temp+"\n");
                }
                writer.flush();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.close();
        System.out.println("merge "+template+" done!");
    }
}

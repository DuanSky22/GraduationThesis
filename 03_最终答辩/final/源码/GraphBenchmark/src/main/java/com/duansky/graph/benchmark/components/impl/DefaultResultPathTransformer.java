package com.duansky.graph.benchmark.components.impl;

import com.duansky.graph.benchmark.components.GraphTemplate;
import com.duansky.graph.benchmark.components.ResultPathTransformer;

import java.io.File;

/**
 * Created by SkyDream on 2017/3/18.
 */
public class DefaultResultPathTransformer implements ResultPathTransformer {

    private static final DefaultResultPathTransformer INSTANCE = new DefaultResultPathTransformer();
    public static DefaultResultPathTransformer getInstance(){return INSTANCE;}

    @Override
    public String getPath(String algorithmName, GraphTemplate template,String folder) {
        return String.format("%s%sresult-%s-%s-%s",
                folder,
                File.separator,
                algorithmName,
                template.getVertexNumber(),
                template.getProbability());
    }
}

package com.duansky.graph.benchmark.components;

/**
 * Result Path Transformer transform a graph template and algorithm to a path.
 * Created by SkyDream on 2017/3/18.
 */
public interface ResultPathTransformer {

    /**
     * get the path of the specific algorithm and graph defined by {@link GraphTemplate}
     * @param algorithmName the name of the algorithm.
     * @param template the graph definition.
     * @param folder the base dir.
     * @return the path of the specific algorithm and graph defined by {@link GraphTemplate}
     */
    String getPath(String algorithmName,GraphTemplate template,String folder);
}

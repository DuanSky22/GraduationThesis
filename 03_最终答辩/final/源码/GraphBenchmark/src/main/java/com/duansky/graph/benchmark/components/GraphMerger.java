package com.duansky.graph.benchmark.components;

/**
 * As we know, {@link GraphWriter} will split the file that
 * store the edge of the graph, but sometimes we want merge those
 * parts into on file.
 * {@link GraphMerger} is used to merge parts that the {@link GraphWriter}
 * generated.
 *
 * Created by SkyDream on 2017/3/17.
 */
public interface GraphMerger {
    /**
     * merge the parts of the graph that defined by the {@link GraphTemplate}
     * @param folder where the base folder of the graph stores.
     * @param template defines the graph.
     */
    void merge(String folder,GraphTemplate template);
}

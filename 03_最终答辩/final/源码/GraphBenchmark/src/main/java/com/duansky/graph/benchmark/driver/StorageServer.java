package com.duansky.graph.benchmark.driver;

import com.duansky.hazelcast.graphflow.storage.HazelcastServer;

/**
 * Created by SkyDream on 2017/3/22.
 */
public class StorageServer {
    public static void main(String[] args) {
        HazelcastServer.getInstance().getServer();
    }
}

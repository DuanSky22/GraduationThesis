# GraphBenchmark
GraphBenchmark is used for createing graph data and test batch & streaming graph framework.

Here are four key parts.

+ DataDriver: create the graph data with/without edge values, the graph data will be stored in the form of files. For the parallelism, we also offer the split function that can take the graph data apart.
+ MergeDriver: Dute to the DataDriver can split the graph data, sometimes we want to merge them as a single file, so the MergeDriver can merge the splits into one file.
+ BatchScrpit: create the scrpit test for Flink Gelly Algorithms.
+ StreamScript: create the script test for HzGraphFlow, also known as Streaming Graph Computing Freamework.
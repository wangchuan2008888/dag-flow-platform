package site.wangchuan.dag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.common.graph.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.wangchuan.dag.impl.ConcurrentDAGJobScheduler;

/**
 * JobFlow依赖图。
 */
public class JobDAG {
    Logger logger = LoggerFactory.getLogger(JobDAG.class);
    final String name;

    ImmutableGraph<JobRunner> graph;

    final ImmutableGraph
            .Builder<JobRunner> builder;

    private boolean fastMode = false;

    private boolean cacheEnable = false;

    private int threadNumber, cpuInsentiveThreadNumber;

    public static final String DEFAULT_NAME = "default";


    public JobDAG(String jobFlowKey, int threadNumber, int cpuInsentiveThreadNumber) {
        this.name = jobFlowKey;
        this.threadNumber = threadNumber;
        this.cpuInsentiveThreadNumber = cpuInsentiveThreadNumber;
        builder = GraphBuilder.directed().allowsSelfLoops(false).immutable();
    }

    /**
     * 注册一个Job
     *
     * @param job
     * @return
     */
    public synchronized JobRunner register(JobRunner job) {
        builder.addNode(job);
        return job;
    }


    /**
     * 注册Job之间的连接关系
     *
     * @param job
     * @param depends
     */
    public synchronized void addDepends(JobRunner job, JobRunner... depends) {
        for (JobRunner dep : depends) {
            builder.putEdge(job, dep);
        }
    }

    public synchronized void finnishBuild() {
        // double check
        if (this.graph == null) {
            this.graph = builder.build();
        }
    }

    /**
     * 检查是否有环
     */

    public void check() {
        if(this.graph==null){
            throw new RuntimeException("Graph have not build");
        }
        boolean hasCycle = Graphs.hasCycle(this.graph);
        if (hasCycle) {
            throw new RuntimeException("Graph has cycle");
        }
    }

    public int getThreadNumber() {
        return threadNumber;
    }

    public int getCpuInsentiveThreadNumber() {
        return cpuInsentiveThreadNumber;
    }

    public String getName() {
        return name;
    }

    public ImmutableGraph<JobRunner> getGraph() {
        return graph;
    }

    public boolean isCacheEnable() {
        return cacheEnable;
    }

    public boolean isFastMode() {
        return fastMode;
    }


    public DAGJobScheduler createScheduler() {
        return new ConcurrentDAGJobScheduler(this);
    }

}

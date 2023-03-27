package site.wangchuan.dag;


import site.wangchuan.dag.common.Consts;
import site.wangchuan.dag.common.ResultStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * JobFlow任务基类。
 */
public abstract class JobRunner {
    
    final public String name;

    @Setter
    protected JobDAG graph;

    
    protected int timeout = Consts.JOB_TIMEOUT_DEFAULT_THRESHOLD;

    protected boolean isCpuIntensive = false;
    
    public JobRunner(JobDAG graph, String name) {
        this.graph = graph;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    
    @Override
    public boolean equals(Object other) {
        JobRunner runner = (JobRunner) other;
        return name.equals(runner.name);
    }
    
    abstract public ResultStatus run(DAGJobScheduler scheduler);
    
    abstract public void fallback(DAGJobScheduler scheduler);

    public String getName() {
        return name;
    }

    public int getTimeout() {
        return timeout;
    }

    public boolean isCpuIntensive() {
        return isCpuIntensive;
    }
}

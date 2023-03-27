package site.wangchuan.dag.impl;


import com.google.common.collect.Sets;

import site.wangchuan.dag.DAGJobScheduler;
import site.wangchuan.dag.JobDAG;
import site.wangchuan.dag.JobRunner;
import site.wangchuan.dag.common.ResultStatus;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

/**
 * 串行JobFlow任务调度器，一般Job均为无IO操作的CPU密集型任务。
 * 线程不安全，不支持并发访问。
 */
@Slf4j
public class SerialDAGJobScheduler extends DAGJobScheduler {
    
    private Set<JobRunner> finishJobs;
    
    SerialDAGJobScheduler(JobDAG graph) {
        super(graph);
        finishJobs = Sets.newHashSet();
    }

    @Override
    public <T extends JobRunner> Future<ResultStatus> submitJob(T runner) throws Exception {
        throw new Exception("unsupported async job execute.");
    }

    @Override
    public <T extends JobRunner> void doJob(T runner) throws Exception {
        if (finishJobs.contains(runner)) return ;
        try {
            Set<JobRunner> depends = jobDAG.getGraph().predecessors(runner);
            for (JobRunner depend : depends) {
                doJob(depend);
            }
            try {
                runner.run(this);
            } catch(Exception e) {
                log.error(e.getMessage(), e);
                runner.fallback(this);
            }
        } catch(Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            finishJobs.add(runner);
        }
    }

    @Override
    public void doJob(List<? extends JobRunner> runners) throws Exception {
        for (JobRunner runner : runners) {
            doJob(runner);
        }
    }

}

package site.wangchuan.dag;


import site.wangchuan.dag.common.ResultStatus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * 任务调度器基类，管理单次Flow请求的所有任务执行。
 */
public abstract class DAGJobScheduler {

    protected JobDAG jobDAG;
    protected Map<String, Object> environment = new ConcurrentHashMap<String, Object>();

    protected DAGJobScheduler(JobDAG jobDAG) {
        this.jobDAG = jobDAG;
    }


    public boolean isFastMode() {
        return jobDAG.isFastMode();
    }

    /**
     * 往JobFlow执行器里增加环境变量。
     */
    @SuppressWarnings("unchecked")
    public <T> T getEnv(String key) {
        return (T) environment.get(key);
    }

    /**
     * 从JobFlow执行器去处环境变量。
     */
    public void addEnv(String key, Object value) {
        this.environment.put(key, value);
    }

    /**
     * 异步执行任务。
     */
    abstract public <T extends JobRunner> Future<ResultStatus> submitJob(T runner) throws Exception;

    /**
     * 同步执行任务。
     */
    abstract public <T extends JobRunner> void doJob(T runner) throws Exception;

    /**
     * 批量同步执行任务。
     */
    abstract public void doJob(List<? extends JobRunner> runners) throws Exception;

}

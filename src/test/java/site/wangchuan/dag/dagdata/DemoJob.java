package site.wangchuan.dag.dagdata;

import site.wangchuan.dag.common.ResultStatus;
import site.wangchuan.dag.JobDAG;
import site.wangchuan.dag.JobRunner;
import site.wangchuan.dag.DAGJobScheduler;

/**
 *
 * @Author: <fujiansheng.com>
 * @Description：
 * @Date: Created in :2019-02-24 20:48  
 * @Modified by:
 */
public class DemoJob extends JobRunner {

    private String name;

    public DemoJob(JobDAG graph, String name) {
        super(name);
        this.name = name;
        this.timeout = 5000000;
    }

    @Override
    public ResultStatus run(DAGJobScheduler scheduler) {
        System.out.println("start job " + name);
        CommonData.putData(name);

//            throw new Exception();
        System.out.println("finish job " + name);
        return ResultStatus.SUCCESS;

    }

    @Override
    public void fallback(DAGJobScheduler scheduler) {
        System.out.println("fallback job " + name);
    }


}

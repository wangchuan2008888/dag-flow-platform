package site.wangchuan.dag.simple;


import site.wangchuan.dag.common.ResultStatus;
import site.wangchuan.dag.JobDAG;
import site.wangchuan.dag.JobRunner;
import site.wangchuan.dag.DAGJobScheduler;

public class JobFlowTest {

    public static void main(String[] args) throws Exception {
        JobDAG flow = new JobDAG("demoGraph", 10, 1);
        JobRunner job1 = new DemoJob(flow, "1");
        JobRunner job31 = new DemoJob(flow, "3.1");
        flow.register(job1);
        flow.register(job31);
        flow.addDepends(job31, job1);
        flow.check();//检查是否有未注册的依赖以及是否有环

        DAGJobScheduler scheduler = flow.createScheduler();
        scheduler.doJob(job31);

    }

    private static class DemoJob extends JobRunner {

        private String name;

        public DemoJob(JobDAG graph, String name) {
            super(graph, name);
            this.name = name;
            this.timeout = 5000;
        }

        @Override
        public ResultStatus run(DAGJobScheduler scheduler) {
            System.out.println("start job " + name);
            try {
                Thread.sleep((int) (Math.random() * 10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            throw new Exception();
            System.out.println("finish job " + name);
            return ResultStatus.SUCCESS;

        }

        @Override
        public void fallback(DAGJobScheduler scheduler) {
            System.out.println("fallback job " + name);
        }

    }

}

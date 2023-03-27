package site.wangchuan.dag.dagtest;


import site.wangchuan.dag.common.ResultStatus;
import site.wangchuan.dag.JobDAG;
import site.wangchuan.dag.JobRunner;
import site.wangchuan.dag.DAGJobScheduler;

public class JobFlowTest {
    
    public static void main(String[] args) throws Exception {

        JobDAG flow = new JobDAG("demoGraph", 10, 1);
        JobRunner job1 = new DemoJob(flow, "1");
        JobRunner job2 = new DemoJob(flow, "2");
        JobRunner job31 = new DemoJob(flow, "3.1");
        flow.register(job1);
        flow.register(job2);
        flow.register(job31);


        flow.addDepends(job31,job1);

        JobRunner job32 = new DemoJob(flow, "3.2");
        flow.register(job32);
        flow.addDepends(job32, job2);

        JobRunner job4 = new DemoJob(flow, "4");
        flow.register(job4);
        flow.addDepends(job4, job31,job32);

        JobRunner job5 = new DemoJob(flow, "5");
        flow.register(job5);
        flow.addDepends(job5,job31, job32);
        flow.finnishBuild();
//        flow.addDepends(job1,job5);//校验循环依赖
        flow.check();//检查是否有未注册的依赖以及是否有环

        DAGJobScheduler scheduler = flow.createScheduler();
        scheduler.doJob(job4);
        scheduler.doJob(job5);
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
                Thread.sleep((int)(Math.random() * 100));
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

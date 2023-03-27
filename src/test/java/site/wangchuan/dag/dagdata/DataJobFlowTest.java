package site.wangchuan.dag.dagdata;


import site.wangchuan.dag.JobDAG;
import site.wangchuan.dag.JobRunner;
import site.wangchuan.dag.DAGJobScheduler;

public class DataJobFlowTest {
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

        JobRunner job6 = new DemoJob(flow, "6");
        flow.register(job6);
        flow.addDepends(job6,job5, job4);


//        flow.addDepends(job1,job5);//校验循环依赖
        flow.check();//检查是否有未注册的依赖以及是否有环
        //create dag pic
        DAGJobScheduler scheduler = flow.createScheduler();
        scheduler.doJob(job6);
        CommonData.printData();
    }
}

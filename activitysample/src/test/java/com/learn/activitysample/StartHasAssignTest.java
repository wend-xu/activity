package com.learn.activitysample;

import com.learn.activitysample.all.OutPutInfo;
import com.learn.activitysample.all.StartHasAssign;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartHasAssignTest {
    StartHasAssign startHasAssign = new StartHasAssign();

    @Test
    public void initTest(){
       startHasAssign  = new StartHasAssign();
       System.out.println(startHasAssign.getNowVersion());
       System.out.println(startHasAssign.getProcessDefinitionId());
    }

    @Test
    public void deployProcessReTest(){
        startHasAssign  = new StartHasAssign();
        System.out.println("before version id : "+startHasAssign.getNowVersion());
        System.out.println("before ProcessDefinitionId id : "+startHasAssign.getProcessDefinitionId());
        System.out.println("======= after deployProcessRe ==========");
        startHasAssign.deployProcessRe("processes/MyProcessHasAssign.bpmn",null);
        System.out.println("after version id : "+startHasAssign.getNowVersion());
        System.out.println("after ProcessDefinitionId id : "+startHasAssign.getProcessDefinitionId());
        System.out.println("======= end deployProcessReTest ==========");
    }

    @Test
    public void startProcessSetCandidateTest(){
        ProcessInstance processInstance = startHasAssign.startProcessSetCandidate(null);
        OutPutInfo.outputProcessInstance(processInstance);
    }

    @Test
    public void startProcessSetAssignTest(){
        ProcessInstance processInstance = startHasAssign.startProcessSetAssign(null);
        OutPutInfo.outputProcessInstance(processInstance);
    }

    @Test
    public void queryProcessInstance(){
        //List<ProcessInstance> myProcess = startHasAssign.queryProcessInstanceByKey("myProcess");
        List<ProcessInstance> myProcess = startHasAssign.queryProcessInstanceByDefinitionId("myProcess:2:5004");
        myProcess.forEach(processInstance -> OutPutInfo.outputProcessInstance(processInstance));
    }

    @Test
    public void queryTaskByUserTest(){
        List<Task> tasks = startHasAssign.queryTaskByUser("小明");
        System.out.println("all task num is : "+tasks.size());
        tasks.forEach(task -> System.out.println(task.getName()+"===="+task.getAssignee()));
    }

    @Test
    public void queryTaskByGroupTest(){
        List<Task> tasks = startHasAssign.queryTaskByGroup("UserIds");
        System.out.println("all task num is : "+tasks.size());
        tasks.forEach(task -> System.out.println(task.getName()));
    }

    @Test
    public void initGroupTest(){
        Map<String,Object> assignInfo = new HashMap<>();
        //这些数据应该从数据库拿比较合适，这里为了见到那
        String[] TL = {"张三","李四","王五"};
        String[] HR = {"甲","乙","丙","丁"};
        String[] USER = {"小明","小红","小刚","小亮"};
        assignInfo.put("TLIds",TL);
        assignInfo.put("HRIds",HR);
        assignInfo.put("UserIds",USER);

        assignInfo.forEach((key, object) -> startHasAssign.initUserAndGroup(key,(String[]) object));
    }

    @Test
    public void queryTaskByAssignTest(){
        //查询执行前全部待办
      /*  List<Task> tasks = startHasAssign.queryTaskByGroup(null);

        ProcessInstance processInstance = startHasAssign.startProcessSetAssign(null);

        List<Task> tasks2 = startHasAssign.queryTaskByGroup(null);*/
        String queryAssignId = "小明";
        //String queryAssignId = "xm,xh,xl,xz";
        List<Task> xm = startHasAssign.queryTaskByAssign(queryAssignId);
        List<Task> xm2 = startHasAssign.queryTaskByUser(queryAssignId);

       /* System.out.println("在执行本方法前待办任务总数数"+tasks.size());
        System.out.println("在执行本方法后待办任务总数数"+tasks2.size());
        OutPutInfo.outputProcessInstance(processInstance);*/
        System.out.println(queryAssignId+" task num is : "+xm.size());
        xm.forEach(task -> System.out.println(task.getName()));
        System.out.println(queryAssignId+" task num is : "+xm2.size());
        xm2.forEach(task -> System.out.println(task.getName()));
    }

    //assign是任务的实际执行者，Candidate是可以被assign的对象。
    //assign之后便不存在candidate
    @Test
    public void claimTaskTest(){
        String queryAssignId = "小明";
        List<Task> xm2 = startHasAssign.queryTaskByUser(queryAssignId);
        System.out.println(queryAssignId+" task num is : "+xm2.size());
        xm2.forEach(task -> {
            System.out.println(task.getName());
            startHasAssign.claimTask(task,queryAssignId);
        });

        List<Task> xm = startHasAssign.queryTaskByAssign(queryAssignId);
        System.out.println(queryAssignId+" task num is : "+xm.size());
        xm.forEach(task -> System.out.println(task.getName()));
    }
}

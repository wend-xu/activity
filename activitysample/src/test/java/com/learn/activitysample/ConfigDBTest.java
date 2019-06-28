package com.learn.activitysample;

import com.learn.activitysample.all.DeployProcess;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class ConfigDBTest {

    DeployProcess deployProcess = new DeployProcess();

    @Test
    public void test1(){
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        ProcessEngine processEngine = configuration.buildProcessEngine();
        processEngine.close();
    }

    @Test
    public void test2(){
        ProcessEngineConfiguration configuration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti_druid.cfg.xml");
        ProcessEngine processEngine = configuration.buildProcessEngine();
        processEngine.close();
    }

    @Test
    public void delopyTest(){
        ProcessDefinition processDefinition = deployProcess.deploy("processes/MyProcess.bpmn", "processes/MyProcess.png");
        if(processDefinition != null){
            System.out.println(processDefinition.getDeploymentId());
            System.out.println(processDefinition.getKey());
            System.out.println(processDefinition.getId());
            System.out.println(processDefinition.getVersion());

        }else{
            System.out.println("UNKNOW ERROR");
        }
    }

    @Test
    public void processDefinitionQueryTest(){
        List<ProcessDefinition> myProcess = deployProcess.queryProcessDefinition("myProcess");
        if(myProcess != null)
        myProcess.forEach(processDefinition -> {
            System.out.println("==========>>>>>>查询成功");
            System.out.println(processDefinition.getDeploymentId());
            System.out.println(processDefinition.getKey());
            System.out.println(processDefinition.getId());
            System.out.println(processDefinition.getVersion());
        });
        else
            System.out.println("查询失败");
    }

    @Test
    public void startProcessTest(){
        ProcessInstance processInstance = deployProcess.startById("myProcess:3:10004");
        deployProcess.outputProcessInstance(processInstance);
    }

    @Test
    public void queryProcessInstanceByProcessDefinitionIdTest(){
        List<ProcessInstance> processInstances = deployProcess.queryProcessInstanceByProcessDefinitionId("myProcess:3:10004");
        if (processInstances != null){
            processInstances.forEach(processInstance -> deployProcess.outputProcessInstance(processInstance));
        }
    }

    @Test
    public void queryProcessInstanceTest(){
        deployProcess.queryProcessInstance();
    }

    @Test
    public void getTaskByProcessInstanceTest(){
        ProcessInstance processInstance = deployProcess.queryProcessInstanceById("12501");
        if(!processInstance.isEnded() || !processInstance.isSuspended()){
            List<Task> tasks = deployProcess.getTaskByProcessInstance(processInstance);
            List<FormProperty> taskFormData = deployProcess.getTasksFormData(tasks);
            System.out.println(MessageFormat.format("taskFormData‘s size is {0}",taskFormData.size()));
        }
    }

    @Test
    public void inputTaskVariableTest(){
        ProcessInstance processInstance = deployProcess.queryProcessInstanceById("12501");
        if(!processInstance.isEnded() || !processInstance.isSuspended()){
            List<Task> tasks = deployProcess.getTaskByProcessInstance(processInstance);
            tasks.forEach( task -> {
                Map<String, Object> map = deployProcess.inputTaskVariable(task);
                System.out.println(map.toString());
                deployProcess.completeTask(task,map);
            });
        }
    }

    @Test
    public void getImageTest(){
        deployProcess.viewImage();
    }

    @Test
    public void queryHistoryTest(){
        deployProcess.queryHistory();
    }
}

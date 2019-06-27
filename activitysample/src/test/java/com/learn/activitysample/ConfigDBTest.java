package com.learn.activitysample;

import com.learn.activitysample.all.DeployProcess;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    public void test3(){
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
    public void test4(){
        List<ProcessDefinition> myProcess = deployProcess.queryProcessDefinition("myProcess");
        if(myProcess != null)
        myProcess.forEach(processDefinition -> {
            System.out.println(processDefinition.getDeploymentId());
            System.out.println(processDefinition.getKey());
            System.out.println(processDefinition.getId());
            System.out.println(processDefinition.getVersion());
        });
        else
            System.out.println("查询失败");
    }

    @Test
    public void test5(){
        ProcessInstance processInstance = deployProcess.startById("myProcess:1:4");
        deployProcess.outputProcessInstance(processInstance);
    }
}

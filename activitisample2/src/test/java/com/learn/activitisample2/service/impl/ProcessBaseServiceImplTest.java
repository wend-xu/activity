package com.learn.activitisample2.service.impl;

import com.learn.activitisample2.service.ProcessBaseService;
import com.learn.activitisample2.service.ProcessDeployService;
import com.learn.activitisample2.util.BaseTools;
import com.learn.activitisample2.util.QueryTool;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProcessBaseServiceImplTest {
    @Autowired
    ProcessBaseService processBaseService;
    @Autowired
    ProcessDeployService processDeployService;
    @Autowired
    BaseTools baseTools;
    @Autowired
    QueryTool queryTool;

    @Test
    public void processTest() throws IllegalAccessException{
        baseTools.outputEntityInfo(processBaseService.getProcessEngine());
        ProcessDefinition processDefinition = processDeployService.deployProcess("processes/MyProcess.bpmn");
        baseTools.outputEntityInfo(processDefinition);
        ProcessInstance processInstance = processDeployService.startProcess(processDefinition.getId());
        baseTools.outputEntityInfo(processInstance);
    }

    @Test
    public void queryTest(){
        List<ProcessInstance> myProcess = queryTool.query(0, 5, "myProcess", queryTool.QUERY_PROCESS_INSTANCE_BY_DEFINTION_KEY);
        myProcess.forEach( processInstance-> {
            try {
                baseTools.outputEntityInfo(processInstance);
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
        });
    }

    @Test
    public void getProcessDefinitionsByKeyTest(){
        List<ProcessDefinition> myProcess = processDeployService.getProcessDefinitionsByKey(0, 5, "myProcess");
        myProcess.forEach( processInstance-> {
            try {
                baseTools.outputEntityInfo(processInstance);
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
        });
    }
}
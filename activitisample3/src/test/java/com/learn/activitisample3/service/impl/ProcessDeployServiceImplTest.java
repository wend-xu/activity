package com.learn.activitisample3.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.learn.activitisample3.controller.ProcessDeployController;
import com.learn.activitisample3.controller.ProcessQueryController;
import com.learn.activitisample3.entity.Result;
import com.learn.activitisample3.service.ProcessBaseService;
import com.learn.activitisample3.service.ProcessDeployService;
import com.learn.activitisample3.service.ProcessTaskService;
import com.learn.activitisample3.util.BaseTools;
import com.learn.activitisample3.util.QueryTool;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;

import org.activiti.editor.language.json.converter.util.JsonConverterUtil;
import org.activiti.engine.RepositoryService;

import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProcessDeployServiceImplTest {
    @Autowired
    ProcessBaseService baseService;


    @Autowired
    QueryTool queryTool;
    @Autowired
    ProcessDeployService deployService;

    @Autowired
    ProcessTaskService taskService;

    @Autowired
    ProcessDeployController deployController;

    @Test
    public void queryTest() throws IOException {
        RepositoryService repositoryService =baseService.getProcessEngine().getRepositoryService();
        //102501
        /*Model model = repositoryService.getModel("152501");
        BaseTools.outputEntityInfo2(model);


        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        byte[] source = repositoryService.getModelEditorSource("152501");
        JsonNode node = new ObjectMapper().readTree(source);
        BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(node);
        BaseTools.outputEntityInfo2(bpmnModel);

        DeploymentBuilder deployment = repositoryService.createDeployment();
        deployment = deployment.addBpmnModel("MyProcess2.bpmn", bpmnModel);
        Deployment deploy = deployment.deploy();
        BaseTools.outputEntityInfo2(deploy);

        ProcessDefinition definition = deployService.getProcessDefinitionByDeploymentId(deploy.getId());
        BaseTools.outputEntityInfo2(definition);*/
    }

    @Test
    public void taskTest(){
        List<Task> taskList = queryTool.query(0, 20, queryTool.QUERY_ALL_ACTIVITY_TASK);
        if(!taskList.isEmpty()){
            for(Task task:taskList){
                List<FormProperty> taskFormProperties = taskService.getTaskFormProperties(task);
                for(FormProperty property:taskFormProperties){
                    BaseTools.outputEntityInfo2(property);
                }
            }
        }
    }

    @Test
    public void taskTest2()throws IllegalAccessException{
        List<Task> taskList = taskService.getTaskByProcessInstanceId(0, 10, "57501");
        BaseTools.outputListInfo2(taskList);
        Task task = taskList.get(0);
        BaseTools.outputEntityInfo3(task);
        Map<String,Object> variable = new HashMap<>();
        variable.put("message","吃饭");
        variable.put("name","张三");
        variable.put("submitTime ","2019-10-25");
        variable.put("submitType","y");
        taskService.handleTask(task.getId(),variable);
        taskList = taskService.getTaskByProcessInstanceId(0, 10, "57501");
        task = taskList.get(0);
        BaseTools.outputEntityInfo3(task);
        //taskService.handleTask(task,);
    }

    @Test
    public void deployTest() throws IllegalAccessException, JsonProcessingException {
        ProcessInstance processInstance = deployService.startProcess("myProcess:1:4");
        Map<String, Object> map = BaseTools.outputEntityInfo3(processInstance);
        Result result = deployController.startProcess("myProcess:1:4");
        String s = new ObjectMapper().writeValueAsString(processInstance);
        System.out.println(s);
    }
}
package com.learn.helloactivity.activityDemo;

import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HelloActivity {

    public static void approve() throws ParseException {
        System.out.println("开始流程");
        //创建流程引擎
        /*
        springboot 可以不需要这样创建
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("配置文件路径")；
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();*/
        ProcessEngine processEngine = getProcessEngine();

        //部署流程定义文件
        //通过processEngine获取repositoryService,进而部署流程文件
        ProcessDefinition processDefinition = getProcessDefinition(processEngine);

        //启动运行流程
        //同理runtimeService也是通过processEngine获得
        ProcessInstance processInstance = getProcessInstance(processEngine, processDefinition);

        //处理流程任务
        //通过流程实例获取待处理任务
        handlingTask(processEngine, processInstance);
        System.out.println("流程结束");
    }

    private static ProcessEngine getProcessEngine() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println("流程引擎版本为："+ProcessEngine.VERSION);
        System.out.println("流程引擎 name 为："+processEngine.getName());
        return processEngine;
    }

    private static ProcessDefinition getProcessDefinition(ProcessEngine processEngine) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/MyProcess.bpmn")
                .addClasspathResource("processes/MyProcess.png")
                .deploy();
        String deploymentId = deployment.getId();
        System.out.println("文件部署成功，ID:"+deploymentId);

        ProcessDefinition processDefinition =  repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId).singleResult();
        System.out.print("流程定义对象："+processDefinition.getName());
        System.out.println(" 流程ID : "+processDefinition.getId());
        return processDefinition;
    }

    private static ProcessInstance getProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        System.out.println("流程启动："+ processInstance.getProcessDefinitionKey());
        return processInstance;
    }

    private static void handlingTask(ProcessEngine processEngine, ProcessInstance processInstance) throws ParseException {
        Scanner scanner = new Scanner(System.in);

        while(processInstance != null && !processInstance.isEnded()){
            TaskService taskService = processEngine.getTaskService();
            List<Task> taskList = taskService.createTaskQuery().list();

            System.out.println("待处理任务数量 ： " + taskList.size());

            Map<String,Object> variables = new HashMap<>();
            for(Task task : taskList){
                System.out.println("待处理任务：" + task.getName());
                FormService formService = processEngine.getFormService();
                TaskFormData taskFormData = formService.getTaskFormData(task.getId());
                List<FormProperty> formProperties = taskFormData.getFormProperties();
                for(FormProperty property : formProperties){
                    if(StringFormType.class.isInstance(property.getType())){
                        System.out.println("请输入"+property.getName());
                        String line = scanner.nextLine();
                        variables.put(property.getId(),line);
                    }else if(DateFormType.class.isInstance(property.getType())){
                        System.out.println("请输入"+property.getName()+",格式： yyyy-MM-dd");
                        String line = scanner.nextLine();
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(line);
                        variables.put(property.getId(),date);
                    }else{
                        System.out.println("暂不支持该类型输入");
                    }
                }
                taskService.complete(task.getId(),variables);
                processInstance = processEngine.getRuntimeService()
                        .createProcessInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .singleResult();
            }
        }

        scanner.close();
    }
}

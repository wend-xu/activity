package com.learn.activitysample.all;

import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class DeployProcess {
    ProcessEngine processEngine;

    public DeployProcess() {
        if (processEngine == null){
            processEngine = GetProcessEngine.getDefaultProcessEngine();
        }
    }

    public ProcessDefinition deploy(String bpmnUrl, String pngUrl){
        RepositoryService repositoryService = processEngine.getRepositoryService();

        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                .addClasspathResource(bpmnUrl)
                .addClasspathResource(pngUrl);
        Deployment deploy = deploymentBuilder.deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        return processDefinition;
    }

    public ProcessInstance startById(String id){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(id);
        System.out.println("流程启动："+processInstance.getProcessDefinitionKey()+":"+processInstance.getProcessDefinitionId());
        return processInstance;
    }

    public ProcessInstance startById(ProcessDefinition processDefinition){
        return startById(processDefinition.getId());
    }

    public ProcessInstance startByKey(String key){
        return processEngine.getRuntimeService().startProcessInstanceByKey(key);
    }

    public List<ProcessDefinition> queryProcessDefinition(String key){
        return processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(key).list();
    }

    public void outputProcessInstance(ProcessInstance processInstance){
        System.out.println(MessageFormat.format("processInstance.getName is {0},processInstance.getBusinessKey is {1},\n" +
                "processInstance.getDeploymentId is {2},processInstance.getProcessDefinitionId is {3},\n" +
                        "processInstance.getProcessDefinitionKey is :{4},processInstance.getId is {5}",
                processInstance.getName(),processInstance.getBusinessKey(),processInstance.getDeploymentId(),
                processInstance.getProcessDefinitionId(),processInstance.getProcessDefinitionKey(),processInstance.getId()));
    }

    public List<ProcessInstance> queryProcessInstanceByProcessDefinitionId(String id){
        return processEngine.getRuntimeService().createProcessInstanceQuery().processDefinitionId(id).list();
    }

    public ProcessInstance queryProcessInstanceById(String id){
        return processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    public void queryProcessInstance(){
        ProcessInstanceQuery processInstanceQuery = processEngine.getRuntimeService().createProcessInstanceQuery();
        System.out.println("deploymentId 为 null 输出全部流程实例");
        List<ProcessInstance> list = processInstanceQuery.deploymentId(null).list();
        if (list != null){
            list.forEach(processInstance -> {
                outputProcessInstance(processInstance);
            });
        }

        list = processInstanceQuery.processInstanceId("12501").list();
        System.out.println("根据deploymentID 输出指定流程实例");
        if (list != null){
            list.forEach(processInstance -> {
                outputProcessInstance(processInstance);
            });
        }
       /* processInstanceQuery.*/
    }

    public List<Task> getTaskByProcessInstance(ProcessInstance processInstance){
        TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
        List<Task> listAll = taskQuery.list();
        List<Task> list = taskQuery.processInstanceId(processInstance.getId()).list();
        if (listAll != null && list != null)
            System.out.println(MessageFormat.format("总待办任务总数： {0}, 当前实例待办任务数： {1}",listAll.size(),list.size()));
        return list;
    }

    public List<FormProperty> getTasksFormData(List<Task> tasks){
        System.out.println("tasks 's size is "+tasks.size());
        List<FormProperty> formProperties = new ArrayList<>();
        tasks.forEach(task -> {
            formProperties.addAll(getTaskFormData(task));
            formProperties.forEach(formProperty -> outputFormProperty(formProperty));
        });
        return formProperties;
    }

    public List<FormProperty> getTaskFormData(Task task){
        return getTaskFormData(task.getId());
    }

    public List<FormProperty> getTaskFormData(String taskId){
        FormService formService = processEngine.getFormService();
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        return taskFormData.getFormProperties();
    }

    private void outputFormProperty(FormProperty formProperty){
        System.out.println(MessageFormat.format("formProperty.getId is {0}; formProperty.getName is {1};\n"+
                "formProperty.getType is {2}; formProperty.getValue is {3}",
                formProperty.getId(),formProperty.getName(),
                formProperty.getType(),formProperty.getValue()));
    }

    public Map<String,Object> inputTaskVariable(Task task){
        Map<String,Object> variable = new HashMap<>();
        List<FormProperty> taskFormData = getTaskFormData(task);

        if(taskFormData.size()>0){
            Scanner scanner = new Scanner(System.in);
            taskFormData.forEach( formProperty -> {
                System.out.println(MessageFormat.format("待输入项 ： {0} ； ID： {1} ; Type: {2}"
                        ,formProperty.getName(),formProperty.getId(),formProperty.getType()));
            });
            System.out.println("============开始录入===========");
            taskFormData.forEach( formProperty -> {
                if(formProperty.getType() instanceof StringFormType){
                    System.out.println("请输入："+formProperty.getName());
                    String s = scanner.nextLine();
                    variable.put(formProperty.getId(),s);
                }else if(formProperty.getType() instanceof DateFormType){
                    System.out.println(MessageFormat.format("请输入： {0};格式为: yyyy-MM-dd",formProperty.getName()));
                    String s = scanner.nextLine();
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(s);
                        variable.put(formProperty.getId(),date);
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("不支持输入该类型");
                }
            });
        }
        return variable;
    }

    public void completeTask(Task task , Map<String,Object> variable){
        processEngine.getTaskService().complete(task.getId(),variable);
    }

    public void viewImage(){
        String deploymentId = "10001";
        RepositoryService repositoryService = processEngine.getRepositoryService();
        List<String> deploymentResourceNames = repositoryService.getDeploymentResourceNames(deploymentId);
        String imageNanme = "";
        for(String name:deploymentResourceNames){
            System.out.println("deployment resource name:"+name);
            if(name.indexOf(".png") > 0){
                imageNanme = name;
                System.out.println("deployment resource image name:"+imageNanme);
            }
        }

        if(!imageNanme.equals("")){
            InputStream resourceAsStream = repositoryService.getResourceAsStream(deploymentId, imageNanme);
            File file = new File("d:/"+imageNanme);

            if(!file.getParentFile().exists()){file.getParentFile().mkdirs();}


            byte[] buf = new byte[1024];
            int len =0;
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while((len = resourceAsStream.read(buf)) != -1){
                    fileOutputStream.write(buf);
                }
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    public void queryHistory(){
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().list();
        list.forEach(historicTaskInstance -> {
            System.out.println(historicTaskInstance.getName()+"======= end:"+historicTaskInstance.getEndTime()+"====== id:"+historicTaskInstance.getId());
            System.out.println(historicTaskInstance.getParentTaskId()+"==========="+historicTaskInstance.getProcessDefinitionId());
         /*   List<FormProperty> taskFormData = getTaskFormData(historicTaskInstance.getId());
            taskFormData.forEach(formProperty -> outputFormProperty(formProperty));*/
            List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery().taskId(historicTaskInstance.getId()).list();
            if(variableInstances == null){
                System.out.println("========= is null =============");
            }
            variableInstances.forEach(historicVariableInstance -> {
                System.out.println("historicVariable："+historicVariableInstance.getVariableName()+"======="+historicVariableInstance.getValue());
            });
        });

    }
}

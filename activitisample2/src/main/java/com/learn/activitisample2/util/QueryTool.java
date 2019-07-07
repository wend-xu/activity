package com.learn.activitisample2.util;

import com.learn.activitisample2.service.ProcessBaseService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.query.Query;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;

@Component
public class QueryTool {
    private ProcessDefinitionQuery processDefinitionQuery;
    private ProcessInstanceQuery processInstanceQuery;
    private TaskQuery taskQuery;

    @Autowired
    public QueryTool(ProcessBaseService processBaseService){
        ProcessEngine processEngine = processBaseService.getProcessEngine();
        processDefinitionQuery = processEngine.getRepositoryService().createProcessDefinitionQuery();
        processInstanceQuery = processEngine.getRuntimeService().createProcessInstanceQuery();
        taskQuery = processEngine.getTaskService().createTaskQuery();
    }

    /**
     * 计算分页的方法
     * type传入startNum计算记录开始index
     *  传入endNum计算记录结束index
     * */
    public BinaryOperator<Integer> startNum = (size, page) -> page.intValue() == 0 ?0 : page*size;

    public BinaryOperator<Integer> endNum = (size,page) -> page.intValue() == 0 ?size-1 :(page+1)*size-1;

    public int countLimitNum(int page,int size ,BinaryOperator<Integer> type){
        return type.apply(size,page);
    }

    /**
    *  泛用查询方法
     *  支持分页
     *  可以使用本类提供的函数接口实现不同查询
     *  也可以自定义函数接口进行查询
    * */
    public List query(int page, int size, String field, Function<String, ? extends Query> queryBy){
        int start = countLimitNum(page, size, startNum);
        int end = countLimitNum(page, size, endNum);
        return queryBy.apply(field).listPage(start, end);
    }

    //流程定义查询
    public final Function<String, ProcessDefinitionQuery> QUERY_PROCESS_DEFINITION_BY_DEFINITION_KEY =
            definitionKey -> processDefinitionQuery.processDefinitionKey(definitionKey);

    //流程实例的查询
    public final Function<String, ProcessInstanceQuery> QUERY_PROCESS_INSTANCE_BY_DEFINITION_ID =
            definitionId -> processInstanceQuery.processDefinitionId(definitionId);

    public final Function<String,ProcessInstanceQuery> QUERY_PROCESS_INSTANCE_BY_DEFINTION_KEY =
            definitionKey -> processInstanceQuery.processDefinitionKey(definitionKey);

    public final Function<String,ProcessInstanceQuery> QUERY_PROCESS_INSTANCE_BY_DEFINTION_NAME =
            definitionName -> processInstanceQuery.processDefinitionName(definitionName);

    public final Function<Set<String>, ProcessInstanceQuery> QUERY_PROCESS_INSTANCE_BY_IDS =
            instanceIds -> processInstanceQuery.processInstanceIds(instanceIds);

    //任务查询
    public final Function<String,TaskQuery> QUERY_TASK_BY_CANDIDATE_USER =
            userIdentity -> taskQuery.taskCandidateUser(userIdentity);

    public final Function<String,TaskQuery> QUERY_TASK_BY_CANDIDATE_DEPT =
            deptIdentity -> taskQuery.taskCandidateGroup(deptIdentity);

    public final Function<String,TaskQuery> QUERY_TASK_BY_ASSIGN_USER =
            userIdentity -> taskQuery.taskAssignee(userIdentity);
}

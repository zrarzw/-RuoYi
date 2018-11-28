package com.ruoyi.workflower.utils;



import com.ruoyi.workflower.model.ProcessList;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipInputStream;


@Component("activitiUtils")
public class ActivitiUtils {
    @Autowired
    private ProcessEngine processEngine;

    /**
     * 部署流程
     *
     * @param file        流程的zip文件
     * @param processName 流程的名字
     * @throws IOException
     */
    public void deployeProcess(File file, String processName) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        this.processEngine.getRepositoryService()
                .createDeployment()
                .name(processName)
                .addZipInputStream(zipInputStream)
                .deploy();
    }

    /**
     * 通过字节流来进行部署流程
     *
     * @param io
     * @param processName
     */
    public void deplyoProcessByInputSterm(InputStream io, String processName) {
        ZipInputStream zipInputStream = new ZipInputStream(io);
        this.processEngine.getRepositoryService()
                .createDeployment()
                .name(processName)
                .addZipInputStream(zipInputStream)
                .deploy();
    }


    /**
     * 查询所有的部署流程
     *
     * @return
     */
    public List<Deployment> getAllDeplyoment() {
        return this.processEngine.getRepositoryService()
                .createDeploymentQuery()
                .orderByDeploymenTime()
                .desc()
                .list();
    }

    /**
     * 查询所有的部署定义信息
     *
     * @return
     */
    public List<ProcessDefinition> getAllProcessInstance() {
        return this.processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
    }

    /**
     * 根据部署ID，来删除部署
     *
     * @param deplyomenId 下面的true表示的是进行彻底相关联的表的内容都进行删除，如果为false，就只会删除部署表中的内容
     */
    public void deleteDeplyomentByPID(String deplyomenId) {
        this.processEngine.getRepositoryService()
                .deleteDeployment(deplyomenId, true);
    }

    /**
     * 查询某个部署流程的流程图
     *
     * @param pid
     * @return
     */
    public InputStream lookProcessPicture(String pid) {
        return this.processEngine.getRepositoryService()
                .getProcessDiagram(pid);
    }

    /**
     * 开启请假的流程实例
     *
     * @param leaveId
     * @param userId
     */
    public void startProceesInstance(String leaveId, String userId, String userName, String days, Date date) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userID", userId);
        variables.put("请假人", userName);
        variables.put("请假日期", date);
        variables.put("请假天数", days);
        // variable表数据条数与put数一致
        this.processEngine.getRuntimeService()
                .startProcessInstanceByKey("leave", "" + leaveId, variables);
    }

    /**
     * 查询当前登陆人的所有任务
     *
     * @param userId
     * @return
     */
    public List<Task> queryCurretUserTaskByAssignerr(String userId) {
        return this.processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee(userId)
                .orderByTaskCreateTime()
                .desc()
                .list();
    }




    /**
     * 根据taskId，判断对应的流程实例是否结束
     * 如果结束了，那么得到的流程实例就是返回一个null
     * 否则就是返回对应的流程实例对象
     * 当然也可以选择返回boolean类型的
     *
     * @param taskId 任务ID
     * @return
     */
    public ProcessInstance isFinishProcessInstancs(String taskId) {
        //1,先根据taskid，得到任务
        Task task = getTaskByTaskId(taskId);
        //2:完成当前任务
        finishCurrentTaskByTaskId(taskId);
        //3:得到当前任务对应得的流程实例对象
        ProcessInstance processInstance = getProcessInstanceByTask(task);
        return processInstance;
    }


    /**
     * 根据taskId获取到task
     *
     * @param taskId
     * @return
     */
    public Task getTaskByTaskId(String taskId) {
        //得到当前的任务
        Task task = this.processEngine.getTaskService()
                .createTaskQuery()
                .taskId(taskId)
                .singleResult();
        return task;
    }

    /**
     * 根据Task中的流程实例的ID，来获取对应的流程实例
     *
     * @param task 流程中的任务
     * @return
     */
    public ProcessInstance getProcessInstanceByTask(Task task) {
        //得到当前任务的流程
        ProcessInstance processInstance = this.processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();
        return processInstance;
    }

    /**
     * 根据Task来获取对应的流程定义信息
     *
     * @param task
     * @return
     */
    public ProcessDefinitionEntity getProcessDefinitionEntityByTask(Task task) {
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) this.processEngine.getRepositoryService()
                .getProcessDefinition(task.getProcessDefinitionId());
        return processDefinitionEntity;
    }

    /**
     * 根据taskId获取到businesskey，这个值是管理activiti表和自己流程业务表的关键之处
     *
     * @param taskId 任务的ID
     * @return
     */
    public String getBusinessKeyByTaskId(String taskId) {
        Task task = this.getTaskByTaskId(taskId);
        ProcessInstance processInstance = this.getProcessInstanceByTask(task);
        //返回值
        return processInstance.getBusinessKey();
    }

    /**
     * 根据taskId，完成任务
     *
     * @param taskId
     */
    public void finishCurrentTaskByTaskId(String taskId) {
        this.processEngine.getTaskService().complete(taskId);
    }

    /**
     * 完成任务的同时，进行下一个节点的审批人员的信息的传递
     *
     * @param taskId
     * @param object
     */
    public void finishCurrentTaskByTaskId(String taskId, Object object) {
        Map<String, Object> map = new HashMap<>();
        map.put("assigeUser", object);
        this.processEngine.getTaskService().complete(taskId, map);
    }

    /**
     * 查询历史流程实例
     *
     * @param processDefinitionId
     */
    public ArrayList<ProcessList> findHisProcessInstance(String processDefinitionId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<HistoricProcessInstance> list = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processDefinitionId(processDefinitionId)//流程定义ID
                .list();
        ArrayList<ProcessList> processLists = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (HistoricProcessInstance hpi : list) {
                ProcessList processList = new ProcessList();
                processList.setId(hpi.getId());
                processList.setStartTime(hpi.getStartTime());
                processList.setEndTime(hpi.getEndTime());
                processLists.add(processList);
            }
        }
        return processLists;
    }
}

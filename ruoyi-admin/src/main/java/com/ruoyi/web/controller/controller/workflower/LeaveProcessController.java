package com.ruoyi.web.controller.controller.workflower;



import com.ruoyi.workflower.model.LeaveBill;
import com.ruoyi.workflower.model.ProcessList;
import com.ruoyi.workflower.service.LeaveBillService;
import com.ruoyi.workflower.service.LeaveProcessService;
import com.ruoyi.workflower.utils.ActivitiUtils;
import com.ruoyi.workflower.utils.ResultType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("process")
public class LeaveProcessController {

    @Autowired
    ActivitiUtils activitiUtils;

    @Autowired
    LeaveProcessService leaveProcessService;

    @Autowired
    LeaveBillService leaveBillService;


    /**
     * 显示部署流程的页面，并且同时获取到已经存在的流程的数据内容
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/workflowlist")
    public String workFlowList(Map<String, Object> map) {
        //获取所有的流程实例
        List<ProcessDefinition> allProcessInstance = activitiUtils.getAllProcessInstance();
        //获取所有的部署内容
        List<Deployment> allDeplyoment = activitiUtils.getAllDeplyoment();
        map.put("process", allProcessInstance);
        map.put("deplyoments", allDeplyoment);
        return "workFlow";
    }


    /**
     * 通过部署流程的ID，来进行删除部署内容
     *
     * @param deploymentId
     * @return
     */
    @RequestMapping(value = "/deletedeplyoment")
    @ResponseBody
    public ResultType deleteDeploymentById(String deploymentId) {
        activitiUtils.deleteDeplyomentByPID(deploymentId);
        return ResultType.getSuccess("删除成功！");
    }

    /**
     * 显示当前流程处理到的节点图
     *
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/showcurrentview")
    public String showCurrentTaskView(String taskId, HttpServletResponse response) throws IOException {
        ProcessInstance instanceByTask = activitiUtils.getProcessInstanceByTask(activitiUtils.getTaskByTaskId(taskId));
        String pdId = instanceByTask.getProcessInstanceId();

        InputStream imageStream = leaveProcessService.getResourceDiagramInputStream(pdId);
        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
  /*      ServletOutputStream os = response.getOutputStream();
        int read;
        byte[] buffer = new byte[4*1024];
        while((read = imageStream.read(buffer)) > 0){
            os.write(buffer, 0, read);
        }*/
        return null;
    }

    /**
     * 通过流程的ID，来进行查看流程图
     *
     * @param pdid
     * @return
     */
    @RequestMapping(value = "/lookprocess")
    public String lookProcessByProcessId(String pdid, HttpServletResponse response) throws IOException {
        //1：得到图片的输入流内容
        InputStream inputStream = activitiUtils.lookProcessPicture(pdid);// act_re_procdef表ID
        //2:得到输出流
        ServletOutputStream os = response.getOutputStream();
        //3:进行输出
        int read;
        byte[] buffer = new byte[4 * 1024];
        while ((read = inputStream.read(buffer)) > 0) {
            os.write(buffer, 0, read);
        }
        return null;
    }


    /**
     * 显示当前登陆人的所有任务列表
     *
     * @return
     */
    @RequestMapping(value = "/showtasklist")
    public String showTaskListUI(String userName, Model model) {
        //查询当前登录人的任务信息
        List<Task> tasks = activitiUtils.queryCurretUserTaskByAssignerr(userName);
        model.addAttribute("tasks", tasks);
        model.addAttribute("userName", userName);
        return "task";
    }


    /**
     * 查询历史流程实例
     * @param processDefinitionId
     * @param model
     * @return
     */
    @RequestMapping(value = "/findHisProcessInstance")
    public String findHisProcessInstance(String processDefinitionId, Model model) {
        //查询当前登录人的任务信息
        ArrayList<ProcessList> list =activitiUtils.findHisProcessInstance(processDefinitionId);
        model.addAttribute("list",list);
        return "hisFlow";
    }


    /**
     * 跳转到当前任务的UI页面
     *
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/totask")
    public String toTaskUI(String taskId, Map<String, Object> map) {
        //1:根据taskId获取到Buseniess_key，为了获取到当前任务的内容
        String businessKey = activitiUtils.getBusinessKeyByTaskId(taskId);
        //2:根据business_key获取到对应的请假信息内容
        LeaveBill oneLeaveBill = leaveBillService.getById(businessKey);
        map.put("leavebill", oneLeaveBill);
        map.put("tackId", taskId);
        return "taskForm";
    }


    /**
     * 完成当然流程任务（这个是非常重要非常重要的）
     *
     * @param taskId 当前任务ID
     * @return
     */
    @RequestMapping(value = "/submittask")
    @ResponseBody
    public ResultType finishTaskByTaskId(String taskId) {
        //设置审批的状态，1表示通过，目前
        //2：进行相应的任务完成的处理操作
        leaveProcessService.finishCurrentTask(taskId);
        return ResultType.getSuccess("审批成功！");
    }
}

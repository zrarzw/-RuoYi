package com.ruoyi.web.controller.controller.workflower;


import com.ruoyi.workflower.model.LeaveBill;
import com.ruoyi.workflower.service.LeaveBillService;
import com.ruoyi.workflower.utils.ActivitiUtils;
import com.ruoyi.workflower.utils.LeavePage;

import com.ruoyi.workflower.utils.ResultType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Controller
@RequestMapping("leave")
public class LeaveBillController {


    @Autowired
    ActivitiUtils activitiUtils;

    @Autowired
    LeaveBillService leaveBillService;



    @RequestMapping("/leaveList")
    public String leaveBillList() {
        return "list";
    }

    @RequestMapping("/createLeave")
    public String createLeaveInfo() {
        return "addLeave";
    }

    @RequestMapping("/info")
    public String leaveInfo(String id,String loginUser,Model model) {
        model.addAttribute("leave", leaveBillService.getById(id));
        model.addAttribute("loginUser", loginUser);
        return "leaveInfo";
    }



    @RequestMapping("/getLeaveList")
    @ResponseBody
    public LeavePage<LeaveBill> getLeaveBillList(@RequestParam(defaultValue = "0") int offset,
                                                 @RequestParam(defaultValue = "10") int limit) {
        return leaveBillService.leaveBillLeavePage(offset, limit);
    }


    @RequestMapping("/{id}")
    @ResponseBody
    public ResultType deleteleave(@PathVariable("id") final String id) {
        return ResultType.getSuccess("删除成功", leaveBillService.deleteLeaveBillById(id));
    }



    /**
     * 流程实例的开启
     *
     * @param userId
     * @param leaveBill
     * @return
     */
    @PostMapping("/start/{userId}")
    @ResponseBody
    public ResultType startProcess(@PathVariable("userId") String userId
            , LeaveBill leaveBill) {
        String leaveId = UUID.randomUUID().toString();
        activitiUtils.startProceesInstance(leaveId, userId,leaveBill.getUserName(),leaveBill.getDays(),leaveBill.getDate());
        leaveBill.setId(leaveId);
        leaveBill.setState(0);
        leaveBillService.createLeaveBill(leaveBill);
        return ResultType.getSuccess("申请成功");
    }



    @PostMapping("/update/{id}")
    @ResponseBody
    public ResultType update(@PathVariable("id") final String id,LeaveBill leaveBill) {
        leaveBill.setId(id);
        leaveBillService.updateLeaveBill(leaveBill);
        return ResultType.getSuccess("更新成功");
    }
}

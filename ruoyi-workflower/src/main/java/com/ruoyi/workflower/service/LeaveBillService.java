package com.ruoyi.workflower.service;



import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.workflower.dao.LeaveBillDao;
import com.ruoyi.workflower.model.LeaveBill;
import com.ruoyi.workflower.utils.LeavePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class LeaveBillService {

    @Autowired
    LeaveBillDao leaveBillDao;

    public void createLeaveBill(LeaveBill leaveBill) {

        leaveBillDao.save(leaveBill);

    }

    public void updateLeaveBill(LeaveBill leaveBill) {

        leaveBillDao.save(leaveBill);

    }

    public List<LeaveBill> leaveBillList() {
        return leaveBillDao.findAll();
    }

    public LeavePage<LeaveBill> leaveBillLeavePage(
           Integer offset, Integer limit) {
        PageHelper.offsetPage(offset, limit, true);
        Page<LeaveBill> page =  leaveBillDao.getLeaveList();
        LeavePage<LeaveBill> list = new LeavePage<>(page);
        return list;
    }

    @Transactional
    public int deleteLeaveBillById(String id){
      return  leaveBillDao.deleteLeaveBillById(id);
    }

    public LeaveBill getById(String id){
        return leaveBillDao.getLeaveById(id);
    }

}

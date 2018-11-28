package com.ruoyi.workflower.dao;


import com.github.pagehelper.Page;
import com.ruoyi.workflower.model.LeaveBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LeaveBillDao extends JpaRepository<LeaveBill,String> {

    @Modifying
    @Query(value = "UPDATE leave_bill SET state =?1 WHERE id =?2 ",nativeQuery = true)
     void update(Integer state, String id);

    @Query(value = "SELECT * from leave_bill lb ORDER BY 'date' DESC ",nativeQuery = true)
    Page<LeaveBill> getLeaveList();


    int deleteLeaveBillById(String id);


    @Query(value = "SELECT * from leave_bill lb WHERE id=?1",nativeQuery = true)
    LeaveBill getLeaveById(String id);

}

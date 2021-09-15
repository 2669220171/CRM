package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int deleteByAids(String[] ids);
    int getCountByAids(String[] ids);
    List<ActivityRemark> getRemarkListByAid(String activityId);
    int deleteRemark(String id);
    int saveRemark(ActivityRemark ar);
    int updateRemark(ActivityRemark ar);

    int deleteById(String id);
}

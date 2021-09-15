package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.fasterxml.jackson.databind.node.BooleanNode;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity a);
    PaginationVO pageList(Map<String,Object> map);
    boolean delete (String[] ids);
    boolean update (Activity activity);
    Map<String,Object> getUserListAndActivity(String id);
    Activity detail(String id);
    List<ActivityRemark> getRemarkListByAid(String activityId);
    boolean deleteRemark(String id);
    boolean saveRemark(ActivityRemark ar);
    boolean updateRemark(ActivityRemark ar);
    List<Activity> getActivityListByClueId(String clueId);
    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);
    List<Activity> getActivityListByName(String aname);
}

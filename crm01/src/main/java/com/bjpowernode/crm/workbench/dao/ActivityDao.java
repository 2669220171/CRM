package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity a);
    int getTotalByCondition (Map<String,Object> map);
    List<Activity> getActivityListByCondition(Map<String,Object> map);
    int delete(String[] ids);
    int update(Activity activity);
    Activity getById(String id);
    Activity detail(String id);
    List<Activity> getActivityListByClueId(String clueId);
    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);
    List<Activity> getActivityListByName(String aname);
}

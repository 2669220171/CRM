package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean save(Clue clue);

    PaginationVO pageList(Map<String, Object> map);

    Boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Clue clue);

    Clue detail(String id);

    List<ClueRemark> getRemarkListByClueId(String clueId);

    boolean unbund(String id);

    boolean bund(String cid,String[] aids);

    boolean convert(String clueId, Tran t, String createBy);
}

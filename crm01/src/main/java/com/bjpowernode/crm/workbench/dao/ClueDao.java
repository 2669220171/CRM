package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue clue);


    int getTotalByCondition(Map<String, Object> map);

    List<Clue> getClueListByCondition(Map<String, Object> map);

    int deleteByList(String[] ids);

    Clue getById(String id);

    int update(Clue clue);

    Clue detail(String id);

    int delete(String clueId);
}

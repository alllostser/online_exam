package com.exam.dao;

import com.exam.pojo.Question;
import com.exam.pojo.QuestionWithBLOBs;

public interface QuestionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuestionWithBLOBs record);

    int insertSelective(QuestionWithBLOBs record);

    QuestionWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuestionWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(QuestionWithBLOBs record);

    int updateByPrimaryKey(Question record);
}
package com.exam.dao;

import com.exam.pojo.ExamQuestion;

public interface ExamQuestionMapper {
    int deleteByPrimaryKey(Integer eqId);

    int insert(ExamQuestion record);

    int insertSelective(ExamQuestion record);

    ExamQuestion selectByPrimaryKey(Integer eqId);

    int updateByPrimaryKeySelective(ExamQuestion record);

    int updateByPrimaryKey(ExamQuestion record);
}
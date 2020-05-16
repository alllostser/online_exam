package com.exam.dao;

import com.exam.pojo.ExamDetail;

public interface ExamDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExamDetail record);

    int insertSelective(ExamDetail record);

    ExamDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExamDetail record);

    int updateByPrimaryKey(ExamDetail record);
}
package com.exam.dao;

import com.exam.pojo.ExamStudent;

public interface ExamStudentMapper {
    int deleteByPrimaryKey(Integer esId);

    int insert(ExamStudent record);

    int insertSelective(ExamStudent record);

    ExamStudent selectByPrimaryKey(Integer esId);

    int updateByPrimaryKeySelective(ExamStudent record);

    int updateByPrimaryKey(ExamStudent record);
}
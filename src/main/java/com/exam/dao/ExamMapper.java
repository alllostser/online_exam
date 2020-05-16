package com.exam.dao;

import com.exam.pojo.Exam;

import java.util.List;

public interface ExamMapper {
    /**
     *试卷列表
     **/
    List<Exam> queryAll(Exam exam);
}
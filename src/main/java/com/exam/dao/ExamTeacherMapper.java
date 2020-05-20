package com.exam.dao;

import com.exam.pojo.ExamTeacher;

import java.util.List;

public interface ExamTeacherMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExamTeacher record);

    int insertSelective(ExamTeacher record);

    ExamTeacher selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExamTeacher record);

    int updateByPrimaryKey(ExamTeacher record);

    List<Integer> selectByExamId(Integer examId);

    /**
     * 通过试卷id删除数据
     * */
    int deleteByExamId(Integer examId);
}
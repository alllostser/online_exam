package com.exam.dao;

import com.exam.pojo.ExamStudent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExamStudentMapper {
    int deleteByPrimaryKey(Integer esId);

    /**
     * 插入数据
     * */
    int insert(ExamStudent record);

    int insertSelective(ExamStudent record);

    ExamStudent selectByPrimaryKey(Integer esId);

    int updateByPrimaryKeySelective(ExamStudent record);

    int updateByPrimaryKey(ExamStudent record);

    /**
     * 查询考生id集合
     * */
    List<Integer> findStudentIdsByExamIds(Integer examId);

    /**
     * 通过试卷id删除数据
     * */
    int deleteByExamId(Integer examId);

    /**
     * 根据exam的id和stu的id获取单条记录
     */
    ExamStudent selectByExamIdAndStuId(@Param("exmaId")Integer examId, @Param("studentId")Integer studentId);
}
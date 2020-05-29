package com.exam.dao;

import com.exam.pojo.Exam;
import com.exam.pojo.vo.ExamVo;

import java.util.List;

public interface ExamMapper {
    /**
     * 查找一条数据byid
     * */
    Exam selectOneById(Integer examId);

    /**
     *试卷列表
     **/
    List<Exam> queryAll(Exam exam);

    /**
     * 添加试卷
     * */
    int addExam(ExamVo examVo);

    /**
     * 更新一条数据byid
     * */
    int updateExamById(ExamVo examVo);

    /**
     * 批量删除通过examid
     * */
    int delectExamByExamId(Integer[] integers);

    /**
     * 获取记录条数
     * */
    Long getCountAll();
}
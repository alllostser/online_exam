package com.exam.service;

import com.exam.commons.ServerResponse;
import com.exam.pojo.ExamQuestion;

import java.util.List;

public interface ExamQuestionService {
    /**
     * 添加试卷试题关联表
     */
    ServerResponse addExamQuestion(ExamQuestion examQuestion);

    /**
     * 通过试卷id查找试题id
     */
    List<Integer> findQuestionIdsByExamId(Integer examId);

    /**
     * 通过试卷id删除数据
     */
    int deleteByExamId(Integer examId);

    /**
     * 通过试卷id查找试题
     */
    List<ExamQuestion> selectExamQuestionListByExamId(Integer examId);
}

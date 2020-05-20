package com.exam.service;

import com.exam.commons.ServerResponse;
import com.exam.pojo.Exam;
import com.exam.pojo.vo.ExamVo;

import java.util.List;


public interface ExamService {
    /**
     * 试卷列表
     * */

    ServerResponse findExamList(Exam exam, Integer pageNum, Integer pageSize, String orderBy);

    /**
     * 添加试卷
     * */
    ServerResponse addExam(ExamVo examVo);

    /**
     * 修改试卷
     * */
    ServerResponse updateExam(ExamVo examVo);

    /**
     * 根据id获取examVo
     * */
    ServerResponse getExamPoById(Integer examId);

    /**
     * 删除试卷
     * */
    ServerResponse delectExam(String ids);

    /**
     * 考生获取考试列表
     * */
    ServerResponse findExamListForStu(Exam exam, Integer id, Integer pageNum, Integer pageSize, String orderBy);

}

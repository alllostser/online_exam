package com.exam.service;

import com.exam.commons.ServerResponse;
import com.exam.commons.TableDataInfo;
import com.exam.pojo.Exam;
import com.exam.pojo.vo.ExamVo;


public interface ExamService {
    /**
     * 试卷列表
     * */

    TableDataInfo findExamList(Exam exam, Integer pageNum, Integer pageSize, String orderBy);

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
     *
     * @return*/
    TableDataInfo findExamListForStu(Exam exam, Integer id, Integer pageNum, Integer pageSize, String orderBy);

    /**
     *  根据examId获取学生的考试的试卷信息
     * */
    ServerResponse getExamForStudentByExamId(Integer examId,Integer StudentId);

    /**
     * 查找需要review的试卷
     *
     * @return*/
    TableDataInfo findExamListToReview(Exam exam, Integer teacherId,Integer pageNum, Integer pageSize, String orderBy);

    /**
     * 获取exam的详细信息，方便老师review
     * */
    ServerResponse findExamDetailToReview(Integer examId, Integer studentId);
}

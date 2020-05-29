package com.exam.service;

import com.exam.commons.ServerResponse;
import com.exam.pojo.ExamRecord;

public interface ExamRecordService {
    /**
     * 根据examId,questionId,StudentId获取考试记录
     * */
    ExamRecord selectRecordByExamIdAndQuestionIdAndStuId(Integer examId, Integer questionId, Integer studentId);

    /**
     * 考试记录,考生每次答题后保存答题记录
     */
    ServerResponse insertOrUpdateRecord(ExamRecord record);

    /**
     * 更新其最终成绩
     * */
    int updateRecordFinalScore(ExamRecord examRecord);

    /**
     *教师更新最终成绩
     * */
    ServerResponse teacherReviewRecord(ExamRecord examRecord);

    /**
     * 设置完成试卷的review
     * */
    ServerResponse finishReview(Integer examId, Integer stuId);
}

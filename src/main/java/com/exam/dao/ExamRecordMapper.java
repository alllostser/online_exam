package com.exam.dao;

import com.exam.pojo.ExamRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExamRecordMapper {
    /**
     * 根据examId,questionId,StudentId获取考试详情
     * */
    ExamRecord selectRecordByExamIdAndQuestionIdAndStuId(@Param("examId") Integer examId, @Param("questionId") Integer id, @Param("stuId") Integer studentId);

    /**
     * 存在就更新，不存在就新增
     */
    int insertOrUpdateRecord(ExamRecord record);

    /**
     * 更新其最终成绩
     * */
    int updateRecordFinalScore(ExamRecord examRecord);

    /**
     * 根据Exam的id和stud的id查询出所有试题的记录，计算总得分
     * */
    List<ExamRecord> selectRecordByExamIdAndStuId(@Param("examId") Integer examId, @Param("stuId") Integer stuId);
}
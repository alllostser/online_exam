package com.exam.dao;

import com.exam.pojo.ExamStudent;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ExamStudentMapper {
    /**
     * 插入数据
     * */
    int insert(ExamStudent record);

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

    /**
     * 根据试卷的id和学生的id，更换它的状态
     * */
    int updateStatusByExamIdAndStuId(@Param("examId") Integer examId, @Param("stuId") Integer stuId, @Param("status") String status);

    /**
     * 根据studentId和exam的id更新成绩
     * */
    int updateReadingAndTotalScoreByStuIdAndExamId(@Param("stuId") Integer stuId, @Param("examId") Integer examId, @Param("score") BigDecimal score, @Param("reading") String reading);

    /**
     * 根据examId获取ExamStudent实体类的信息
     * */
    List<ExamStudent> selectByExamId(Integer examId);

    /**
     * 根据studentId和exam的id更新成绩和添加阅卷人id
     * */
    int updateReadingAndTotalScoreAndReviewerIdByStuIdAndExamId(@Param("stuId") Integer stuId, @Param("examId") Integer examId, @Param("score") BigDecimal score, @Param("reading") String reading,@Param("reviewerId") Integer reviewerId);
}
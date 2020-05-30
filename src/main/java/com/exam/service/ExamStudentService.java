package com.exam.service;

import com.exam.commons.ServerResponse;
import com.exam.commons.TableDataInfo;
import com.exam.pojo.ExamStudent;
import com.exam.pojo.vo.ScoreVo;

import java.math.BigDecimal;
import java.util.List;

public interface ExamStudentService {
    /**
     * 插入数据
     * */
    ServerResponse insert(ExamStudent examStudent);

    /**
     * 查询考生id集合
     * */
    List<Integer> findStudentIdsByExamIds(Integer examId);

    /**
     * 通过试卷id删除数据
     * */
    int deleteByExamId(Integer examId);

    /**
     * 通过examId和studentId查询数据
     * */
    ExamStudent selectByExamIdAndStuId(Integer examId, Integer id);

    /**
     *结束考试,交卷
     * */
    ServerResponse finishExam(Integer examId, Integer userId);

    /**
     * 根据examId获取ExamStudent实体类的信息
     * */
    List<ExamStudent> selectByExamId(Integer examId);

    /**
     * 根据studentId和exam的id更新成绩
     * */
    int updateReadingAndTotalScoreAndReviewerIdByStuIdAndExamId(Integer stuId, Integer examId, BigDecimal score, String reading,Integer reviewerId);

    /**
     * 成绩详情
     * */
    TableDataInfo findScoreList(ScoreVo scoreVo, Integer examId, Integer pageNum, Integer pageSize, String orderBy);



}

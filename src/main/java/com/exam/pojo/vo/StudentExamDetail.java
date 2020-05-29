package com.exam.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 给前端传递考试数据的载体
 */
@Data
public class StudentExamDetail {
    private Integer examId;
    //试卷名称
    private String examName;
    //考生id
    private Integer studentId;
    //考试持续时间
    private Long lastTime;
    //考试开始时间
    private String startDate;
    //试卷的总分
    private BigDecimal score;
    //学生考试的总分
    private BigDecimal totalScore;

    private List<QuestionVo> radioQuestion;
    private List<QuestionVo> checkboxQuestion;
    private List<QuestionVo> judgeQuestion;
    private List<QuestionVo> shortQuestion;
    private List<QuestionVo> balckQuestion;
}

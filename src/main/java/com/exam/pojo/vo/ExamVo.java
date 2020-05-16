package com.exam.pojo.vo;


import lombok.Data;

import java.util.Date;
@Data
public class ExamVo {
    private static final long serialVersionUID = 922283745093904434L;

    private Integer examId;
    //试卷名
    private String examName;
    //考试开始时间
    private Date examStartDate;
    //考试持续的时间，单位为分钟
    private Long examLastTime;
    //批阅者的id
    private Integer reviewerId;
    //试卷的总分
    private Double score;

    //学生考试总分
    private Double totalScore;

    /**
     * 试题的id
     */
    private Integer[] ids;
    /**
     * 试题的id变成str
     */
    private String idsStr;
    /**
     * 选择参加考试的学生的id
     */
    private Integer[] studentIds;

    /**
     * 当前考试是否参加
     */
    private Boolean accessed;

    /**
     * 当前试卷的学生的id
     */
    private Integer studentId;
    /**
     * 学生的姓名
     */
    private String studentName;
    /**
     * 是否已经批阅
     */
    private String reading;
}

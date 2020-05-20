package com.exam.pojo.Bo;

import com.exam.pojo.Question;
import com.exam.pojo.SysUser;
import com.exam.pojo.vo.UserVo;
import com.exam.utils.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
public class ExamBo extends BaseEntity {
    private static final long serialVersionUID = 922283745093904434L;

    private Integer examId;
    //试卷名
    private String examName;
    //考试开始时间
    private Date examStartDate;
    //考试持续的时间，单位为分钟
    private Long examLastTime;
    //批阅者集合
    private List<UserVo> reviewers;
    //试卷的总分
    private BigDecimal score;

    //学生考试总分
    private BigDecimal totalScore;

    private String createDate;

    private String updateDate;

    /**
     * 试题的集合
     */
    private List<Question> questions;

    /**
     * 选择参加考试的学生
     */
    private List<UserVo> students;

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

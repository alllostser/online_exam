package com.exam.pojo.vo;

import com.exam.utils.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ExamReturnVo
 * @Description //TODO
 * @Author GuXinYu
 * @Date 2020/5/23 19:10
 * @Version 1.0
 **/
@Data
public class ExamReturnVo  {
    private Integer examId;

    private String examName;

    private String examStartDate;

    private Long examLastTime;

    private String reviewerIds;

    private String ids;

    /**
     * 选择参加考试的学生的id
     */
    private String studentIds;

    private String createBy;

    private String createDate;

    private String updateDate;

    private String updateBy;

    private BigDecimal score;

    /**
     * 当前考试是否参加
     */
    private Boolean accessed;

    //学生考试总分
    private BigDecimal totalScore;
}

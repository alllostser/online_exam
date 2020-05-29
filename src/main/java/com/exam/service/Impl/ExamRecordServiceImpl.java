package com.exam.service.Impl;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.dao.ExamRecordMapper;
import com.exam.pojo.ExamRecord;
import com.exam.pojo.SysUser;
import com.exam.service.ExamRecordService;
import com.exam.service.ExamStudentService;
import com.exam.utils.BigDecimalUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName examRecordServiceImpl
 * @Description //TODO
 * @Author GuXinYu
 * @Date 2020/5/21 21:37
 * @Version 1.0
 **/
@Service
public class ExamRecordServiceImpl implements ExamRecordService {
    @Resource
    private ExamRecordMapper examRecordMapper;
    @Resource
    private ExamStudentService examStudentService;

    /**
     * 根据examId,questionId,StudentId获取考试记录
     * @param examId
     * @param questionId
     * @param studentId
     * @return
     */
    @Override
    public ExamRecord selectRecordByExamIdAndQuestionIdAndStuId(Integer examId, Integer questionId, Integer studentId) {
        ExamRecord examRecord = examRecordMapper.selectRecordByExamIdAndQuestionIdAndStuId(examId,questionId,studentId);
        return examRecord;
    }

    /**
     * 考试记录,考生每次答题后保存答题记录
     * @param record
     * @return
     */
    @Override
    public ServerResponse insertOrUpdateRecord(ExamRecord record) {
        //非空判断
        if (record == null || record.getExamId() == null || record.getAnswer() == null || record.getQuestionId() == null) {
            return ServerResponse.serverResponseByFail(500, "数据异常，请刷新页面重试或者联系管理员");
        }
        int result = examRecordMapper.insertOrUpdateRecord(record);
        if (result <=0 ) {
            return ServerResponse.serverResponseByFail(500, "数据异常，请刷新页面重试或者联系管理员");
        } else if (result == 1) {
            return ServerResponse.serverResponseBySucess("当前答案已保存");
        } else if (result == 2) {
            return ServerResponse.serverResponseBySucess("当前答案已更新");
        } else {
            return ServerResponse.serverResponseBySucess(result);
        }
    }

    /**
     * 更新其最终成绩
     * */
    @Override
    public int updateRecordFinalScore(ExamRecord examRecord) {
        int i = examRecordMapper.updateRecordFinalScore(examRecord);
        return i;
    }

    /**
     * 教师更新最终成绩
     * @param examRecord
     * @return
     */
    @Override
    @RequiresRoles({"teacher"})
    public ServerResponse teacherReviewRecord(ExamRecord examRecord) {
        int i = updateRecordFinalScore(examRecord);
        return ServerResponse.serverResponseBySucess(i);
    }

    /**
     * 设置完成试卷的review
     * @param examId
     * @param stuId
     * @return
     */
    @Override
    @RequiresRoles({"teacher"})
    public ServerResponse finishReview(Integer examId, Integer stuId) {
        //根据Exam的id和stud的id查询出所有试题的记录，计算总得分
        List<ExamRecord> records = examRecordMapper.selectRecordByExamIdAndStuId(examId, stuId);
        //遍历record
        BigDecimal score = new BigDecimal("0");
        for (ExamRecord record : records) {
            score = BigDecimalUtil.add(score.doubleValue(),record.getFinalScore().doubleValue());
        }
        SysUser loginUesr = (SysUser) SecurityUtils.getSubject().getPrincipal();
        int result = examStudentService.updateReadingAndTotalScoreAndReviewerIdByStuIdAndExamId(stuId, examId, score, Consts.ExamStatusEnum.CHECK_THE_TEST_PAPER_YES,loginUesr.getId());
        if (result<=0){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(result);
    }
}

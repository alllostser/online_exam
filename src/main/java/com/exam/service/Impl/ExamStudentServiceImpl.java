package com.exam.service.Impl;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.dao.ExamStudentMapper;
import com.exam.pojo.ExamQuestion;
import com.exam.pojo.ExamRecord;
import com.exam.pojo.ExamStudent;
import com.exam.pojo.Question;
import com.exam.service.ExamQuestionService;
import com.exam.service.ExamRecordService;
import com.exam.service.ExamStudentService;
import com.exam.service.QuestionService;
import com.exam.utils.BigDecimalUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 试卷考生关联表service层
 * */
@Service
public class ExamStudentServiceImpl implements ExamStudentService {
    @Resource
    private ExamStudentMapper examStudentMapper;
    @Resource
    private ExamQuestionService examQuestionService;
    @Resource
    private QuestionService questionService;
    @Resource
    private ExamRecordService examRecordService;
    /**
     * 插入数据
     * */
    @Override
    public ServerResponse insert(ExamStudent examStudent) {
        if (examStudent == null){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(),Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        //操作数据库
        int insert = examStudentMapper.insert(examStudent);
        if (insert<=0){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(insert);
    }

    /**
     * 查询考生id集合
     * */
    @Override
    public List<Integer> findStudentIdsByExamIds(Integer examId) {
        List<Integer> studentIds = examStudentMapper.findStudentIdsByExamIds(examId);
        return studentIds;
    }

    /**
     * 通过试卷id删除数据
     * */
    @Override
    public int deleteByExamId(Integer examId) {
        int result = examStudentMapper.deleteByExamId(examId);
        return result;
    }

    /**
     * 通过examId和studentId查询数据
     * */
    @Override
    public ExamStudent selectByExamIdAndStuId(Integer examId, Integer id) {
        ExamStudent examStudent = examStudentMapper.selectByExamIdAndStuId(examId,id);
        return examStudent;
    }

    @Override
    public ServerResponse finishExam(Integer examId, Integer userId) {
        //阅卷客观题
        ReadingExamObjective(examId, userId);
        int result = examStudentMapper.updateStatusByExamIdAndStuId(examId, userId, Consts.ExamStatusEnum.TAKE_THE_EXAN_YES);
        if (result <=0 ) {
            return ServerResponse.serverResponseByFail(500, "数据异常，请联系管理员");
        }
        return ServerResponse.serverResponseBySucess(result);
    }
    /**
     * 批阅所有的客观题
     *
     * @param examId
     * @param stuId
     */
    private void ReadingExamObjective(Integer examId, Integer stuId) {
        //查询出所有的question id
        List<ExamQuestion> examQuestions = examQuestionService.selectExamQuestionListByExamId(examId);
        //获取所有的题的数量
        int count = examQuestions.size();
        BigDecimal totalScore =new BigDecimal("0") ;//试卷考试总分
        int index = 0;//试题数量
        for (ExamQuestion examQuestion : examQuestions) {
            //获取对应的question的信息
            ServerResponse questionResponse = questionService.selectOneById(examQuestion.getQuestionId());
            if (!questionResponse.isSucess()){
                continue;
            }
            Question question = (Question) questionResponse.getData();
            ExamRecord examRecord = examRecordService.selectRecordByExamIdAndQuestionIdAndStuId(examId, question.getId(), stuId);
            switch (question.getType()) {
                //单选和多选,判断
                case "1":
                case "2":
                case "4":
//                    if (examRecord.getAnswer()!=null)
                    if (question.getAnswer().equals(examRecord.getAnswer())) {
                        examRecord.setFinalScore(question.getScore());
                        totalScore = BigDecimalUtil.add(totalScore.doubleValue(),question.getScore().doubleValue()) ;
                    } else {
                        examRecord.setFinalScore(new BigDecimal("0"));
                    }
                    index++;
                    break;
            }
            int result = examRecordService.updateRecordFinalScore(examRecord);
        }
        //如果这两者相等，说明只有客观题，不需要老师来review
        if (index == count) {
            //更新试卷的状态为已阅
            examStudentMapper.updateReadingAndTotalScoreByStuIdAndExamId(stuId, examId, totalScore, Consts.ExamStatusEnum.CHECK_THE_TEST_PAPER_YES);
        }
    }

    /**
     * 根据examId获取ExamStudent实体类的信息
     * @param examId
     * @return
     */
    @Override
    public List<ExamStudent> selectByExamId(Integer examId) {
        List<ExamStudent> examStudents = examStudentMapper.selectByExamId(examId);
        return examStudents;
    }

    /**
     * 根据studentId和exam的id更新成绩
     * @param stuId
     * @param examId
     * @param score
     * @param reading
     * @return
     */
    @Override
    public int updateReadingAndTotalScoreAndReviewerIdByStuIdAndExamId(Integer stuId, Integer examId, BigDecimal score, String reading,Integer reviewerId) {
        int result = examStudentMapper.updateReadingAndTotalScoreAndReviewerIdByStuIdAndExamId(stuId, examId, score, reading,reviewerId);
        return result;
    }

}

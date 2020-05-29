package com.exam.service.Impl;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.dao.ExamQuestionMapper;
import com.exam.pojo.ExamQuestion;
import com.exam.service.ExamQuestionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExamQuestionServiceImpl implements ExamQuestionService {
    @Resource
    private ExamQuestionMapper examQuestionMapper;

    /**
     * 添加试卷试题关联表
     *
     * @param examQuestion
     * @return
     */
    @Override
    public ServerResponse addExamQuestion(ExamQuestion examQuestion) {
        //1.非空判断
        if (examQuestion == null) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(), Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        //2.操作数据库
        int result = examQuestionMapper.insert(examQuestion);
        if (result <= 0) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(), Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(result);
    }

    /**
     * 通过试卷id查找试题id
     *
     * @param examId
     * @return
     */
    @Override
    public List<Integer> findQuestionIdsByExamId(Integer examId) {
        List<Integer> questionIds = examQuestionMapper.findQuestionIdsByExamId(examId);
        return questionIds;
    }

    /**
     * 通过试卷id删除数据
     */
    @Override
    public int deleteByExamId(Integer examId) {
        int result = examQuestionMapper.deleteByExamId(examId);
        return result;
    }

    /**
     * 通过试卷id查找试题
     *
     * @param examId
     * @return
     */
    @Override
    public List<ExamQuestion> selectExamQuestionListByExamId(Integer examId) {
        List<ExamQuestion> examQuestions = examQuestionMapper.selectExamQuestionListByExamId(examId);
        return examQuestions;
    }
}

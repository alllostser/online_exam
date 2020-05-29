package com.exam.service.Impl;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.commons.TableDataInfo;
import com.exam.dao.QuestionMapper;
import com.exam.pojo.Question;
import com.exam.pojo.SysUser;
import com.exam.pojo.vo.QuestionVo;
import com.exam.service.QuestionService;
import com.exam.utils.Convert;
import com.exam.utils.GuavaCacheUtils;
import com.exam.utils.PoToVoUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Resource
    private QuestionMapper questionMapper;

    /**
     * 获取试题列表
     */
    @Override
    @RequiresRoles({"teacher"})
    public TableDataInfo findQuestionList(Question question, Integer pageNum, Integer pageSize, String orderBy) {
        if (GuavaCacheUtils.getKey("questionCount")==null){//如何缓存为空
            Long count = questionMapper.getCountAll();
            GuavaCacheUtils.setKey("questionCount",Long.toString(count));//存入缓存
        }
        PageHelper.startPage(pageNum, pageSize);
        if (orderBy != null && !"".equals(orderBy)) {
            if (orderBy.contains("&")) {
                //filedname&desc/filedname&asc
                String[] orderbys = orderBy.split("&");
                PageHelper.orderBy(orderbys[0] + " " + orderbys[1]);
            }
        }
        List<Question> questions = questionMapper.queryAll(question);
        if (questions == null || questions.size() <= 0) {
            return TableDataInfo.ResponseByFail(0, "没有找到任何试题信息");
        }
        List<QuestionVo> questionVos = new ArrayList<>();
        for (Question ques : questions) {
            QuestionVo questionVo = PoToVoUtil.questionPoToVO(ques);
            questionVos.add(questionVo);
        }
//        PageInfo pageInfo = new PageInfo(questionVos);
        return TableDataInfo.ResponseBySucess("", Long.valueOf(GuavaCacheUtils.getKey("questionCount")),questionVos);
    }

    /**
     * 添加试题
     */
    @Override
    @RequiresRoles({"teacher"})
    public ServerResponse insert(Question question) {
//        执行增删操作删除缓存
        GuavaCacheUtils.setKey("questionCount","null");
        //1.非空判断
        if (question.getTitle() == null || "".equals(question.getTitle())) {
            return ServerResponse.serverResponseByFail(Consts.QuestionStatusEnum.TITLE_CANNOT_BE_NULL.getStatus(), Consts.QuestionStatusEnum.TITLE_CANNOT_BE_NULL.getDesc());
        }
        if (question.getScore() == null || question.getScore().doubleValue() < 0) {
            return ServerResponse.serverResponseByFail(Consts.QuestionStatusEnum.SCORE_NOT_TRUE.getStatus(), Consts.QuestionStatusEnum.SCORE_NOT_TRUE.getDesc());
        }
        if (question.getAnswer() == null || "".equals(question.getAnswer())) {
            return ServerResponse.serverResponseByFail(Consts.QuestionStatusEnum.TRUE_ANSWER_CANNOT_BE_NULL.getStatus(), Consts.QuestionStatusEnum.TRUE_ANSWER_CANNOT_BE_NULL.getDesc());
        }
        if (Consts.QuestionStatusEnum.SINGLE_CHOICE.equals(question.getType())) {
            int cont = 0;
            if (question.getOptionA() == null || "".equals(question.getOptionA())) {
                cont++;
            }
            if (question.getOptionB() == null || "".equals(question.getOptionB())) {
                cont++;
            }
            if (question.getOptionC() == null || "".equals(question.getOptionC())) {
                cont++;
            }
            if (question.getOptionD() == null || "".equals(question.getOptionD())) {
                cont++;
            }
            if (cont > 2) {
                return ServerResponse.serverResponseByFail(Consts.QuestionStatusEnum.OPTION_COUNT_NOT_TRUE.getStatus(), Consts.QuestionStatusEnum.OPTION_COUNT_NOT_TRUE.getDesc());
            }
        }
        if (Consts.QuestionStatusEnum.MULTIPLE_CHOICE.equals(question.getType())) {
            int cont = 0;
            if (question.getOptionA() == null || "".equals(question.getOptionA())) {
                cont++;
            }
            if (question.getOptionB() == null || "".equals(question.getOptionB())) {
                cont++;
            }
            if (question.getOptionC() == null || "".equals(question.getOptionC())) {
                cont++;
            }
            if (question.getOptionD() == null || "".equals(question.getOptionD())) {
                cont++;
            }
            if (cont > 1) {
                return ServerResponse.serverResponseByFail(Consts.QuestionStatusEnum.OPTION_COUNT_NOT_TRUE.getStatus(), Consts.QuestionStatusEnum.OPTION_COUNT_NOT_TRUE.getDesc());
            }
        }
        if (Consts.QuestionStatusEnum.TRUE_OR_FALSE_QUESTION.equals(question.getType())) {
            int cont = 0;
            if (question.getOptionA() == null || "".equals(question.getOptionA())) {
                cont++;
            }
            if (question.getOptionA() == null || "".equals(question.getOptionA())) {
                cont++;
            }
            if (question.getOptionA() == null || "".equals(question.getOptionA())) {
                cont++;
            }
            if (question.getOptionA() == null || "".equals(question.getOptionA())) {
                cont++;
            }
            if (cont < 2) {
                return ServerResponse.serverResponseByFail(Consts.QuestionStatusEnum.OPTION_COUNT_NOT_TRUE.getStatus(), Consts.QuestionStatusEnum.OPTION_COUNT_NOT_TRUE.getDesc());
            }
        }
        //2.获取当前登录用户
        SysUser login_user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        question.setCreateBy(login_user.getId());
        System.out.println("question" + login_user);
        //3.操作数据库
        int result = questionMapper.insert(question);
        if (result <= 0) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(), Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(result);
    }

    /**
     * 修改试题
     */
    @Override
    @RequiresRoles({"teacher"})
    public ServerResponse updateQuestion(Question question) {
        //1.非空判断
        if (question.getId() == null) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(),Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        SysUser principal = (SysUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录用户
        question.setUpdateBy(principal.getId());
        //2.操作数据库
        int result = questionMapper.updateQuestion(question);
        if (result <= 0){//更新失败
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(result);
    }

    /**
     * 通过id删除试题
     * */
    @Override
    @RequiresRoles({"teacher"})
    public ServerResponse deleteQuestion(Integer id) {
        //        执行增删操作删除缓存
        GuavaCacheUtils.setKey("questionCount","null");
        //1.非空判断
        if (id == null) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(),Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        //2.操作数据库
        int result = questionMapper.deleteByPrimaryKey(id);
        if (result<=0){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(result);
    }

    /**
     * 通过id批量删除试题
     * */
    @Override
    @RequiresRoles({"teacher"})
    public ServerResponse deleteQuestion(String ids) {
        //        执行增删操作删除缓存
        GuavaCacheUtils.setKey("questionCount","null");
        //1.非空判断
        if (ids == null || StringUtils.isBlank(ids)) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(),Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        Integer[] integers = Convert.toIntArray(ids);
        int result = questionMapper.deleteByIds(integers);
        if (result<=0){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(result);
    }

    /**
     * 通过id查找一条试题
     * @param id
     * @return
     */
    @Override
    public ServerResponse selectOneById(Integer id) {
        if (id == null){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(),Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        //操作数据库
        Question question = questionMapper.selectOneById(id);
        return ServerResponse.serverResponseBySucess(question);
    }

    /**
     * 通过问题id查询试题集合
     * */
    @Override
    public List<Question> findQuestionListByExamId(List<Integer> questionIds) {
        List<Question>  questions = questionMapper.findQuestionListByExamId(questionIds);
        return questions;
    }
}

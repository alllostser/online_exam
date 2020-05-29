package com.exam.service.Impl;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.commons.TableDataInfo;
import com.exam.dao.ExamMapper;
import com.exam.pojo.*;
import com.exam.pojo.Bo.ExamBo;
import com.exam.pojo.vo.*;
import com.exam.service.*;
import com.exam.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {
    @Resource
    private ExamMapper examMapper;
    @Resource
    private ExamQuestionService examQuestionService;
    @Resource
    private QuestionService questionService;
    @Resource
    private ExamStudentService examStudentService;
    @Resource
    private ExamTeacherService examTeacherService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private ExamRecordService examRecordService;

    @Override
//    @RequiresRoles({"teacher"})
    public TableDataInfo findExamList(Exam exam, Integer pageNum, Integer pageSize, String orderBy) {
        if (GuavaCacheUtils.getKey("examCount")==null){//如果缓存为空怎重新获取，存入缓存
           Long count= examMapper.getCountAll();
           //将总记录条数放入缓存
           GuavaCacheUtils.setKey("examCount",Long.toString(count) );
       }
//        if (exam.getCreateDate()!=null){
//
//        }
        PageHelper.startPage(pageNum, pageSize);
        if (orderBy != null && !"".equals(orderBy)) {
            if (orderBy.contains("&")) {
                //filedname&desc/filedname&asc
                String[] orderbys = orderBy.split("&");
                PageHelper.orderBy(orderbys[0] + " " + orderbys[1]);
            }
        }
        List<Exam> exams = examMapper.queryAll(exam);
        if (exams == null || exams.size() <= 0) {
            return TableDataInfo.ResponseByFail(404, "没有找到任何试卷信息");
        }
        //将返回exams转为examReturnVo返回
        List<ExamReturnVo> examReturnVoList = new ArrayList<>();
        for (Exam temp : exams) {
            ExamReturnVo examReturnVo = new ExamReturnVo();
            if (temp.getCreateBy()!=null){
                ServerResponse createBy = sysUserService.findUserById(temp.getCreateBy());
                UserVo user = (UserVo) createBy.getData();
                examReturnVo.setCreateBy(user.getNickName());
            }
            if (temp.getUpdateBy()!=null){
                ServerResponse updateBy = sysUserService.findUserById(temp.getUpdateBy());
                UserVo user = (UserVo) updateBy.getData();
                examReturnVo.setUpdateBy(user.getNickName());
            }
            //获取判卷人id
            List<Integer> ReviewerIds = examTeacherService.selectByExamId(temp.getExamId());
            if (ReviewerIds.size()>0){
                StringBuffer reviewerIdsBuffer = new StringBuffer();
                for (Integer reviewerId : ReviewerIds) {
                    reviewerIdsBuffer.append(reviewerId+",");
                }
                String reviewerIds = reviewerIdsBuffer.toString();
                reviewerIds=reviewerIds.substring(0, reviewerIds.length() -1);
                examReturnVo.setReviewerIds(reviewerIds);
            }
            //获取考生id
            List<ExamStudent> examStudents = examStudentService.selectByExamId(temp.getExamId());
            if (examStudents.size()>0){
                StringBuffer studentIdsBuffer = new StringBuffer();
                for (ExamStudent examStudent : examStudents) {
                    studentIdsBuffer.append(examStudent.getStudentId()+",");
                }
                String studentIds = studentIdsBuffer.toString();
                studentIds = studentIds.substring(0, studentIds.length()-1);
                examReturnVo.setStudentIds(studentIds);
            }

            //获取试题id
            List<ExamQuestion> examQuestions = examQuestionService.selectExamQuestionListByExamId(temp.getExamId());
            if (examQuestions.size()>0){
                StringBuffer questionIdsBuffer = new StringBuffer();
                for (ExamQuestion examQuestion : examQuestions) {
                    questionIdsBuffer.append(examQuestion.getQuestionId()+",");
                }
                String questionIds = questionIdsBuffer.toString();
                questionIds = questionIds.substring(0, questionIds.length()-1);
                examReturnVo.setIds(questionIds);
            }
            examReturnVo.setExamId(temp.getExamId());
            examReturnVo.setExamName(temp.getExamName());
            examReturnVo.setExamStartDate(TimeUtils.dateToStr(temp.getExamStartDate()));
            examReturnVo.setExamLastTime(temp.getExamLastTime());
            examReturnVo.setCreateDate(TimeUtils.dateToStr(temp.getCreateDate(),"yyyy-MM-dd"));
            examReturnVo.setUpdateDate(TimeUtils.dateToStr(temp.getUpdateDate(),"yyyy-MM-dd"));
            examReturnVo.setScore(temp.getScore());
            examReturnVoList.add(examReturnVo);
        }
//        PageInfo pageInfo = new PageInfo(exams);
        return TableDataInfo.ResponseBySucess("",Long.valueOf(GuavaCacheUtils.getKey("examCount")),examReturnVoList);
    }


    /**
     * 添加考试
     * @param examVo
     * @return
     */
    @Override
    @RequiresRoles({"teacher"})
    public ServerResponse addExam(ExamVo examVo) {
        //执行增删操作，清空缓存
        GuavaCacheUtils.setKey("examCount","null");
        //插入试卷
        SysUser principal = (SysUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录用户
        examVo.setCreateBy(principal.getId());
        int result = examMapper.addExam(examVo);
        if (result <= 0) {
            return ServerResponse.serverResponseByFail(Consts.ExamStatusEnum.ADD_EXAM_FAILED.getStatus(), Consts.ExamStatusEnum.ADD_EXAM_FAILED.getDesc());
        }
        Integer[] ids = Convert.toIntArray(examVo.getIds());;//获取所有试题id数组
        BigDecimal score = new BigDecimal(0);
        if (ids != null && ids.length > 0) {
            //设置exam和question的关联
            for (Integer id : ids) {
                ExamQuestion examQuestion = new ExamQuestion();
                examQuestion.setExamId(examVo.getExamId());
                examQuestion.setQuestionId(id);
                ServerResponse response = examQuestionService.addExamQuestion(examQuestion);
                if (!response.isSucess()) {
                    return response;
                }
                ServerResponse question = questionService.selectOneById(id);
                if (!question.isSucess()) {
                    return question;
                }
                Question data = (Question) question.getData();
                //设置试卷的总分
                score = BigDecimalUtil.add(score.doubleValue(), data.getScore().doubleValue());
            }
        }
        examVo.setScore(score);
        ServerResponse examByResponse = updateExamById(examVo);
        if (!examByResponse.isSucess()) {
            return examByResponse;
        }
        //获取参加考试的学生的信息
        Integer[] studentIds = Convert.toIntArray(examVo.getStudentIds());
        if (studentIds != null) {
            for (Integer studentId : studentIds) {
                ExamStudent examStudent = new ExamStudent();
                examStudent.setExamId(examVo.getExamId());
                examStudent.setStudentId(studentId);
                ServerResponse response = examStudentService.insert(examStudent);
                if (!response.isSucess()) {
                    return response;
                }
            }
        }
        //获取批卷人id
        Integer[] reviewerIds = Convert.toIntArray(examVo.getReviewerIds());
        if (reviewerIds != null) {
            for (Integer reviewerId : reviewerIds) {
                ExamTeacher examTeacher = new ExamTeacher();
                examTeacher.setExamId(examVo.getExamId());
                examTeacher.setTeacherId(reviewerId);
                ServerResponse response = examTeacherService.insert(examTeacher);
                if (!response.isSucess()) {
                    return response;
                }
            }
        }
        return ServerResponse.serverResponseBySucess(result);
    }

    /**
     * 根据id更新试卷
     */
    private ServerResponse updateExamById(ExamVo examVo) {
        if (examVo.getExamId() != null) {
            int result = examMapper.updateExamById(examVo);
            if (result <= 0) {
                return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(), Consts.StatusEnum.UPDATA_FAILED.getDesc());
            }
            return ServerResponse.serverResponseBySucess(result);
        } else {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(), Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
    }

    /**
     * 根据id获取examBo
     */
    @Override
    public ServerResponse getExamPoById(Integer examId) {
        if (examId != null) {
            ExamBo examBo = new ExamBo();
            Exam exam = examMapper.selectOneById(examId);
            if (exam == null) {
                return ServerResponse.serverResponseByFail(304, "没有找到任何examId为" + examId + "的试卷信息");
            }
            examBo.setExamId(exam.getExamId());
            examBo.setExamName(exam.getExamName());
            examBo.setExamStartDate(exam.getExamStartDate());
            examBo.setExamLastTime(exam.getExamLastTime());
            examBo.setCreateDate(TimeUtils.dateToStr(exam.getCreateDate()));
            examBo.setUpdateDate(TimeUtils.dateToStr(exam.getUpdateDate()));
            examBo.setCreateBy(exam.getCreateBy());
            examBo.setUpdateBy(exam.getUpdateBy());
            List<Integer> teacherids = examTeacherService.selectByExamId(exam.getExamId());
            if (teacherids.size() > 0) {
                List<SysUser> teachers = sysUserService.findUserByIds(teacherids);
                //将查询到的教师数据转化为vo对象
                List<UserVo> userVos = new ArrayList<>();
                for (SysUser teacher : teachers) {
                    UserVo userVo = PoToVoUtil.SysUserToVo(teacher);
                    userVos.add(userVo);
                }
                examBo.setReviewers(userVos);
            }

            examBo.setScore(exam.getScore());
            List<Integer> questionIds = examQuestionService.findQuestionIdsByExamId(examId);
            if (questionIds.size() > 0) {
                //查询试题集合
                List<Question> questions = questionService.findQuestionListByExamId(questionIds);
                examBo.setQuestions(questions);
            }
            List<Integer> studentIds = examStudentService.findStudentIdsByExamIds(exam.getExamId());
            if (studentIds.size() > 0) {
                //查询考生集合
                List<SysUser> students = sysUserService.findUserByIds(studentIds);
                //将查询到的学生数据转化为vo对象
                List<UserVo> studentVos = new ArrayList<>();
                for (SysUser student : students) {
                    UserVo userVo = PoToVoUtil.SysUserToVo(student);
                    studentVos.add(userVo);
                }
                examBo.setStudents(studentVos);
            }
            return ServerResponse.serverResponseBySucess(examBo);
        }

        return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(), Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
    }

    /**
     * 删除试卷
     *
     * @param ids
     * @return
     */
    @Override
    public ServerResponse delectExam(String ids) {
        //执行增删操作，清空缓存
        GuavaCacheUtils.setKey("examCount","null");
        Integer[] integers = Convert.toIntArray(ids);
        if (integers.length > 0) {
            for (Integer id : integers) {
                //删除与试题关联
                int row1 = examQuestionService.deleteByExamId(id);
                //删除和学生的关联
                int row2 = examStudentService.deleteByExamId(id);
                //删除和教师的关联
                int row3 = examTeacherService.deleteByExamId(id);
            }
            int result = examMapper.delectExamByExamId(integers);
            if (result <= 0) {
                return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(), Consts.StatusEnum.UPDATA_FAILED.getDesc());
            }
            return ServerResponse.serverResponseBySucess(result);//返回影响行数
        }
        return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(), Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
    }


    /**
     * 修改试卷
     *
     * @param examVo
     * @return
     */
    @Override
    @RequiresRoles({"teacher"})
    public ServerResponse updateExam(ExamVo examVo) {
        //1.删除关联
        //删除关联，根据examid
        int row1 = examQuestionService.deleteByExamId(examVo.getExamId());
        if (row1 <= 0) {
            System.out.println("row1" + row1);
        }
        //删除和学生的关联
        int row2 = examStudentService.deleteByExamId(examVo.getExamId());
        if (row2 <= 0) {
            System.out.println("row2" + row2);
        }
        //删除和教师的关联
        int row3 = examTeacherService.deleteByExamId(examVo.getExamId());
        if (row3 <= 0) {
            System.out.println("row3" + row3);
        }
        //2.重新设置关联
        BigDecimal score = new BigDecimal("0");
        Integer[] ids = Convert.toIntArray(examVo.getIds());//获取试题id数组
        if (ids != null) {
            for (Integer id : ids) {//设置试卷与试题关联
                ExamQuestion examQuestion = new ExamQuestion();
                examQuestion.setExamId(examVo.getExamId());
                examQuestion.setQuestionId(id);
                ServerResponse response = examQuestionService.addExamQuestion(examQuestion);
                if (!response.isSucess()) {
                    return response;
                }
                ServerResponse question = questionService.selectOneById(id);
                if (!question.isSucess()) {
                    return question;
                }
                Question data = (Question) question.getData();
                //设置试卷的总分
                score = BigDecimalUtil.add(score.doubleValue(), data.getScore().doubleValue());
            }
        }
        examVo.setScore(score);
        //重置和学生的关联
        if (examVo.getStudentIds() != null) {
            for (Integer studentId : Convert.toIntArray(examVo.getStudentIds())) {
                ExamStudent examStudent = new ExamStudent();
                examStudent.setExamId(examVo.getExamId());
                examStudent.setStudentId(studentId);
                ServerResponse response = examStudentService.insert(examStudent);
                if (!response.isSucess()) {
                    return response;
                }
            }
        }
        //重置批卷人与试卷关联
        Integer[] reviewerIds = Convert.toIntArray(examVo.getReviewerIds());
        if (reviewerIds != null) {
            for (Integer reviewerId : reviewerIds) {
                ExamTeacher examTeacher = new ExamTeacher();
                examTeacher.setExamId(examVo.getExamId());
                examTeacher.setTeacherId(reviewerId);
                ServerResponse response = examTeacherService.insert(examTeacher);
                if (!response.isSucess()) {
                    return response;
                }
            }
        }
        SysUser principal = (SysUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录用户
        examVo.setUpdateBy(principal.getId());
        ServerResponse response = updateExamById(examVo);
        return response;
    }

    /**
     * 考生获取考试列表
     *
     * @param exam
     * @param id
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public TableDataInfo findExamListForStu(Exam exam, Integer id, Integer pageNum, Integer pageSize, String orderBy) {
        TableDataInfo examListResponse = findExamList(exam, pageNum, pageSize, orderBy);
        if (!examListResponse.isSucess()) {
            return examListResponse;
        }
        Long aLong = examListResponse.getCount();
        List<ExamReturnVo> examList = (List<ExamReturnVo>) examListResponse.getData();
        List<ExamReturnVo> returnExamVos = new ArrayList<>();
        Iterator<ExamReturnVo> iterator = examList.iterator();
        while (iterator.hasNext()) {
            ExamReturnVo examVo = iterator.next();
//            ExamVo examVo = PoToVoUtil.examPoToVo(temp);
            ExamStudent examStudent = examStudentService.selectByExamIdAndStuId(examVo.getExamId(), id);
            //说明没有指定该学生可以考试
            if (examStudent == null) {
                //删除此条记录
                iterator.remove();
                aLong--;
                continue;
            } else if ("0".equals(examStudent.getStatus())) {
                //说明还没有做，可以做
                examVo.setAccessed(false);
            } else if ("1".equals(examStudent.getStatus())) {
                //说明已经做过了，只能显示不能做
                examVo.setAccessed(true);
            }
            examVo.setTotalScore(examStudent.getTotalScore());
            returnExamVos.add(examVo);
        }
        //构建返回数据的分页模型
//        PageInfo returnPageInfo = new PageInfo(returnExamVos);
        return TableDataInfo.ResponseBySucess("", aLong,returnExamVos);
    }

    /**
     * 根据examId获取学生的考试的试卷信息
     *
     * @param examId
     * @return
     */
    @Override
    public ServerResponse getExamForStudentByExamId(Integer examId,Integer StudentId) {
        Exam exam = examMapper.selectOneById(examId);
        if (exam == null){
            return ServerResponse.serverResponseByFail(404,"该考试不存在！");
        }
        //根据试卷id获取与试题关联信息
        List<ExamQuestion> examQuestions = examQuestionService.selectExamQuestionListByExamId(examId);
        if (examQuestions.size()<=0){
            return ServerResponse.serverResponseByFail(404,"该考试无试题信息！");
        }
        //新建5中类型的List集合
        List<QuestionVo> radioQuestion = new ArrayList<>();
        List<QuestionVo> checkboxQuestion = new ArrayList<>();
        List<QuestionVo> blackQuestion = new ArrayList<>();
        List<QuestionVo> judgeQuestion = new ArrayList<>();
        List<QuestionVo> shortQuestion = new ArrayList<>();
        //创建返回模型
        StudentExamDetail studentExamDetail = new StudentExamDetail();
        //设置试卷信息
        studentExamDetail.setExamName(exam.getExamName());
        studentExamDetail.setLastTime(exam.getExamLastTime());
        studentExamDetail.setStartDate(TimeUtils.dateToStr(exam.getExamStartDate()));
        studentExamDetail.setExamId(examId);
        BigDecimal score = new BigDecimal("0");
        for (ExamQuestion examQuestion : examQuestions) {
            ServerResponse questionResponse = questionService.selectOneById(examQuestion.getQuestionId());
            Question question = (Question) questionResponse.getData();
            QuestionVo questionVo = PoToVoUtil.questionPoToVO(question);
            //查询出已经保存的有的数据，方便页面回显
            ExamRecord examRecord = examRecordService.selectRecordByExamIdAndQuestionIdAndStuId(examId, question.getId(), StudentId);
            String answer = "";
            if (examRecord == null) {
                questionVo.setFinalScore(new BigDecimal("0"));
            } else {
                questionVo.setFinalScore(examRecord.getFinalScore());
                answer = examRecord.getAnswer();
            }
            score =BigDecimalUtil.add(score.doubleValue(), question.getScore().doubleValue());

            setAnswer(answer, questionVo);//设置答案

            if ("1".equals(question.getType())) {
                //获取单选
                radioQuestion.add(questionVo);
            } else if ("2".equals(question.getType())) {
                //获取多选
                checkboxQuestion.add(questionVo);
            } else if ("3".equals(question.getType())) {
                //获取填空
                blackQuestion.add(questionVo);
            } else if ("4".equals(question.getType())) {
                //获取判断
                judgeQuestion.add(questionVo);
            } else if ("5".equals(question.getType())) {
                //获取简答
                shortQuestion.add(questionVo);
            }
        }
        studentExamDetail.setCheckboxQuestion(checkboxQuestion);
        studentExamDetail.setRadioQuestion(radioQuestion);
        studentExamDetail.setShortQuestion(shortQuestion);
        studentExamDetail.setJudgeQuestion(judgeQuestion);
        studentExamDetail.setBalckQuestion(blackQuestion);

        studentExamDetail.setScore(score);
        return ServerResponse.serverResponseBySucess(studentExamDetail);
    }
    /**
     * 设置答案
     *
     * @param answer
     * @param questionVo
     */
    private void setAnswer(String answer, QuestionVo questionVo) {
        //单选 多选ABC
        switch (questionVo.getType()) {
            case "1":
            case "2":
                String[] split = answer.split(",");
                for (String s : split) {
                    if ("A".equals(s)) {
                        questionVo.setOptionACheckedStu("A");
                    } else if ("B".equals(s)) {
                        questionVo.setOptionBCheckedStu("B");
                    } else if ("C".equals(s)) {
                        questionVo.setOptionCCheckedStu("C");
                    } else if ("D".equals(s)) {
                        questionVo.setOptionDCheckedStu("D");
                    }
                }
                break;
            //判断
            case "4":
                if ("1".equals(answer)) {
                    questionVo.setJudgeAnswer1Stu("1");
                } else if ("0".equals(answer)) {
                    questionVo.setJudgeAnswer0Stu("0");
                }
                break;
            case "3":
            case "5":
                questionVo.setTextAnswerStu(answer);
                break;
            default:
                break;
        }
    }

    /**
     * 查找需要review的试卷
     * @param exam
     * @param teacherId
     * @return
     */
    @Override
    public TableDataInfo findExamListToReview(Exam exam, Integer teacherId, Integer pageNum, Integer pageSize, String orderBy) {
        TableDataInfo examListResponse = findExamList(exam,pageNum,pageSize,orderBy);
        if (!examListResponse.isSucess()){
            return examListResponse;
        }
        List<ExamReturnVo> examList = (List<ExamReturnVo>) examListResponse.getData();
        int count = examList.size();
        List<ExamPaperVo> examsReturn = new ArrayList<ExamPaperVo>();
        for (ExamReturnVo temp : examList) {
            Integer examId = temp.getExamId();
            List<Integer> teacherIds = examTeacherService.selectByExamId(examId);
            if (!teacherIds.contains(teacherId)){//过滤掉不是当前登录教师试卷的记录
                count--;
                continue;
            }
            List<ExamStudent> examStudents = examStudentService.selectByExamId(examId);
            for (ExamStudent examStudent : examStudents) {
                //过滤掉还未参加考试的学生
                if (Consts.ExamStatusEnum.TAKE_THE_EXAN_NO.equals(examStudent.getStatus())) {
                    continue;
                }
                ExamPaperVo returnExam = new ExamPaperVo();
                returnExam.setExamId(examId);
                returnExam.setReviewerId(examStudent.getReviewerId());
                returnExam.setStudentId(examStudent.getStudentId());
                returnExam.setExamName(temp.getExamName());
                returnExam.setStudentName(((UserVo)sysUserService.findUserById(examStudent.getStudentId()).getData()).getLoginName());
                returnExam.setReading(examStudent.getReading());
                returnExam.setExamStartDate(TimeUtils.strToDate(temp.getExamStartDate()));
                examsReturn.add(returnExam);
            }
        }

        //构建分页模型
//        PageInfo pageInfo1 = new PageInfo(examsReturn);
        return TableDataInfo.ResponseBySucess("", (long) count,examsReturn);
    }

    /**
     * 获取exam的详细信息，方便老师review
     * @param examId
     * @param studentId
     * @return
     */
    @Override
    @RequiresRoles({"teacher"})
    public ServerResponse findExamDetailToReview(Integer examId, Integer studentId) {
        //根据试卷id获取与试题关联信息
        List<ExamQuestion> examQuestions = examQuestionService.selectExamQuestionListByExamId(examId);
        Exam exam = examMapper.selectOneById(examId);
        //新建5中类型的List集合
        List<QuestionVo> radioQuestion = new ArrayList<>();
        List<QuestionVo> checkboxQuestion = new ArrayList<>();
        List<QuestionVo> blackQuestion = new ArrayList<>();
        List<QuestionVo> judgeQuestion = new ArrayList<>();
        List<QuestionVo> shortQuestion = new ArrayList<>();
        //创建返回模型
        StudentExamDetail studentExamDetail = new StudentExamDetail();
        //设置试卷信息
        studentExamDetail.setExamName(exam.getExamName());
        studentExamDetail.setLastTime(exam.getExamLastTime());
        studentExamDetail.setStartDate(TimeUtils.dateToStr(exam.getExamStartDate()));
        studentExamDetail.setExamId(examId);

        //获取学生总成绩
        ExamStudent examStudent = examStudentService.selectByExamIdAndStuId(examId, studentId);
        //设置学生总成绩
        studentExamDetail.setTotalScore(examStudent.getTotalScore());
        BigDecimal score = new BigDecimal("0");
        for (ExamQuestion examQuestion : examQuestions) {
            ServerResponse questionResponse = questionService.selectOneById(examQuestion.getQuestionId());
            Question question = (Question) questionResponse.getData();
            QuestionVo questionVo = PoToVoUtil.questionPoToVO(question);
            //查询出已经保存的有的数据，方便页面回显
            ExamRecord examRecord = examRecordService.selectRecordByExamIdAndQuestionIdAndStuId(examId, question.getId(), studentId);
            String answer = "";
            if (examRecord == null) {
                continue;
            } else {
                questionVo.setFinalScore(examRecord.getFinalScore());
                answer = examRecord.getAnswer();
            }
            score =BigDecimalUtil.add(score.doubleValue(), question.getScore().doubleValue());

            setAnswer(answer, questionVo);//设置答案

            if ("1".equals(question.getType())) {
                //获取单选
                radioQuestion.add(questionVo);
            } else if ("2".equals(question.getType())) {
                //获取多选
                checkboxQuestion.add(questionVo);
            } else if ("3".equals(question.getType())) {
                //获取填空
                blackQuestion.add(questionVo);
            } else if ("4".equals(question.getType())) {
                //获取判断
                judgeQuestion.add(questionVo);
            } else if ("5".equals(question.getType())) {
                //获取简答
                shortQuestion.add(questionVo);
            }
        }
        studentExamDetail.setCheckboxQuestion(checkboxQuestion);
        studentExamDetail.setRadioQuestion(radioQuestion);
        studentExamDetail.setShortQuestion(shortQuestion);
        studentExamDetail.setJudgeQuestion(judgeQuestion);
        studentExamDetail.setBalckQuestion(blackQuestion);
        studentExamDetail.setStudentId(studentId);
        studentExamDetail.setScore(score);
        return ServerResponse.serverResponseBySucess(studentExamDetail);
    }
}

package com.exam.service.Impl;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.dao.ExamMapper;
import com.exam.pojo.*;
import com.exam.pojo.Bo.ExamBo;
import com.exam.pojo.vo.ExamVo;
import com.exam.pojo.vo.UserVo;
import com.exam.service.*;
import com.exam.utils.BigDecimalUtil;
import com.exam.utils.Convert;
import com.exam.utils.PoToVoUtil;
import com.exam.utils.TimeUtils;
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

    @Override
    @RequiresRoles({"teacher"})
    public ServerResponse findExamList(Exam exam, Integer pageNum, Integer pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize);
        if (orderBy != null && !"".equals(orderBy)) {
            if (orderBy.contains("&")){
                //filedname&desc/filedname&asc
                String[] orderbys = orderBy.split("&");
                PageHelper.orderBy(orderbys[0] + " " + orderbys[1]);
            }
        }
        List<Exam> exams = examMapper.queryAll(exam);
        if (exams == null || exams.size()<=0){
            return ServerResponse.serverResponseByFail(0,"没有找到任何试卷信息");
        }
        PageInfo pageInfo = new PageInfo(exams);
        return ServerResponse.serverResponseBySucess(pageInfo);
    }

    @Override
    @RequiresRoles({"teacher"})
    public ServerResponse addExam(ExamVo examVo) {
        //插入试卷
        SysUser principal = (SysUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录用户
        examVo.setCreateBy(principal.getId());
        int result = examMapper.addExam(examVo);
        if (result<=0){
            return ServerResponse.serverResponseByFail(Consts.ExamStatusEnum.ADD_EXAM_FAILED.getStatus(),Consts.ExamStatusEnum.ADD_EXAM_FAILED.getDesc());
        }
        Integer[] ids = examVo.getIds();//获取所有试题id数组
        BigDecimal score = new BigDecimal(0);
        if (ids != null && ids.length>0){
            //设置exam和question的关联
            for (Integer id : ids) {
                ExamQuestion examQuestion = new ExamQuestion();
                examQuestion.setExamId(examVo.getExamId());
                examQuestion.setQuestionId(id);
                ServerResponse response = examQuestionService.addExamQuestion(examQuestion);
                if (!response.isSucess()){
                    return response;
                }
                ServerResponse question = questionService.selectOneById(id);
                if (!question.isSucess()){
                    return question;
                }
                Question data = (Question) question.getData();
                //设置试卷的总分
                score=BigDecimalUtil.add(score.doubleValue(),data.getScore().doubleValue());
            }
        }
        examVo.setScore(score);
        ServerResponse examByResponse = updateExamById(examVo);
        if (!examByResponse.isSucess()){
            return examByResponse;
        }
        //获取参加考试的学生的信息
        Integer[] studentIds = examVo.getStudentIds();
        if (studentIds != null) {
            for (Integer studentId : studentIds) {
                ExamStudent examStudent = new ExamStudent();
                examStudent.setExamId(examVo.getExamId());
                examStudent.setStudentId(studentId);
                ServerResponse response = examStudentService.insert(examStudent);
                if (!response.isSucess()){
                    return response;
                }
            }
        }
        //获取批卷人id
        Integer[] reviewerIds = examVo.getReviewerIds();
        if (reviewerIds !=null){
            for (Integer reviewerId : reviewerIds) {
                ExamTeacher examTeacher =new ExamTeacher();
                examTeacher.setExamId(examVo.getExamId());
                examTeacher.setTeacherId(reviewerId);
                ServerResponse response = examTeacherService.insert(examTeacher);
                if (!response.isSucess()){
                    return response;
                }
            }
        }
        return ServerResponse.serverResponseBySucess(result);
    }
    /**
     * 根据id更新试卷
     * */
    private ServerResponse updateExamById(ExamVo examVo){
        if (examVo.getExamId()!=null){
           int result = examMapper.updateExamById(examVo);
           if (result<=0){
               return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
           }
           return ServerResponse.serverResponseBySucess(result);
        }else {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(),Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
    }

    /**
     * 根据id获取examVo
     * */
    @Override
    public ServerResponse getExamPoById(Integer examId) {
        if (examId != null){
            ExamBo examBo = new ExamBo();
            Exam exam = examMapper.selectOneById(examId);
            if (exam == null){
                return ServerResponse.serverResponseByFail(304,"没有找到任何examId为"+examId+"的试卷信息");
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
            if (teacherids.size()>0){
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
            if (questionIds.size()>0){
                //查询试题集合
                List<Question> questions = questionService.findQuestionListByExamId(questionIds);
                examBo.setQuestions(questions);
            }
            List<Integer> studentIds = examStudentService.findStudentIdsByExamIds(exam.getExamId());
            if (studentIds.size()>0){
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

        return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(),Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
    }

    /**
     * 删除试卷
     * @param ids
     * @return
     */
    @Override
    public ServerResponse delectExam(String ids) {
        Integer[] integers = Convert.toIntArray(ids);
        if (integers.length>0){
            for (Integer id : integers) {
                //删除与试题关联
                int row1 = examQuestionService.deleteByExamId(id);
                //删除和学生的关联
                int row2 = examStudentService.deleteByExamId(id);
                //删除和教师的关联
                int row3 = examTeacherService.deleteByExamId(id);
            }
            int result = examMapper.delectExamByExamId(integers);
            if (result<=0){
                return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
            }
            return ServerResponse.serverResponseBySucess(result);//返回影响行数
        }
        return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(),Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
    }


    /**
     * 修改试卷
     * @param examVo
     * @return
     */
    @Override
    @RequiresRoles({"teacher"})
    public ServerResponse updateExam(ExamVo examVo) {
        //1.删除关联
        //删除关联，根据examid
        int row1 = examQuestionService.deleteByExamId(examVo.getExamId());
        if (row1<=0){
            System.out.println("row1"+row1);
        }
        //删除和学生的关联
        int row2 = examStudentService.deleteByExamId(examVo.getExamId());
        if (row2<=0){
            System.out.println("row2"+row2);
        }
        //删除和教师的关联
        int row3 = examTeacherService.deleteByExamId(examVo.getExamId());
        if (row3<=0){
            System.out.println("row3"+row3);
        }
        //2.重新设置关联
        BigDecimal score = new BigDecimal("0");
        Integer[] ids = examVo.getIds();//获取试题id数组
        if (ids != null) {
            for (Integer id : ids) {//设置试卷与试题关联
                ExamQuestion examQuestion = new ExamQuestion();
                examQuestion.setExamId(examVo.getExamId());
                examQuestion.setQuestionId(id);
                ServerResponse response = examQuestionService.addExamQuestion(examQuestion);
                if (!response.isSucess()){
                    return response;
                }
                ServerResponse question = questionService.selectOneById(id);
                if (!question.isSucess()){
                    return question;
                }
                Question data = (Question) question.getData();
                //设置试卷的总分
                score=BigDecimalUtil.add(score.doubleValue(),data.getScore().doubleValue());
            }
        }
        examVo.setScore(score);
        //重置和学生的关联
        if (examVo.getStudentIds() != null) {
            for (Integer studentId : examVo.getStudentIds()) {
                ExamStudent examStudent = new ExamStudent();
                examStudent.setExamId(examVo.getExamId());
                examStudent.setStudentId(studentId);
                ServerResponse response = examStudentService.insert(examStudent);
                if (!response.isSucess()){
                    return response;
                }
            }
        }
        //重置批卷人与试卷关联
        Integer[] reviewerIds = examVo.getReviewerIds();
        if (reviewerIds !=null){
            for (Integer reviewerId : reviewerIds) {
                ExamTeacher examTeacher =new ExamTeacher();
                examTeacher.setExamId(examVo.getExamId());
                examTeacher.setTeacherId(reviewerId);
                ServerResponse response = examTeacherService.insert(examTeacher);
                if (!response.isSucess()){
                    return response;
                }
            }
        }
        SysUser principal = (SysUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录用户
        examVo.setUpdateBy(principal.getId());
        ServerResponse response = updateExamById(examVo);
        return response;
    }

    @Override
    public ServerResponse findExamListForStu(Exam exam, Integer id, Integer pageNum, Integer pageSize, String orderBy) {
        ServerResponse examListResponse = findExamList(exam, pageNum, pageSize, orderBy);
        if (!examListResponse.isSucess()){
            return examListResponse;
        }
        PageInfo<Exam> pageInfo = (PageInfo<Exam>) examListResponse.getData();
        List<Exam> examList = pageInfo.getList();
        List<ExamVo> returnExamVos = new ArrayList<>();
        Iterator<Exam> iterator = examList.iterator();
        while (iterator.hasNext()) {
            Exam temp = iterator.next();
            ExamVo examVo = PoToVoUtil.examPoToVo(temp);
            ExamStudent examStudent = examStudentService.selectByExamIdAndStuId(temp.getExamId(), id);
            //说明没有指定该学生可以考试
            if (examStudent == null) {
                //删除此条记录
                iterator.remove();
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
        PageInfo returnPageInfo = new PageInfo(returnExamVos);
        return ServerResponse.serverResponseBySucess(returnPageInfo);
    }




}

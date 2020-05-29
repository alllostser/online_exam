package com.exam.controller;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.commons.TableDataInfo;
import com.exam.pojo.Exam;
import com.exam.pojo.ExamRecord;
import com.exam.pojo.ExamStudent;
import com.exam.pojo.SysUser;
import com.exam.pojo.vo.StudentExamDetail;
import com.exam.pojo.vo.UserVo;
import com.exam.service.ExamRecordService;
import com.exam.service.ExamService;
import com.exam.service.ExamStudentService;
import com.exam.service.SysUserService;
import com.exam.utils.PoToVoUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TeacherReviewController
 * @Description //教师批卷控制层
 * @Author GuXinYu
 * @Date 2020/5/22 0:42
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@RequestMapping("/exam/teacher")
public class TeacherReviewController {
    //注入对象
    @Resource
    ExamService examService;
    @Resource
    ExamRecordService examRecordService;
    @Resource
    ExamStudentService examStudentService;
    @Resource
    SysUserService sysUserService;


    /**
     * 阅卷列表
     * @param exam
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping("/list.do")
    public TableDataInfo list(
            Exam exam,
            @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
            @RequestParam(required = false,defaultValue = "")String orderBy)
    {
        try {
            SysUser loginUesr = (SysUser) SecurityUtils.getSubject().getPrincipal();
            TableDataInfo response = examService.findExamListToReview(exam,loginUesr.getId(),pageNum,pageSize,orderBy);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return TableDataInfo.ResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }

    /**
     * 批卷,获取exam的详细信息，方便老师review
     * @param examId
     * @param studentId
     * @return
     */
    @GetMapping("/reviewView.do")
    public ServerResponse reviewStuExam( Integer examId,Integer studentId) {
        try {
            ServerResponse response = examService.findExamDetailToReview(examId, studentId);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }

    /**
     * 异步提交并更新单个试题的得分
     * @param examRecord
     * @return
     */
    @PostMapping("/reviewExam.do")
    public ServerResponse reviewExam(ExamRecord examRecord) {
        try {
            ServerResponse response = examRecordService.teacherReviewRecord(examRecord);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }

    /**
     * 完成阅卷，提交阅卷的结果，返回index界面，并更新exam_student中，total_Score的值
     * @param examId
     * @param stuId
     * @return
     */
    @PostMapping("/finishReview.do")
    public ServerResponse finishReview(Integer examId, Integer stuId) {
        try {
            SysUser loginUesr = (SysUser) SecurityUtils.getSubject().getPrincipal();
            ServerResponse response = examRecordService.finishReview(examId, stuId);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }

    /**
     * 展示试卷的详情，阅卷完成详情
     * @param examId
     * @param stuId
     * @return
     */
    @GetMapping("/detail.do")
    public ServerResponse examDetail( Integer examId,  Integer stuId) {
        try {
            Map<String,Object> map = new HashMap<>();
            ServerResponse examDetailToReview = examService.findExamDetailToReview(examId, stuId);
            if (!examDetailToReview.isSucess()){
                return examDetailToReview;
            }
            StudentExamDetail studentExamDetail = (StudentExamDetail) examDetailToReview.getData();
            map.put("exam",studentExamDetail);
            ServerResponse userById = sysUserService.findUserById(stuId);
            if (!userById.isSucess()){
                return userById;
            }
            UserVo student = (UserVo) userById.getData();
            map.put("student", student);
            ExamStudent examStudent = examStudentService.selectByExamIdAndStuId(examId, stuId);
            map.put("examStudent",examStudent);
            return ServerResponse.serverResponseBySucess(map);
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }


}

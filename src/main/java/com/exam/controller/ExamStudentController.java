package com.exam.controller;

import com.exam.commons.ServerResponse;
import com.exam.commons.TableDataInfo;
import com.exam.pojo.Exam;
import com.exam.pojo.ExamRecord;
import com.exam.pojo.SysUser;
import com.exam.service.ExamRecordService;
import com.exam.service.ExamService;
import com.exam.service.ExamStudentService;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 考生考试控制层
 */
@RestController
@CrossOrigin
@RequestMapping("/student/exam")
public class ExamStudentController {
    @Resource
    private ExamStudentService examStudentService;
    @Resource
    private ExamService examService;
    @Resource
    private ExamRecordService examRecordService;


    /**
     * 考生获取考试列表
     * @param exam
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo list(
            Exam exam,
            @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
            @RequestParam(required = false,defaultValue = "")String orderBy)
    {
        //设置只有在当前时间之前的考试可见
        Map map = new HashMap();
        map.put("endTime", new SimpleDateFormat("yyyy-MM-dd hh:ss:mm").format(new Date()));
        exam.setParams(map);
        //获取当前登录用户
        SysUser SysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        TableDataInfo examList = examService.findExamListForStu(exam, SysUser.getId(),pageNum,pageSize,orderBy);
        return examList;
    }

    /**
     * 开始考试，获取试题列表/获取考试详情
     * @param examId
     * @return
     */
    @GetMapping("/detail.do")
    public ServerResponse startExam(Integer examId) {
        SysUser loginUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        ServerResponse examPoResponse = examService.getExamForStudentByExamId(examId,loginUser.getId());
        return examPoResponse;
    }

    /**
     * 考试记录,考生每次答题后调用
     * @param record
     * @return
     */
    @PostMapping("/record.do")
    public ServerResponse record(ExamRecord record) {
        SysUser loginUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        record.setStuId(loginUser.getId());
        ServerResponse response = examRecordService.insertOrUpdateRecord(record);
       return response;
    }
    /**
     * 结束考试,交卷接口
     * @param examId
     * @return
     */
    @PostMapping("/submit.do")
    public ServerResponse submitPaper(Integer examId) {
        SysUser loginUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        ServerResponse response = examStudentService.finishExam(examId, loginUser.getId());
        return response;
    }

}

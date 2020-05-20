package com.exam.controller;

import com.exam.commons.ServerResponse;
import com.exam.pojo.Exam;
import com.exam.pojo.SysUser;
import com.exam.pojo.vo.ExamVo;
import com.exam.service.ExamService;
import com.exam.service.StudentExamService;
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
public class StudentExamController {
    @Resource
    private StudentExamService studentExamService;
    @Resource
    private ExamService examService;


    /**
     * 考生获取考试列表
     * @param exam
     * @return
     */
    @GetMapping("/list")
    public ServerResponse list(
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
        ServerResponse examList = examService.findExamListForStu(exam, SysUser.getId(),pageNum,pageSize,orderBy);
        return examList;
    }


}

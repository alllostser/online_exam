package com.exam.controller;


import com.exam.commons.ServerResponse;
import com.exam.pojo.vo.ExamVo;
import com.exam.service.ExamService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 考试管理控制层
 */
@RestController
@CrossOrigin
@RequestMapping("/exam")
public class ExamController {
    @Resource
    private ExamService examService;
//    @RequestMapping("/add")
//    public ServerResponse addExam(ExamVo examVo){
//        examService.addExam(examVo)
//    }

}

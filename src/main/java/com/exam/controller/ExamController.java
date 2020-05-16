package com.exam.controller;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.pojo.Exam;
import com.exam.service.ExamService;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 试卷管理控制层
 */
@RestController
@CrossOrigin
@RequestMapping("/exam")
public class ExamController {
    @Resource
    private ExamService examService;

    /**
     * 试卷列表
     * @param exam
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping("/list.do")
    public ServerResponse list(
            Exam exam,
            @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
            @RequestParam(required = false,defaultValue = "")String orderBy)
    {
        try {
            System.out.println("-------"+exam+"********");
            ServerResponse response = examService.findExamList(exam,pageNum,pageSize,orderBy);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }
    public ServerResponse addExam(@RequestBody Exam exam) {
        try {
//            ServerResponse response = examService.findExamList(exam,pageNum,pageSize,orderBy);
//            return response;
            return null;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }

}

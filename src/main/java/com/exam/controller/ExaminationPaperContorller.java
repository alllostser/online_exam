package com.exam.controller;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.pojo.Exam;
import com.exam.pojo.vo.ExamVo;
import com.exam.service.ExamService;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName ExaminationPaperContorller
 * @Description //TODO
 * @Author GuXinYu
 * @Date 2020/5/18 18:22
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@RequestMapping("/exam/paper")
public class ExaminationPaperContorller {
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

    /**
     * 添加试卷
     * @param examVo
     * @return
     */
    @PostMapping("/add.do")
    public ServerResponse addExam(@RequestBody ExamVo examVo) {
        try {
            ServerResponse response = examService.addExam(examVo);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }

    /**
     * 根据id获取examVo
     * @param examId
     * @return
     */
    @GetMapping("/get.do")
    public ServerResponse getExamPoById(Integer examId){
        try {
            ServerResponse response = examService.getExamPoById(examId);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }
    /**
     * 修改试卷
     * @param examVo
     * @return
     */
    @PutMapping("/update.do")
    public ServerResponse updateExam(ExamVo examVo) {
        try {
            ServerResponse response = examService.updateExam(examVo);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }

    @DeleteMapping("/delete.do")
    public ServerResponse delete(String ids) {
        try {
            ServerResponse response = examService.delectExam(ids);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }
}

package com.exam.controller;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.commons.TableDataInfo;
import com.exam.pojo.Question;
import com.exam.service.QuestionService;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/question")
@CrossOrigin
public class QuestionController {
    @Resource
    private QuestionService questionService;

    /**
     *通过实体作为筛选条件查询试题列表
     * @param question
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping("/list.do")
    public TableDataInfo list(
            Question question,
            @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
            @RequestParam(required = false,defaultValue = "")String orderBy)
    {
        try {
            System.out.println("-------"+question+"********");
            TableDataInfo response = questionService.findQuestionList(question,pageNum,pageSize,orderBy);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return TableDataInfo.ResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }

    /**
     * 添加试题
     * @param question
     * @return
     */
    @PostMapping("/add.do")
    public ServerResponse addQuestion(@RequestBody Question question) {
        try {
            ServerResponse response = questionService.insert(question);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }

    /**
     * 修改试题
     * @param question
     * @return
     */
    @PutMapping("/update.do")
    public ServerResponse updateQuestion(@RequestBody Question question) {
        try {
            ServerResponse response = questionService.updateQuestion(question);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }

    /**
     * 删除试题
     * @param ids
     * @return
     */
    @DeleteMapping("/delete.do")
    public ServerResponse deleteQuestion(String ids) {
        try {
            ServerResponse response = questionService.deleteQuestion(ids);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }
}

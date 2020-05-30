package com.exam.controller;

import com.exam.commons.ServerResponse;
import com.exam.commons.TableDataInfo;
import com.exam.pojo.vo.ScoreVo;
import com.exam.service.ExamStudentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName ScoreController
 * @Description //成绩管理Controller
 * @Author GuXinYu
 * @Date 2020/5/22 17:49
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@RequestMapping("/exam/score")
public class ScoreController {
    @Resource
    ExamStudentService examStudentService;

    @GetMapping("/list.do")
    public TableDataInfo list(
            ScoreVo scoreVo,
            Integer examId,
            @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
            @RequestParam(required = false,defaultValue = "")String orderBy)
    {
        TableDataInfo scoreResponse  = examStudentService.findScoreList(scoreVo,examId,pageNum,pageSize,orderBy);
        return scoreResponse;
    }
}

package com.exam.controller;

import com.exam.commons.ServerResponse;
import com.exam.pojo.vo.ScoreVo;
import com.exam.service.ExamStudentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName ScoreController
 * @Description //成绩管理的Controller
 * @Author GuXinYu
 * @Date 2020/5/22 17:49
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@RequestMapping("exam/score")
public class ScoreController {
    @Resource
    ExamStudentService examStudentService;
//
//    @GetMapping("/list")
//    @ResponseBody
//    public ServerResponse list(ScoreVo scoreVo) {
//        ServerResponse scoreResponse  = examStudentService.findScoreList(scoreVo);
//        return getDataTable(scores);
//    }
}

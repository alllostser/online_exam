package com.exam.controller;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.commons.TableDataInfo;
import com.exam.pojo.Notice;
import com.exam.pojo.SysUser;
import com.exam.service.NoticeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName NoticeController
 * @Description //公告控制层
 * @Author GuXinYu
 * @Date 2020/5/30 0:51
 * @Version 1.0
 **/
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Resource
    private NoticeService noticeService;

    /**
     * 公告列表
     * @param notice
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @GetMapping("/list.do")
    public TableDataInfo list(
            Notice notice,
            @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
            @RequestParam(required = false,defaultValue = "") String orderBy){
        TableDataInfo notices = noticeService.findNoticeList(notice,pageNum,pageSize,orderBy);
        return notices;
    }

    /**
     * 添加公告
     * @param notice
     * @return
     */
    @PostMapping("/add.do")
    public ServerResponse addSave(@RequestBody Notice notice) {
        try {
            ServerResponse response = noticeService.insert(notice);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());

        }
    }

    /**
     * 获取公告信息根据id
     * @param noticeId
     * @return
     */
    @GetMapping("/select.do")
    public ServerResponse select(Integer noticeId) {
        try {
            ServerResponse response = noticeService.queryById(noticeId);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());

        }
    }

    /**
     * 修改公告
     * @param notice
     * @return
     */
    @PutMapping("/update.do")
    public ServerResponse updateSave(@RequestBody Notice notice) {
        try {
            SysUser loginUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
            notice.setUpdateBy(loginUser.getId());
            ServerResponse response = noticeService.update(notice);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());

        }
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/delete.do")
    public ServerResponse delete(String ids) {
        try {
            ServerResponse response = noticeService.delete(ids);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());

        }
    }
}

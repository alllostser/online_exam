package com.exam.controller;

import com.exam.commons.Consts;
import com.exam.commons.KuaY;
import com.exam.commons.ServerResponse;
import com.exam.commons.TableDataInfo;
import com.exam.pojo.SysUser;
import com.exam.service.SysUserService;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class SysUserController extends KuaY {
    //注入service层
    @Resource
    SysUserService sysUserService;

    /**
     * 添加用户
     * @param user
     * @return
     */
    @PostMapping("/add.do")
    public ServerResponse addUser(@RequestBody SysUser user){
        try{
            ServerResponse response = sysUserService.addUser(user);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }

    }


    @RequestMapping("/select.do")
    public ServerResponse findUserById(Integer id){
        try{
            ServerResponse response = sysUserService.findUserById(id);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }

    /**
     *更新用户信息
     * @param sysUser
     * @return
     */
    @PostMapping("/update.do")
    public ServerResponse updateUser(@RequestBody SysUser sysUser) {
        try{
            ServerResponse response = sysUserService.updateUser(sysUser);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
    }

    /**
     * 用户列表搜索+动态排序
     * @param keyword  关键字
     * @param pageNum 第几页
     * @param pageSize 一页多少条数据
     * @param orderBy 排序字段filedname_desc/filedname_asc
     * @return
     */
        @GetMapping("/list.do")
        @CrossOrigin
        public TableDataInfo list(
                @RequestParam(required = false)Integer userType,
                @RequestParam(required = false,defaultValue = "")String keyword,
                @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
                @RequestParam(required = false,defaultValue = "")String orderBy
        ) {
            try {
                    TableDataInfo sysUsers = sysUserService.userList(userType,keyword,pageNum,pageSize,orderBy);
                return sysUsers;
            }catch (UnauthorizedException exception){//无权限
                return TableDataInfo.ResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());

            }
        }

    /**
     * 删除用户
     * @param ids
     * @return
     */
    @RequestMapping("/delete.do")
    public ServerResponse delete(String ids) {
        try {
            ServerResponse response = sysUserService.deleteSysUserByIds(ids);
            return response;
        }catch (UnauthorizedException exception){//无权限
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),Consts.StatusEnum.USER_LIMITED_AUTHORITY.getDesc());

        }
    }

}

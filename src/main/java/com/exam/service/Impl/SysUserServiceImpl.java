package com.exam.service.Impl;

import com.exam.commons.Consts;
import com.exam.commons.ServerResponse;
import com.exam.dao.SysUserMapper;
import com.exam.pojo.SysUser;
import com.exam.pojo.vo.UserVo;
import com.exam.service.SysUserService;
import com.exam.utils.Constants;
import com.exam.utils.PoToVoUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    SysUserMapper sysUserMapper;

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @Override
    @RequiresRoles({"admin"})
    public ServerResponse addUser(SysUser user) {
        //step1:1非空判断
        if (user.getLoginName() == null || "".equals(user.getLoginName())) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USERNAME_NOT_EMPTY.getStatus(), Consts.StatusEnum.USERNAME_NOT_EMPTY.getDesc());
        }
        if (StringUtils.isBlank(user.getPassword())) {//如果未设密码则使用默认密码
            user.setPassword(Constants.DEFAULT_PASSWORD);
        }
        //step2:操作数据库
        int result = sysUserMapper.insertSelective(user);
        if (result <= 0) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(), Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(Consts.StatusEnum.UPDATE_SUCCESS.getDesc());
    }

    /**
     * 根据id查找用户
     *
     * @param id
     * @return
     */
    @Override
    @RequiresRoles({"admin"})
    public ServerResponse findUserById(Integer id) {
        //step:1参数非空判断
        if (id == null || id < 0) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(), Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        //step2：查询数据库
        SysUser user = sysUserMapper.findUserById(id);
        if (user == null) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.USERID_NOT_EXISTS.getStatus(), Consts.StatusEnum.USERID_NOT_EXISTS.getDesc());
        }
        UserVo userVo = PoToVoUtil.SysUserToVo(user);
        return ServerResponse.serverResponseBySucess(userVo);
    }

    /**
     * 更新用户信息
     *
     * @param sysUser
     * @return
     */
    @Override
    @RequiresRoles({"admin"})
    @Transactional
    public ServerResponse updateUser(SysUser sysUser) {
        //step1:非空判断
        if (sysUser.getId() == null) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(), Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        //操作数据库
        int result = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if (result <= 0) {
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(), Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(Consts.StatusEnum.UPDATE_SUCCESS.getDesc());
    }

    /**
     * 用户列表搜索+动态排序
     *
     * @param userType
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    @RequiresRoles({"admin"})
    public ServerResponse userList(Integer userType, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        //step1:判读是否传递了userType和keyword
        if (userType == null && (keyword == null || "".equals(keyword))) {
            //前端没有传递userType和keyword,向前端返回空的数据
            //PageHelper
            PageHelper.startPage(pageNum, pageSize);
            if (orderBy != null && !"".equals(orderBy)) {
                if (orderBy.contains("&")){
                    //filedname&desc/filedname&asc
                    String[] orderbys = orderBy.split("&");
                    PageHelper.orderBy(orderbys[0] + " " + orderbys[1]);
                }
            }
            List<SysUser> usertList = sysUserMapper.selectAll();
            List<UserVo> userVos = new ArrayList<>();
            for (SysUser user : usertList) {
                UserVo userVo = PoToVoUtil.SysUserToVo(user);
                userVos.add(userVo);
            }
            PageInfo pageInfo = new PageInfo(userVos);
            return ServerResponse.serverResponseBySucess(pageInfo);
        }
        //step2:判断userType是否传递
//        if (userType != -1 || userType!= null) {//传递了userType
//            //查询userTypeq权限的所有用户
//            List<SysUser> sysUsers = sysUserMapper.selectByUserTypeAndkeyword(userType,keyword);
//        }
        //step3:判断keyword是否传递
        if (keyword != null && !"".equals(keyword)) {
            keyword = "%" + keyword + "%";
        }
        //step4：执行查询
        //写在查询前
        PageHelper.startPage(pageNum, pageSize);
        if (orderBy != null && !"".equals(orderBy)) {
            //filedname_desc/filedname_asc
            String[] orderbys = orderBy.split("_");
            PageHelper.orderBy(orderbys[0] + " " + orderbys[1]);
        }
        List<SysUser> sysUsers = sysUserMapper.selectByUserTypeAndkeyword(userType, keyword);
        //转化vo对象
        List<UserVo> userVos = new ArrayList<>();
        for (SysUser user : sysUsers) {
            UserVo userVo = PoToVoUtil.SysUserToVo(user);
            userVos.add(userVo);
        }
        //构建分页模型
        PageInfo pageInfo = new PageInfo(userVos);
        //step5：返回结果
        if (userVos.size() <= 0) {
            return ServerResponse.serverResponseBySucess("无查找结果");
        }
        return ServerResponse.serverResponseBySucess(pageInfo);
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @Override
    @RequiresRoles({"admin"})
    public ServerResponse deleteSysUserByIds(String ids) {
        //非空判断
        if (StringUtils.isEmpty(ids)) {
            return ServerResponse.serverResponseByFail(0, "没有选择任何用户");
        }
        //查数据库
       Integer id = Integer.parseInt(ids);
        SysUser user = sysUserMapper.selectByPrimaryKey(id);
        if (user==null){
            return ServerResponse.serverResponseByFail(0,"该用户已被删除");
        }
        //执行删除操作
        int row = sysUserMapper.deleteByPrimaryKey(id);
        if (row<=0){
            return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess(row);//返回受影响行数
    }

    /**
     * 用户登录查找-shiro调用
     * */
    @Override
    public SysUser findSysUserByLoginName(String username) {
        SysUser sysUser = sysUserMapper.selectByUsernameAndPassword2(username);
        return sysUser;
    }

}

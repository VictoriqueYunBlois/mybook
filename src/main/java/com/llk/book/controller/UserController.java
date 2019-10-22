package com.llk.book.controller;

import com.llk.book.annotation.LoginRequired;
import com.llk.book.common.DataGridDataSource;
import com.llk.book.common.PageBean;
import com.llk.book.model.Permission;
import com.llk.book.model.Role;
import com.llk.book.model.User;
import com.llk.book.service.*;
import com.llk.book.common.JsonData;
import com.llk.book.util.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public JsonData login(@RequestParam(value = "userName") String userName,
                          @RequestParam(value = "userPassword") String userPassword,
                          HttpServletRequest request,
                          HttpSession session) throws Exception {

        if (StringUtils.isEmpty(userName)) {
            return JsonData.fail("用户名不能为空！");
        }
        if (StringUtils.isEmpty(userPassword)) {
            return JsonData.fail("密码不能为空！");
        }
        User user = userService.findUserByUserName(userName);
        if (user == null) {
            return JsonData.fail("用户不存在！");
        }
        if (user.getUserState() == 0) {
            return JsonData.fail("账号已被禁用！请联系管理员！");
        }
        if (Md5Util.md5(userPassword, Md5Util.SALT).equals(user.getUserPassword())) {
            // 获取用户角色信息
            List<Role> roleList = roleService.findByUserId(user.getUserId());
            System.out.println(roleList);
            StringBuffer stringBuffer = new StringBuffer();
            for (Role role : roleList) {
                stringBuffer.append("," + role.getRoleName());
            }
            user.setRoles(stringBuffer.toString().replaceFirst(",", ""));
            session.setAttribute("user", user);
            // 获取用户权限信息
            List<Permission> permissions = permissionService.queryPermissionsByUser(user);
            Map<Integer, Permission> permissionMap = new HashMap<>();
            Permission root = null;
            Set<String> uriSet = new HashSet<>();
            for (Permission permission : permissions) {
                permissionMap.put(permission.getPermissionId(), permission);
                if (permission.getPermissionUrl() != null && !"".equals(permission.getPermissionUrl())) {
                    uriSet.add(permission.getPermissionUrl());
                }
            }
            session.setAttribute("authUriSet", uriSet);
            for (Permission permission : permissions) {
                Permission child = permission;
                if (child.getPermissionParentId() == null) {
                    root = permission;
                } else {
                    Permission parent = permissionMap.get(child.getPermissionParentId());
                    parent.getChildren().add(child);
                }
            }
            session.setAttribute("rootPermission", root);
            return JsonData.success();
        } else {
            return JsonData.fail("用户名或密码错误！");
        }
    }


    /**
     * 新增用户
     */
    @PostMapping("/save")
    @LoginRequired
    public JsonData saveUser(User user) {
        int count = userService.saveUser(user);
        if (count > 0) {
            return JsonData.success(count, "添加成功");
        } else {
            return JsonData.fail("添加失败");
        }

    }


    /**
     * 更新用户
     */
    @PutMapping("/update")
    @LoginRequired
    public JsonData updateUser(User user) {
        int count = userService.updateUser(user);
        if (count > 0) {
            return JsonData.success(count, "更新成功");
        } else {
            return JsonData.fail("更新失败");
        }

    }

    /**
     * 根据id删除用户
     */
    @DeleteMapping("/delete")
    @LoginRequired
    public JsonData deleteUser(@RequestParam(value = "userId") Long userId) {
        //TODO 删除用户前先根据用户id将用户角色关联表的记录删除
        roleService.deleteRoleUserRsByUserId(userId);
        int count = userService.deleteUser(userId);
        if (count > 0) {
            return JsonData.success(count, "删除成功");
        } else {
            return JsonData.fail("删除失败");
        }
    }

    /**
     * 根据用户id禁用用户
     */
    @PostMapping("/disable")
    @LoginRequired
    public JsonData disable(@RequestParam(value = "userId") Long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setUserState(0);
        int count = userService.updateUser(user);
        if (count > 0) {
            return JsonData.success(count, "禁用成功");
        } else {
            return JsonData.fail("禁用失败");
        }
    }

    /**
     * 根据id启用用户
     */
    @PostMapping("/enable")
    @LoginRequired
    public JsonData enable(@RequestParam(value = "userId") Long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setUserState(1);
        int count = userService.updateUser(user);
        if (count > 0) {
            return JsonData.success(count, "启用成功");
        } else {
            return JsonData.fail("启用失败");
        }
    }


    /**
     * 带条件服务端分页查询用户列表
     */
    @PostMapping("/list")
    @LoginRequired
    public DataGridDataSource<User> getUserList(@RequestParam(value = "userName", required = false, defaultValue = "") String userName,
                                                @RequestParam(value = "userTrueName", required = false, defaultValue = "") String userTrueName,
                                                @RequestParam(value = "userEmail", required = false, defaultValue = "") String userEmail,
                                                @RequestParam(value = "userPhone", required = false, defaultValue = "") String userPhone,
                                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                @RequestParam(value = "rows", required = false, defaultValue = "5") Integer rows) {

        PageBean pageBean = new PageBean(page, rows);
        Map<String, Object> map = new HashMap<>();
        map.put("userName", "%" + userName + "%");
        map.put("userTrueName", "%" + userTrueName + "%");
        map.put("userEmail", "%" + userEmail + "%");
        map.put("userPhone", "%" + userPhone + "%");
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        List<User> userList = userService.selectUserList(map);
        //查询用户角色
        for (User u : userList) {
            List<Role> roleList = roleService.findByUserId(u.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for (Role role : roleList) {
                stringBuffer.append("," + role.getRoleName());
            }
            u.setRoles(stringBuffer.toString().replaceFirst(",", ""));
        }
        int totalUser = userService.getTotalUser(map);
        DataGridDataSource<User> dataGridDataSource = new DataGridDataSource<>();
        dataGridDataSource.setTotal(totalUser);
        dataGridDataSource.setRows(userList);
        return dataGridDataSource;
    }

    /**
     * 用户角色设置(先删除当前用户拥有的角色关系, 再重新设置)
     */
    @PostMapping("/saveRoleSet")
    @LoginRequired
    public JsonData saveRoleSet(Long userId, Integer[] roleIds) {
        //先删除当前用户拥有的角色关系
        roleService.deleteRoleUserRsByUserId(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("roleIds", roleIds);
        int count = userService.insertUserRoles(map);
        if (count > 0) {
            return JsonData.success(count, "设置成功");
        } else {
            return JsonData.fail("设置失败");
        }
    }


    /**
     * 修改密码
     */
    @PostMapping("/modifyPassword")
    @LoginRequired
    public JsonData modifyPassword(String oldPassword, String newPassword, HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        User user = userService.findUserByUserId(currentUser.getUserId());
        if (!Md5Util.md5(oldPassword, Md5Util.SALT).equals(user.getUserPassword())) {
            return JsonData.fail("原密码错误");
        }
        user.setUserPassword(newPassword);
        int i = userService.updateUser(user);
        if (i > 0) {
            return JsonData.success(i, "修改成功");
        } else {
            return JsonData.fail("修改失败");
        }
    }


    /**
     * 查询用户信息(借书管理)
     */
    @PostMapping("/userInfo")
    @LoginRequired
    public JsonData userInfo(Long userId) {
        User user = userService.findUserByUserId(userId);
        user.setUserPassword(null);
        return JsonData.success(user);
    }
}

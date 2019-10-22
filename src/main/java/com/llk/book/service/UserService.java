package com.llk.book.service;

import com.llk.book.model.User;

import java.util.List;
import java.util.Map;

/**
 * UserService
 */
public interface UserService {

    /**
     * 根据用户id查询用户
     */
     User findUserByUserId(Long userId);


    /**
     * 根据用户名查询用户
     */
     User findUserByUserName(String userName);


    /**
     * 新增用户
     */
     int saveUser(User user);

    /**
     * 更新用户
     */
     int updateUser(User user);

    /**
     * 根据id删除用户
     */
     int deleteUser(Long userId);


    /**
     * 查询用户列表
     */
     List<User> selectUserList(Map<String, Object> map);


    /**
     * 查询用户总数
     */
     int getTotalUser(Map<String, Object> map);

    /**
     * 为用户分配角色
     */
     int insertUserRoles(Map<String, Object> map);
}

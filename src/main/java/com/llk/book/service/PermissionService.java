package com.llk.book.service;

import com.llk.book.model.Permission;
import com.llk.book.model.User;

import java.util.List;

/**
 * PermissionService
 */
public interface PermissionService {

    /**
     * 获取用户权限信息
     */
    List<Permission> queryPermissionsByUser(User user);

    /**
     * 获取所有权限列表
     */
    List<Permission> queryAll();

    /**
     * 通过角色id删除角色权限表的关联关系
     */
    void deleteRolePermissionRsByRoleId(Integer roleId);

    /**
     * 通过角色id查询已经分配的权限信息
     */
    List<Integer> queryPermissionIdsByRoleId(Integer roleId);
}

package com.llk.book.service;

import com.llk.book.model.Role;
import com.llk.book.model.User;

import java.util.List;
import java.util.Map;

/**
 * RoleService
 */
public interface RoleService {


    /**
     * 新增角色
     */
     int saveRole(Role role);


    /**
     * 更新角色
     */
     int updateRole(Role role);


    /**
     * 根据id删除角色
     */
     int deleteRole(Integer roleId);


    /**
     * 通过用户id删除用户角色表的关联关系
     */
     void deleteRoleUserRsByUserId(Long userId);


    /**
     * 通过角色id删除用户角色表的关联关系
     */
     void deleteRoleUserRsByRoleId(Integer roleId);


    /**
     * 查询角色列表
     */
     List<Role> selectRoleList(Map<String, Object> map);


    /**
     * 查询角色总数
     */
     int getTotalRole(Map<String, Object> map);


    /**
     * 根据用户id查找角色集合
     */
     List<Role> findByUserId(Long userId);


    /**
     * 为角色分配权限
     */
     int insertRolePermissions(Map<String, Object> map);
}

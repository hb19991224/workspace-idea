/**
 * Copyright (C), 2019-2019, 金科
 * FileName: UserDao
 * Author:  黄斌
 * Date:     2019/8/9 11:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.jk.dao;

import com.jk.model.*;

import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 斌
 * @create 2019/8/9
 * @since 1.0.0
 */
public interface UserDao {

    User login(String username);

    List<Tree> getTreeAll(Integer id);


    Integer queryUserCount(Map map);

    List<User> queryUser(Map map);

    List<Integer> getRoleByUserId(Integer roleId);

    List<Role> queryRole();

    int deleteRoleById(Integer uidTwo);

    int insert(UserRole urm);

    Integer queryRoleCount(Map map);

    List<Role> queryRolelist(Map map);

    List<String> getPermissionByRId(Integer roleId);

    List<Tree> getPermissionAll();

    int deleteRolePermissByRid(Integer roleId);

    int insertroletree(RoleTree rpm);

    Integer queryPermissionCount(Map map);

    List<Tree> queryTreelist(Map map);

    List<Role> query();

    void addrole(List<Role> list);
}

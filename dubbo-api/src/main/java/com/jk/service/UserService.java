/**
 * Copyright (C), 2019-2019, 金科
 * FileName: UserService
 * Author:  黄斌
 * Date:     2019/8/9 11:27
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.jk.service;

import com.jk.model.LogModel;
import com.jk.model.Role;
import com.jk.model.Tree;
import com.jk.model.User;

import java.util.HashMap;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 斌
 * @create 2019/8/9
 * @since 1.0.0
 */
public interface UserService {


    User login(String username);

    List<Tree> getTreeAll(Integer id);


    HashMap queryUser(Integer page, Integer rows);

    List<Role> getRoleByUserId(Integer roleId);

    int updateUserRole(Integer[] roleIds, Integer uidTwo);

    HashMap queryRole(Integer page, Integer rows);

    List<Tree> getPermissionByRId(Integer roleId);

    int updateRolePermiss(Integer[] perids, Integer roleId);

    HashMap queryPermission(Integer page, Integer rows);

    List<Role> query();

    void addrole(List<Role> list);

    HashMap queryLogList(Integer page, Integer rows);
}

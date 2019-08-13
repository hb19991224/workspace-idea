/**
 * Copyright (C), 2019-2019, 金科
 * FileName: UserServiceImpl
 * Author:  黄斌
 * Date:     2019/8/9 11:33
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.jk.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jk.dao.UserDao;
import com.jk.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
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
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public User login(String username) {
        return userDao.login(username);
    }

    @Override
    public List<Tree> getTreeAll(Integer id) {

        return userDao.getTreeAll(id);

    }

    @Override
    public HashMap queryUser(Integer page, Integer rows) {
       Map map = new HashMap();
       Integer total = userDao.queryUserCount(map);
       map.put("start",(page-1)*rows);
       map.put("end",rows);
       List<User> list = userDao.queryUser(map);
       HashMap mp = new HashMap<>();
       mp.put("total",total);
       mp.put("rows",list);
    return mp;
    }

    @Override
    public List<Role> getRoleByUserId(Integer roleId) {
        // 先查 自己的
        List<Integer> list = userDao.getRoleByUserId(roleId);
        // 再查所有的 角色
        List<Role> listtwo = userDao.queryRole();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < listtwo.size(); j++) {
                if (list.get(i) == listtwo.get(j).getId()) {
                    listtwo.get(j).setChecked("true");
                }
            }
        }
        return listtwo;
    }

    @Override
    public int updateUserRole(Integer[] roleIds, Integer uidTwo) {
        int i = userDao.deleteRoleById(uidTwo);
        if(i >=0){
            for (int j = 0; j < roleIds.length; j++) {
                UserRole urm = new  UserRole();
                urm.setRid(roleIds[j]);
                urm.setUid(uidTwo);
                i = userDao.insert(urm);
            }
        }
        return i;
    }

    @Override
    public HashMap queryRole(Integer page, Integer rows) {
        Map map = new HashMap();
        Integer total = userDao.queryRoleCount(map);
        map.put("start",(page-1)*rows);
        map.put("end",rows);
        List<Role> list = userDao.queryRolelist(map);
        HashMap mp = new HashMap<>();
        mp.put("total",total);
        mp.put("rows",list);
        return mp;
    }

    @Override
    public List<Tree> getPermissionByRId(Integer roleId) {
        //查出  角色 id  所对应的 菜单 的id
        List<String> list =  userDao.getPermissionByRId(roleId);
        // 查询所有 的菜单
        List<Tree>  listTwo = userDao.getPermissionAll();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < listTwo.size(); j++) {
                // 用  原来 选中的 菜单id  和所有的  菜单id  对比 ，对比成功 就选中
                if(list.get(i).equals(listTwo.get(j).getId().toString())){
                    listTwo.get(j).setChecked("true");
                }
            }
        }
        return listTwo;
    }

    @Override
    public int updateRolePermiss(Integer[] perids, Integer roleId) {
        //先 删除 原有的 关联
        int i  = userDao.deleteRolePermissByRid(roleId);
        //再建立  新的 关联
        if(i >=0){
            for (int j = 0; j < perids.length; j++) {
                RoleTree rpm = new RoleTree();
                rpm.setTid(perids[j]);
                rpm.setRid(roleId);
                i=userDao.insertroletree(rpm);
            }
        }
        return i;
    }

    @Override
    public HashMap queryPermission(Integer page, Integer rows) {
        Map map = new HashMap();
        Integer total = userDao.queryPermissionCount(map);
        map.put("start",(page-1)*rows);
        map.put("end",rows);
        List<Tree> list = userDao.queryTreelist(map);
        HashMap mp = new HashMap<>();
        mp.put("total",total);
        mp.put("rows",list);
        return mp;
    }

    @Override
    public List<Role> query() {
        return userDao.query();
    }

    @Override
    public void addrole(List<Role> list) {
        userDao.addrole(list);
    }

    @Override
    public HashMap queryLogList(Integer page, Integer rows) {
        Query query = new Query();
        Integer  skip =(page-1)*rows;
        query.skip(skip);
        query.limit(rows);
        Long total = mongoTemplate.count(query,LogModel.class,"logs");
        query.with(new Sort(Sort.Direction.DESC, "logtime"));
        List<LogModel> list =  mongoTemplate.find(query, LogModel.class, "logs");
        HashMap mp = new HashMap<>();
        mp.put("total",total);
        mp.put("rows",list);
        return mp;
    }
}

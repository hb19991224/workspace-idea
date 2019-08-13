/**
 * Copyright (C), 2019-2019, 金科
 * FileName: UserController
 * Author:  黄斌
 * Date:     2019/8/9 10:54
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.jk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jk.model.LogModel;
import com.jk.model.Role;
import com.jk.model.Tree;
import com.jk.model.User;
import com.jk.service.UserService;
import com.jk.util.ExportExcel;
import com.jk.util.TreeNoteUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.Book;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 斌
 * @create 2019/8/9
 * @since 1.0.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("jump")
    public String jump(){
        return "login";
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(User user, HttpServletRequest request){
        String str = "0";
        User us=userService.login(user.getUsername());
        if (us == null) {
            str = "1";
            return str;
        }
        if (!us.getPassword().equals(user.getPassword())){
            str = "2";
            return str;
        }
        str = "3";
        request.getSession().setAttribute("user", us);
        return str;

    }

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("getTreeAll")
    @ResponseBody
    public List<Tree> getTreeAll(HttpServletRequest request){
        List<Tree> list = new ArrayList();
        User user= (User) request.getSession().getAttribute("user");
        String key = "trees"+user.getId();
        if (redisTemplate.hasKey(key)){
         System.out.println("------------走缓存-----------------");
          list = (List<Tree>) redisTemplate.opsForValue().get(key);
        }else{
            System.out.println("------------走数据库-----------------");
            list = userService.getTreeAll(user.getId());
            list = TreeNoteUtil.getFatherNode(list);
            redisTemplate.opsForValue().set(key,list);
            redisTemplate.expire(key, 30, TimeUnit.MINUTES);
        }
        return list;
    }

    @RequestMapping("/userlist")
    public String userlist(){
        return "queryuser";
    }

    @RequestMapping("/queryUser")
    @ResponseBody
    public HashMap queryUser(Integer page, Integer rows){
        return userService.queryUser(page,rows);
    }

    @RequestMapping("getRoleByUserId")
    @ResponseBody
    public  List<Role>  getRoleByUserId(Integer roleId){
        List<Role> list = userService.getRoleByUserId(roleId);
        return list;
    }

    @RequestMapping("updateUserRole")
    @ResponseBody
    public int updateUserRole(Integer[] roleIds,Integer uidTwo){

        int i = userService.updateUserRole(roleIds,uidTwo);

        return i;
    }

    @RequestMapping("/rolelist")
    public String rolelist(){
        return "queryrole";
    }

    @RequestMapping("/queryRole")
    @ResponseBody
    public HashMap queryRole(Integer page, Integer rows){
        return userService.queryRole(page,rows);
    }

    @RequestMapping("getPermissionByRId")
    @ResponseBody
    public List<Tree>  getPermissionByRId(Integer roleId){
        List<Tree> list = userService.getPermissionByRId(roleId);
        //自己调用自己（递归）
        list = TreeNoteUtil.getFatherNode(list);
        return list;
    }

    @RequestMapping("updateRolePermiss")
    @ResponseBody
    public  String updateRolePermiss(Integer[] perids,Integer roleId){
        int i = userService.updateRolePermiss(perids,roleId);
        if(i<1){
            return "0";
        }
        return "1";
    }

    @RequestMapping("/treelist")
    public String treelist(){
        return "querytree";
    }

    @RequestMapping("/queryPermission")
    @ResponseBody
    public HashMap queryPermission(Integer page, Integer rows){
        return userService.queryPermission(page,rows);
    }

    @RequestMapping("exportExcel")
    public void exportExcel(HttpServletResponse response){
        //导出的excel的标题
        String title = "角色管理";
        //导出excel的列名
        String[] rowName = {"编号","职位名称","简介"};
        //导出的excel数据
        List<Object[]>  dataList = new ArrayList<Object[]>();
        //查询的数据库的角色信息
        List<Role> list=   userService.query();
        //循环角色信息
        for(Role role:list){
            Object[] obj =new Object[rowName.length];
            obj[0]=role.getId();
            obj[1]=role.getName();
            obj[2]=role.getText();
            dataList.add(obj);
        }
        ExportExcel exportExcel =  new ExportExcel(title,rowName,dataList,response);
        try {
            exportExcel.export();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("importExcel")
    public String importExcel(MultipartFile file, HttpServletResponse response){
        //获得上传文件上传的类型
        String contentType = file.getContentType();
        //上传文件的名称
        String fileName = file.getOriginalFilename();
        //获得文件的后缀名
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //上传文件的新的路径
        //生成新的文件名称
        String filePath = "./src/main/resources/templates/fileupload/";
        //创建list集合接收excel中读取的数据
        List<Role> list =new ArrayList<Role>();
        try {
            uploadFile(file.getBytes(), filePath, fileName);

            //通过文件忽的WorkBook
            FileInputStream fileInputStream = new FileInputStream(filePath+fileName);
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            //通过workbook对象获得sheet页 有可能不止一个sheet
            for(int i=0 ;i<workbook.getNumberOfSheets();i++){
                //获得里面的每一个sheet对象
                Sheet sheetAt = workbook.getSheetAt(i);
                //通过sheet对象获得每一行
                for(int j=3;j<sheetAt.getPhysicalNumberOfRows();j++){
                    //创建一个Role对象接收excel的数据
                    Role role=new Role();
                    //获得每一行的数据
                    Row row = sheetAt.getRow(j);

                    //获得每一个单元格的数据
                    if(row.getCell(1)!=null && !"".equals(row.getCell(1))){
                        role.setName(this.getCellValue(row.getCell(1)));
                    }
                    if(row.getCell(2)!=null && !"".equals(row.getCell(2))){
                        role.setText(this.getCellValue(row.getCell(2)));
                    }

                    list.add(role);
                }
            }
            userService.addrole(list);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return "redirect:../user/rolelist";
    }

    //上传文件的方法
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    //判断从Excel文件中解析出来数据的格式
    private static String getCellValue(Cell cell){
        String value = null;
        //简单的查检列类型
        switch(cell.getCellType())
        {
            case Cell.CELL_TYPE_STRING://字符串
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC://数字
                long dd = (long)cell.getNumericCellValue();
                value = dd+"";
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            case Cell.CELL_TYPE_FORMULA:
                value = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BOOLEAN://boolean型值
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                value = String.valueOf(cell.getErrorCellValue());
                break;
            default:
                break;
        }
        return value;
    }

    @RequestMapping("/loglist")
    public String loglist(){
        return "querylog";
    }

    @RequestMapping("queryLogList")
    @ResponseBody
    public HashMap queryLogList(Integer page, Integer rows){

        return userService.queryLogList(page,rows);
    }
}

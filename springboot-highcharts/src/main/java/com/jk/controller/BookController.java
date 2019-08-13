/**
 * Copyright (C), 2019-2019, 金科
 * FileName: BookController
 * Author:  黄斌
 * Date:     2019/8/12 18:53
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.jk.controller;

import com.jk.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 斌
 * @create 2019/8/12
 * @since 1.0.0
 */
@Controller
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping("toShow")
    public String toShow(){
        return "chart";
    }

    @RequestMapping("chartBook")
    @ResponseBody
    public List<Map<String,Object>> chartBook(){
        List<Map<String,Object>> list = bookService.chartBook();
        List<Map<String,Object>> list1 = new ArrayList<>();
        for (Map<String,Object> map1:list) {
            Map<String,Object> map = new HashMap<>();
            map.put("name",map1.get("年"));
            map.put("y",map1.get("总数"));
            map.put("sliced","true");
            map.put("selected","true");
            list1.add(map);
        }
        return list1;
    }

    @RequestMapping("toShow1")
    public String toShow1(){
        return "broken";
    }

    @RequestMapping("brokenBook")
    @ResponseBody
    public List<Map<String,Object>> brokenBook(){
        List<Map<String,Object>> list = bookService.brokenBook();
        List<Map<String,Object>> list1 = new ArrayList<>();

        for (Map<String,Object> map1:list) {
            Map<String, Object> map = new HashMap<>();
            map.put("name",map1.get("月"));
            map.put("y",map1.get("总数"));
            list1.add(map);
        }
        return list1;
    }
}

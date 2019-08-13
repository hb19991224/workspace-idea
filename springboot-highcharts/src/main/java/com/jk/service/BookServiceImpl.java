/**
 * Copyright (C), 2019-2019, 金科
 * FileName: BookServiceImpl
 * Author:  黄斌
 * Date:     2019/8/12 19:04
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.jk.service;

import com.jk.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookDao bookDao;

    @Override
    public List<Map<String, Object>> chartBook() {

        return bookDao.chartBook();
    }

    @Override
    public List<Map<String, Object>> brokenBook() {

        return bookDao.brokenBook();
    }
}

/**
 * Copyright (C), 2019-2019, 金科
 * FileName: BookDao
 * Author:  黄斌
 * Date:     2019/8/12 19:05
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.jk.dao;

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
public interface BookDao {
    List<Map<String, Object>> chartBook();

    List<Map<String, Object>> brokenBook();
}

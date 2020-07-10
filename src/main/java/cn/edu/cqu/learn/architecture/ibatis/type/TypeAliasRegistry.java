package cn.edu.cqu.learn.architecture.ibatis.type;

import cn.edu.cqu.learn.architecture.ibatis.io.Resources;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.*;

/**
 * 类型别人的注册类
 */
public class TypeAliasRegistry {

    // 就是这个对象，存的是 String Class 键值对，就是类型别名的映射
    // 别名就是 String，类型就是 Class
    private final Map<String, Class<?>> typeAliases = new HashMap<>();


}

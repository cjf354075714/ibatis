package cn.edu.cqu.learn.architecture.ibatis.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class MethodTest {
    public void testedFun(String name, int age) {
    }

    @Test
    public void methodTest() {
        Method[] methods = MethodTest.class.getMethods();
        Method testedFun = methods[0];
        // 获取方法的返回类型
        // 首先，方法的返回结果，是不会返回 null ，即使方法返回的是 void
        // 返回的也应该是 void 的 Class 对象
        Class<?> resultType = testedFun.getReturnType();
        System.out.println(resultType);
        Class<?>[] params = testedFun.getParameterTypes();
        for ( Class<?> param : params ) {
            System.out.println(param);
        }
    }
}

package cn.edu.cqu.learn.architecture.ibatis.utils;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ReflectorUtilTest {

    private static final Logger SLF4J;

    static {
        SLF4J = LoggerFactory.getLogger(ReflectorUtilTest.class);
    }

    public Object testedFun(String name, Integer age) {
        return new Object();
    }

    @Test
    public void methodTest() {
        Method[] methods = ReflectorUtilTest.class.getMethods();
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

    @Test
    public void getMethodSignatureTest() {
        Method[] methods = ReflectorUtilTest.class.getMethods();
        String signature = ReflectorUtil.getMethodSignature(methods[1]);
        SLF4J.info(signature);
    }
}

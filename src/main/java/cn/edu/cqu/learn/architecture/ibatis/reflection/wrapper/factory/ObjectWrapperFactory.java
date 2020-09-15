package cn.edu.cqu.learn.architecture.ibatis.reflection.wrapper.factory;

import cn.edu.cqu.learn.architecture.ibatis.reflection.MetaObject;
import cn.edu.cqu.learn.architecture.ibatis.reflection.wrapper.ObjectWrapper;

/**
 * 对象包装工厂接口
 */
public interface ObjectWrapperFactory {

    /**
     * 是否包装了这个类
     * @param object 判断是否被包装的对象
     * @return 是否包装
     */
    boolean hasWrapperFor(Object object);

    /**
     * 包装某一个类，并返回包装的对象
     * @param metaObject 元数据对象
     * @param object 被包装的对象
     * @return 包装结果
     */
    ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);
}

package cn.edu.cqu.learn.architecture.ibatis.service;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

public interface ISimpleService {

    Class<?> getTypeClass() throws Exception;

    Reflector getReflector() throws Exception;

    ReflectorFactory defaultReflectorFactory() throws Exception;

    ObjectFactory defaultObjectFactory() throws Exception;

    SqlSessionFactoryBuilder sqlSessionFactoryBuilder() throws Exception;

    Environment environment() throws Exception;

    SqlSessionFactory sqlSessionFactory() throws Exception;

    Configuration configuration() throws Exception;

    DefaultSqlSession defaultSqlSession() throws Exception;
}

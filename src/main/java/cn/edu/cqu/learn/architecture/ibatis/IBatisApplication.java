package cn.edu.cqu.learn.architecture.ibatis;

import cn.edu.cqu.learn.architecture.ibatis.entity.BaseEntity;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

@SpringBootApplication
public class IBatisApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(IBatisApplication.class, args);
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BaseEntity inDb = new BaseEntity();
        inDb.setId(new BigInteger("1"));

        // 执行 insert
        sqlSession.insert("insert", inDb);
//        sqlSession.delete("cn.edu.cqu.learn.architecture.ibatis.mapper.BaseEntityMapper.delete", new BigInteger("1"));
        sqlSession.commit();
        sqlSession.close();

    }

}

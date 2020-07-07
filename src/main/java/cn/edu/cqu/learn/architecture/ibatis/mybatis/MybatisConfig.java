package cn.edu.cqu.learn.architecture.ibatis.mybatis;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class MybatisConfig {

    public DataSource pooledDataSource() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/feng";
        String username = "root";
        String password = "123456";

        // 到最后，还是调用的 DriverManager.getConnection 来获取链接对象
        // 分库分表，是否可以从这里去实现
        return new PooledDataSource(driver, url, username, password);
    }

    public TransactionFactory jdbcTransactionFactory() {
        return new JdbcTransactionFactory();
    }

    public Environment environment() {
        return new Environment("test", jdbcTransactionFactory(), pooledDataSource());
    }

    public Configuration configuration() {
        // 在 new Configuration 的时候，里面的 Mapper  集合已经初始化好了，只是没有添加上 Mapper 对象
        // 这个对象，要去好好了解下
        Configuration result = new Configuration();
        result.setEnvironment(environment());
//        result.addMapper();
        return result;
    }
}

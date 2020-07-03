package cn.edu.cqu.learn.architecture.ibatis.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 应该是调用链，在执行预编译的时候，去调用这些别名，改成我们自己的业务关系
 * 然后，在返回结果的时候，再去调用
 * 但细节我还是不会用
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
public class SimpleTypeHandler implements TypeHandler<String> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public String getResult(ResultSet resultSet, String s) throws SQLException {
        return resultSet.getString(s);
    }

    @Override
    public String getResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public String getResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}

## 流程

* XML 工具类将去读取 Mybatis 的配置文件，我们不需要关注这个构建细节，只需要知道构建就行了
* 构建之后将返回一个对象，里面包含了 Mybatis 的所有核心配置，Configuration类
* 这个类将包含 Enviroment 对象，存的是数据库连接池对象
* SqlSessionFactoryBuilder 将根据这个 Enviroment 对象，去返回一个SqlSessionFactory
* SqlSessionFactory 将返回 SqlSession，SqlSession本质上就是 Connection 的包装，就是通过 Enviroment去构建的
* 
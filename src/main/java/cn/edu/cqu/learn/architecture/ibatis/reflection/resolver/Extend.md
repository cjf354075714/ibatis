**JAVA TYPE**

首先这个知识点是 JAVA 反射中的，在理解这个知识点的时候，需要去明白，为什么需要这个知识，但是现在我还理解不到

首先，我定义了一个 JAVA 变量，int，或者 String，那我们我们可以抽象一下，它们都有自己的 Class 对象，没错，Class 也是一个类型，只不过它具体指定了一个类型而已。那么，在 JAVA 中，还有什么 T，T[]，List\<String\> 等等，我这个 Class 显然是不够去描述它们的，所以就需要额外引入类型，来描述它们。



* Type 接口：这个接口，是 JAVA 中，所有类型的父接口，就是用来表述那些普通的类型和泛型等一系列类型的接口，且这些接口在自己的领域下，将有独特的方法

  ```java
  public interface Type {
      // 返回当前类型的名称
      default String getTypeName();
  }
  ```

  这个接口没有什么可说的，现在我一个一个类型的去学习，并且知道，他们该怎么构造

* ParamterizedType 接口，汉语翻译过来，就是参数化类型，就是说，那些带有<>的类型，它就是一个参数化类型

  ```java
  public class simple {
      // 这个 list 就是一个参数化类型，当然它也是一个类型
      // 那么，我拿到这个参数化类型，自然就有一些方法，去拿到这些 obejct
      // 等等
  	private List<Object> list;
  }
  ```

  请看这段代码：

  ```java
  public class TypeParameterResolverTest {
  
      private static final Logger SLF4J;
  
      static {
          SLF4J = LoggerFactory.getLogger(ReflectorUtilTest.class);
      }
  
  
      private String simpleString;
      private List simpleList;
      private List<Object> objectList;
  
      @Test
      public void typeTest() {
          // 我拿到当前类的所有字段
          Field[] fields = TypeParameterResolverTest.class.getDeclaredFields();
          for ( Field index : fields ) {
              // 拿到这个类的通常类型，就是 genericType
              // 如果返回类型是一个普通的 Class 则 getGenericType 和 getType 是一样的
              // 我觉得还是 getGenericType 的功能更强大
              // 如果，当前这个字段就是一个参数类型，那么这个 type 就是一个参数化类型的一个实例
              // 所以，我可以拿到参数化类型去获取到它的某些信息
              Type type = index.getGenericType();
              SLF4J.info(type.toString());
          }
      }
  
  }
  ```

  ```java
  public interface ParameterizedType {
  	// 拿到
  	Type[] getActualTypeArguments();
  }
  ```

  
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
  	// 拿到模板类型里面的类型，还是举例说明
      // List<String> list
      // 如果我拿到 list 这个类型，那么它一定是个 ParameterizedType 类型
      // 那么这个 getActualTypeArguments 返回结果就是一个大小为 1 的数组
    // 里面的值就是 String 类型
      // 依次类推，Map<String, Object> 返回的是 [String.class, Object.class]
  	Type[] getActualTypeArguments();
      
      // 这个方法，将返回最外面的集合类型，一般都是集合类型
      // Map<String, Object> 将返回 Map
      // List<String> 将返回 List
      // 但是，这个 rawType 将返回的是一个 Class 对象
      // 那我岂不是拿不到多层次嵌套结构
      Type getRawType();
      
      // 返回当前集合类型的所在类型
      // 前提是，你这个集合类型是某一个类的成员类型
      // 比如，Map.Entry<String, Object> 就是 Map<String, Object> 的成员类型
      // 那 Map.Entry<String, Object> 的 ownerType 就将返回 Map<String, Object>
      Type getOwnerType();
  }
  ```
  
* TypeVariable 接口：翻译过来就是类型变量，那么本质上就是用来描述某个类中，那些字段是变量的类型

  举例子：

  ```
  public class SimpleType<K extends InputStream, V> {
  	K key;
  	V value;
  	
  }
  ```

  
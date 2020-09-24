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

  ```java
  public class SimpleType<K extends InputStream, V> {
      // 那么这里的 K 就是一个 TypeVariable，V 也是，然后这个 TypeVariable 的一些方法
      // 将用来描述它们的细节
  	K key;
  	V value;
  }
  ```

  ```java
  public interface TypeVariable<D extends GenericDeclaration> 
      extends Type, AnnotatedElement {
      // 拿到当前的这个类型的边界，注意，边界是上边界，如果没有定义就是 Object.class
      // 为什么是数组呢，因为一个类型变量可以继承多个边界
      Type[] getBounds();
      
      // 当前变量类型所在的那个 Type 对象，比如，你给一个 Map<K, V> 对象
      // K 这个 TypeVariable 变量，这个方法将返回 Map 这个类型
      D getGenericDeclaration();
      
      // 获取当前类型的名字
      String getName();
      
      // 暂时不管
      AnnotatedType[] getAnnotatedBounds();
  }
  ```

* WildcardType 接口：它的理解，其实和参数类型有点相似，举例：

  ```java
  public class WildcardTypeTest {
      // 这个变量就是一个参数类型，因为它指定了 List 中具体的实体，就是它的 getActualTypeArguments 
      // 将返回一个 Class 类型
      private List<InputStream> inputList;
      
      // 因为你这里没有指定一个具体的类型，但是指定了一个通配符。
      // 所以，它的 getActualTypeArguments 就是一个通配符类型
      // 但是它有一个最高等级的类型，所以 getUpperBounds 方法，将返回这个类型，
      // 虽然 getUpperBounds 这个方法，返回的是数组，但是这个是因为可用性而设计的
      private List<? extends InputStream> extInputStream;
      
      // 既然有上边界，那就有下边界，下边界的概念和上边界完全一样，getLowerBounds 将返回这个类型
      private List<? super InputStream> supInputStream;
      
  }
  ```

  ```java
  // 通配符类型 <? extends File> 就是通配符
  public interface WildcardType extends Type {
      // 获取通配符的顶级类型，虽然是数组返回结果，但是1.8版本的 JAVA 是只返回一个类型
      Type[] getUpperBounds();
      
      // 获取通配符的底层类型，和 getUpperBounds 一样
      Type[] getLowerBounds();
  }
  ```

* GenericArrayType 接口：这个接口还有点意思，官方的解释说的是，参数类型和类型变量的数组

  ```java
  public class GenericArrayTypeTest<T> {
      // 它是一个参数类型
      T type;
      
      // 它是个 Class 类型，哦，懂了实际上 Class 类型它和什么通配符类型啊，是一个等级的
      List<Object> object;
      
      // 它是个参数类型
      List<T> typeList;
      
      // 当然这种写法是错误的，这里只是为了好看
      // 它是个通配符类型
      List<? extends InputStream> tInputSList;
      List<? super FileInputStream> tFileSList;
      
      // 这个是个通常数组类型
      T[] types;
      List<T>[] tListS;
      
      // 既然通常数组类型和通配符类型，参数类型的获取方式差不多，那我就不详细写了
  }
  ```

  ```java
  // 通配符类型，当前这个类型，就代表了那些参数类型和类型变量的数组
  // 我好奇，通配符类型数组将返回什么
  public interface GenericArrayType extends Type {
      // 这个方法，将返回当前这个数字，去掉 [] 的类型，比如
      // T[] 去掉 [] 就是 T，那么他将返回类型变量
      // Map<K, V>[] 去掉 [] 就是 Map<K, V>，将返回参数类型
      // 那通配符呢，多维数组呢？
      // 嘿嘿，对不起，通配符类型不能单独定义
      // 如果是多维数组，那去掉 [] 之后，还是个数组，那就还是通常数组类型
      // 所以，这个类型，可以产生其他的类型
      Type getGenericComponentType();
  }
  
  ```



# 学完了！
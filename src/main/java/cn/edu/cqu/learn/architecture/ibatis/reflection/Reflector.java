package cn.edu.cqu.learn.architecture.ibatis.reflection;

import cn.edu.cqu.learn.architecture.ibatis.reflection.invoker.Invoker;
import cn.edu.cqu.learn.architecture.ibatis.reflection.invoker.impl.MethodInvoker;
import cn.edu.cqu.learn.architecture.ibatis.reflection.property.PropertyUtil;
import cn.edu.cqu.learn.architecture.ibatis.utils.ReflectorUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public final class Reflector {

    private final Class<?> type;

    private final String[] readablePropertyNames = null;

    private final String[] writablePropertyNames = null;

    private final Map<String, Invoker> setMethods = new HashMap<>();

    private final Map<String, Invoker> getMethods = new HashMap<>();

    private final Map<String, Class<?>> setTypes = new HashMap<>();

    private final Map<String, Class<?>> getTypes = new HashMap<>();

    private Constructor<?> defaultConstructor;

    private Map<String, String> caseInsensitivePropertyMap = new HashMap<>();

    /**
     * 构造函数，传入类型
     * @param type 类型
     */
    public Reflector(Class<?> type) {
        // 记录当前类型
        this.type = type;
        // 获取当前对象的默认构造函数
        this.addDefaultConstructor(type);
    }

    /**
     * 获取传入类型的构造函数参数个数为 0 的构造方法
     * @param type 传入类型
     */
    private void addDefaultConstructor(Class<?> type) {
        Constructor<?>[] constructors = type.getConstructors();
        for ( Constructor<?> index : constructors ) {
            if ( 0 == index.getParameterCount() ) {
                this.defaultConstructor = index;
                return ;
            }
        }
        this.defaultConstructor = null;
    }

    private void addGetMethods(Class<?> type) {
        Map<String, List<Method>> conflictGetMethod = new HashMap<>();
        // 拿到所有的方法
        Method[] methods = ReflectorUtil.getClassMethods(type, new HashMap<>());
        // 便利所有的方法，然后去判断当前方法的参数长度和方法名
        // 只有没有参数的方法，并且按照协议，这个方法它是 get 方法
        for ( Method index : methods ) {
            if ( 0 == index.getParameters().length && PropertyUtil.isGetter(index.getName())) {
                this.addMethodConflict(conflictGetMethod, index.getName(), index);
            }
        }
        // 方法集合都已经解决好了，现在去执行方法的最终判定

    }

    /**
     * 解析所有的 Get 方法，并且找出最合适的 Get 方法
     * 寻找的逻辑是根据这些方法的返回结果来定的，规则就是：谁返回的类型是子类，那就是这个 Get 方法
     * 最后，会调用方法，将该 Method 包装成 Invoke 并和这个方法名组成键值对，放在该类的 GetMap 中
     * @param conflictingGetters 待解析的 Map 集合
     */
    private void resolveGetterConflicts(Map<String, List<Method>> conflictingGetters) {
        // 首先我去遍历 Map 的所有键值对
        // 我的目的是找出所有键值对中，值最合适的方法对象
        for ( Map.Entry<String, List<Method>> index : conflictingGetters.entrySet() ) {
            // 当前键值对中的获胜者
            Method winner = null;
            // 当前键值对的键
            String propName = index.getKey();
            // 是否是摸棱两可？
            boolean isAmbiguous = false;
            // 拿到所有的方法，去遍历
            // 这个 for 循环，遍历完毕之后，将自动获得合适的方法
            for ( Method candidate : index.getValue() ) {
                // 在循环比较的时候，发现胜利者是 null，则将当前挑战者赋值给胜利者
                // 这个判断只会出现一次，且赋值之后，就代表当前的竞争成功了，自然就不需要继续下一次竞争判断了
                if ( null == winner ) {
                    winner = candidate;
                    // 所以这里直接进行下一个循环判断
                    continue ;
                }
                // 获取胜利者的返回结果
                Class<?> winnerType = winner.getReturnType();
                // 获取竞争者的返回结果
                Class<?> candidateType = candidate.getReturnType();

                // 根据返回结果，来判断，谁才是谁的子类方法实现，当然，我们都会用子类的方法

                // 如果两个方法的返回类型是一样的
                // 就是说：当返回类型是 Boolean 且方法是 is 开头，这才允许竞争成功
                // 否则，跳转到下一个比较
                if ( candidateType.equals(winnerType) ) {
                    // 如果返回的结果是 Boolean 类型
                    // 那么我就去判断，是不是 is 和 get 给搞混了，如果是，则使用 is 这个方法
                    if ( !Boolean.class.equals(candidateType) ) {
                        // 如果相同，但是又不是 Boolean 的类型，那么就表示这次比较是模糊的，就执行下一次比较
                        isAmbiguous = true;
                        break;
                        // is 开头，就表示，这个竞争者是获胜者
                    } else if ( candidate.getName().startsWith("is") ) {
                        winner = candidate;
                    }
                    // 如果竞争者的方法返回类型是当前胜利者方法返回类型的父类
                    // 父类是否可以由子类而来，当然，现在的胜利者是具体的实现方法，那就竞争失败
                } else if ( candidateType.isAssignableFrom(winnerType) ) {
                    // do nothing
                    // 竞争者方法，才是具体的实现方法，交换
                } else if ( winnerType.isAssignableFrom(candidateType) ) {
                    winnerType = candidateType;
                    // 实际上不应该有这个 else ，也许有些复杂的业务对象
                } else {
                    isAmbiguous = true;
                    break;
                }
            }
            // 记录下合适的 get 方法

        }
    }

    /**
     * 将合适的 Get 方法包装成 Invoker，并与方法名构成键值对，放在 Map 中
     * 如果是摸棱两可的方法，Mybatis 中是先去构建一个错误的 Invoker 对象，等到在调用的时候
     * 异常就报出来了
     * 我这里就不去写了
     * @see Invoker
     * @param name 方法名
     * @param method 方法
     * @param isAmbiguous 是否是摸棱两可的
     */
     private void addGetMethod(String name, Method method, boolean isAmbiguous) {
        if ( isAmbiguous ) {
            // 错误日志输出
        } else {
            // 包装
            Invoker methodInvoker = new MethodInvoker(method);
            // 键值对记录下来
            // 但是，我还需要去记录一个东西，那就是我这个 Get 方法的返回类型的记录，记录的话也是方法名作为 key
            // 那我么我就要去看，这个返回结果，到底有哪些类型
            // 这些就是 JAVA 中 TYPE 的知识
            this.getMethods.put(name, methodInvoker);

        }
     }

    /**
     * 添加冲突的方法，到集合中去
     * 比如：我有两个 getName 方法，且他们的方法名是一样的，这是因为他们是父子类重载方式而来
     * 那么这个方法存的那个数据结构是什么样的呢？
     *
     * Map 结构，其中 Key 是方法名称， Value 是 List，存的是方法的集合
     *
     * @param conflictingMethods 方法结构
     * @param name 方法名
     * @param method 方法
     */
    private void addMethodConflict(Map<String, List<Method>> conflictingMethods, String name, Method method) {
        // 这个方法我不管
        if ( isValidPropertyName(name) ) {
            // 先去判断 Map 中是否有这个方法名称所对应的 List 集合
            // 如果有，则去拿，没有则新增一个键值对
            List<Method> methodList = conflictingMethods.computeIfAbsent(name, k -> new LinkedList<>());
            // List 中去存储
            methodList.add(method);
        }
    }

    /**
     * 我不知道这个方法有什么用，但是他很简单
     * @param name 检查字符串
     * @return 是否符合条件
     */
    private boolean isValidPropertyName(String name) {
        return !(name.startsWith("$")) || "serialVersionUID".equals(name) || "class".equals(name);
    }


    public static boolean canControlMemberAccessible() {
        return false;
    }
}

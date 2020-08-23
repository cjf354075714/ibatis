package cn.edu.cqu.learn.architecture.ibatis.context;


/**
 * 在 Mybatis 的执行上下文的时候，会出现许多问题
 * 需要有一个实例类，去记录下来，是什么原因导致了错误
 * 需要线程隔离
 */
public class ErrorContext {

    // 线程隔离的存储对象
    private static final ThreadLocal<ErrorContext> LOCAL;

    static {
        LOCAL = ThreadLocal.withInitial(ErrorContext::new);
    }

    // 我不知道这个对象有什么用，应该是当前存储的对象
    private ErrorContext stored;

    // 是否是资源文件读取错误
    private String resource;

    // 未知的错误信息
    private String activity;

    // 未知的错误信息
    private String object;

    // 错误原因
    private String message;

    // 当前执行的 SQL
    private String sql;

    // 执行报错的异常，因为是未知异常，所以是所有异常的父类
    private Throwable cause;

    public ErrorContext getStored() {
        return stored;
    }

    public void setStored(ErrorContext stored) {
        this.stored = stored;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    private ErrorContext() {

    }

    // 返回当前上下文的错误记录对象
    public static ErrorContext getInstance() {
        return LOCAL.get();
    }

    public ErrorContext store() {
        ErrorContext newContext = new ErrorContext();

        // 就是给自己的上下文对象，设置一个 stored 记录对象
        newContext.setStored(this);

        // 那我为什么要设置当前这个对象作为上下文对象呢
        LOCAL.set(newContext);
        return LOCAL.get();
    }

    // 这个方法和上面的方法，似乎有点印象了
    // 因为你没有解决，上下文实例化的问题
    public ErrorContext recall() {
        if ( null != this.getStored() ) {
            LOCAL.set(this.getStored());
            this.setStored(null);
        }
        return LOCAL.get();
    }

    public ErrorContext resource(String resource) {
        this.setResource(resource);
        return this;
    }

    public ErrorContext activity(String activity) {
        this.setActivity(activity);
        return this;
    }

    public ErrorContext object(String object) {
        this.setObject(object);
        return this;
    }

    public ErrorContext message(String message) {
        this.setMessage(message);
        return this;
    }

    public ErrorContext sql(String sql) {
        this.setSql(sql);
        return this;
    }

    public ErrorContext cause(Throwable cause) {
        this.setCause(cause);
        return this;
    }

    // 注意，stored 是没有被重置的
    public ErrorContext reset() {
        LOCAL.remove();
        this.setCause(null);
        this.setSql(null);
        this.setMessage(null);
        this.setObject(null);
        this.setActivity(null);
        this.setResource(null);
        return this;
    }

    // 现在去根据上下文中的记录的错误信息，去构建正确的错误返回
    @Override
    public String toString() {
        StringBuilder description = new StringBuilder();

        // 这个是换行符的代表，就是系统在显示一行字符的时候
        // 需要一个换行符，在 win 下是 "\n" unix 下是 "\r\n"
        String lineSeparator = System.lineSeparator();

        // 比如这个就报 SQL 执行异常：什么什么 SQL 语法错误
        if ( null != this.getMessage() ) {
            description.append(lineSeparator);
            // 我可算懂了，这是人家在代码里面写好了的
            description.append("### ");
            description.append(this.getMessage());
        }

        // 原因是什么，有可能存在于这个对象中
        if ( null != this.getResource() ) {
            description.append(lineSeparator);
            description.append("### 这个错误可能存在于");
            description.append(this.getResource());
        }

        // 比如你给的实体类，没有提供 set get 方法
        if ( null != this.getObject()) {
            description.append(lineSeparator);
            description.append("### 这个错误可能存在于");
            description.append(this.getObject());
        }

        if ( null != this.getActivity() ) {
            description.append(lineSeparator);
            description.append("### 当执行以下过程的时候发生错误");
            description.append(this.getActivity());
        }

        // 合理的去校验 SQL
        if ( null != this.getSql() ) {
            description.append(lineSeparator);
            description.append("### SQL: ");
            description.append(this.getSql().replace('\n', ' ').replace('\r', ' ').replace('\t', ' ').trim());
        }

        if ( null != this.getCause() ) {
            description.append(lineSeparator);
            description.append("### 原因：");
            description.append(cause.toString());
        }
        return description.toString();
    }
}

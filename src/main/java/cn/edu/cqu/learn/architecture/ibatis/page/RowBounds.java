package cn.edu.cqu.learn.architecture.ibatis.page;

/**
 * SQL 查询的时候，传入的分页配置类
 */
public class RowBounds {

    public static final int NO_ROW_OFFSET;

    public static final int NO_ROW_LIMIT;

    public static final RowBounds DEFAULT;

    static {

        // 如果不支持分页查询，那就是查询全部的数据
        // 那就是查询第一页，查询的数量限制应该是最大值
        NO_ROW_OFFSET = 0;
        NO_ROW_LIMIT = Integer.MAX_VALUE;
        DEFAULT = new RowBounds();
    }

    private final int offset;
    private final int limit;

    public RowBounds() {
        this.offset = NO_ROW_OFFSET;
        this.limit = NO_ROW_LIMIT;
    }

    public RowBounds(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }
}

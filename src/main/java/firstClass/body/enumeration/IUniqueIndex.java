package firstClass.body.enumeration;

/**
 * @Author: wkit
 * @Date: 2019-10-15 20:52
 */
public interface IUniqueIndex {
    /**
     * 唯一索引对应字段名
     *
     * @return 字段名
     */
    String getPropertyName();

    /**
     * 唯一索引对应列名
     *
     * @return 列名
     */
    String getColumnName();

    /**
     * 唯一索引数据库中定义的名称
     *
     * @return 所以名称
     */
    String getIndexName();
}

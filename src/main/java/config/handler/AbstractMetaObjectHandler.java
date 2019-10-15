package config.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.Assert;

import java.time.ZonedDateTime;

/**
 * @Author: wkit
 * @Date: 2019-10-14 16:13
 */
abstract public class AbstractMetaObjectHandler implements MetaObjectHandler {

    private static final String IS_ENABLED = "isEnabled";
    private static final String IS_DELETED = "isDeleted";
    private static final String CREATED_DATE = "createdDate";
    private static final String CREATED_BY = "createdBy";
    private static final String LAST_UPDATED_DATE = "lastUpdatedDate";
    private static final String LAST_UPDATED_BY = "lastUpdatedBy";

    @Override
    public void insertFill(MetaObject metaObject) {
        Long currentUserId = getCurrentUserId();
        setValue(IS_ENABLED, Boolean.TRUE, metaObject);
        setValue(IS_DELETED, Boolean.FALSE, metaObject);
        setValue(CREATED_DATE, ZonedDateTime.now(), metaObject);
        if (currentUserId != null) {
            setValue(CREATED_BY, currentUserId, metaObject);
        } else {
            setValue(CREATED_BY, 0L, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long currentUserId = getCurrentUserId();
        setValue(LAST_UPDATED_DATE, ZonedDateTime.now(), metaObject);
        if (currentUserId != null) {
            setValue(LAST_UPDATED_BY, currentUserId, metaObject);
        } else {
            setValue(LAST_UPDATED_BY, 0L, metaObject);
        }
    }

    /**
     * 如果字段值为Null，设置默认值
     *
     * @param fieldName    字段名称
     * @param defaultValue 默认值
     * @param metaObject   MyBatis Plus元数据
     */
    private void setValue(String fieldName, Object defaultValue, MetaObject metaObject) {
        Assert.notNull(defaultValue, "默认值为空");
        Object field = getFieldValByName(fieldName, metaObject);
        if (field == null) {
            setFieldValByName(fieldName, defaultValue, metaObject);
        }
    }

    /**
     * 获取当前用户Id
     *
     * @return 当前用户Id
     */
    abstract protected Long getCurrentUserId();
}

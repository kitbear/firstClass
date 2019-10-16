package firstClass.body.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import firstClass.body.entity.IBaseObject;
import firstClass.body.enumeration.IUniqueIndex;
import firstClass.body.exception.IdNullInUpdateActionException;
import firstClass.body.exception.ObjectNotFoundException;
import firstClass.body.rest.adapter.PageAdapter;
import firstClass.body.service.decorator.AbstractEntityDecorator;
import firstClass.body.service.output.PageResult;
import firstClass.body.service.request.PageRequest;
import firstClass.body.utils.CacheUtil;
import firstClass.body.utils.ClassUtil;
import firstClass.body.utils.LocaleUtil;
import firstClass.body.utils.ValidationExceptionUtil;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: wkit
 * @Date: 2019-10-15 19:06
 */
abstract public class BaseService<M extends BaseMapper<T>, T extends IBaseObject> {
    protected final M baseMapper;
    protected final CacheManager cacheManager;

    private final ApplicationContext applicationContext;

    protected final Class<T> entityClass;
    protected final String cacheName;
    protected final boolean isI18NEntity;
    private List<AbstractEntityDecorator> entityDecorators;

    public BaseService(M baseMapper, CacheManager cacheManager, ApplicationContext applicationContext) {
        this.baseMapper = baseMapper;
        this.cacheManager = cacheManager;
        this.applicationContext = applicationContext;
        this.entityClass = ClassUtil.getConcreteClassFromGenericType(this.getClass(), 1);
        this.isI18NEntity = false;
        this.cacheName = CacheUtil.getCacheName(this.getClass());
        Assert.notNull(this.cacheName, "cacheName is not set to CacheConfig");
    }

    @PostConstruct
    public void init() {
        this.entityDecorators = getEntityDecorators();
    }

    public T save(T object) {
        try {
            validateBeforeCreation(object);
            adapterBeforeCreation(object);
            entityDecorators.forEach(decorator -> decorator.decoratePreCreate(object));
            baseMapper.insert(object);
            putEntityInCache(object);
            entityDecorators.forEach(decorator -> decorator.decoratePostCreate(object));
            return object;
        } catch (DuplicateKeyException e) {
            throw ValidationExceptionUtil.handleUniqueIndex(e, getUniqueIndexes());
        }
    }

    public T getById(Long objectId) {
        T object = CacheUtil.getCachedObject(this.cacheName, getCacheKey(objectId.toString()), cacheManager, this.entityClass);
        if (object != null) {
            return object;
        }
        object = baseMapper.selectById(objectId);
        if (object != null) {
            putEntityInCache(object);
        }
        return object;
    }

    public T getOne(LambdaQueryWrapper<T> wrapper) {
        wrapper.select(this.entityClass, s -> s.getColumn().equals("id"));
        T t = baseMapper.selectOne(wrapper);
        if (t == null) {
            return null;
        }
        return getById(t.getId());
    }

    protected T update(T object, T persisted) {
        validateBeforeCreation(object);
        entityDecorators.forEach(decorator -> decorator.decoratePreUpdate(object, persisted));
        adapterBeforeUpdate(object, persisted);
        entityDecorators.forEach(decorator -> decorator.decoratePostUpdate(object, persisted));
        try {
            baseMapper.updateById(persisted);
            putEntityInCache(persisted);
            return persisted;
        } catch (DuplicateKeyException e) {
            throw ValidationExceptionUtil.handleUniqueIndex(e, getUniqueIndexes());
        }
    }

    public T update(T object) {
        if (object.getId() == null) {
            throw new IdNullInUpdateActionException();
        }
        T persisted = this.getById(object.getId());
        if (persisted == null) {
            throw new ObjectNotFoundException(this.entityClass, object.getId());
        }
        return update(object, persisted);
    }

    public T updateOne(T object, Wrapper<T> updateWrapper) {
        T persisted = baseMapper.selectOne(updateWrapper);
        if (persisted == null) {
            throw new ObjectNotFoundException(this.entityClass, object.getId());
        }
        return update(object, persisted);
    }

    public T saveOrUpdate(T object) {
        if (object.getId() == null) {
            return this.save(object);
        }
        T persisted = this.getById(object.getId());
        if (persisted == null) {
            return this.save(object);
        } else {
            return this.update(object, persisted);
        }
    }

    public boolean deleteById(Long objectId) {
        T object = this.getById(objectId);
        if (object != null) {
            boolean isSuccess = retBool(baseMapper.deleteById(objectId));
            if (isSuccess) {
                evictEntityFromCache(object);
            }
            return isSuccess;
        } else {
            return true;
        }
    }

    public PageResult<T> page(PageRequest pageRequest, LambdaQueryWrapper<T> pageWrapper) {
        IPage<T> page = PageAdapter.adaptPageRequest(pageRequest);
        page = baseMapper.selectPage(page, pageWrapper.select(this.entityClass, s -> s.getColumn().equals("id")));
        page.setRecords(getObjectsByObjectIds(page.getRecords()));
        return PageAdapter.adaptPageResult(page);
    }

    public List<T> list(LambdaQueryWrapper<T> wrapper) {
        List<T> ids = baseMapper.selectList(wrapper.select(this.entityClass, s -> s.getColumn().equals("id")));
        return getObjectsByObjectIds(ids);
    }

    private List<T> getObjectsByObjectIds(List<T> ids) {
        if (ids == null || ids.size() == 0) {
            return Collections.emptyList();
        }
        return getObjectsByIds(ids.stream().map(T::getId).collect(Collectors.toList()));
    }

    public List<T> getObjectsByIds(List<Long> ids){
        if (ids == null || ids.size() == 0) {
            return Collections.emptyList();
        }
        return ids.stream().map(this::getById).collect(Collectors.toList());
    }

    abstract protected IUniqueIndex[] getUniqueIndexes();

    abstract protected void adapterBeforeUpdate(T object, T persisted);

    protected String getCacheKey(String cacheKey) {
        if (this.isI18NEntity) {
            return cacheKey + ":" + LocaleUtil.getCurrentLocaleString();
        } else {
            return cacheKey;
        }
    }

    protected void putEntityInCache(T entity) {
        CacheUtil.putCachedObject(this.cacheName, getCacheKey(entity.getId().toString()), entity, cacheManager);
    }

    protected void evictEntityFromCache(T entity) {
        CacheUtil.evictCachedObject(this.cacheName, getCacheKey(entity.getId().toString()), cacheManager);
    }

    protected List<AbstractEntityDecorator> getEntityDecorators() {
        return Collections.emptyList();
    }

    /**
     * <p>
     * 判断数据库操作是否成功
     * </p>
     *
     * @param result 数据库操作返回影响条数
     * @return boolean
     */
    protected boolean retBool(Integer result) {
        return SqlHelper.retBool(result);
    }

    private void adapterBeforeCreation(T entity) {
        // do nothing for default;
    }

    private void validateBeforeCreation(T entity) {
        // do nothing for default;
    }
}

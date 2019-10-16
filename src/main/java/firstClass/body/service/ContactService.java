package firstClass.body.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import firstClass.body.entity.Contact;
import firstClass.body.enumeration.FirstClassConstants;
import firstClass.body.enumeration.IUniqueIndex;
import firstClass.body.persistence.ContactMapper;
import firstClass.body.service.output.PageResult;
import firstClass.body.service.request.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @Author: wkit
 * @Date: 2019-10-16 12:30
 */
@Slf4j
@Service
@CacheConfig(cacheNames = FirstClassConstants.ENTITY_FC_CONTACT)
public class ContactService extends BaseService<ContactMapper, Contact> {

    public ContactService(
            ContactMapper baseMapper,
            @Qualifier("firstClassCacheManager") CacheManager cacheManager,
            ApplicationContext applicationContext) {
        super(baseMapper, cacheManager, applicationContext);
    }

    @Override
    protected IUniqueIndex[] getUniqueIndexes() {
        return new IUniqueIndex[0];
    }

    @Override
    protected void adapterBeforeUpdate(Contact object, Contact persisted) {

    }

    public PageResult<Contact> searchContactList(String userName, PageRequest pageRequest) {
        LambdaQueryWrapper<Contact> queryWrapper = new LambdaQueryWrapper<>();
        // P.S sharding-jdbc 不支持 OR 等关键词
        queryWrapper
                .and(wrapper -> wrapper
                        .like(Contact::getNickName, userName)
                        .or()
                        .like(Contact::getEmail, userName)
                        .or()
                        .like(Contact::getPhone, userName));
        return this.page(pageRequest, queryWrapper);
    }
}

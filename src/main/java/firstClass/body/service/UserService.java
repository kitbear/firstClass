package firstClass.body.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import firstClass.body.entity.Contact;
import firstClass.body.entity.User;
import firstClass.body.enumeration.FirstClassConstants;
import firstClass.body.enumeration.IUniqueIndex;
import firstClass.body.persistence.UserMapper;
import firstClass.body.service.output.PageResult;
import firstClass.body.service.request.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: wkit
 * @Date: 2019-10-16 10:32
 */
@Slf4j
@Service
@CacheConfig(cacheNames = FirstClassConstants.ENTITY_FC_USER)
public class UserService extends BaseService<UserMapper, User> {

    private ContactService contactService;

    public UserService(
            UserMapper baseMapper,
            @Qualifier("firstClassCacheManager") CacheManager cacheManager,
            ApplicationContext applicationContext,
            ContactService contactService) {
        super(baseMapper, cacheManager, applicationContext);
        this.contactService = contactService;
    }

    public List<User> searchUserList(String userName, PageRequest pageRequest) {
        PageResult<Contact> contactList = contactService.searchContactList(userName, pageRequest);
        if (CollectionUtils.isEmpty(contactList.getData())){
            return new ArrayList<>();
        }
        Set<Long> contactIds = contactList.getData().stream().map(Contact::getId).collect(Collectors.toSet());
        return searchUserByContactIds(contactIds);
    }

    private List<User> searchUserByContactIds(Set<Long> contactIds) {
        if (CollectionUtils.isEmpty(contactIds)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(User::getContactId, contactIds);
        return this.list(wrapper);
    }


    @Override
    protected IUniqueIndex[] getUniqueIndexes() {
        return new IUniqueIndex[0];
    }

    @Override
    protected void adapterBeforeUpdate(User object, User persisted) {

    }

}

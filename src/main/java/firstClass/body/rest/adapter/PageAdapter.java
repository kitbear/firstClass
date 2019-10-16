package firstClass.body.rest.adapter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import firstClass.body.entity.IBaseObject;
import firstClass.body.service.output.PageResult;
import firstClass.body.service.request.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * @Author: wkit
 * @Date: 2019-10-15 22:59
 */
public class PageAdapter {

    public static <T extends IBaseObject>PageResult<T> adaptPageResult(IPage<T> page) {
        Assert.notNull(page, "page is null");
        PageResult<T> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setPages(page.getPages());
        result.setData(page.getRecords());
        return result;
    }

    public static <T extends IBaseObject> IPage<T> adaptPageRequest(PageRequest pageRequest) {
        if (pageRequest == null) {
            pageRequest = new PageRequest();
        }
        Page<T> page = new Page<>();
        page.setCurrent(pageRequest.getCurrent());
        page.setSize(pageRequest.getSize());
        if (!StringUtils.isEmpty(pageRequest.getOrderBy())) {
            if (pageRequest.getIsAsc()) {
                page.setAsc(pageRequest.getOrderBy().split(","));
            } else {
                page.setDesc(pageRequest.getOrderBy().split(","));
            }
        }
        return page;
    }
}

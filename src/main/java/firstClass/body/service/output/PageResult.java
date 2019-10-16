package firstClass.body.service.output;

import firstClass.body.entity.IBaseObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wkit
 * @Date: 2019-10-15 22:47
 */
@Data
public class PageResult<T extends IBaseObject> {
    private long total;
    private long current;
    private long size;
    private long pages;
    private List<T> data = new ArrayList<>();
}

package firstClass.body.service.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wkit
 * @Date: 2019-10-15 23:07
 */
@Data
public class PageRequest implements Serializable {
    private int current = 1;
    private int size = 20;
    private String orderBy;
    private Boolean isAsc = true;
    private String criteria;
}

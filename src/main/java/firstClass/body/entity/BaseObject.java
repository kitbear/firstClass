package firstClass.body.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Author: wkit
 * @Date: 2019-10-15 16:02
 */
@Data
public class BaseObject implements IBaseObject{
    @TableId
    protected Long id;
}

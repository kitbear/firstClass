package firstClass.body.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @Author: wkit
 * @Date: 2019-10-15 16:47
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fc_blog_content")
public class BlogContent extends BaseObject{

    @TableField(value = "blog_oid")
    private UUID blogOID;

    @TableField(value = "user_id")
    private UUID userOID;

    @TableField(value = "title")
    private String title;

    @TableField(value = "content")
    private String content;

    @TableField(value = "viewers")
    private Integer viewers;

    @TableField(value = "publish_date")
    private ZonedDateTime publishDate;

    @TableField(value = "created_date")
    private ZonedDateTime createdDate;
}

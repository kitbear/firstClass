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
 * @Date: 2019-10-15 16:37
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fc_user")
public class Contact extends BaseObject{

    @TableField(value = "contact_oid")
    private UUID contactOID;

    @TableField(value = "nick_name")
    private String nickName;

    @TableField(value = "age")
    private Integer age;

    @TableField(value = "gender")
    private Integer gender;

    @TableField(value = "head_portrait")
    private String headPortrait;

    @TableField(value = "email")
    private String email;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "created_date")
    private ZonedDateTime createdDate;

    @TableField(value = "last_modified_date")
    private ZonedDateTime lastModifiedDate;

}

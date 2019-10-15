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
 * @Date: 2019-10-15 16:00
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("fc_user")
public class User extends BaseObject {

    @TableField(value = "user_oid")
    private UUID userOID;

    @TableField(value = "user_ip")
    private String userIp;

    @TableField(value = "contact_id")
    private Long contactId;

    @TableField(value = "login_no")
    private String loginNo;

    @TableField(value = "password_hash")
    private String passwordHash;

    @TableField(value = "is_deleted")
    private Boolean isDeleted;

    @TableField(value = "created_date")
    private ZonedDateTime createdDate;

    @TableField(value = "last_modified_date")
    private ZonedDateTime lastModifiedDate;


}

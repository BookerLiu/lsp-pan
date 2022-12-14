package com.booker.lsp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户-角色关联关系表
 * </p>
 *
 * @author BookerLiu
 * @since 2022-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("a_user_role")
@ApiModel(value="UserRole对象", description="用户-角色关联关系表")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = " 用户ID")
    private String userId;

    @ApiModelProperty(value = "角色ID")
    private String roleId;


}

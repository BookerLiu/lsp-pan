package com.booker.lsp.vo;

import com.booker.lsp.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.List;

/**
 * @Author BookerLiu
 * @Date 2022/12/6 10:53
 * @Description user
 **/
@ApiModel("脱敏userVO")
public class UserVO {


    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "是否启用 Y启用, N 未启用 默认Y")
    private String enable;

    @ApiModelProperty(value = "是否锁定 Y锁定, N 未锁定 默认N")
    private String locked;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "头像图片base64")
    private String pictureBase64;

    @ApiModelProperty(value = "用户角色")
    private List<Role> userRoles;

}

package com.booker.lsp.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author BookerLiu
 * @since 2022-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("a_user")
@ApiModel(value="User对象", description="用户表")
public class User  implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

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

    private List<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        authorities = new ArrayList<>();
        for (Role role : userRoles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }



    @Override
    public boolean isAccountNonExpired() {
        //默认账户不会过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //是否锁定
        return "N".equals(locked);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //默认密码不会过期
        return true;
    }

    @Override
    public boolean isEnabled() {
        //是否启用
        return "Y".equals(enable);
    }

}

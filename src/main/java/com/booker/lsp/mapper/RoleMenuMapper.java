package com.booker.lsp.mapper;

import com.booker.lsp.entity.Role;
import com.booker.lsp.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色_菜单 关联关系表 Mapper 接口
 * </p>
 *
 * @author BookerLiu
 * @since 2022-11-12
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    List<Role> getRoleByMenuUri(@Param("uri") String uri);

}

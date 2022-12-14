package com.booker.lsp.mapper;

import com.booker.lsp.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author BookerLiu
 * @since 2022-11-12
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> queryRolesByUserId(@Param("userId") String userId);

}

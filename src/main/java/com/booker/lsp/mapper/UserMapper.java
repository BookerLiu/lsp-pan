package com.booker.lsp.mapper;

import com.booker.lsp.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author BookerLiu
 * @since 2022-11-12
 */
public interface UserMapper extends BaseMapper<User> {

    User queryUserByUsername(@Param("username") String username);

}

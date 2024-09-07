package com.study.vue.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.study.vue.entity.Account;
@Mapper
public interface UserMapper {
    @Select("select * from ad_account where username = #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);

}

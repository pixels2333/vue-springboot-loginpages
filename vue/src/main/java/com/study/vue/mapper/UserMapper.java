
/**
 * UserMapper 接口提供了与数据库进行用户相关操作的方法。
 * 它使用 MyBatis 注解将 SQL 查询映射到 Java 方法。
 * 
 * 方法:
 * - findAccountByNameOrEmail: 根据提供的用户名或电子邮件从数据库中检索 Account 实体。
 * 
 * 注解:
 * - @Mapper: 表示该接口是一个 MyBatis 映射器。
 * - @Select: 指定要执行的 SQL 查询。
 * 
 * @see com.study.vue.entity.Account
 */
package com.study.vue.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.study.vue.entity.Account;
@Mapper
public interface UserMapper {
    @Select("select * from ad_account where username = #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);
    @Insert("insert into db_account(email,username,password) values(#{username},#{password},#{email})")
    int creatAccount(String username, String password, String email);

}

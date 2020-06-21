package com.chao.eurekaServe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * UserDetailsService 换成jdbc模式
 * @author 杨文超
 * @Date 2020-06-21
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private DataSource dataSource;

    /**
     * 初始化UserDetailsService实例
     * 根据用户名获取用户信息进行登录校验
     * @return
     * @throws Exception
     * @author 杨文超
     * @Date 2020-06-20
     */
    @Bean
    public UserDetailsService users() throws Exception {
        //jdbc模式,通过loadUserByUsername()方法从数据库查询数据验证
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(dataSource);
        return jdbcUserDetailsManager;
    }
}

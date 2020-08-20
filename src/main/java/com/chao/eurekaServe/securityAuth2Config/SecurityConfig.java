package com.chao.eurekaServe.securityAuth2Config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * UserDetailsService 换成jdbc模式
 * @author 杨文超
 * @Date 2020-06-21
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 登录拦截过滤器配置
     * @param http
     * @throws Exception
     * @author 杨文超
     * @Date 2020-06-21
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //禁用 csrf
                .cors().and().csrf().disable()
                .authorizeRequests()
                //放行注册服务
                .antMatchers("/eureka/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin();
    }

    /**
     * 初始化UserDetailsService实例
     * 根据用户名获取用户信息进行登录校验
     * @return
     * @throws Exception
     * @author 杨文超
     * @Date 2020-06-21
     * @modify  2020-08-19
     */
    @Bean
    public UserDetailsService users() throws Exception {
        //自定义redis模式,通过loadUserByUsername()方法从redis查询数据验证
        RedisUserDetailsManager redisUserDetailsManager = new RedisUserDetailsManager();
        return redisUserDetailsManager;
    }
}

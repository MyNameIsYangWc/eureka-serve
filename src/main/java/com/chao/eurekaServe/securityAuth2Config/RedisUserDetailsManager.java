package com.chao.eurekaServe.securityAuth2Config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 获取用户认证信息
 * @author 杨文超
 * @Date 2020-08-19
 */
public class RedisUserDetailsManager implements UserDetailsManager {

    private Logger logger= LoggerFactory.getLogger(RedisUserDetailsManager.class);

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DataSource dataSource;

    /**
     * 根据账号获取用户信息校验
     * @param username
     * @author 杨文超
     * @Date 2020-08-19
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HashOperations ops = redisTemplate.opsForHash();
        boolean flag;
        JSONObject userDetails;
        do{
            userDetails = JSON.parseObject((String) ops.get("oauth", username.toLowerCase()));
            flag=false;
            if (userDetails == null) {
                logger.warn("{}在redis中不存在",username.toLowerCase());
                flag=true;
                JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
                jdbcUserDetailsManager.setUsersByUsernameQuery("select username,password,enabled from user where username = ? and del_flag=0");
                UserDetails userDetails1 = jdbcUserDetailsManager.loadUserByUsername(username);
                if(userDetails1==null){
                    throw new UsernameNotFoundException(username);
                }
                ops.put("oauth",username.toLowerCase(),JSON.toJSONString(userDetails1));
            }
        }while (flag);
        logger.info("{}在redis存在",username.toLowerCase());
        //权限列表
        Collection authorities=new ArrayList();
        userDetails.getJSONArray("authorities").stream().forEach(item->{
            authorities.add(new SimpleGrantedAuthority( ((Map)item).get("authority").toString()));
        });
        return new User(userDetails.getString("username"),userDetails.getString("password"),userDetails.getBoolean("enabled"),userDetails.getBoolean("accountNonExpired"),
                userDetails.getBoolean("credentialsNonExpired"),userDetails.getBoolean("accountNonLocked"),authorities);
    }

    @Override
    public void createUser(UserDetails userDetails) {

    }

    @Override
    public void updateUser(UserDetails userDetails) {

    }

    @Override
    public void deleteUser(String s) {

    }

    @Override
    public void changePassword(String s, String s1) {

    }

    @Override
    public boolean userExists(String s) {
        return false;
    }
}

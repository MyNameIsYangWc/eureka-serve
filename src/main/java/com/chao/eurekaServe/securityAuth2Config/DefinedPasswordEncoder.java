package com.chao.eurekaServe.securityAuth2Config;

import com.chao.eurekaServe.utils.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 密码验证前加密在校验
 * @author 杨文超
 * @Date 2020-08-20
 */
@Component
public class DefinedPasswordEncoder implements PasswordEncoder {

    private static final PasswordEncoder INSTANCE = new DefinedPasswordEncoder();

    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    /**
     * 密码校验
     * @param rawPassword
     * @param encodedPassword
     * @return
     */
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String str="{noop}";
        String pwdSecurity = SecurityUtils.pwdSecurity(rawPassword.toString());
        return (str+pwdSecurity).equals(encodedPassword);
    }

    public static PasswordEncoder getInstance() {
        return INSTANCE;
    }

    private DefinedPasswordEncoder() {
    }
}

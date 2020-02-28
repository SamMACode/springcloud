package com.netflix.cloud.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author Sam Ma
 * @date 2020/2/27
 * Oauth2Config服务定义哪些应用程序可以使用服务
 */
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 自定义UserDetailsService服务，在内存中设置用户名、密码信息
     * please see https://github.com/crownwangguan/springboot-authorization-server-oauth2
     * @return
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        final User.UserBuilder userBuilder = User.builder().passwordEncoder(encoder::encode);
        UserDetails user = userBuilder
                .username("john.carnell")
                .password("password")
                .roles("USER")
                .build();

        UserDetails admin = userBuilder
                .username("william.woodward")
                .password("password")
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

}

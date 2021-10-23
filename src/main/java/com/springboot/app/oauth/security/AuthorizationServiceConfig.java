package com.springboot.app.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;



@Configuration
@EnableAuthorizationServer
public class AuthorizationServiceConfig extends AuthorizationServerConfigurerAdapter
{

    @Autowired
    private AuthenticationManager authenticationManager;

    // POST /oauth/token
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
    {
        endpoints.authenticationManager(authenticationManager)
                 .tokenStore(tokenStore())
                 .accessTokenConverter(accessTokenConverter());
    }

    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() 
    {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();

        // Secret code to encode the JWT tokens, will be needed to decrypt later on also.
        tokenConverter.setSigningKey("1234");

        return tokenConverter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception 
    {
        clients.inMemory().withClient("frontend_app") // Here the client Id we would like to register, an angular app, react, ios...
                          .secret("1234")
                          .scopes("read", "write")
                          .authorizedGrantTypes("password", "refresh_token") // password -> the users does exist in our system (username + pass)
                          .accessTokenValiditySeconds(3600) // 1 hour
                          .refreshTokenValiditySeconds(3600) // 1 hour
                          .and()
                          .withClient("android_app") // Here the client Id we would like to register, an angular app, react, ios...
                          .secret("12345")
                          .scopes("read", "write")
                          .authorizedGrantTypes("password", "refresh_token") // password -> the users does exist in our system (username + pass)
                          .accessTokenValiditySeconds(3600) // 1 hour
                          .refreshTokenValiditySeconds(3600); // 1 hour
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception
    {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }
}

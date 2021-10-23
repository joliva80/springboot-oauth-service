package com.springboot.app.oauth.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


@RefreshScope
@Configuration
@EnableAuthorizationServer
public class AuthorizationServiceConfig extends AuthorizationServerConfigurerAdapter
{
    @Autowired 
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenExtraInfo tokenExtraInfo; // Token extra data

    @Autowired
    private Environment env;

    // POST /oauth/token
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
    {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain(); // optional token data
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenExtraInfo, accessTokenConverter()));

        endpoints.authenticationManager(authenticationManager)
                 .tokenStore(tokenStore())
                 .accessTokenConverter(accessTokenConverter())
                 .tokenEnhancer(tokenEnhancerChain); // add optional data
    }

    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() 
    {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();

        // Secret code to encode the JWT tokens, will be needed to decrypt later on also.
        //tokenConverter.setSigningKey("1234");
        tokenConverter.setSigningKey(env.getProperty("config.security.oauth.jwt.key"));
        
        return tokenConverter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception 
    {
        //clients.inMemory().withClient("frontend_app") // Here the client Id we would like to register, an angular app, react, ios...
        clients.inMemory().withClient(env.getProperty("config.security.oauth.client.id")) // Here the client Id we would like to register, an angular app, react, ios...
                          //.secret("1234")
                          //.secret(passwordEncoder.encode("1234"))
                          .secret(passwordEncoder.encode(env.getProperty("config.security.oauth.client.secret")))
                          .scopes("read", "write")
                          .authorizedGrantTypes("password", "refresh_token") // password -> the users does exist in our system (username + pass)
                          .accessTokenValiditySeconds(3600) // 1 hour
                          .refreshTokenValiditySeconds(3600) // 1 hour
                          //.and()
                          //.withClient("android_app") // Here the client Id we would like to register, an angular app, react, ios...
                          //.secret("1234")
                          //.scopes("read", "write")
                          //.authorizedGrantTypes("password", "refresh_token") // password -> the users does exist in our system (username + pass)
                          //.accessTokenValiditySeconds(3600) // 1 hour
                          //.refreshTokenValiditySeconds(3600) // 1 hour
                          ;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception
    {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }
}

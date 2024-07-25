package com.sinocarbon.workflow.client.oauth2.token;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;
import com.sinocarbon.workflow.client.oauth2.token.store.RedisTemplateTokenStore;

/**
 * @author owen 类说明 redis存储token
 */
@Configuration
@Slf4j
public class TokenStoreConfig {

	@Resource
	private DataSource dataSource;

	@Autowired(required = false)
	private RedisTemplate<String, Object> redisTemplate;

	@Bean
	@ConditionalOnProperty(prefix = "spring.security.oauth2.token.store", name = "type", havingValue = "jdbc", matchIfMissing = false)
	public JdbcTokenStore jdbcTokenStore() {
		log.info("JdbcTokenStore must be provided");
		return new JdbcTokenStore(dataSource);

	}

	@Bean
	@ConditionalOnProperty(prefix = "spring.security.oauth2.token.store", name = "type", havingValue = "redis", matchIfMissing = true)
	public RedisTemplateTokenStore redisTokenStore() {
		log.info("RedisTemplateTokenStore must be provided");
		Assert.state(redisTemplate != null, "RedisTemplate must be provided");
		RedisTemplateTokenStore redisTemplateStore = new RedisTemplateTokenStore();
		redisTemplateStore.setRedisTemplate(redisTemplate);
		return redisTemplateStore;

	}

	// 使用jwt替换原有的uuid生成token方式
	@Configuration
	@ConditionalOnProperty(prefix = "spring.security.oauth2.token.store", name = "type", havingValue = "jwt", matchIfMissing = false)
	public static class JWTTokenConfig {
		@Bean
		public JwtTokenStore jwtTokenStore() {
			log.info("JwtTokenStore must be provided");
			return new JwtTokenStore(jwtAccessTokenConverter());
		}

		@Bean
		public JwtAccessTokenConverter jwtAccessTokenConverter() {
			JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
			accessTokenConverter.setSigningKey("neusoft");
			return accessTokenConverter;
		}
	}

}

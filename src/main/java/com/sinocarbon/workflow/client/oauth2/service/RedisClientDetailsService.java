package com.sinocarbon.workflow.client.oauth2.service;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisClientDetailsService extends JdbcClientDetailsService {

	@Resource
	private ObjectMapper objectMapper;

	public RedisClientDetailsService(DataSource dataSource) {
		super(dataSource);
	}
	private static final String CACHE_CLIENT_KEY = "oauth_client_details";
	
	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
		ClientDetails clientDetails = null;
		// 先从redis获取
		Object value = redisTemplate.boundHashOps(CACHE_CLIENT_KEY).get(clientId);
		if (value == null) {
			log.error("从redis获取clientId信息错误");
			throw new InvalidClientException("从redis获取clientId信息错误");
		} else {
			try {
				clientDetails = objectMapper.readValue(value.toString(), BaseClientDetails.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return clientDetails;
	}

}

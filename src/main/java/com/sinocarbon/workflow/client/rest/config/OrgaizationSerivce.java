package com.sinocarbon.workflow.client.rest.config;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

@Service
public class OrgaizationSerivce {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${orga.uri}")
	public String orgaUri;
	
	
	public String getToEntpCode(String clientId, String uniqueCode, Integer level, String orgaVersion) {
		String url = orgaUri + "/organization/superior?clientId=" + clientId + "&uniqueCode="
				+ uniqueCode + "&level=" +level ;
		if(!StringUtils.isEmpty(orgaVersion)) {
			url = url + "&versionId=" +orgaVersion;
		}
		String response = this.restTemplate.getForObject(url, String.class);
		return JSONObject.parseObject(response).getString("data");
	}
}

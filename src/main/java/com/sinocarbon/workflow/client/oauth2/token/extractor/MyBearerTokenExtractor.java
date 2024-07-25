package com.sinocarbon.workflow.client.oauth2.token.extractor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.sinocarbon.polaris.commons.utils.Constant;
import com.sinocarbon.polaris.commons.utils.TokenCommonsUtils;

public class MyBearerTokenExtractor implements TokenExtractor {

	@Override
	public Authentication extract(HttpServletRequest request) {
		String tokenValue = TokenCommonsUtils.extractToken(request);
		if (tokenValue != null && !Constant.EMPTY.equals(tokenValue)) {
			request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, OAuth2AccessToken.BEARER_TYPE);
			PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(tokenValue,
					Constant.EMPTY);
			return authentication;
		} else {
			return null;
		}
	}

}

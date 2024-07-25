package com.sinocarbon.workflow.client.rest.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
		// 长连接
		PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager();
		// 总连接数
		pollingConnectionManager.setMaxTotal(1000);
		// 同路由的并发数
		pollingConnectionManager.setDefaultMaxPerRoute(1000);
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		httpClientBuilder.setConnectionManager(pollingConnectionManager);
		HttpClient httpClient = httpClientBuilder.build();

		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
				httpClient);
		// 连接超时
		clientHttpRequestFactory.setConnectTimeout(12000);
		// 数据读取超时时间，即SocketTimeout
		clientHttpRequestFactory.setReadTimeout(12000);
		// 连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
		clientHttpRequestFactory.setConnectionRequestTimeout(200);
		// 缓冲请求数据，默认值是true。通过POST或者PUT大量发送数据时，建议将此属性更改为false，以免耗尽内存。
		clientHttpRequestFactory.setBufferRequestBody(false);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(clientHttpRequestFactory);
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
		//post请求乱码的问题
		List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
		for (HttpMessageConverter<?> httpMessageConverter : list) {
			if (httpMessageConverter instanceof StringHttpMessageConverter) {
				((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(StandardCharsets.UTF_8);
				break;
			}
		}
		return restTemplate;
	}
	
	class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
				throws IOException {
//			HttpServletRequest parentRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//					.getRequest();
			if(RequestContextHolder.getRequestAttributes() != null) {
				ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				RequestContextHolder.setRequestAttributes(sra, true);
				HttpServletRequest parentRequest  = sra.getRequest();
				// 获取旧的请求头传入到新的请求头中
				Enumeration<String> headersNames = parentRequest.getHeaderNames();
				while (headersNames.hasMoreElements()) {
					String headerName = (String) headersNames.nextElement();
					String headerValue = parentRequest.getHeader(headerName);
					if ((request.getHeaders().get(headerName) == null || request.getHeaders().get(headerName).size() <= 0)
							&& (!headerName.equals("cookie") && !headerName.startsWith("authorization")
									&& !headerName.startsWith("host"))) {
						request.getHeaders().add(headerName, headerValue);
					}
				}
			}else {
				log.info("HeaderRequestInterceptor Handler---invokes url: {}", request.getURI());
			}
			log.info("Headers: {}", request.getHeaders());
			// 保证请求继续被执行
			return execution.execute(request, body);
		}

	}

}

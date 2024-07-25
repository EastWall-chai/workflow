package com.sinocarbon.workflow.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.sinocarbon.polaris.commons.utils.BaseResponse;

import lombok.extern.slf4j.Slf4j;


/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 * 异常通用处理
*/
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

	/**
	 * IllegalArgumentException异常处理返回json
	 * 状态码:400
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({ IllegalArgumentException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<BaseResponse> badRequestException(HttpServletRequest request,IllegalArgumentException exception) {
		log.error("IllegalArgumentException Handler---Host: {} invokes url: {} ERROR: {}", request.getRemoteHost(),
				request.getRequestURL(), exception);
		return new ResponseEntity<BaseResponse>(BaseResponse.failed(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage()),HttpStatus.BAD_REQUEST);
	}
	/**
	 * AccessDeniedException异常处理返回json
	 * 状态码:403
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({ AccessDeniedException.class })
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<BaseResponse> badMethodExpressException(HttpServletRequest request,AccessDeniedException exception) {
		log.error("AccessDeniedException Handler---Host: {} invokes url: {} ERROR: {}", request.getRemoteHost(),
				request.getRequestURL(), exception);
		return new ResponseEntity<BaseResponse>(BaseResponse.failed(HttpServletResponse.SC_FORBIDDEN, exception.getMessage()),HttpStatus.FORBIDDEN);
	
	}
	
	/**
	 * 系统抛出的没有处理过的异常
	 * 
	 * @param request
	 * @param e
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ResponseEntity<BaseResponse> defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
		if (e instanceof NoHandlerFoundException) {
			log.error("NoHandlerFoundException Handler---Host: {} invokes url: {} ERROR: {}", request.getRemoteHost(),
					request.getRequestURL(), e);
			return new ResponseEntity<BaseResponse>(
					BaseResponse.failed(HttpServletResponse.SC_NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
		} else {
			log.error("DefaultException Handler---Host: {} invokes url: {} ERROR: {}", request.getRemoteHost(),
					request.getRequestURL(), e);
			return new ResponseEntity<BaseResponse>(
					BaseResponse.failed(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

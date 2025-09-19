package com.lnjoying.justice.commonweb.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class RequestHolder implements Filter
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AccessFilter.class);

	private static ThreadLocal<HttpServletRequest> httpServletRequestHolder =
			new ThreadLocal<HttpServletRequest>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
						 FilterChain chain) throws IOException, ServletException {
		httpServletRequestHolder.set((HttpServletRequest) request); // 绑定到当前线程
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			throw e;
		} finally {
			httpServletRequestHolder.remove(); // 清理资源引用
		}
	}

	@Override
	public void destroy() {
	}

	public static HttpServletRequest getHttpServletRequest() {
		return httpServletRequestHolder.get();
	}

}
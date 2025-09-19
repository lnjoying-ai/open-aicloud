/*
 * Copyright 2002-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lnjoying.justice.iam.authz.opa.pep.pathMatcher;

import org.springframework.http.server.PathContainer;
import org.springframework.http.server.RequestPath;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.MappingMatch;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;


public abstract class ServletRequestPathUtils
{


	public static final String PATH_ATTRIBUTE = ServletRequestPathUtils.class.getName() + ".PATH";


	public static RequestPath parseAndCache(HttpServletRequest request) {
		RequestPath requestPath = ServletRequestPath.parse(request);
		request.setAttribute(PATH_ATTRIBUTE, requestPath);
		return requestPath;
	}


	public static RequestPath getParsedRequestPath(ServletRequest request) {
		RequestPath path = (RequestPath) request.getAttribute(PATH_ATTRIBUTE);
		Assert.notNull(path, () -> "Expected parsed RequestPath in request attribute \"" + PATH_ATTRIBUTE + "\".");
		return path;
	}


	public static void setParsedRequestPath(@Nullable RequestPath requestPath, ServletRequest request) {
		if (requestPath != null) {
			request.setAttribute(PATH_ATTRIBUTE, requestPath);
		}
		else {
			request.removeAttribute(PATH_ATTRIBUTE);
		}
	}

	public static boolean hasParsedRequestPath(ServletRequest request) {
		return (request.getAttribute(PATH_ATTRIBUTE) != null);
	}


	public static void clearParsedRequestPath(ServletRequest request) {
		request.removeAttribute(PATH_ATTRIBUTE);
	}


	// Methods to select either parsed RequestPath or resolved String lookupPath


	public static Object getCachedPath(ServletRequest request) {

		// RequestPath is parsed once and cached in the DispatcherServlet if any HandlerMapping uses PathPatterns.
		// A String lookupPath is resolved and cached in each HandlerMapping that uses String matching.
		// So we try lookupPath first, then RequestPath second

		String lookupPath = (String) request.getAttribute(RequestMappingInfoUtils.PATH_ATTRIBUTE);
		if (lookupPath != null) {
			return lookupPath;
		}
		RequestPath requestPath = (RequestPath) request.getAttribute(PATH_ATTRIBUTE);
		if (requestPath != null) {
			return requestPath.pathWithinApplication();
		}
		throw new IllegalArgumentException(
				"Neither a pre-parsed RequestPath nor a pre-resolved String lookupPath is available.");
	}




	public static boolean hasCachedPath(ServletRequest request) {
		return (request.getAttribute(PATH_ATTRIBUTE) != null ||
				request.getAttribute(RequestMappingInfoUtils.PATH_ATTRIBUTE) != null);
	}


	/**
	 * Simple wrapper around the default {@link RequestPath} implementation that
	 * supports a servletPath as an additional prefix to be omitted from
	 * {@link #pathWithinApplication()}.
	 */
	private static final class ServletRequestPath implements RequestPath {

		private final RequestPath requestPath;

		private final PathContainer contextPath;

		private ServletRequestPath(String rawPath, @Nullable String contextPath, String servletPathPrefix) {
			Assert.notNull(servletPathPrefix, "`servletPathPrefix` is required");
			this.requestPath = RequestPath.parse(URI.create(rawPath), contextPath + servletPathPrefix);
			this.contextPath = PathContainer.parsePath(StringUtils.hasText(contextPath) ? contextPath : "");
		}

		@Override
		public String value() {
			return this.requestPath.value();
		}

		@Override
		public List<Element> elements() {
			return this.requestPath.elements();
		}

		@Override
		public PathContainer contextPath() {
			return this.contextPath;
		}

		@Override
		public PathContainer pathWithinApplication() {
			return this.requestPath.pathWithinApplication();
		}

		@Override
		public RequestPath modifyContextPath(String contextPath) {
			throw new UnsupportedOperationException();
		}


		@Override
		public boolean equals(@Nullable Object other) {
			if (this == other) {
				return true;
			}
			if (other == null || getClass() != other.getClass()) {
				return false;
			}
			return (this.requestPath.equals(((ServletRequestPath) other).requestPath));
		}

		@Override
		public int hashCode() {
			return this.requestPath.hashCode();
		}

		@Override
		public String toString() {
			return this.requestPath.toString();
		}


		public static RequestPath parse(HttpServletRequest request) {
			String requestUri = (String) request.getAttribute(WebUtils.INCLUDE_REQUEST_URI_ATTRIBUTE);
			requestUri = (requestUri != null ? requestUri : request.getRequestURI());
			String servletPathPrefix = getServletPathPrefix(request);
			return (StringUtils.hasText(servletPathPrefix) ?
					new ServletRequestPath(requestUri, request.getContextPath(), servletPathPrefix) :
					RequestPath.parse(URI.create(requestUri), request.getContextPath()));
		}

		@Nullable
		private static String getServletPathPrefix(HttpServletRequest request) {
			HttpServletMapping mapping = (HttpServletMapping) request.getAttribute(RequestDispatcher.INCLUDE_MAPPING);
			mapping = (mapping != null ? mapping : request.getHttpServletMapping());
			if (ObjectUtils.nullSafeEquals(mapping.getMappingMatch(), MappingMatch.PATH)) {
				String servletPath = (String) request.getAttribute(WebUtils.INCLUDE_SERVLET_PATH_ATTRIBUTE);
				servletPath = (servletPath != null ? servletPath : request.getServletPath());
				servletPath = (servletPath.endsWith("/") ? servletPath.substring(0, servletPath.length() - 1) : servletPath);
				return UriUtils.encodePath(servletPath, StandardCharsets.UTF_8);
			}
			return null;
		}
	}

}

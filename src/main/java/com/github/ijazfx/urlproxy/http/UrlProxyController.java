package com.github.ijazfx.urlproxy.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ijazfx.urlproxy.model.Url;
import com.github.ijazfx.urlproxy.repo.UrlRepository;
import com.google.common.base.Strings;

import io.graphenee.util.storage.FileStorage;

@RestController
public class UrlProxyController {

	@Autowired
	UrlRepository repo;

	@Autowired
	FileStorage storage;

	@RequestMapping(path = { "/call/{alias}", "/call/{alias}/**" })
	public void call(@PathVariable("alias") String alias, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Url url = repo.findOneByAlias(alias);
		if (url != null && url.getIsActive()) {
			CloseableHttpClient httpClient;
			try {
				if (url.getHasCertificate()) {
					String resourcePath = storage.resourcePath("certificates", url.getFilePath());
					InputStream certificateInputStream = storage.resolve(resourcePath);
					SSLContext customSSLContext = CustomSSLContext.createCustomSSLContext(certificateInputStream, url.getPassphrase());
					httpClient = HttpClients.custom().setSSLContext(customSSLContext).build();
				} else {
					httpClient = HttpClients.createDefault();
				}

				String pathPrefix = "/call/" + alias;
				HttpRequestBase httpRequest = convertToHttpRequest(url.getUrl(), pathPrefix, request);

				try (CloseableHttpResponse apacheResponse = httpClient.execute(httpRequest)) {
					response.setStatus(apacheResponse.getStatusLine().getStatusCode());
					for (Header header : apacheResponse.getAllHeaders()) {
						response.setHeader(header.getName(), header.getValue());
					}
					apacheResponse.getEntity().writeTo(response.getOutputStream());
				}

			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setContentType("text/plain");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write("Error: " + e.getMessage());
			}

		} else {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("No url definition found for alias " + alias);
		}
	}

	// Utility method to convert HttpServletRequest to Apache HttpRequestBase
	private HttpRequestBase convertToHttpRequest(String url, String pathPrefix, HttpServletRequest request) throws UnsupportedEncodingException {
		String path = request.getRequestURI().replace(pathPrefix, "");
		String method = request.getMethod();
		String queryString = request.getQueryString();
		String host = "";
		try {
			URI uri = new URI(url);
			host = uri.getHost();
			url = url + path;
		} catch (URISyntaxException e) {
			// nothing to log.
		}

		HttpRequestBase httpRequest;

		switch (method) {
		case "GET":
			if (!Strings.isNullOrEmpty(queryString)) {
				url += "?" + queryString;
			}
			httpRequest = new HttpGet(url);
		break;
		case "POST":
			httpRequest = new HttpPost(url);
			// Add form parameters to the request
			List<NameValuePair> params = new ArrayList<>();

			// Assuming 'request' is an instance of HttpServletRequest
			Map<String, String[]> parameterMap = request.getParameterMap();
			for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
				String key = entry.getKey();
				String[] values = entry.getValue();
				for (String value : values) {
					params.add(new BasicNameValuePair(key, value));
				}
			}

			// Encode the parameters and set them as the entity of the POST request
			HttpEntity entity = new UrlEncodedFormEntity(params);
			((HttpPost) httpRequest).setEntity(entity);
		break;
		// Add other cases for other HTTP methods if required
		default:
			throw new UnsupportedOperationException("HTTP method not supported: " + method);
		}

		// Copy headers from the original request to the proxy request
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			if (!headerName.matches("(host)")) {
				Enumeration<String> headerValues = request.getHeaders(headerName);
				while (headerValues.hasMoreElements()) {
					String headerValue = headerValues.nextElement();
					httpRequest.addHeader(headerName, headerValue);
				}
			}
		}

		httpRequest.addHeader("Host", host);

		return httpRequest;
	}

	// Utility method to convert Apache HttpResponse to Spring's HttpHeaders
	private HttpHeaders convertToHttpHeaders(CloseableHttpResponse response) {
		HttpHeaders httpHeaders = new HttpHeaders();

		// Add headers from Apache HttpResponse to Spring's HttpHeaders
		for (Header header : response.getAllHeaders()) {
			httpHeaders.add(header.getName(), header.getValue());
		}

		return httpHeaders;
	}

}

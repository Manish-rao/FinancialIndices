package com.sol.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class MyUtils {

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	public static <T> HttpEntity<T> getEnityWithHttpHeader(T requestObject) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<T> request = new HttpEntity<>(requestObject, headers);
		return request;
	}
	
	public static HttpEntity<?> getHttpHeader() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<?> request = new HttpEntity<>(headers);
		return request;
	}
}

package com.sol.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "No records were found ...")
public class NoRecordsFoundException extends RuntimeException {

	static final long serialVersionUID = -3387516993224229948L;

	public NoRecordsFoundException(String message) {
		super(message);
	}

}

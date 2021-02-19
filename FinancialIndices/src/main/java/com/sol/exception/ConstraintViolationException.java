package com.sol.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Some constraints are violated ...")
public class ConstraintViolationException extends RuntimeException {

	static final long serialVersionUID = -3387516993224229948L;

	public ConstraintViolationException(String message) {
		super(message);
	}

}

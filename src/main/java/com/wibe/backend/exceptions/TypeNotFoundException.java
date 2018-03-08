package com.wibe.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Wrong Type")
public class TypeNotFoundException extends RuntimeException{


}

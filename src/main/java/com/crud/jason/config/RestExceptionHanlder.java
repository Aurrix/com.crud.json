/*
 * Copyright 2002-2018 the original author or authors.
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
package com.crud.jason.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javassist.NotFoundException;

/**
 * @author Alisher Urunov
 *
 * Uncomment @ControllerAdvice for custom exception handling
 */
@ControllerAdvice
public class RestExceptionHanlder extends ResponseEntityExceptionHandler {
	 	
		@ExceptionHandler(NotFoundException.class)

	    public ResponseEntity<?> notFoundReply(NotFoundException ex) {
	        ExceptionEntity reply;
	        return new ResponseEntity<>(reply = new ExceptionEntity(HttpStatus.NOT_FOUND,"Error 404, try again with different path variable", ex.getLocalizedMessage())
	        		,new HttpHeaders(),reply.getStatus());
	    }
		
		@Override
		protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
				HttpHeaders headers, HttpStatus status, WebRequest request) {
			 ExceptionEntity reply;
		        return new ResponseEntity<>(reply = new ExceptionEntity(HttpStatus.BAD_REQUEST, "Entry didn't path validation(should not be blank and not exceed 40 characters)", ex.getLocalizedMessage())
		        		,new HttpHeaders(),reply.getStatus());
		}

}

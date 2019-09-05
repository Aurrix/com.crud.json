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

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Alisher Urunov
 *
 */
@Getter
@Setter
public class ExceptionEntity {

	private HttpStatus status;
    private String message;
    private List<Object> errors;
	
    public ExceptionEntity(HttpStatus status, String message, Object error) {
		super();
		this.status = status;
		this.message = message;
		errors = new ArrayList<Object>();
		errors.add(error);
	}
    
    public ExceptionEntity(HttpStatus status, String message, List<Object> errors) {
		super();
		this.status = status;
		this.message = message;
		this.errors = errors;
	}

    
    
}
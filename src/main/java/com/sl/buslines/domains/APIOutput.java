package com.sl.buslines.domains;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public @Data class  APIOutput<T> {
	@JsonProperty("StatusCode")
	private int statusCode;
	@JsonProperty("Message")
	private String message;
	@JsonProperty("ExecutionTime")
	private int executionTime;
	@JsonProperty("ResponseData")
	private ResponseData<T> responseData;
}
		
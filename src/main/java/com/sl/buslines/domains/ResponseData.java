package com.sl.buslines.domains;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public @Data class ResponseData<T> {
	@JsonProperty("Version")
	private String version;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("Result")
	private List<T> result;
}

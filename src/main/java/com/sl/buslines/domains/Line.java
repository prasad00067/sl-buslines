package com.sl.buslines.domains;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


public @Data class Line {
	@JsonProperty("LineNumber")
	private int lineNumber;
	@JsonProperty("LineDesignation")
	private String lineDesignation;
	@JsonProperty("DefaultTransportMode")
	private String defaultTransportMode;
	@JsonProperty("DefaultTransportModeCode")
	private String defaultTransportModeCode;
	@JsonProperty("LastModifiedUtcDateTime")
	private String lastModifiedUtcDateTime;
	@JsonProperty("ExistsFromDate")
	private String existsFromDate;
}

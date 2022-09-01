package com.sl.buslines.domains;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


public @Data class JourneyPatternPointOnLine {
	@JsonProperty("LineNumber")
	private int lineNumber;
	@JsonProperty("DirectionCode")
	private int directionCode;
	@JsonProperty("JourneyPatternPointNumber")
	private int journeyPatternPointNumber;
	@JsonProperty("LastModifiedUtcDateTime")
	private String lastModifiedUtcDateTime;
	@JsonProperty("ExistsFromDate")
	private String existsFromDate;
	private StopPoint stopPoint;
}

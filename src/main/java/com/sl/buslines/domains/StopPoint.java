package com.sl.buslines.domains;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


public @Data class StopPoint {
	@JsonProperty("StopPointNumber")
	private int stopPointNumber;
	@JsonProperty("StopPointName")
	private String stopPointName;
	@JsonProperty("StopAreaNumber")
	private int stopAreaNumber;
	@JsonProperty("LocationNorthingCoordinate")
	private Double locationNorthingCoordinate;
	@JsonProperty("LocationEastingCoordinate")
	private Double locationEastingCoordinate;
	@JsonProperty("ZoneShortName")
	private String zoneShortName;
	@JsonProperty("StopAreaTypeCode")
	private String stopAreaTypeCode;
	@JsonProperty("LastModifiedUtcDateTime")
	private String lastModifiedUtcDateTime;
	@JsonProperty("ExistsFromDate")
	private String existsFromDate;

}

package com.sl.buslines.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.sl.buslines.domains.APIOutput;
import com.sl.buslines.domains.JourneyPatternPointOnLine;
import com.sl.buslines.domains.LineInformation;
import com.sl.buslines.domains.StopPoint;

import om.sl.sllonglines.exception.BusServiceException;

@Service
public class BusService {
	private static final Logger logger = LoggerFactory.getLogger(BusService.class);

	@Value("${apiUrl}")
	private String apiUrl;

	@Value("${apiKey}")
	private String apiKey = System.getProperty("apiKey");
	
	private RestTemplate restTemplate = new RestTemplate();
	
	/**
	 * To get the top ten list lines of lines information
	 * @return 
	 * @throws BusServiceException 
	 */
	public Set<LineInformation> getBusLinesInformation() throws BusServiceException{
		if (StringUtils.isEmpty(apiKey)){
			logger.error("Missing apiKey in configuration file!, please update it first and rerun");
			throw new BusServiceException("Please provide apiKey in application.properties file!", 401);
		}
		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("key", apiKey);
		uriParams.put("model", "jour");
		logger.info("Fetching bus lines from sl api: {}", apiUrl);
		APIOutput<JourneyPatternPointOnLine> out = restTemplate.exchange(
				apiUrl,
		        HttpMethod.GET,
		        null,
		        new ParameterizedTypeReference<APIOutput<JourneyPatternPointOnLine>>() {},
		        uriParams).getBody();
		try {
			return filterDataByLineNumber(out);
		} catch (IOException e) {
			throw new BusServiceException("Internal server error!", 500);
		}
	}

	/**
	 * To get the stops information from sl api
	 * @return map - stop id and stop address
	 * @throws StreamReadException
	 * @throws DatabindException
	 * @throws IOException
	 */
	public Map<Integer, StopPoint> getTotalBusStops(){
		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("key", apiKey);
		uriParams.put("model", "stop");
		logger.info("Get total stops from sl rest api using model: stop");
		APIOutput<StopPoint> out = restTemplate.exchange(
				apiUrl,
		        HttpMethod.GET,
		        null,
		        new ParameterizedTypeReference<APIOutput<StopPoint>>() {},
		        uriParams).getBody();

		List<StopPoint> stopPoints = out.getResponseData().getResult();
		// Map every stop in the list to its unique id 
		return stopPoints.stream().collect(Collectors.toMap(StopPoint::getStopPointNumber, Function.identity()));
	}

	/**
	 * 
	 * @param <T>
	 * @param linesApiResponse
	 * @return
	 */
	private <T> Set<LineInformation> filterDataByLineNumber(APIOutput<JourneyPatternPointOnLine> linesApiResponse)
			throws StreamReadException, DatabindException, IOException {
		List<JourneyPatternPointOnLine> totalStopPoints = linesApiResponse.getResponseData().getResult();
		// group stops by line number
		Map<Integer, List<JourneyPatternPointOnLine>> lines = totalStopPoints.stream().collect(Collectors.groupingBy(
				p -> p.getLineNumber(), Collectors.mapping((JourneyPatternPointOnLine p) -> p, Collectors.toList())));
		//total stops in sl bus lines
		Map<Integer, StopPoint> stops = getTotalBusStops();
		// Sort lines based on stops count in each line
		TreeSet<LineInformation> lineInfos = new TreeSet<>();
		for (Entry<Integer, List<JourneyPatternPointOnLine>> entry : lines.entrySet()) {
			LineInformation info = new LineInformation();
			info.setLineNumber((int) entry.getKey());
			info.updateStopsInfo(entry.getValue(), stops);
			lineInfos.add(info);
		}
		// return top 10 lines which has maximum stops in thier route
		return lineInfos.stream().limit(10).collect(Collectors.toCollection(TreeSet::new));
	}

}

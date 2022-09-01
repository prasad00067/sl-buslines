package com.sl.buslines.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.buslines.domains.APIOutput;
import com.sl.buslines.domains.JourneyPatternPointOnLine;
import com.sl.buslines.domains.LineInformation;
import com.sl.buslines.domains.StopPoint;

import om.sl.sllonglines.exception.BusServiceException;

@ExtendWith(MockitoExtension.class)
public class BusServiceTest {

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.setProperty("apiKey", "testkey");
	}
	
	@InjectMocks
    private BusService busService;

    @Mock
    private RestTemplate restTemplate;
    
    @Test
    public void getBusLinesInformation() throws  BusServiceException, StreamReadException, DatabindException, IOException {
		ObjectMapper mapper = new ObjectMapper(); 
		APIOutput<JourneyPatternPointOnLine> out = mapper.readValue(readFile("jour.json"),
				new TypeReference<APIOutput<JourneyPatternPointOnLine>>() {
				});
    	Mockito.when(restTemplate.exchange(Mockito.any(), Mockito.any(),
    			Mockito.any(), eq(new ParameterizedTypeReference<APIOutput<JourneyPatternPointOnLine>>() {}), 
    			Mockito.anyMap())).thenReturn(new ResponseEntity<>(out, HttpStatus.OK));
    	
    	APIOutput<StopPoint> stopPointOut = mapper.readValue(readFile("stop.json"),
				new TypeReference<APIOutput<StopPoint>>() {
				});
    	Mockito.when(restTemplate.exchange(Mockito.any(), Mockito.any(),
    			Mockito.any(), eq(new ParameterizedTypeReference<APIOutput<StopPoint>>() {}), 
    			Mockito.anyMap())).thenReturn(new ResponseEntity<>(stopPointOut, HttpStatus.OK));
    	Set<LineInformation> info = busService.getBusLinesInformation();
    	List<LineInformation>  list = new ArrayList<>(info);
    	assertEquals(info.size(), 10);
    	assertEquals(list.get(0).getLineNumber(), 312);
    	assertTrue(list.get(0).getStops().size()>list.get(1).getStops().size());
    }
    
    private InputStream readFile(String filename) {
    	return getClass().getClassLoader().getResourceAsStream(filename);
    }
    
}

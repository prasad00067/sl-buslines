package com.sl.buslines.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.sl.buslines.service.BusService;

import om.sl.sllonglines.exception.BusServiceException;

@Controller
public class BusLinesController {
	private static final Logger logger = LoggerFactory.getLogger(BusLinesController.class);
	
	@Value("${server.port}")
	private int serverPort;
	
	@Value("${server.url}")
	private String url;

	@Autowired
	private BusService data;
	
	/**
	 * To get top 10 bus lines with most stops in the route
	 * @return Set - set of lines information
	 * @throws StreamReadException
	 * @throws DatabindException
	 * @throws IOException
	 */
	@GetMapping(value = "stops")
	@ResponseBody
	public ResponseEntity<?> getStops() {
		try {
			return new ResponseEntity<>(data.getBusLinesInformation(), HttpStatus.OK);
		} catch (BusServiceException e) {
			logger.error(e.getMessage());
			if (e.getErrorCode() == 500)
				return new ResponseEntity<>("Internal server error!", HttpStatus.INTERNAL_SERVER_ERROR);
			else 
				return new ResponseEntity<>("Missing authentication token in application configuraiton file", HttpStatus.UNAUTHORIZED);
		}
	}

	/**
	 * To display bus lines with most stops in a well formated HTML page
	 * @return 
	 */
	@RequestMapping(value = "/")
	public String getIndex(Model model){
		model.addAttribute("server_port", serverPort);
		model.addAttribute("server_url", url);
		return "index";
	}
}


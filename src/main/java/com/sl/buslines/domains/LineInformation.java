package com.sl.buslines.domains;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;

public @Data class LineInformation implements Comparable<LineInformation>{
	
	private static final Logger logger = LoggerFactory.getLogger(LineInformation.class);
	
	private int lineNumber;
	private Stop origin;
	private Stop destination;
	private TreeSet<Stop> stops = new TreeSet<>(new Comparator<Stop>() {
		// sorting stops based on their stopAreaNumber
        @Override
        public int compare(Stop stop1, Stop stop2) {
        	StopPoint add1 = stop1.getStopPointaddress();
        	StopPoint add2 = stop2.getStopPointaddress();
        	// To remove duplicate stop names
        	// Few they have different stoppoint number but stopname and area number are same
            if (add1.getStopAreaNumber() > add2.getStopAreaNumber())
            	return 1;
            else if (add1.getStopAreaNumber() == add2.getStopAreaNumber())
                return 0;
            else
            	return -1;
        }
    });
	
	
	/**
	 * To update the available stops details in a line 
	 * @param stopPointsInLine
	 * @param totalStopPoints
	 */
	public void updateStopsInfo(List<JourneyPatternPointOnLine> stopPointsInLine, Map<Integer, StopPoint> totalStopPoints) {
		Map<Object, List<JourneyPatternPointOnLine>> stopsByDirection = getStopsByDirection(stopPointsInLine);
		int directionWithMaxStops = (int) Collections.max(stopsByDirection.entrySet(), (entry1, entry2) -> entry1.getValue().size() - entry2.getValue().size()).getKey();
		for (JourneyPatternPointOnLine pointOnline : stopsByDirection.get(directionWithMaxStops)) {
			Stop stop = new Stop();
			stop.setDirectionCode(pointOnline.getDirectionCode());
			StopPoint sp = totalStopPoints.get(pointOnline.getJourneyPatternPointNumber());
			if (sp == null) {
				// if stop address not found don't add it to the stop list
				logger.info("Stop address not found: {}", pointOnline.getJourneyPatternPointNumber());
				continue;
			}
			stop.setStopPointaddress(totalStopPoints.get(pointOnline.getJourneyPatternPointNumber()));
			this.stops.add(stop);
		}
		if(!this.stops.isEmpty()) {
			this.setOrigin(this.stops.first());
			this.setDestination(this.stops.last());
		}else {
			logger.info("Address not found for the stops on line {}", this.lineNumber);
		}
	}

	/**
	 * To divide stops by direction in a line
	 * @param stopPointsInLine
	 * @return stops by direction
	 */
	private Map<Object, List<JourneyPatternPointOnLine>> getStopsByDirection(List<JourneyPatternPointOnLine> stopPointsInLine) {
		// Separate stops in line by direction of the route
		return stopPointsInLine.stream().collect(Collectors.groupingBy(
				s -> s.getDirectionCode(), Collectors.mapping((JourneyPatternPointOnLine p) -> p, Collectors.toList())));
	}

	@Override
	public int compareTo(LineInformation line2) {
		if (this.stops.size() > line2.stops.size()) {
			return -1;
		} else if (this.stops.size() == line2.getStops().size()
				&& this.lineNumber == line2.lineNumber) {
			return 0;
		} else {
			return 1;
		}
	}
}

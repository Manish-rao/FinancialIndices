package com.sol.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.sol.dto.StatisticsDTO;
import com.sol.dto.TickDTO;
import com.sol.exception.NoRecordsFoundException;

@Service
public class TickService {

	Map<String, List<TickDTO>> tickMap = new ConcurrentHashMap<>();
	private static final int SIXTY_SECONDS = 60 * 1000;
	long currentTime = System.currentTimeMillis() - SIXTY_SECONDS;

	Log logger = LogFactory.getLog(TickService.class);

	public void create(TickDTO tickDto) {
		logger.info(tickDto.getInstrument() + "Price:" + tickDto.getPrice() + " Time : " + tickDto.getTimestamp());
		tickMap.computeIfAbsent(tickDto.getInstrument(), k -> new CopyOnWriteArrayList<TickDTO>()).add(tickDto);
	}

	public StatisticsDTO getAllStatistics() {
		long start = System.currentTimeMillis();
		if (tickMap.isEmpty())
			throw new NoRecordsFoundException("No records were found");
		// Prepare flatennedList by combining List of List into a single list and then
		// filter records from last 60 seconds
		List<TickDTO> flattenedList = tickMap.values()
				.stream().flatMap(List::stream).collect(Collectors.toList())
				.stream().filter(t -> t.getTimestamp() > currentTime).collect(Collectors.toList());

		StatisticsDTO stats = new StatisticsDTO(); 
		stats = stats.calculateStatistics(flattenedList);
		logger.info("Stats: "+stats.getAvg()+" Count:"+stats.getCount()+" Max:"+stats.getMax()+" Min:"+stats.getMin());
		logger.info("Time taken in MS: "+(System.currentTimeMillis()-start));
		return stats;
	}

	public StatisticsDTO getStatisticsForIdentifier(String identifier) {
		long start = System.currentTimeMillis();
		if (tickMap.isEmpty() || !tickMap.containsKey(identifier))
			throw new NoRecordsFoundException("No records were found");
		
		List<TickDTO> flattenedList = tickMap.get(identifier)
				.stream().filter(t -> t.getTimestamp() > currentTime).collect(Collectors.toList());
		StatisticsDTO stats = new StatisticsDTO();
		stats = stats.calculateStatistics(flattenedList);
		logger.info("Stats for Single Instr: "+stats.getAvg()+" Count:"+stats.getCount()+" Max:"+stats.getMax()+" Min:"+stats.getMin());
		logger.info("Time taken in ms for Single Instr: "+(System.currentTimeMillis()-start));
		return stats;
	}
	
	public void clearOldData() {
		logger.info("Clearing old Data###");
		long sixtySeconds = System.currentTimeMillis() - SIXTY_SECONDS;
		Iterator<List<TickDTO>> iterator = tickMap.values().iterator();
		while(iterator.hasNext()) {
			iterator.next().removeIf(x-> sixtySeconds > x.getTimestamp());
		}
		tickMap.values().removeIf(x-> x == null || x.isEmpty()) ;
	   }

}

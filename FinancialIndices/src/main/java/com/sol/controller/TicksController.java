package com.sol.controller;


import java.sql.SQLException;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sol.dto.StatisticsDTO;
import com.sol.dto.TickDTO;
import com.sol.service.TickService;


@RestController
@Validated
public class TicksController {

	private static final int SIXTY_SECONDS = 60 * 1000;
	@Autowired
	private TickService tickService;
	Log logger = LogFactory.getLog(TickService.class);
	

	@PostMapping("/ticks")
	public ResponseEntity<String>  postTicks(@Valid @RequestBody TickDTO tickDto) throws SQLException
	{
		if(System.currentTimeMillis()-tickDto.getTimestamp()>SIXTY_SECONDS) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		tickService.create(tickDto);
		return new ResponseEntity<>(
		          HttpStatus.CREATED);
	}
	
	@GetMapping("/statistics")
	public StatisticsDTO getStatistics() {
		return tickService.getAllStatistics();
	}
	
	
	@GetMapping("/statistics/{inst_id}")
	public StatisticsDTO getStatisticsForInstrument(@PathVariable String inst_id) {
		return tickService.getStatisticsForIdentifier(inst_id);
	}
	 
}

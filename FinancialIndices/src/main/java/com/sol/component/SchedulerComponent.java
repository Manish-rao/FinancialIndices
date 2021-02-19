package com.sol.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sol.service.TickService;

@Component
public class SchedulerComponent {
	
	@Autowired
	private TickService tickService;
	
	@Scheduled(cron = "0 0/5 * * * *")
	public void clearOldData() {
		tickService.clearOldData();
	}
}

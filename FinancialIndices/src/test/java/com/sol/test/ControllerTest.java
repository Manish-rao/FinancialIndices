package com.sol.test;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import com.sol.FinancialIndicesApplication;
import com.sol.dto.StatisticsDTO;
import com.sol.dto.TickDTO;
import com.sol.service.TickService;
import com.sol.util.MyUtils;;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { FinancialIndicesApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { "test" })
public class ControllerTest {
	
	//ToDo change below
	private static String[] instArr = new String[] {"IBM_N", "Bayer", "BASF", "Munich Re", "Volkswagen",
			"Adidas", "Beiersdorf", "RWE", "Henkel", "Continental", "Fresenius", "Daimler"};
	
	@Autowired
	private  TestRestTemplate  restTemplate;

	private static ExecutorService executor = Executors.newFixedThreadPool(4);
	
	private int status = 201;
	
	@Test
	public void testTicksPostSuccess() {
		ResponseEntity<Object> exchange = this.restTemplate.exchange(
				UriComponentsBuilder.fromUriString("/ticks").buildAndExpand(new HashMap<>()).toUri(), HttpMethod.POST,
				MyUtils.getEnityWithHttpHeader(new TickDTO(instArr[(int) (Math.random() * instArr.length - 1)],
						Math.random() * Double.MAX_VALUE, System.currentTimeMillis())), Object.class);
		Assert.assertEquals(201, exchange.getStatusCodeValue());
	}
	
	@Test
	public void testTicksPostNoContent() {
		long sevenDaysBefore = System.currentTimeMillis() - 1000 * 60 * 60 * 24;
		//random long between 7 days ago and 60 seconds ago
		 long generatedLong = sevenDaysBefore + (long) (Math.random() * (System.currentTimeMillis() - 1000 * 60 - sevenDaysBefore));
		ResponseEntity<Object> exchange = this.restTemplate.exchange(
				UriComponentsBuilder.fromUriString("/ticks").buildAndExpand(new HashMap<>()).toUri(), HttpMethod.POST,
				MyUtils.getEnityWithHttpHeader(new TickDTO(instArr[(int) (Math.random() * instArr.length - 1)],
						Math.random() * Double.MAX_VALUE, generatedLong)), Object.class);
		Assert.assertEquals(204, exchange.getStatusCodeValue());
	}
	
	@Test
	public void testTicksPostSuccessBulk() {
		for(int i=0;i<12000;i++) {
		ResponseEntity<Object> exchange = this.restTemplate.exchange(
				UriComponentsBuilder.fromUriString("/ticks").buildAndExpand(new HashMap<>()).toUri(), HttpMethod.POST,
				MyUtils.getEnityWithHttpHeader(new TickDTO(instArr[(int) (Math.random() * instArr.length - 1)],
						Math.random() * Double.MAX_VALUE, System.currentTimeMillis())), Object.class);
		Assert.assertEquals(201, exchange.getStatusCodeValue());
		}
	}
	
	@Test
	public void checkStatisticsWithAllUniqueMapKeys() {
		TickService tickService = new TickService();
		tickService.clearOldData();
		for(int i=0;i<12000;i++) {
		this.restTemplate.exchange(
				UriComponentsBuilder.fromUriString("/ticks").buildAndExpand(new HashMap<>()).toUri(), HttpMethod.POST,
				MyUtils.getEnityWithHttpHeader(new TickDTO(instArr[1]+i,
						Math.random() * 100, System.currentTimeMillis())), Object.class);
		}
		System.out.println("CALLING statics now********");
		ResponseEntity<StatisticsDTO> exchange = this.restTemplate.exchange(
				UriComponentsBuilder.fromUriString("/statistics").buildAndExpand(new HashMap<>()).toUri()
				, HttpMethod.GET
				, MyUtils.getHttpHeader(), StatisticsDTO.class);
		Assert.assertEquals(200, exchange.getStatusCodeValue());
	}
	
	@Test
	public void testPostAndGetWithConcurrency() throws InterruptedException {
	    int numberOfThreads = 500;
	    ExecutorService service = Executors.newFixedThreadPool(500, new CustomizableThreadFactory("thread-"));
	    CountDownLatch latch = new CountDownLatch(numberOfThreads);
	    for (int i = 0; i < numberOfThreads; i++) {
	        service.execute(() -> {
	        	ResponseEntity<Object> exchange = this.restTemplate.exchange(
	    				UriComponentsBuilder.fromUriString("/ticks").buildAndExpand(new HashMap<>()).toUri(), HttpMethod.POST,
	    				MyUtils.getEnityWithHttpHeader(new TickDTO(instArr[1],
	    						Math.random() * Double.MAX_VALUE, System.currentTimeMillis())), Object.class);
	        	String uriString = "/statistics/"+instArr[1];
				this.restTemplate.exchange(
						UriComponentsBuilder.fromUriString(uriString).buildAndExpand(new HashMap<>()).toUri()
						, HttpMethod.GET
						, MyUtils.getHttpHeader(), StatisticsDTO.class);
	        	System.out.println("testPostAndGetWithConcurrency****"+Thread.currentThread().getName());
	        	if(exchange.getStatusCodeValue()!=201)
	        		status=exchange.getStatusCodeValue();
	            latch.countDown();
	        });
	    }
	    latch.await();
	    Assert.assertEquals(201, status);
	}
	
	@Test
	public void testMixedRequests() {
		CompletableFuture<ResponseEntity<Object>>[]  task = new CompletableFuture[50];
		for(int i=0;i<50;i++) {
			CompletableFuture<ResponseEntity<Object>> c= CompletableFuture.supplyAsync(()->{
				ResponseEntity<Object> exchange = restTemplate.exchange(
						UriComponentsBuilder.fromUriString("/ticks").buildAndExpand(new HashMap<>()).toUri(), HttpMethod.POST,
						MyUtils.getEnityWithHttpHeader(new TickDTO(instArr[(int) (Math.random() * instArr.length - 1)],
								Math.random() * Double.MAX_VALUE, System.currentTimeMillis())), Object.class);
				Assert.assertEquals(201, exchange.getStatusCodeValue());
				return exchange;
			}, executor);
					
			task[i] = c;
		}
		CompletableFuture.allOf(task)
		.thenRun(()->{
			ResponseEntity<StatisticsDTO> exchange = this.restTemplate.exchange(
					UriComponentsBuilder.fromUriString("/statistics").buildAndExpand(new HashMap<>()).toUri()
					, HttpMethod.GET
					, MyUtils.getHttpHeader(), StatisticsDTO.class);
			Assert.assertEquals(200, exchange.getStatusCodeValue());
		});
	}
	
	@Test 
	public void checkTimeTakenForAllStats() {
		ResponseEntity<StatisticsDTO> exchange = this.restTemplate.exchange(
				UriComponentsBuilder.fromUriString("/statistics").buildAndExpand(new HashMap<>()).toUri()
				, HttpMethod.GET
				, MyUtils.getHttpHeader(), StatisticsDTO.class);
		Assert.assertEquals(200, exchange.getStatusCodeValue());
	}
	
	@Test 
	public void checkTimeTakenForAllStatsForOneInstr() {
		for(int i=0;i<instArr.length;i++) {
			String uriString = "/statistics/"+instArr[i];
			ResponseEntity<StatisticsDTO> exchange = this.restTemplate.exchange(
					UriComponentsBuilder.fromUriString(uriString).buildAndExpand(new HashMap<>()).toUri()
					, HttpMethod.GET
					, MyUtils.getHttpHeader(), StatisticsDTO.class);
			boolean isStatus200Or204 = exchange.getStatusCodeValue()==200 || exchange.getStatusCodeValue()==204;
			Assert.assertTrue(isStatus200Or204);
		}
	}
}

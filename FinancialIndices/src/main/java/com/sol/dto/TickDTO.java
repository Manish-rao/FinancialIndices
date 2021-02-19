package com.sol.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TickDTO {
	
	@JsonIgnore
	private Long id;
	
	@NotNull(message = "Instrument cannot be null")
	private String instrument;
	
	@DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
	private Double price;
	
	private Long timestamp;
       
	public TickDTO(String instrument, double price, Long timestamp) {
		super();
		this.instrument = instrument;
		this.price = price;
		this.timestamp = timestamp;
	}
	
	public String getInstrument() {
		return instrument;
	}
	
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public Long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
    
    
    
}

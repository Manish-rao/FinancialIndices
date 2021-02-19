package com.sol.dto;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sol.util.MyUtils;

@JsonInclude
public class StatisticsDTO {
	private volatile Double avg = 0d; // we want more precision since it's calculated by summing.
    private volatile Double min = Double.MAX_VALUE;
    private volatile Double max = 0d;
    private AtomicLong count = new AtomicLong();
    
	public StatisticsDTO(Double avg, Double min, Double max, AtomicLong count) {
		super();
		this.avg = avg;
		this.min = min;
		this.max = max;
		this.count = count;
	}

	public StatisticsDTO() {
		super();
	}

	@JsonIgnore
	public StatisticsDTO calculateStatistics(List<TickDTO> tickEntities) {
    	float sum =0;
		for (TickDTO ent: tickEntities) {
			count.getAndIncrement();
            sum += ent.getPrice();
            min = Math.min(min, ent.getPrice());
            max = Math.max(max, ent.getPrice());
        }
		avg = MyUtils.round((sum / count.get()),2);
		return getCurrentStats();
	}
	
	@JsonIgnore
    public StatisticsDTO getCurrentStats() {
        return new StatisticsDTO(avg, min, max, count);
    }

	@JsonProperty
	public Double getAvg() {
		return avg;
	}

	@JsonProperty
	public Double getMin() {
		return min;
	}

	@JsonProperty
	public Double getMax() {
		return max;
	}


	@JsonProperty
	public long getCount() {
		return count.get();
	}
}

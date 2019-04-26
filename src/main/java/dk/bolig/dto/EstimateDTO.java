package dk.bolig.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EstimateDTO {

	private long price;
	private double [][] salesHistory;
	private double [][] trend;

	public double[][] getSalesHistory() {
		return salesHistory;
	}

	public void setSalesHistory(double[][] salesHistory) {
		this.salesHistory = salesHistory;
	}

	public EstimateDTO() {
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long value) {
		this.price = value;
	}

	public double [][] getTrend() {
		return trend;
	}

	public void setTrend(double [][] trend) {
		this.trend = trend;
	}

}

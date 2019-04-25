package dk.bolig.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EstimateDTO {

	private String numberOfSales;
	private String price;
	private double [][] salesHistory;

	public double[][] getSalesHistory() {
		return salesHistory;
	}

	public void setSalesHistory(double[][] salesHistory) {
		this.salesHistory = salesHistory;
	}

	public EstimateDTO() {
	}

	public String getNumberOfSales() {
		return numberOfSales;
	}

	public void setNumberOfSales(String type) {
		this.numberOfSales = type;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String value) {
		this.price = value;
	}

}

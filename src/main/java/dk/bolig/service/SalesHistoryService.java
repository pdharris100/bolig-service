package dk.bolig.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SalesHistoryService {
	private static final Logger LOG = LoggerFactory.getLogger(SalesHistoryService.class);

	// @Cacheable("estimate")
	public double[][] getSalesDataForPostCodeAndStreet(String postCode, String street) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://api.boliga.dk/api/v2/sold/search/results?"
				+ "zipcodeFrom=" + postCode
				+ "&zipcodeTo=" + postCode
				+ "&street=" + street
				+ "&propertyType=3&sort=date-d";
		
		int page = 1;
		List<double[]> dataSet = new ArrayList<double[]>();

		ResponseEntity<String> response = callBoliga(restTemplate, url, page);
		processResults(dataSet, response);		
		
		while (page < getMaxPage(response)) {
			page++;
			response = callBoliga(restTemplate, url, page);
			processResults(dataSet, response);	
		}

		double[][] dataArray = new double[dataSet.size()][];
		dataArray = dataSet.toArray(dataArray);

		return dataArray;
	}

	private ResponseEntity<String> callBoliga(RestTemplate restTemplate, String url, int page) {
		url = url + "&page=" + page;
		//LOG.debug("****** Connecting to " + url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		//LOG.debug("****** Response " + response.getBody());
		return response;
	}

	private void processResults(List<double[]> dataSet, ResponseEntity<String> response) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root;
		try {
			root = mapper.readTree(response.getBody());
		} catch (IOException e1) {
			return;
		}
		JsonNode results = root.get("results");
		if (results != null) {
			Iterator<JsonNode> iter = results.iterator();
			while (iter.hasNext()) {
				JsonNode resultNode = iter.next();
				Date date;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd").parse(resultNode.get("soldDate").asText());
				} catch (ParseException e) {
					continue;
				}

				long price = resultNode.get("price").asLong();

				LOG.debug(date + " " + price);

				dataSet.add(new double[] { Double.valueOf(date.getTime()), Double.valueOf(price) });
			}
		}
	}
	
	private int getMaxPage(ResponseEntity<String> response) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root;
		try {
			root = mapper.readTree(response.getBody());
		} catch (IOException e1) {
			return Integer.MIN_VALUE;
		}
		return root.path("meta").path("maxPage").asInt();
	}

	private String processPostCode(String postCode) {
		int postCodeAsInt = Integer.valueOf(postCode).intValue();
		if (postCodeAsInt >= 1050 && postCodeAsInt <= 1549) {
			return "1050-1549";
		} else if (postCodeAsInt >= 1550 && postCodeAsInt <= 1799) {
			return "1550-1799";
		} else if (postCodeAsInt >= 1800 && postCodeAsInt <= 1999) {
			return "1800-1999";
		} else {
			return postCode;
		}
	}

}

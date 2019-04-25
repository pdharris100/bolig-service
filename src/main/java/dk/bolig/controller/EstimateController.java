package dk.bolig.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dk.bolig.dto.EstimateDTO;
import dk.bolig.service.SalesHistoryService;
import dk.bolig.tools.PriceEstimator;

@CrossOrigin
@RestController
public class EstimateController {
	
	private static final Logger LOG = LoggerFactory.getLogger(EstimateController.class);


	@GetMapping("/estimate")
    public EstimateDTO estimate(@RequestParam(value="postcode") String postcode,
    		@RequestParam(value="street") String street) throws IOException {
    	LOG.debug("********** Estimating for " + street + " " + postcode);
    	SalesHistoryService salesHistoryService = new SalesHistoryService();
    	double [][] salesHistory = salesHistoryService.getSalesDataForPostCodeAndStreet(postcode, street);
        EstimateDTO estimate = PriceEstimator.estimate(salesHistory);
        estimate.setSalesHistory(salesHistory);
        return estimate;
    }
}

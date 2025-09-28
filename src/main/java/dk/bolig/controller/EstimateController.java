package dk.bolig.controller;

import dk.bolig.dto.EstimateDTO;
import dk.bolig.service.SalesHistoryService;
import dk.bolig.tools.PriceEstimator;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class EstimateController {

    private static final Logger LOG = LoggerFactory.getLogger(EstimateController.class);

    private final SalesHistoryService salesHistoryService;
    private final PriceEstimator priceEstimator;

    @Autowired
    public EstimateController(SalesHistoryService salesHistoryService, PriceEstimator priceEstimator) {
        this.salesHistoryService = salesHistoryService;
        this.priceEstimator = priceEstimator;
    }

    @GetMapping("/estimate")
    public EstimateDTO estimate(@RequestParam(value = "postcode") String postcode,
                                @RequestParam(value = "street") String street) throws IOException {
        LOG.debug("********** Estimating for " + street + " " + postcode);
        double[][] salesHistory = salesHistoryService.getSalesDataForPostCodeAndStreet(postcode, street);
        EstimateDTO estimate = priceEstimator.estimate(salesHistory);
        estimate.setSalesHistory(salesHistory);
        return estimate;
    }
}

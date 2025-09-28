package dk.bolig.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SalesHistoryService {
    private static final Logger LOG = LoggerFactory.getLogger(SalesHistoryService.class);

    @Value("${max.price}")
    private int maxPrice;

    @Value("${boliga.api.url}")
    private String boligaApiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public SalesHistoryService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Cacheable("estimate")
    public double[][] getSalesDataForPostCodeAndStreet(String postCode, String street) throws IOException {
        String url = boligaApiUrl
                + "zipcodeFrom=" + postCode
                + "&zipcodeTo=" + postCode
                + "&street=" + street
                + "&salesDateMin=2009&propertyType=3&sort=date-d";

        int page = 1;
        List<double[]> dataSet = new ArrayList<>();

        // Get first page
        ResponseEntity<String> response = callBoliga(url, page);
        processResults(dataSet, response);

        // Get remaining pages
        while (page < getMaxPage(response)) {
            page++;
            response = callBoliga(url, page);
            processResults(dataSet, response);
        }

        return dataSet.toArray(new double[0][]);
    }

    private ResponseEntity<String> callBoliga(String url, int page) {
        url = url + "&page=" + page;
        //LOG.debug("****** Connecting to " + url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        //LOG.debug("****** Response " + response.getBody());
        return response;
    }

    private void processResults(List<double[]> dataSet, ResponseEntity<String> response) {
        JsonNode root;
        try {
            root = objectMapper.readTree(response.getBody());
        } catch (IOException | NullPointerException e1) {
            return;
        }
        JsonNode results = root.get("results");
        if (results != null) {
            for (JsonNode resultNode : results) {
                if (!resultNode.path("saleType").asText().equals("Alm. Salg")) {
                    continue;
                }

                Date date;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(resultNode.get("soldDate").asText());
                } catch (ParseException e) {
                    continue;
                }

                double price = resultNode.get("sqmPrice").asDouble();
                if (price > maxPrice) {
                    continue;
                }

                LOG.debug(date + " " + price);

                dataSet.add(new double[]{Double.valueOf(date.getTime()), price});
            }
        }
    }

    private int getMaxPage(ResponseEntity<String> response) {
        JsonNode root;
        try {
            root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        } catch (IOException | NullPointerException e1) {
            return Integer.MIN_VALUE;
        }
        return root.path("meta").path("maxPage").asInt();
    }
}

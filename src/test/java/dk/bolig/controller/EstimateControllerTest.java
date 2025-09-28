
package dk.bolig.controller;

import dk.bolig.dto.EstimateDTO;
import dk.bolig.service.SalesHistoryService;
import dk.bolig.tools.PriceEstimator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EstimateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalesHistoryService salesHistoryService;

    @MockBean
    private PriceEstimator priceEstimator;

    @Test
    public void testEstimateHappyPath() throws Exception {
        double[][] salesHistory = {{1.0, 2.0}};
        EstimateDTO estimate = new EstimateDTO();
        estimate.setPrice(12345L);

        when(salesHistoryService.getSalesDataForPostCodeAndStreet("1234", "test street")).thenReturn(salesHistory);
        when(priceEstimator.estimate(salesHistory)).thenReturn(estimate);

        mockMvc.perform(get("/estimate?postcode=1234&street=test street"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(12345));
    }

    @Test
    public void testEstimateServiceThrowsException() throws Exception {
        when(salesHistoryService.getSalesDataForPostCodeAndStreet(any(), any())).thenThrow(new IOException("Test Exception"));

        mockMvc.perform(get("/estimate?postcode=1234&street=test street"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testEstimateMissingQueryParam() throws Exception {
        mockMvc.perform(get("/estimate?postcode=1234"))
                .andExpect(status().isBadRequest());
    }
}

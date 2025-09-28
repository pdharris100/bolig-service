
package dk.bolig.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class EstimateDTOTest {

    @Test
    public void testPriceBehavior() {
        EstimateDTO estimate = new EstimateDTO();
        long price = 1000000L;
        estimate.setPrice(price);
        assertEquals(price, estimate.getPrice());
    }

    @Test
    public void testSalesHistoryBehavior() {
        EstimateDTO estimate = new EstimateDTO();
        double[][] salesHistory = {{1.0, 2.0}, {3.0, 4.0}};
        estimate.setSalesHistory(salesHistory);
        assertArrayEquals(salesHistory, estimate.getSalesHistory());
    }

    @Test
    public void testTrendBehavior() {
        EstimateDTO estimate = new EstimateDTO();
        double[][] trend = {{5.0, 6.0}, {7.0, 8.0}};
        estimate.setTrend(trend);
        assertArrayEquals(trend, estimate.getTrend());
    }

    @Test
    public void testInitialStateBehavior() {
        EstimateDTO estimate = new EstimateDTO();
        assertEquals(0, estimate.getPrice());
        assertNull(estimate.getSalesHistory());
        assertNull(estimate.getTrend());
    }
}

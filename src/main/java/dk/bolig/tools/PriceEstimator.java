package dk.bolig.tools;

import java.io.IOException;
import java.util.GregorianCalendar;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dk.bolig.dto.EstimateDTO;

public class PriceEstimator {
	private static final Logger LOG = LoggerFactory.getLogger(PriceEstimator.class);

	public static EstimateDTO estimate(double[][] salesData) throws IOException {

		SimpleRegression regression = new SimpleRegression();
		regression.addData(salesData);
		String simplePredictionRounded = String
				.valueOf(Math.round(regression.predict((new GregorianCalendar()).getTime().getTime())));

		LOG.debug("Price = " + simplePredictionRounded);
		LOG.debug(salesData.length + " sales");

		EstimateDTO estimate = new EstimateDTO();
		estimate.setNumberOfSales(String.valueOf(salesData.length));
		estimate.setPrice(simplePredictionRounded);

		return estimate;
	}
}

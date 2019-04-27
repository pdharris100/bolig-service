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
		long today = (new GregorianCalendar()).getTime().getTime();
		long originDate = 1230764400000l; // 01-01-2009
		long simplePredictionRounded = Math.round(regression.predict(today));
		long originPredictionRounded = Math.round(regression.predict(originDate));

		LOG.debug("Price = " + simplePredictionRounded);
		LOG.debug("Origin Price: " + originPredictionRounded);
		LOG.debug(salesData.length + " sales");

		EstimateDTO estimate = new EstimateDTO();
		estimate.setPrice(simplePredictionRounded);
		estimate.setTrend(new double [][] {
			{originDate, originPredictionRounded},
			{today, simplePredictionRounded}
			});

		return estimate;
	}
}

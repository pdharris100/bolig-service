package dk.bolig.tools;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.bolig.dto.EstimateDTO;
import dk.bolig.maths.PolyTrendLine;
import dk.bolig.maths.TrendLine;

public class PriceEstimator {
	private static final Logger LOG = LoggerFactory.getLogger(PriceEstimator.class);
    
	public static EstimateDTO estimate(double [][] salesData) throws IOException {
		TrendLine t = new PolyTrendLine(2);
		t.setValues(salesData[1], salesData[0]);
		double prediction = t.predict(Double.valueOf((new GregorianCalendar()).getTime().getTime()));
	    String predictionRounded = String.valueOf(Math.round(prediction*10.0)/10.0);
		
	    LOG.debug(salesData[0].length + " sales");
	    LOG.debug(predictionRounded); 
	    
	    EstimateDTO estimate = new EstimateDTO();
	    estimate.setNumberOfSales(String.valueOf(salesData[0].length));
	    estimate.setPrice(predictionRounded);
	    
	    return estimate;	    
    }
}

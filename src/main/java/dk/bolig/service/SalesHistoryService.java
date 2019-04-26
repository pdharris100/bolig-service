package dk.bolig.service;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SalesHistoryService {
	private static final Logger LOG = LoggerFactory.getLogger(SalesHistoryService.class);
    
	public static final String BASE_URL = "https://www.boliga.dk/salg/resultater?so=1&sort=omregnings_dato-a&maxsaledate=today";
	public static final String POST_CODE_URL = "iPostnr";
	public static final String STREET_URL = "gade";
	public static final String TYPE_URL = "type";
	public static final String ORIGINAL_STREET_URL = "origgade";
	public static final String AND = "&";
	public static final String EQUALS_URL = "=";
	
    //@Cacheable("estimate")
	public double[][] getSalesDataForPostCodeAndStreet(String postCode, String street) throws IOException {
		postCode = processPostCode(postCode);
		String type = "Ejerlejlighed";
		
        String url = BASE_URL +
				AND + POST_CODE_URL + EQUALS_URL + postCode +
				AND + STREET_URL + EQUALS_URL + street +
				AND + TYPE_URL + EQUALS_URL + type +
				AND + "minsaledate=2009&origgade=" + street;
		
        int page = 1; 
        List <double []> dataSet = new ArrayList <double []>();
		while (true) {
			Document doc = Jsoup.connect(url + "&p=" + page).get();
			if (rowsExist(doc)) {
				process(doc, dataSet);
				page ++;
			} else {
				break;
			}			
		}  

		double [][] dataArray = new double [dataSet.size()][]; 
		dataArray = dataSet.toArray(dataArray);
	    
		return dataArray;
    }
	
	private void process(Document doc, List <double []> dataSet) {
        Elements rows = doc.select("[class=d-md-none d-block]");
        for (Element row : rows) {
        	String dateElement = row.select("h5:eq(1)").toString();
            String dateString = parseDate(dateElement); 
			if ("error".equals(dateString)) continue;
			
            Date date;
			try {
				date = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
			} catch (ParseException e) {
				continue;
			}
			
            String price = parsePrice(row.select("h5:eq(3)").toString());             
            if (price.equals("error")) continue;
            
            LOG.debug(date + " " + price);
            
            dataSet.add(new double [] { Double.valueOf(date.getTime()), Double.valueOf(price) });
        }
	}
	
    private String processPostCode(String postCode) {
    	int postCodeAsInt = Integer.valueOf(postCode).intValue();
    	if ( postCodeAsInt >= 1050 && postCodeAsInt <= 1549 ) {
    		return "1050-1549";
    	} else if (postCodeAsInt >= 1550 && postCodeAsInt <= 1799 ) {
    		return "1550-1799";
    	} else if (postCodeAsInt >= 1800 && postCodeAsInt <= 1999 ) {
    		return "1800-1999";
    	} else {
    		return postCode;    		
    	}		
	}

	private boolean rowsExist(Document doc) {
    	return !(doc.select("[class=d-md-none d-block]").isEmpty());
	}
		
	private String parsePrice(String string) {
    	String [] tokens = string.split(" k");
		String price = tokens[0].substring(17);
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);	
 		try {
			price = nf.parse(price).toString();
		} catch (Exception e) {
			price = "error";
		}
		return price;
		
	}
	
	private String parseDate(String string) {
    	if (!string.contains("Alm.")) {
    		return "error";
    	}
		String [] tokens = string.split(",");
		String date = tokens[0].substring(4);
		return date;		
	}
}

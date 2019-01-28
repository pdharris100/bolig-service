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

import dk.bolig.math.PolyTrendLine;
import dk.bolig.math.TrendLine;

/**
 * Hello world!
 *
 */
public class PriceCalculator {
	
	public static final String BASE_URL = "https://www.boliga.dk/salg/resultater?so=1&sort=omregnings_dato-a&maxsaledate=today";
	public static final String POST_CODE_URL = "iPostnr";
	public static final String STREET_URL = "gade";
	public static final String TYPE_URL = "type";
	public static final String ORIGINAL_STREET_URL = "origgade";
	public static final String AND = "&";
	public static final String EQUALS_URL = "=";
	public static void main(String[] args) throws IOException {
		String postCode = processPostCode(args[0]);
		String street = args[1];
		String type = args[2];
		
        String url = BASE_URL +
				AND + POST_CODE_URL + EQUALS_URL + postCode +
				AND + STREET_URL + EQUALS_URL + street +
				AND + TYPE_URL + EQUALS_URL + type +
				AND + "minsaledate=2009&origgade=" + street;
		
        int page = 1; 
        List <Double> xDataSet = new ArrayList <Double>();
        List <Double> yDataSet = new ArrayList <Double>();
		while (true) {
			Document doc = Jsoup.connect(url + "&p=" + page).get();
			if (rowsExist(doc)) {
				process(doc, xDataSet, yDataSet);
				page ++;
			} else {
				break;
			}			
		}  
		
		TrendLine t = new PolyTrendLine(2);
	    double[] x = new double [xDataSet.size()];
	    double[] y = new double [yDataSet.size()];
	    for (int i=0; i<xDataSet.size(); i++) { x [i] = xDataSet.get(i); }
	    for (int i=0; i<yDataSet.size(); i++) { y [i] = yDataSet.get(i); }
	    t.setValues(y,x);
	    System.out.println(x.length + " sales");
	    System.out.println(t.predict(Double.valueOf((new GregorianCalendar()).getTime().getTime()))); 
    }
	
	private static void process(Document doc, List <Double> xDataSet, List <Double> yDataSet) {
        Elements rows = doc.select("[class=d-md-none d-block]");
        for (Element row : rows) {
            String dateString = parseDate(row.select("h5:eq(1)").toString()); 
            Date date;
			try {
				date = new SimpleDateFormat("MM-dd-yyyy").parse(dateString);
			} catch (ParseException e) {
				continue;
			}
			if ("family".equals(date)) continue;
			
            String price = parsePrice(row.select("h5:eq(3)").toString());
            if (price.equals("error")) continue;
            
            xDataSet.add(Double.valueOf(date.getTime()));
            yDataSet.add(Double.valueOf(price));
        }
	}
	
    private static String processPostCode(String postCode) {
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

	private static boolean rowsExist(Document doc) {
    	return !(doc.select("[class=d-md-none d-block]").isEmpty());
	}
		
	private static String parsePrice(String string) {
    	String [] tokens = string.split(" k");
		String price = tokens[0].substring(17);
		try {
			Double.valueOf(price);
		} catch (NumberFormatException e) {
			price = "error";
		}
		return price;
		
	}
	private static String parseDate(String string) {
    	if (string.contains("Fam.")) {
    		return "family";
    	}
		String [] tokens = string.split(",");
		String date = tokens[0].substring(4);
		return date;		
	}
    
	private static void log(String msg, String... vals) {
        System.out.println(msg);
    }
}

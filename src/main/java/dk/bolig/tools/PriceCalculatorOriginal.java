package dk.bolig.tools;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
public class PriceCalculatorOriginal {
	
	public static final String BASE_URL = "https://www.boliga.dk/salg/resultater?so=1&sort=omregnings_dato-a&maxsaledate=today";
	public static final String POST_CODE_URL = "iPostnr";
	public static final String STREET_URL = "gade";
	public static final String TYPE_URL = "type";
	public static final String ORIGINAL_STREET_URL = "origgade";
	public static final String AND = "&";
	public static final String EQUALS_URL = "=";
	public static void main(String[] args) throws IOException {
		String url = "https://www.boliga.dk/salg/resultater?so=1&sort=omregnings_dato-d&maxsaledate=today&iPostnr=1050-1549&gade=Amaliegade&type=Ejerlejlighed&minsaledate=2009";
		int page = 1;        
		while (true) {
			Document doc = Jsoup.connect(url + "&p=" + page).get();
			if (rowsExist(doc)) {
				process(doc);
				page ++;
			} else {
				break;
			}			
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

	private static void process(Document doc) {
    	log(doc.title());

        Elements rows = doc.select("[class=d-md-none d-block]");
        for (Element row : rows) {
            String date = parseDate(row.select("h5:eq(1)").toString());           
            String price = parsePrice(row.select("h5:eq(3)").toString());
            if ("family".equals(date)) continue;
            log(date + "," + price);
        }
		
	}
	private static String parsePrice(String string) {
    	String [] tokens = string.split(" k");
		String price = tokens[0].substring(17);
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

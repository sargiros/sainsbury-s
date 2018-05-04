import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


public class JsoupTester {

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    public static void main(String[] args) throws IOException {


        Document doc = Jsoup.connect("https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html").get();

        Elements berriesInfo = doc.select("div#productLister");
        for (Element element : berriesInfo.select("div.productNameAndPromotions h3")) {
            /*selects only the URL of the products*/
            String berriesUrl = element.select("a").attr("href");
            String alteredBerriesUrl = berriesUrl.replace("../", "");
            List<String> products = new ArrayList<>();
            products.add(alteredBerriesUrl);



            for (String product : products) {
                Document data = Jsoup.connect("https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/" + alteredBerriesUrl).userAgent(USER_AGENT).get();
                Elements relativeInfo = data.select("div#main");
                final List <SearchResult> resultList = new ArrayList<>();
                for (Element newIfo : relativeInfo.select("div.productContent")) {
                    /* Getting the product Description*/
                    String desc = String.valueOf(newIfo.select("div#information .productText p:first-of-type").first());
                    String newDesc = desc.replace("<p>","" );
                    String newDesc2 = newDesc.replace("</p>","" );
                    String description = newDesc2.trim();

                    /*get the title of the product*/
                    String title = String.valueOf(newIfo.select(".productTitleDescriptionContainer").text());


                    String price_per_unit= String.valueOf(newIfo.select("p.pricePerUnit").text());
                    String unit_price_no_symbol= price_per_unit.replace("£", "");
                    String unit_price= unit_price_no_symbol.replace("/unit", "");
                    /*get the product kcals per 100gr*/
                    String kcal_per_100g1 = newIfo.select(".nutritionLevel1:first-child").text();
                    String kcal_per_100g2 = newIfo.select(".tableRow0:first-child").text();
                    String kcal_per_100g = kcal_per_100g1 + kcal_per_100g2;
                    System.out.println(title + kcal_per_100g + unit_price + description);
                    /*for(Element counter : relativeInfo) {
                        double total = Double.valueOf(unit_price);
                        total = total + total;
                        System.out.println(total);
                    }*/
                    /*adding the information to the List*/
                    resultList.add(new SearchResult(title, kcal_per_100g, unit_price, description));
                }

                OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File("results.json"), resultList);
            }
        }

    }
}
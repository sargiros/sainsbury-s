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

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static Object resultList;

    public static void main(String[] args) throws IOException {


        Document doc = Jsoup.connect("https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html").get();

        Elements berriesInfo = doc.select("div#productLister");
        for (Element element : berriesInfo.select("div.productNameAndPromotions h3")) {
            /*selects only the URL of the products*/
            String berriesUrl = element.select("a").attr("href");
            String alteredBerriesUrl = berriesUrl.replace("../", "");
            List<String> products = new ArrayList<>();
            products.add(alteredBerriesUrl);
            /*get the title of the product*/
            String title = element.text();
            System.out.println(title);
            for (String product : products) {
                Document data = Jsoup.connect("https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/" + alteredBerriesUrl).get();
                final List<SearchResult> resultList = new ArrayList<>();
                Elements relativeInfo = data.select("div.productContent");
            for (Element newIfo : relativeInfo.select("div#information")) {
                    String description = newIfo.select("productText:nth-of-type(1)").text();
                    System.out.println(description);
                    String unit_price = newIfo.select("pricePerUnit").text();
                    /*get the product kcals per 100gr*/
                    String kcal_per_100g1 = newIfo.select(".nutritionLevel1:first-child").text();
                    String kcal_per_100g2 = newIfo.select(".tableRow0:first-child").text();
                    String kcal_per_100g = kcal_per_100g1 + kcal_per_100g2;
                    System.out.println(kcal_per_100g);
                    resultList.add(new SearchResult(title, kcal_per_100g));
                }

            }

        }
        OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File("results.json"), resultList);
    }
}
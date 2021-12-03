package aoc2021.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Samson
 */
public class HttpUtils {
    public static List<String> getLines(String url, Map<String, String> cookies) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla")
                    .cookies(cookies)
                    .get();
            return Arrays.asList(doc.body().text().split(" "));
        } catch (IOException ioe) {
            return Collections.emptyList();
        }
    }
}

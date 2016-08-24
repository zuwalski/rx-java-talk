package app.gateway;

import app.util.ObservableRestClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@RestController
public class DetailsGateway {
    private final Logger log = LoggerFactory.getLogger(DetailsGateway.class);

    private final ObservableRestClient rest = new ObservableRestClient();

    @RequestMapping(path = "/getDetails")
    public JsonNode details(@RequestParam String partId) {

        return Observable.zip(
                rest.get("stock?partId=" + partId, JsonNode.class),

                rest.get("part?partId=" + partId, JsonNode.class),

                Observable.merge(
                        partReviews(), partTipsAndTricks(), partOnTwitter()
                )
                        .take(200, TimeUnit.MILLISECONDS)
                        .buffer(10)
                        .onErrorResumeNext(Observable.empty())
                        .firstOrDefault(Collections.emptyList()),

                (stock, description, reviews) -> {
                    ObjectNode combined = JsonNodeFactory.instance.objectNode();

                    combined.replace("stock", stock);
                    combined.replace("part", description);
                    combined.putArray("items").addAll(reviews);

                    combined.put("available", stock.get("inStock").asInt() > 0);
                    combined.put("price", String.format("%.1f0", stock.get("unitPrice").asDouble()));

                    return combined;
                }
        ).toBlocking().first();
    }

    public Observable<JsonNode> partReviews() {
        return Observable.interval(5, TimeUnit.MILLISECONDS)
                .map(i -> format("partReviews " + i, "review, review, review"));
    }

    public Observable<JsonNode> partTipsAndTricks() {
        return Observable.interval(5, TimeUnit.MILLISECONDS)
                .map(i -> format("partTipsAndTricks " + i, "tip, trick, tip, trick"));
    }

    public Observable<JsonNode> partOnTwitter() {
        return Observable.interval(5, TimeUnit.MILLISECONDS)
                .map(i -> format("twitter " + i, "twit, twit, twit.."));
    }

    private static JsonNode format(String header, String details) {
        ObjectNode txt = JsonNodeFactory.instance.objectNode();
        txt.put("header", header);
        txt.put("details", details);
        return txt;
    }
}

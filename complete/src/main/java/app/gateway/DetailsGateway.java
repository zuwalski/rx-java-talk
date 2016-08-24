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

@RestController
public class DetailsGateway {
    private final Logger log = LoggerFactory.getLogger(DetailsGateway.class);

    private final ObservableRestClient rest = new ObservableRestClient();

    @RequestMapping(path = "/getDetails")
    public JsonNode details(@RequestParam String partId) {

        return Observable.zip(
                rest.get("stock?partId=" + partId, JsonNode.class),

                rest.get("part?partId=" + partId, JsonNode.class),

                (stock, description) -> {
                    ObjectNode combined = JsonNodeFactory.instance.objectNode();

                    combined.replace("stock", stock);
                    combined.replace("part", description);

                    combined.put("available", stock.get("inStock").asInt() > 0);
                    combined.put("price", String.format("%.1f0", stock.get("unitPrice").asDouble()));

                    return combined;
                }
        ).toBlocking().first();
    }
}

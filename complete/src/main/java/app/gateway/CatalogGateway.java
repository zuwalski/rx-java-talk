package app.gateway;

import app.util.ObservableRestClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;

import java.util.concurrent.TimeUnit;

@RestController
public class CatalogGateway {
    private final Logger log = LoggerFactory.getLogger(CatalogGateway.class);

    private final ObservableRestClient rest = new ObservableRestClient();

    @RequestMapping(path = "/getCatalog", method = RequestMethod.POST)
    public JsonNode catalog(@RequestParam String regNr) {

        Observable<JsonNode> catalog = Observable.amb(
                rest.post("dmr?regNr=" + regNr, String.class),

                rest.post("dmrCache?regNr=" + regNr, String.class)
        )
                .timeout(2, TimeUnit.SECONDS)
                .onErrorReturn(e -> "UNKNOWN")

                .flatMap(
                        type -> rest.get("catalog?type=" + type, JsonNode.class)
                );

        return catalog.toBlocking().first();
    }
}

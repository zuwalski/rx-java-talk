package app.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

@RestController
public class Gateway {
    private final Logger log = LoggerFactory.getLogger(Gateway.class);

    private final RestTemplate rest = new RestTemplate();

    @RequestMapping(path = "/getCatalog", method = RequestMethod.POST)
    public JsonNode catalog(@RequestParam String regNr) {

        Observable<String> dmr = Observable.fromCallable(() ->
                rest.postForObject("http://localhost:8080/dmr?regNr=" + regNr, null, String.class)
        )
                .doOnNext(log::info)
                .timeout(1, TimeUnit.SECONDS)
                .doOnError(e -> log.warn("error " + e.getClass().getSimpleName()))
                .onErrorReturn(e -> "");

        Observable<JsonNode> catalog = dmr.map(
                type -> rest.getForObject("http://localhost:8080/catalog?type=" + type, JsonNode.class)
        )
                //.subscribeOn(Schedulers.io())
        ;

        return catalog.toBlocking().first();
    }
}

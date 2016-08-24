package app.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Gateway {
    private final RestTemplate rest = new RestTemplate();

    @RequestMapping(path = "/getCatalog", method = RequestMethod.POST)
    public JsonNode catalog(@RequestParam String regNr) {
        String type = rest.postForObject("http://localhost:8080/dmr?regNr=" + regNr, null, String.class);

        JsonNode node = rest.getForObject("http://localhost:8080/catalog?type=" + type, JsonNode.class);

        return node;
    }
}

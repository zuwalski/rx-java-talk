package app.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class SparePartCatalog {

    public static class CatalogItem {
        public String text;
        public String partId;
        public List<CatalogItem> nodes;

        public CatalogItem(String text, String partId) {
            this.text = text;
            this.partId = partId;
        }

        public CatalogItem() {
        }

        public CatalogItem add(String text, String partId) {
            if (nodes == null) {
                nodes = new ArrayList<>();
            }
            nodes.add(new CatalogItem(text, partId));
            return this;
        }
    }

    static CatalogItem cat(String text, CatalogItem... subs) {
        CatalogItem it = new CatalogItem(text, null);
        if (subs.length > 0)
            it.nodes = Arrays.asList(subs);
        return it;
    }

    @RequestMapping(path = "/catalog")
    public List<CatalogItem> catalog(@RequestParam String type) {
        return cat("root",
                cat("Aircondition",
                        cat("Aircon pumper").add("pumpe til " + type, "p1")
                ),
                cat("Anhængertræk",
                        cat("Krog").add("Stor krog til " + type, "k1").add("lille krog til " + type, "k2")
                ),
                cat("Bremser",
                        cat("Bremse for").add("Bremseskive " + type, "b1").add("Luksusskive " + type, "b2"),
                        cat("Bremse bag")
                ),
                cat("Filtre",
                        cat("Luftfilter")),
                cat("Elektriske dele",
                        cat("Lygter")),
                cat("Motor",
                        cat("Topstykke")),
                cat("Plader",
                        cat("SKærme"))
        ).nodes;
    }
}

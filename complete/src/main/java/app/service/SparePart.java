package app.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SparePart {

    public static class Part {
        public String partId;
        public String name;
        public String description;

        public Part(String partId, String name, String description) {
            this.partId = partId;
            this.name = name;
            this.description = description;
        }

        public Part() {
        }
    }

    @RequestMapping("/part")
    public Part part(@RequestParam String partId) {
        return new Part(partId, "name of " + partId, "description of " + partId);
    }
}

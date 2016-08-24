package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DMRCache {
    private final Logger log = LoggerFactory.getLogger(DMRCache.class);

    @RequestMapping(path = "/dmrCache", method = RequestMethod.POST)
    public String dmr(@RequestParam String regNr) {
        log.info("return from cache");
        return regNr + " FROM CACHE";
    }
}

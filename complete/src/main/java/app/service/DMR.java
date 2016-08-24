package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class DMR {
    private final Logger log = LoggerFactory.getLogger(DMR.class);

    Random rnd = new Random();

    @RequestMapping(path = "/dmr", method = RequestMethod.POST)
    public String dmr(@RequestParam String regNr) throws InterruptedException {
        Thread.sleep(rnd.nextInt(5000) + 5000);
        log.info("return " + regNr);
        return regNr;
    }
}

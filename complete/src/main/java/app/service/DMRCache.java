package app.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DMRCache {

    @RequestMapping(path = "/dmrCache", method = RequestMethod.POST)
    public String dmr(@RequestParam String regNr) {
        return regNr;
    }
}

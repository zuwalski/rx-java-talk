package app.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class SparePartStock {

    public static class Stock {
        public String partId;
        public Integer inStock;
        public Double unitPrice;

        public Stock(String partId, Integer inStock, Double unitPrice) {
            this.partId = partId;
            this.inStock = inStock;
            this.unitPrice = unitPrice;
        }

        public Stock() {
        }
    }

    Random rnd = new Random();

    @RequestMapping("/stock")
    public Stock stock(@RequestParam String partId) {
        return new Stock(partId, Math.max(0, rnd.nextInt(10) - 4), rnd.nextDouble() * 10000);
    }
}

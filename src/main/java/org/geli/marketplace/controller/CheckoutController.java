package org.geli.marketplace.controller;

import org.geli.marketplace.service.CheckoutService;
import org.geli.marketplace.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("checkout/api/")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping("sell")
    public ResponseEntity<ResponseUtil> sellVariant(@RequestParam Long variantId, @RequestParam int quantity) {
        return checkoutService.sellVariant(variantId, quantity);
    }
}

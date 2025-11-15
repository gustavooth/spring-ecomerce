package me.gustavoo.ecomerce.controllers;

import me.gustavoo.ecomerce.db.repository.ProductPageRepository;
import me.gustavoo.ecomerce.db.repository.ProductRepository;
import me.gustavoo.ecomerce.services.UserSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class IndexController {
    UserSession userSession;
    ProductPageRepository productPageRepository;
    ProductRepository productRepository;

    IndexController(UserSession userSession, ProductPageRepository productPageRepository, ProductRepository productRepository) {
        this.userSession = userSession;
        this.productPageRepository = productPageRepository;
        this.productRepository = productRepository;
    }
}

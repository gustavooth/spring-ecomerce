package me.gustavoo.ecomerce.controllers;

import jakarta.transaction.Transactional;
import jakarta.websocket.server.PathParam;
import me.gustavoo.ecomerce.Api;
import me.gustavoo.ecomerce.controllers.responseModels.ApiResponse;
import me.gustavoo.ecomerce.controllers.responseModels.PublicProductPageResponse;
import me.gustavoo.ecomerce.db.repository.ProductPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class PageController {
    @Autowired
    ProductPageRepository productPageRepository;

    @Transactional
    @GetMapping("/p/{slug}")
    public ApiResponse handleProduct(@PathVariable(name = "slug")String slug) {
        var resp = productPageRepository.findBySlug(slug);
        return resp.map(productPageModel -> ApiResponse.successResponse(
                new PublicProductPageResponse(productPageModel))
        ).orElseGet(() -> ApiResponse.errorResponse("pagina nao encontrada!"));
    }
}

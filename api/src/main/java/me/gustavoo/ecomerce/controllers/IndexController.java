package me.gustavoo.ecomerce.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import me.gustavoo.ecomerce.controllers.responseModels.ApiResponse;
import me.gustavoo.ecomerce.controllers.responseModels.ListResponse;
import me.gustavoo.ecomerce.controllers.responseModels.PublicProductPageResponse;
import me.gustavoo.ecomerce.db.models.ProductPageModel;
import me.gustavoo.ecomerce.db.repository.ProductPageRepository;
import me.gustavoo.ecomerce.db.repository.ProductRepository;
import me.gustavoo.ecomerce.services.UserSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping
    public ApiResponse handleIndex(@NonNull HttpServletRequest request) {
        List<ProductPageModel> pages = productPageRepository.findAll();
        List<PublicProductPageResponse> pages_resp = new ArrayList<>(pages.size());
        for (ProductPageModel page: pages) {
            pages_resp.add(new PublicProductPageResponse(page));
        }
        return ApiResponse.successResponse(new ListResponse<PublicProductPageResponse>(pages_resp));
    }
}

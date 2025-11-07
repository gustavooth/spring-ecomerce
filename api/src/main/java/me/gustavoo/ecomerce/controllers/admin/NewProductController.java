package me.gustavoo.ecomerce.controllers.admin;

import me.gustavoo.ecomerce.controllers.RequestBody.AdminNewProduct;
import me.gustavoo.ecomerce.controllers.models.ApiResponse;
import me.gustavoo.ecomerce.controllers.models.NewProductResponse;
import me.gustavoo.ecomerce.db.models.ProductModel;
import me.gustavoo.ecomerce.db.models.ProductPageModel;
import me.gustavoo.ecomerce.db.repository.ProductPageRepository;
import me.gustavoo.ecomerce.db.repository.ProductRepository;
import me.gustavoo.ecomerce.services.UserSession;
import me.gustavoo.ecomerce.services.models.SessionInternal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/admin/new_product")
public class NewProductController {
    UserSession userSession;
    ProductPageRepository productPageRepository;
    ProductRepository productRepository;

    public NewProductController(UserSession userSession, ProductPageRepository productPageRepository, ProductRepository productRepository) {
        this.userSession = userSession;
        this.productPageRepository = productPageRepository;
        this.productRepository = productRepository;
    }

    public ApiResponse handleNewProduct(@RequestBody AdminNewProduct body) {
        SessionInternal session = userSession.handleSession(body.token);
        if (session == null) {
            return ApiResponse.errorResponse("");
        }
        if (session.logged == false) {
            return ApiResponse.errorResponse("");
        }
        if (session.userInternal.role.equals("admin")) {
            return ApiResponse.errorResponse("");
        }

        try {
            var result = productPageRepository.findBySlug(body.slug);
            if (result != null) {
                return ApiResponse.successResponse(new NewProductResponse("invalid slug"));
            }
        } catch (Exception e) {
            return ApiResponse.successResponse(new NewProductResponse(String.format("invalid slug|%s", e.getMessage())));
        }

        String imagePath = "";
        for (int i = 0; i < body.imagePath.length; i++) {
            imagePath = String.format("%s|%s", imagePath, body.imagePath[i]);
        }

        ProductPageModel newPage = new ProductPageModel(body.title, body.slug, imagePath, body.shortDescription, body.description);

        var products = new ArrayList<ProductModel>(body.productInfo.length);
        for (int i = 0; i < body.productInfo.length; i++) {
            var newProduct = new ProductModel(newPage.id, body.productInfo[i].shortImagePath, body.productInfo[i].variationTypes);
            products.add(newProduct);
        }

        productPageRepository.save(newPage);
        for (int i = 0; i < products.size(); i++) {
            productRepository.save(products.get(i));
        }

        return ApiResponse.successResponse(new NewProductResponse(newPage.slug));
    }
}

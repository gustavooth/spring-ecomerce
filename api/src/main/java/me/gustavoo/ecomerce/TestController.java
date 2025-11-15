package me.gustavoo.ecomerce;

import jakarta.servlet.http.HttpServletRequest;
import me.gustavoo.ecomerce.controllers.requestModels.SelectRequest;
import me.gustavoo.ecomerce.controllers.responseModels.ApiResponse;
import me.gustavoo.ecomerce.db.repository.ProductPageRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/test")
public class TestController {
    ProductPageRepository productPageRepository;

    public TestController(ProductPageRepository productPageRepository) {
        this.productPageRepository = productPageRepository;
    }

    @GetMapping
    public ApiResponse handleTest(HttpServletRequest request, @RequestBody SelectRequest body) {
        var resp = productPageRepository.findById(body.getId());
        if (resp.isPresent()) {
            return ApiResponse.successResponse(resp.get());
        }

        return ApiResponse.errorResponse("nao encontrado");
    }
}

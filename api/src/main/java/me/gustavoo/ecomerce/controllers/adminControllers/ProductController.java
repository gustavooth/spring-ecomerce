package me.gustavoo.ecomerce.controllers.adminControllers;

import jakarta.servlet.http.HttpServletRequest;
import me.gustavoo.ecomerce.Api;
import me.gustavoo.ecomerce.controllers.requestModels.*;
import me.gustavoo.ecomerce.controllers.responseModels.ApiResponse;
import me.gustavoo.ecomerce.controllers.responseModels.GeneralResponse;
import me.gustavoo.ecomerce.controllers.responseModels.ListResponse;
import me.gustavoo.ecomerce.controllers.responseModels.ProductPageResponse;
import me.gustavoo.ecomerce.db.models.*;
import me.gustavoo.ecomerce.db.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class ProductController {
    @Autowired
    ProductPageRepository productPageRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductValueRepository productValueRepository;
    @Autowired
    AttributeRepository attributeRepository;
    @Autowired
    AttributeValuesRepository attributeValuesRepository;
    @Autowired
    PageImageRepository pageImageRepository;

    boolean checkSlug(String slug) {
        var resp = productPageRepository.findBySlug(slug);
        return resp.getProducts().isEmpty();
    }

    @PostMapping("/new-page")
    public ApiResponse handleNewPage(@NonNull HttpServletRequest request , @RequestBody NewProductPageRequest body) {
        ProductPageModel productPageModel = new ProductPageModel(body.getTitle(), body.getSlug(), body.getShortDescription(), body.getDescription());

        try {
            productPageRepository.save(productPageModel);
            return ApiResponse.successResponse(new GeneralResponse(productPageModel.getId()));
        } catch (Exception e) {
            return ApiResponse.errorResponse("exception: "+e.toString());
        }
    }

    @PostMapping("/new-product")
    public ApiResponse handleNewProduct(@NonNull HttpServletRequest request , @RequestBody NewProductRequest body) {
        try {
            Double price = Double.parseDouble(body.getPrice());
            var page_resp = productPageRepository.findById(body.getPageId());
            if (page_resp.isPresent()) {
                ProductModel model = new ProductModel(page_resp.get(), price, body.getImagePath());
                productRepository.save(model);
                return ApiResponse.successResponse(new GeneralResponse(model.getId()));
            }else {
                return ApiResponse.errorResponse("pagina nao encontrada!");
            }
        }catch (Exception e) {
            return ApiResponse.errorResponse("exception: "+e.toString());
        }
    }

    @PostMapping("/new-attribute")
    public ApiResponse handleNewAttribute(@NonNull HttpServletRequest request , @RequestBody NewAttributeRequest body) {
        try {
            var page_resp = productPageRepository.findById(body.getPageId());
            if (page_resp.isPresent()) {
                AttributeModel model = new AttributeModel(body.getName(), page_resp.get());
                attributeRepository.save(model);
                return ApiResponse.successResponse(new GeneralResponse(model.getId()));
            }else {
                return ApiResponse.errorResponse("pagina nao encontrada!");
            }
        }catch (Exception e) {
            return ApiResponse.errorResponse("exception: "+e.toString());
        }
    }

    @PostMapping("/new-attribute-value")
    public ApiResponse handleNewAttributeValue(@NonNull HttpServletRequest request , @RequestBody NewAttributeValueRequest body) {
        try {
            var attribute_resp = attributeRepository.findById(body.getAttributeId());
            if (attribute_resp.isPresent()) {
                AttributeValueModel model = new AttributeValueModel(attribute_resp.get(), body.getValue());
                attributeValuesRepository.save(model);
                return ApiResponse.successResponse(new GeneralResponse(model.getId()));
            }else {
                return ApiResponse.errorResponse("atributo nao encontrado!");
            }
        }catch (Exception e) {
            return ApiResponse.errorResponse("exception: "+e.toString());
        }
    }

    @PostMapping("/new-product-value")
    public ApiResponse handleNewProductValue(@NonNull HttpServletRequest request , @RequestBody NewProductValueRequest body) {
        try {
            var attribute_value_resp = attributeValuesRepository.findById(body.getAttributeValueId());
            if (attribute_value_resp.isPresent()) {
                var product_resp = productRepository.findById(body.getProductId());
                if (product_resp.isPresent()) {
                    ProductValueModel model = new ProductValueModel(attribute_value_resp.get(), product_resp.get());
                    productValueRepository.save(model);
                    return ApiResponse.successResponse(new GeneralResponse(model.getId()));
                }else {
                    return ApiResponse.errorResponse("produto nao encontrado!");
                }
            }else {
                return ApiResponse.errorResponse("valor de atributo nao encontrado!");
            }
        }catch (Exception e) {
            return ApiResponse.errorResponse("exception: "+e.toString());
        }
    }

    @PostMapping("/new-page-image")
    public ApiResponse handleNewPageImage(@NonNull HttpServletRequest request , @RequestBody NewPageImageRequest body) {
        try {
            var page_resp = productPageRepository.findById(body.getPageId());
            if (page_resp.isPresent()) {
                PageImageModel model = new PageImageModel(page_resp.get(), body.getPath(), body.getIndex());
                pageImageRepository.save(model);
                return ApiResponse.successResponse(new GeneralResponse(model.getId()));
            }else {
                return ApiResponse.errorResponse("pagina nao encontrada!");
            }
        }catch (Exception e) {
            return ApiResponse.errorResponse("exception: "+e.toString());
        }
    }

    @PostMapping("/remove-page")
    public ApiResponse handleRemovePage(@NonNull HttpServletRequest request , @RequestBody SelectRequest body) {
        try {
            var resp = productPageRepository.findById(body.getId());
            if (resp.isPresent()) {
                productPageRepository.delete(resp.get());
                return ApiResponse.successResponse(new GeneralResponse(body.getId()));
            }else {
                return ApiResponse.errorResponse("pagina nao encontrada!");
            }
        } catch (Exception e) {
            return ApiResponse.errorResponse("exception: "+e.toString());
        }
    }

    @PostMapping("/remove-product")
    public ApiResponse handleRemoveProduct(@NonNull HttpServletRequest request , @RequestBody SelectRequest body) {
        try {
            var resp = productRepository.findById(body.getId());
            if (resp.isPresent()) {
                productRepository.delete(resp.get());
                return ApiResponse.successResponse(new GeneralResponse(body.getId()));
            }else {
                return ApiResponse.errorResponse("produto nao encontrado!");
            }
        } catch (Exception e) {
            return ApiResponse.errorResponse("exception: "+e.toString());
        }
    }

    @PostMapping("/remove-attribute")
    public ApiResponse handleRemoveAttribute(@NonNull HttpServletRequest request , @RequestBody SelectRequest body) {
        try {
            var resp = attributeRepository.findById(body.getId());
            if (resp.isPresent()) {
                attributeRepository.delete(resp.get());
                return ApiResponse.successResponse(new GeneralResponse(body.getId()));
            }else {
                return ApiResponse.errorResponse("atributo nao encontrado!");
            }
        } catch (Exception e) {
            return ApiResponse.errorResponse("exception: "+e.toString());
        }
    }

    @PostMapping("/remove-attribute-value")
    public ApiResponse handleRemoveAttributeValue(@NonNull HttpServletRequest request , @RequestBody SelectRequest body) {
        try {
            var resp = attributeValuesRepository.findById(body.getId());
            if (resp.isPresent()) {
                attributeValuesRepository.delete(resp.get());
                return ApiResponse.successResponse(new GeneralResponse(body.getId()));
            }else {
                return ApiResponse.errorResponse("valor de atributo nao encontrado!");
            }
        } catch (Exception e) {
            return ApiResponse.errorResponse("exception: "+e.toString());
        }
    }

    @PostMapping("/remove-product-value")
    public ApiResponse handleNewProductValue(@NonNull HttpServletRequest request , @RequestBody SelectRequest body) {
        try {
            var resp = productValueRepository.findById(body.getId());
            if (resp.isPresent()) {
                productValueRepository.delete(resp.get());
                return ApiResponse.successResponse(new GeneralResponse(body.getId()));
            }else {
                return ApiResponse.errorResponse("valor de produto nao encontrado!");
            }
        } catch (Exception e) {
            return ApiResponse.errorResponse("exception: "+e.toString());
        }
    }

    @PostMapping("/remove-page-image")
    public ApiResponse handleNewPageImage(@NonNull HttpServletRequest request , @RequestBody SelectRequest body) {
        try {
            var resp = pageImageRepository.findById(body.getId());
            if (resp.isPresent()) {
                pageImageRepository.delete(resp.get());
                return ApiResponse.successResponse(new GeneralResponse(body.getId()));
            }else {
                return ApiResponse.errorResponse("imagem nao encontrada!");
            }
        } catch (Exception e) {
            return ApiResponse.errorResponse("exception: "+e.toString());
        }
    }

    @PostMapping("/select-page")
    public ApiResponse handleSelectPage(@NonNull HttpServletRequest request, @RequestBody SelectRequest body) {
        try {
            var page = productPageRepository.findById(body.getId());
            if (page.isPresent()) {
                return ApiResponse.successResponse(new ProductPageResponse(page.get()));
            }else {
                return ApiResponse.errorResponse("pagina nao encontrada!");
            }
        } catch (Exception e) {
            return ApiResponse.errorResponse("exception: "+e.toString());
        }
    }

    @PostMapping("/select-pages")
    public ApiResponse handleSelectPages(@NonNull HttpServletRequest request) {
        try {
            List<ProductPageModel> pages = productPageRepository.findAll();
            List<ProductPageResponse> response = new ArrayList<>(pages.size());

            for (ProductPageModel model : pages) {
                response.add(new ProductPageResponse(model));
            }

            return ApiResponse.successResponse(new ListResponse<ProductPageResponse>(response));
        } catch (Exception e) {
            return ApiResponse.errorResponse("exception: "+e.toString());
        }
    }
}

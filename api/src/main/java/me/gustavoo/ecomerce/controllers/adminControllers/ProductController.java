package me.gustavoo.ecomerce.controllers.adminControllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
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

    @PostMapping("/new-page")
    public ApiResponse handleNewPage(@NonNull HttpServletRequest request , @RequestBody NewProductPageRequest body) {
        ProductPageModel productPageModel = new ProductPageModel(body.getTitle(), body.getSlug(), body.getShortDescription(), body.getDescription(), true);
        try {
            productPageRepository.save(productPageModel);
        } catch (Exception e) {
            return ApiResponse.errorResponse("exception: "+e.toString());
        }
        var images = body.getImages();
        for (NewPageImageRequest image : images) {
            PageImageModel pageImageModel = new PageImageModel(productPageModel, image.getPath(), image.getIndex());

            try {
                pageImageRepository.save(pageImageModel);
            } catch (Exception e) {
                return ApiResponse.errorResponse("exception: " + e.toString());
            }
        }

        var attributes = body.getAttributes();
        List<AttributeValueModel> values = new ArrayList<>(10);
        for (NewAttributeRequest attribute : attributes) {
            AttributeModel attributeModel = new AttributeModel(attribute.getName(), attribute.isShowImage(), productPageModel, true);

            try {
                attributeRepository.save(attributeModel);
            } catch (Exception e) {
                return ApiResponse.errorResponse("exception: " + e.toString());
            }

            for (NewAttributeValueRequest value : attribute.getValues()) {
                AttributeValueModel valueModel = new AttributeValueModel(value.getValue(), attributeModel, true);

                try {
                    attributeValuesRepository.save(valueModel);
                } catch (Exception e) {
                    return ApiResponse.errorResponse("exception: " + e.toString());
                }

                values.add(valueModel);
            }
        }

        var products = body.getProducts();
        for (NewProductRequest product : products) {
            var price = Double.valueOf(product.getPrice());
            var stock = Integer.valueOf(product.getStock());
            ProductModel productModel = new ProductModel(productPageModel, price, stock, product.getImagePath(), true);

            try {
                productRepository.save(productModel);
            } catch (Exception e) {
                return ApiResponse.errorResponse("exception: " + e.toString());
            }

            for (String productValue : product.getValues()) {
                AttributeValueModel attributeValueModel = null;
                for (int i = 0; i < values.size(); i++) {
                    if (values.get(i).getValue().equals(productValue)) {
                        attributeValueModel = values.get(i);
                    }
                }
                if (attributeValueModel == null) {
                    return ApiResponse.errorResponse("falha ao linkar valor de atributo ao produto");
                }

                ProductValueModel productValueModel = new ProductValueModel(attributeValueModel, productModel, true);
                try {
                    productValueRepository.save(productValueModel);
                } catch (Exception e) {
                    return ApiResponse.errorResponse("exception: " + e.toString());
                }
            }
        }

        return ApiResponse.successResponse(new GeneralResponse(productPageModel.getId()));
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

    @Transactional
    @PostMapping("update-page")
    public ApiResponse handleUpdatePage(@NonNull HttpServletRequest request , @RequestBody UpdateProductPageRequest body) {
        var page = productPageRepository.findById(body.getId());
        if (!page.isPresent()) {
            return ApiResponse.errorResponse("pagina nao encontrada!");
        }

        ProductPageModel data = page.get();
        data.setTitle(body.getTitle());
        data.setSlug(body.getSlug());
        data.setShortDescription(body.getShortDescription());
        data.setDescription(body.getDescription());

        var newImages = body.getImages();
        if (!newImages.isEmpty()) {
            var images = pageImageRepository.findAllByPageId(body.getId());
            for (PageImageModel image: images) {
                pageImageRepository.delete(image);
            }
        }
        for (int i = 0; i < newImages.size(); i++) {
            NewPageImageRequest newImage = newImages.get(i);
            PageImageModel imageModel = new PageImageModel(data, newImage.getPath(), newImage.getIndex());
            pageImageRepository.save(imageModel);
        }

        var newAttributes = body.getAttributes();
        for (NewAttributeRequest newAttribute : newAttributes) {
            AttributeModel attributeModel = new AttributeModel(newAttribute.getName(), newAttribute.isShowImage(), data, true);

            var newValues = newAttribute.getValues();

            for (NewAttributeValueRequest newValue: newValues) {
                AttributeValueModel attributeValueModel = new AttributeValueModel(newValue.getValue(), attributeModel, true);
                attributeValuesRepository.save(attributeValueModel);
            }

            attributeRepository.save(attributeModel);
        }

        var updateAttributes = body.getUpdateAttributes();
        for (UpdateAttributeRequest updateAttribute: updateAttributes) {
            var attributeResp = attributeRepository.findById(updateAttribute.getId());
            if (attributeResp.isPresent()) {
                AttributeModel attributeModel = attributeResp.get();
                attributeModel.setName(updateAttribute.getName());
                attributeModel.setShowImage(updateAttribute.isShowImage());

                if (attributeModel.isActive() && !updateAttribute.isActive()) {
                    List<AttributeValueModel> valueModels = attributeValuesRepository.findAllByAttributeIdAndActive(attributeModel.getId(), true);
                    for (AttributeValueModel valueModel : valueModels) {
                        valueModel.setActive(false);
                        attributeValuesRepository.save(valueModel);
                    }
                }

                attributeModel.setActive(updateAttribute.isActive());
                attributeRepository.save(attributeModel);

                var newValues = updateAttribute.getNewValues();
                for (NewAttributeValueRequest newValue : newValues) {
                    AttributeValueModel attributeValueModel = new AttributeValueModel(newValue.getValue(), attributeModel, true);
                    attributeValuesRepository.save(attributeValueModel);
                }
            }else {
                return ApiResponse.errorResponse("Atributo nao encontrado: "+ updateAttribute.getId());
            }
        }

        var updateAttributeValues = body.getUpdateAttributeValues();
        for (UpdateAttributeValueRequest updateAttributeValueRequest: updateAttributeValues) {
            var valueResp = attributeValuesRepository.findById(updateAttributeValueRequest.getId());
            if (valueResp.isPresent()) {
                var valueModel = valueResp.get();
                valueModel.setValue(updateAttributeValueRequest.getValue());
                valueModel.setActive(updateAttributeValueRequest.isActive());
            }
        }

        var availableValue = new ArrayList<AttributeValueModel>(10);
        var attributesResp = attributeRepository.findAllByPageIdAndActive(body.getId(), true);
        for (AttributeModel attributeModel: attributesResp) {
            var values = attributeValuesRepository.findAllByAttributeIdAndActive(attributeModel.getId(), true);
            availableValue.addAll(values);
        }

        var updateProducts = body.getUpdateProducts();
        for (UpdateProductRequest productRequest: updateProducts) {
            var productResp = productRepository.findById(productRequest.getId());
            if (productResp.isPresent()) {
                var productModel = productResp.get();
                var price = Double.valueOf(productRequest.getPrice());
                var stock = Integer.valueOf(productRequest.getStock());
                productModel.setPrice(price);
                productModel.setStock(stock);
                productModel.setImagePath(productRequest.getImagePath());
                productModel.setActive(productRequest.isActive());
                productRepository.save(productModel);

                var productValues = productValueRepository.findAllByProductId(productModel.getId());
                for (ProductValueModel productValueModel: productValues) {
                    productValueModel.setActive(false);
                    productValueRepository.save(productValueModel);
                }

                for (String value: productRequest.getValues()) {
                    for (AttributeValueModel valueModel: availableValue) {
                        if (valueModel.getValue().equals(value)) {
                            ProductValueModel productValueModel = new ProductValueModel(valueModel, productModel, true);
                            productValueRepository.save(productValueModel);
                            break;
                        }
                    }
                }
            }
        }

        var newProducts = body.getProducts();
        for (NewProductRequest newProduct: newProducts) {
            var price = Double.valueOf(newProduct.getPrice());
            var stock = Integer.valueOf(newProduct.getStock());
            ProductModel productModel = new ProductModel(data, price, stock, newProduct.getImagePath(), true);
            productRepository.save(productModel);

            for (String value: newProduct.getValues()) {
                for (AttributeValueModel valueModel: availableValue) {
                    if (valueModel.getValue().equals(value)) {
                        ProductValueModel productValueModel = new ProductValueModel(valueModel, productModel, true);
                        productValueRepository.save(productValueModel);
                        break;
                    }
                }
            }
        }

        return ApiResponse.successResponse(new GeneralResponse(body.getId()));
    }
}

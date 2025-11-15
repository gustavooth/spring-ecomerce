package me.gustavoo.ecomerce.controllers.responseModels;

import lombok.Data;
import me.gustavoo.ecomerce.db.models.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class PublicProductPageResponse {
    private final String title;
    private final String slug;
    private final String shortDescription;
    private final String description;
    private final List<PublicProductResponse> products;
    private final List<PublicAttributeResponse> attributes;
    private final List<PublicPageImageResponse> images;

    public PublicProductPageResponse(ProductPageModel productPageModel) {
        this.title = productPageModel.getTitle();
        this.slug = productPageModel.getSlug();
        this.shortDescription = productPageModel.getShortDescription();
        this.description = productPageModel.getDescription();

        List<ProductModel> _products = productPageModel.getProducts();
        List<PublicProductResponse> products = new ArrayList<>(_products.size());
        for (ProductModel _product : _products) {
            List<ProductValueModel> _values = _product.getValues();
            List<PublicProductValueResponse> values = new ArrayList<>(_values.size());
            for (ProductValueModel _value : _values) {
                AttributeValueModel _attribute_value = _value.getAttributeValue();
                values.add(new PublicProductValueResponse(_attribute_value.getValue()));
            }

            products.add(new PublicProductResponse(_product.getPrice(), _product.getImagePath(), values));
        }

        List<AttributeModel> _attributes = productPageModel.getAttributes();
        List<PublicAttributeResponse> attributes = new ArrayList<>(_attributes.size());
        for (AttributeModel _attribute : _attributes) {
            List<AttributeValueModel> _values = _attribute.getValues();

            List<PublicAttributeValueResponse> values = new ArrayList<>(_values.size());
            for (AttributeValueModel _value : _values) {
                values.add(new PublicAttributeValueResponse(_value.getValue()));
            }

            attributes.add(new PublicAttributeResponse(_attribute.getName(), values));
        }

        List<PageImageModel> _images = productPageModel.getImages();
        List<PublicPageImageResponse> images = new ArrayList<>(_images.size());
        for (PageImageModel _image : _images) {
            images.add(new PublicPageImageResponse(_image.getPath(), _image.getIndex()));
        }

        this.products = products;
        this.attributes = attributes;
        this.images = images;
    }
}

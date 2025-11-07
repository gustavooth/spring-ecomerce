package me.gustavoo.ecomerce.controllers.RequestBody;

public class AdminNewProduct {
    public String token;

    public String title;
    public String slug;
    public String shortDescription;
    public String longDescription;
    public String description;

    public AdminNewProductInfo[] productInfo;
    public String[] imagePath;
}

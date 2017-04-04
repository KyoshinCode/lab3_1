package pl.com.bottega.ecommerce.sales.application.api.handler;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

/**
 * Created by Sasho on 2017-04-02.
 */
public class ProductBuilder {
    private Id id = Id.generate();
    private Money price;
    private String name = "customProduct";
    private ProductType productType = ProductType.FOOD;
    boolean available = true;

    public ProductBuilder setId(Id id) {
        this.id = id;
        return this;
    }

    public ProductBuilder setPrice(Double price) {
        this.price = new Money(price);
        return this;
    }

    public ProductBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder setType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    public ProductBuilder unavailable() {
        available = false;
        return this;
    }

    public Product build() {
        Product product = new Product(id, price, name, productType);
        if(!available) {
                product.markAsRemoved();
            }
        return product;
    }
}

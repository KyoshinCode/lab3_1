package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {

    private Id id = Id.generate();
    private Money price = new Money(100, "PLN");
    private String name = "customProduct";
    private ProductType productType = ProductType.FOOD;
    boolean available = true;

    public Product build() {
        Product product = new Product(id, price, name, productType);
        return product;
    }

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
}

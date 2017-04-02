package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

/**
 * Created by Konrad Gos on 02.04.2017.
 */
public class ProductBuilder {
    private Id id = Id.generate();
    private Money productPrice;
    private String productName;
    private ProductType productType;

    public ProductBuilder() {
    }

    public ProductBuilder Product() {
        return new ProductBuilder();
    }

    public ProductBuilder withId(Id id) {
        this.id = id;
        return this;
    }

    public ProductBuilder withPrice(Money productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public ProductBuilder withName(String productName) {
        this.productName = productName;
        return this;
    }

    public ProductBuilder withProductType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    public Product build() {
        return new Product(id,productPrice, productName, productType);
    }
}

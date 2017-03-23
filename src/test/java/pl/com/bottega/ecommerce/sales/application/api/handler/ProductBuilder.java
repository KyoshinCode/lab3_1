package pl.com.bottega.ecommerce.sales.application.api.handler;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {

    private Id aggregateId = Id.generate();
    private Money price;
    private String name = "dummyProduct";
    private ProductType productType = ProductType.STANDARD;

    public ProductBuilder withAggregateId(Id aggregateId) {
        this.aggregateId = aggregateId;
        return this;
    }

    public ProductBuilder withPrice(Double price) {
        this.price = new Money(price);
        return this;
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder withType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    public Product build() {
        return new Product(aggregateId, price, name, productType);
    }
}

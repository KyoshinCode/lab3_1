package pl.com.bottega.ecommerce.sales.application.api.handler;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

/**
 * Created by pebuls on 03.04.17.
 */
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

    public Product buildProduct() {
        return new Product(aggregateId, price, name, productType);
    }
}

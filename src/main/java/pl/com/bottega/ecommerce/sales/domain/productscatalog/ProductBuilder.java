package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

/**
 * Created by Piotrek on 02.04.2017.
 */
public class ProductBuilder {
    private Money price;
    private String name;
    private ProductType productType;
    private Id id = Id.generate();

    private ProductBuilder() {
    }

    public ProductBuilder productBuilder(){
        return new  ProductBuilder();
    }

    public ProductBuilder withId(Id id) {
        this.id = id;
        return this;
    }

    public ProductBuilder withMoney(Money price) {
        this.price = price;
        return this;
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder withProductType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    public Product build(){
        Product product = new Product(id, price, name, productType);
        return product;
    }
}

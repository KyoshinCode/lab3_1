package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

/**
 * Created by Wojciech Szczepaniak on 11.04.2017.
 */
public class ProductBuilder {

    private Id id = Id.generate();
    private Money price = Money.ZERO;
    private String name = "Test";
    private ProductType productType = ProductType.STANDARD;
    private boolean removed = false;

    public ProductBuilder withId(Id id) {
        this.id = id;

        return this;
    }

    public ProductBuilder withPrice(Money price) {
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

    public ProductBuilder removed() {
        removed = true;

        return this;
    }

    public Product build() {
        Product product = new Product(id, price, name, productType);

        if (removed) {
            product.markAsRemoved();
        }
        return product;
    }
}

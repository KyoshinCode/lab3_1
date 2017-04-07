package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder  {

    private Id id = Id.generate();
    private Money price = new Money(5);
    private String name = "Ziemniak";
    private ProductType productType = ProductType.FOOD;

    public ProductBuilder id(Id id) {
        this.id = id;
        return this;
    }

    public ProductBuilder price(Double price) {
        this.price = new Money(price);
        return this;
    }

    public ProductBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder type(ProductType productType) {
        this.productType = productType;
        return this;
    }


    public Product build() {
        return new Product(id, price, name, productType);
    }

}

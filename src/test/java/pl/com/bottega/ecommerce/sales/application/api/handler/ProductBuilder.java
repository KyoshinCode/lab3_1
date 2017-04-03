package pl.com.bottega.ecommerce.sales.application.api.handler;


import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {
    private Id id = Id.generate();
    private String name;
    private ProductType productType;
    private Money money;

    public ProductBuilder id(Id id) {
        this.id = id;
        return this;
    }
    public ProductBuilder name(String name) {
        this.name = name;
        return this;
    }
    public ProductBuilder money(Money money) {
        this.money = money;
        return this;
    }
    public ProductBuilder productType(ProductType productType) {
        this.productType = productType;
        return this;
    }
    public Product build() {
        Product product = new Product(id, money, name, productType);
        return product;
    }
}

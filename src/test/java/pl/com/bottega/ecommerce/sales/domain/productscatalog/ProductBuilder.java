package pl.com.bottega.ecommerce.sales.domain.productscatalog;


import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {
    private Money price = new Money(0);
    private String name = "name";
    private ProductType productType = ProductType.STANDARD;
    private Id id = Id.generate();

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

    public ProductBuilder withId(Id id) {
        this.id = id;
        return this;
    }

   public static ProductBuilder product() {
        return new ProductBuilder();
    }

    public Product build() {
        return new Product(id, price, name, productType);
    }

}
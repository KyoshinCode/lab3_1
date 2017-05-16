package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductDataBuilder {
	private Id id = Id.generate();
    private Money price = new Money(20, "EUR");
    private String name = "ananas";
    private Date snapshotDate = new Date();
    private ProductType type = ProductType.FOOD;

    public ProductDataBuilder setProductId(Id id) {
        this.id = id;
        return this;
    }

    public ProductDataBuilder setPrice(Money price) {
        this.price = price;
        return this;
    }

    public ProductDataBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductDataBuilder setSnapshotDate(Date snapshotDate) {
        this.snapshotDate = snapshotDate;
        return this;
    }

    public ProductDataBuilder setType(ProductType type) {
        this.type = type;
        return this;
    }

    public ProductData build() {
        return new ProductData(id, price, name, type, snapshotDate);
    }
}

package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

/**
 * Created by Sasho on 2017-04-02.
 */
public class ProductDataBuilder {
    private Id id = Id.generate();
    private Money price = new Money(10, "PLN");
    private String name = "warzywa na patelnie";
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

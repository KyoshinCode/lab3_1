package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductDataBuilder {

	private Id productId = Id.generate();
	private Money price = new Money(100, "PLN");
	private String name = "ProductDataName";
	private Date snapshotDate = new Date();
	private ProductType type = ProductType.STANDARD;

    public ProductData build() {
        return new ProductData(productId, price, name, type, snapshotDate);
    }

	public ProductDataBuilder setProductId(Id productId) {
		this.productId = productId;
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

}

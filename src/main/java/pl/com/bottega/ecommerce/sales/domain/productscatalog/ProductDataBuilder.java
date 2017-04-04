package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductDataBuilder {
	private Id productId = Id.generate();
	private Money price = new Money(5);
	private String name = "testData";
	private Date snapshotDate = new Date();
	private ProductType pType = ProductType.STANDARD;

	public ProductDataBuilder withPrice(Money price) {
		this.price = price;
		return this;
	}

	public ProductDataBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public ProductDataBuilder withSnapshotDate(Date snapshotDate) {
		this.snapshotDate = snapshotDate;
		return this;
	}

	public ProductDataBuilder withType(ProductType pType) {
		this.pType = pType;
		return this;
	}
	
	public ProductData build() {
		return new ProductData(productId, price, name, pType, snapshotDate);
	}
}
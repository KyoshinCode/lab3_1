package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductDataBuilder {
	private Id productId = Id.generate();
	private Money price;
	private String name;
	private Date snapshotDate;
	private ProductType type;

	public ProductDataBuilder withPrice(Money money) {
		price = money;
		return this;
	}

	public ProductDataBuilder withName(String string) {
		name = string;
		return this;
	}

	public ProductDataBuilder withSnapshotDate(Date date) {
		snapshotDate = date;
		return this;
	}

	public ProductDataBuilder withType(ProductType productType) {
		type = productType;
		return this;
	}
	
	public ProductData build() {
		return new ProductData(productId, price, name, type, snapshotDate);
	}
}

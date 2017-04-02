package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductDataBuilder {
	
	private Id productId = Id.generate();
	private Money price = new Money(1);
	private String name = "testProductData";
	private Date snapshotDate = new Date();
	private ProductType type = ProductType.STANDARD;
	
	public ProductDataBuilder withProductId(Id id) {
		this.productId = id;
		return this;
	}
	
	public ProductDataBuilder withPrice(Money price) {
		this.price = price;
		return this;
	}
	
	public ProductDataBuilder withName(String name) {
		this.name = name;
		return this;
	}
	
	public ProductDataBuilder withDate(Date date) {
		this.snapshotDate = date;
		return this;
	}
	
	public ProductDataBuilder withType(ProductType type) {
		this.type = type;
		return this;
	}
	
	public ProductData build() {
		return new ProductData(this.productId, this.price, this.name, this.type, this.snapshotDate);
	}
}

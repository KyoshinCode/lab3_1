package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {
	
	private Id productId = Id.generate();
	private Money price = new Money(0);
	private String name = "Default name";
	private ProductType type = ProductType.STANDARD;
	
	public ProductBuilder() {
	}
	
	public ProductBuilder withProductId(Id productId) {
		this.productId = productId;
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
	
	public ProductBuilder withType(ProductType type) {
		this.type = type;
		return this;
	}
	
	public Product build() {
		return new Product(this.productId, this.price, this.name, this.type);
	}
}

package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {
	
	private String name = "testProduct";
	private ProductType type = ProductType.STANDARD;
	private Money price = null;
	private Id id = Id.generate();
	boolean isAv = false;
	
	public ProductBuilder withName(String name) {
		this.name = name;
		return this;
	}
	
	public ProductBuilder withType(ProductType type) {
		this.type = type;
		return this;
	}
	
	public ProductBuilder withPrice(Money price) {
		this.price = price;
		return this;
	}
	
	public ProductBuilder withId(Id id) {
		this.id = id;
		return this;
	}
	
	public ProductBuilder available() {
		this.isAv = true;
		return this;
	}
	
	public ProductBuilder unavailable() {
		this.isAv = false;
		return this;
	}
	
	public Product build() {
		return new Product(this.id, this.price, this.name, this.type);
	}
}

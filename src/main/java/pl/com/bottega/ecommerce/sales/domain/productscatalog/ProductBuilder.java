package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

/**
 * Created by Patryk Wierzyński
 */

public class ProductBuilder {

	private Id id = Id.generate();
	private Money price = new Money(0);
	private String name = "defaultName";
	private ProductType productType = ProductType.FOOD;
	private boolean available = true;

	public ProductBuilder withId(Id id) {
		this.id = id;
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
		this.productType = type;
		return this;
	}

	public ProductBuilder available() {
		this.available = true;
		return this;
	}

	public ProductBuilder unacailable() {
		this.available = false;
		return this;
	}

	public Product build() {
		Product product = new Product(id, price, name, productType);
		if (!available) {
			product.markAsRemoved();
		}
		return product;
	}

}

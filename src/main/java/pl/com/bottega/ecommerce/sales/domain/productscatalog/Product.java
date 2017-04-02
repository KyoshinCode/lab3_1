package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import java.util.Date;

import pl.com.bottega.ddd.support.domain.BaseAggregateRoot;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class Product extends BaseAggregateRoot {
	private Money price;

	private String name;

	private ProductType productType;

	private Product() {
	}

	public Product(Id aggregateId, Money price, String name, ProductType productType) {
		this.id = aggregateId;
		this.price = price;
		this.name = name;
		this.productType = productType;
	}

	public boolean isAvailable() {
		return !isRemoved();
	}

	public Money getPrice() {
		return price;
	}

	public String getName() {
		return name;
	}

	public ProductType getProductType() {
		return productType;
	}

	public ProductData generateSnapshot() {
		return new ProductData(getId(), price, name, productType, new Date());
	}

	public static class Builder {
		private Id id = Id.generate();
		private Money price;
		private String name;
		private ProductType productType;
		
		public Builder id(Id id) {
			this.id = id;
			return this;
		}

		public Builder price(Money price) {
			this.price = price;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder productType(ProductType productType) {
			this.productType = productType;
			return this;
		}

		public Product build() {
			return new Product(this);
		}
	}

	private Product(Builder builder) {
		this.id = builder.id;
		this.price = builder.price;
		this.name = builder.name;
		this.productType = builder.productType;
	}
}

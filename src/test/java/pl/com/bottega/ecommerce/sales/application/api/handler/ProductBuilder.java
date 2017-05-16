package pl.com.bottega.ecommerce.sales.application.api.handler;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Invoice;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {
	private Id aggregateId;
	private Money price;
	private String name;
	private ProductType productType;

	public ProductBuilder() {
		aggregateId = Id.generate();
		price = new Money(1);
		name = "default";
		productType = ProductType.STANDARD;
	}

	public ProductBuilder withId(Id id) {
		this.aggregateId = id;
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

	public ProductBuilder withProductType(ProductType productType) {
		this.productType = productType;
		return this;
	}

	public Product build() {
		return new Product(aggregateId, price, name, productType);
	}
}
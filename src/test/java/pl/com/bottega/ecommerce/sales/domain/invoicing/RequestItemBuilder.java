package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemBuilder {
	private ProductData productData;
	private int quantity;
	private Money totalCost;

	public RequestItemBuilder() {
		this.productData = new ProductDataBuilder().build();
		this.quantity = 1;
		this.totalCost = new Money(1);
	}

	public RequestItemBuilder withProductData(ProductData data) {
		this.productData = data;
		return this;
	}

	public RequestItemBuilder withQuantity(int quantity) {
		this.quantity = quantity;
		return this;
	}

	public RequestItemBuilder witTotalCost(double cost) {
		this.totalCost = new Money(cost);
		return this;
	}

	public RequestItem build() {
		return new RequestItem(productData, quantity, totalCost);
	}

}
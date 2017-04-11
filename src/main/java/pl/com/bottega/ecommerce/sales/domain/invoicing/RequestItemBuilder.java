package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemBuilder {
	private ProductData productData = new ProductDataBuilder.build();
	private int quantity = 1;
	private Money totalCost = new Money(1);
	
	public RequestItemBuilder() {}
	
	public RequestItemBuilder withproductData(ProductData productData) {
		this.productData = productData;
		return this;
	}
	
	public RequestItemBuilder withSnapshotDate(int quantity) {
		this.quantity = quantity;
		return this;
	}
	
	public RequestItemBuilder withType(Money totalCost) {
		this.totalCost = totalCost;
		return this;
	}
	
	public RequestItem build() {
		return new RequestItem(productData, quantity, totalCost);
	}
}

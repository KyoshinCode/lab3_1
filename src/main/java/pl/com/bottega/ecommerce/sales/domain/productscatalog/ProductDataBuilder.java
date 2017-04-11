package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;
import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItemBuilder;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductDataBuilder {
	
	private Id productId = Id.generate();
 	private Money price = new Money(0);
 	private String name = "Adam";
  	private Date snapshotDate = new Date();
  	private ProductType type = ProductType.STANDARD;
	
	private ProductData productData = new ProductDataBuilder().build();
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

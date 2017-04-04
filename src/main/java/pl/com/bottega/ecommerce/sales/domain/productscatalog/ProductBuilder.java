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
}

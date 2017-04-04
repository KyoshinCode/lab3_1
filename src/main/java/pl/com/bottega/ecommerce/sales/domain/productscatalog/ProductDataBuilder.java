package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductDataBuilder {
	
	private Id productId = Id.generate();
	private Money price = new Money(0);
	private String name = "Default name";
	private Date snapshotDate = new Date();
	private ProductType type = ProductType.STANDARD;
	
	public ProductDataBuilder() {
	}
}

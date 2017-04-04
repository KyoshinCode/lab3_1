package pl.com.bottega.ecommerce.sales.application.api.command;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

public class AddProductCommandBuilder {
	
	private Id orderId = Id.generate();
	private Id productId = Id.generate();;
	private int quantity = 0;
	
	public AddProductCommandBuilder() {
	}
}

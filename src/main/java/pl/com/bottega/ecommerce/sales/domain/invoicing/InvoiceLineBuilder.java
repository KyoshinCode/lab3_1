package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class InvoiceLineBuilder {
	
	private ProductData product;
	private int quantity;
	private Money net;
	private Tax tax;

}

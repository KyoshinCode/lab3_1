package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class InvoiceLineBuilder {
	
	private ProductData product;
  	private int quantity;
  	private Money net;
  	private Tax tax;
 	
 	public InvoiceLineBuilder() {}
 	
 	public InvoiceLineBuilder withProductData(ProductData product) {
 		this.product = product;
 		return this;
 	}
 	
 	public InvoiceLineBuilder withQuantity(int quantity) {
 		this.quantity = quantity;
 		return this;
 	}
 	
 	public InvoiceLineBuilder withNet(Money net) {
 		this.net = net;
		return this;
 	}
 	
 	public InvoiceLineBuilder withTax(Tax tax) {
 		this.tax = tax;
 		return this;
 	}
 	
 	public InvoiceLine build() {
 		return new InvoiceLine(product, quantity, net, tax);
 	}

}

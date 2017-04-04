package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sharedkernel.Money;

public class TaxBuilder {
	private Money amount;
	private String description;
	
	public TaxBuilder withMoney(Money money) {
		amount = money;
		return this;
	}
	
	public TaxBuilder withDescription(String d) {
		description = d;
		return this;
	}
	
	public Tax build() {
		return new Tax(amount, description);
	}
}

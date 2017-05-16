package pl.com.bottega.ecommerce.sales.domain.invoicing;


import pl.com.bottega.ecommerce.sharedkernel.Money;

public class TaxBuilder {
    private Money amount = new Money(10);
    private String description = "description";

    public TaxBuilder amount(Money amount) {
        this.amount = amount;
        return this;
    }

    public TaxBuilder description(String description) {
        this.description = description;
        return this;
    }

    public Tax build(){
        return new Tax(amount, description);
    }
}

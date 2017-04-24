package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Test;
import org.junit.Rule;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

public class InvoiceLineTest {
	private InvoiceLine invoiceLine;
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Mock
	private ProductData dummyProductData;
	
	@Mock
	private Money dummyMoney;
	
	@Mock
	private Tax dummyTax;
	
	@Test
	public void testInvoceLineShouldCallMoneyAdd() {
		invoiceLine = new InvoiceLine(dummyProductData, 1, dummyMoney, dummyTax);
		verify(dummyMoney, times(1)).add(any(Money.class));
		verify(dummyTax, times(1)).getAmount();
	}
}

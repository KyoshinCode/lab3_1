package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Test;
import org.junit.Rule;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Matchers;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.*;
import pl.com.bottega.ecommerce.sales.domain.client.*;
import pl.com.bottega.ecommerce.sales.domain.payment.*;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.*;

public class BookKeeperTest {
	private BookKeeper bookKeeper;
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Mock
	private TaxPolicy taxPolicy;
	
	@Mock
	private ProductData dummyProductData;
	
	@Before 
	public void setUp() {
		bookKeeper = new BookKeeper(new InvoiceFactory());
	}
	
	@Test
	public void TestIssuenceOnPositionInvoice() {;
		Money dummyMoney = new Money(0);
		Tax dummyTax = new Tax(dummyMoney, "foo");
		when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(dummyTax);
		Id clientId = Id.generate();
		InvoiceRequest singleInvoiceRequest = new InvoiceRequest(new ClientData(clientId, "John Doe"));
		singleInvoiceRequest.add(new RequestItem(dummyProductData, 1, dummyMoney));
		Invoice invoice = bookKeeper.issuance(singleInvoiceRequest, taxPolicy);
		
		assertThat(invoice.getItems().size(), is(equalTo(1)));
	}
}

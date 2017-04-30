package pl.com.bottega.ecommerce.sales.domain.invoicing;

import java.util.Date;
import org.junit.Test;
import org.junit.Rule;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.Matchers;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

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
	public void testIssuenceOnPositionInvoice() {;
		Money dummyMoney = new Money(0);
		Tax dummyTax = new Tax(dummyMoney, "foo");
		when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(dummyTax);
		Id clientId = Id.generate();
		InvoiceRequest singleInvoiceRequest = new InvoiceRequest(new ClientData(clientId, "John Doe"));
		singleInvoiceRequest.add(new RequestItem(dummyProductData, 1, dummyMoney));
		Invoice invoice = bookKeeper.issuance(singleInvoiceRequest, taxPolicy);
		
		assertThat(invoice.getItems().size(), is(equalTo(1)));
	}
	
	@Test
	public void testIssuenceTwoPositonInvoice() {
		Money dummyMoney = new Money(100);
		
		ProductDataBuilder productDataBuilder = new ProductDataBuilder();
		
		ProductData dummyProduct1 = productDataBuilder.withPrice(dummyMoney).withName("Product1").build();
		ProductData dummyProduct2 = productDataBuilder.withPrice(dummyMoney).withName("Product2").build();
		
		when(taxPolicy.calculateTax(ProductType.STANDARD, dummyMoney)).thenReturn(new Tax(new Money(10), "dummy tax"));
		InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "John Doe"));
		
		invoiceRequest.add(new RequestItem(dummyProduct1, 1, dummyMoney));
		invoiceRequest.add(new RequestItem(dummyProduct2, 1, dummyMoney));
		
		bookKeeper.issuance(invoiceRequest, taxPolicy);
		
		verify(taxPolicy, times(2)).calculateTax(ProductType.STANDARD, dummyMoney);
		
	}
}

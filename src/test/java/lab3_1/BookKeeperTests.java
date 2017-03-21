package lab3_1;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.BookKeeper;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Invoice;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceFactory;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Tax;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TaxPolicy;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTests {
	public TaxPolicy taxPolicy;
	public RequestItem requestItem;
	public ClientData clientData;
	public BookKeeper bookKeeper;
	public InvoiceRequest invoiceRequest;
	
	@Before
	public void setUp() {
		// Mocking
		taxPolicy = Mockito.mock(TaxPolicy.class);
		Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class)))
				.thenReturn(new Tax(new Money(0), "mocking"));
		// Generating data
		requestItem = new RequestItem(
				new ProductData(Id.generate(), new Money(123.45), "Pomidorek", ProductType.FOOD, new Date()), 1,
				new Money(10.25));
		clientData = new ClientData(Id.generate(), "Zawadzki");

		bookKeeper = new BookKeeper(new InvoiceFactory());
		invoiceRequest = new InvoiceRequest(clientData);
	}

	@Test
	public void testIssuanceNumOfItems1() {
		invoiceRequest.add(requestItem);
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);		
		
		assertThat(invoice.getItems().size(), is(equalTo(1)));
	}
	
	@Test
	public void testIssuanceNumOfMethodCalls2() {
		invoiceRequest.add(requestItem);
		invoiceRequest.add(requestItem);		
		bookKeeper.issuance(invoiceRequest, taxPolicy);		
		
		Mockito.verify(taxPolicy, Mockito.times(2)).calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class));
	}
	
	@Test
	public void testIssuanceNumOfItems0() {
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);		
		
		assertThat(invoice.getItems().size(), is(equalTo(0)));
	}
	
	@Test
	public void testIssuanceNumOfMethodCalls0() {
		bookKeeper.issuance(invoiceRequest, taxPolicy);		
		
		Mockito.verify(taxPolicy, Mockito.times(0)).calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class));
	}	
	@Test
	public void testClientData() {
		ClientData clientDataResult = bookKeeper.issuance(invoiceRequest, taxPolicy).getClient();
		assertThat(clientDataResult.getAggregateId(), is(equalTo(clientData.getAggregateId())));
		assertThat(clientDataResult.getName(), is(equalTo(clientData.getName())));
	}

}

package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperUnitTests {
	
	private TaxPolicy taxPolicy;
	private ProductData productData;
	private RequestItem requestItem;
	private BookKeeper bookKeeper;
	private InvoiceRequest invoiceRequest;
	private ClientData clientData;
	
	@Before
	public void prepareToTest() {
		taxPolicy = Mockito.mock(TaxPolicy.class);
		Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class))).thenReturn(new Tax(new Money(0), "mocking"));
		productData = new ProductData(Id.generate(), new Money(150.5), "Hamburger", ProductType.FOOD, new Date());
		requestItem = new RequestItem(productData, 1, new Money(3.5));	
		bookKeeper = new BookKeeper(new InvoiceFactory());
		clientData = new ClientData(Id.generate(), "Adam");
		invoiceRequest = new InvoiceRequest(clientData);
	}

	@Test
 	public void onePositionInvoice() {
	 	invoiceRequest.add(requestItem);
	 	Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
	 	
	 	assertThat(invoice.getItems().size(), is(equalTo(1)));
	}	
	
	@Test
 	public void twoCalculationsTaxCalls() {
	 	invoiceRequest.add(requestItem);
	 	invoiceRequest.add(requestItem);
	 	bookKeeper.issuance(invoiceRequest, taxPolicy);
	 	
	 	Mockito.verify(taxPolicy, Mockito.times(2)).calculateTax(Mockito.any(ProductType.class),
 				Mockito.any(Money.class));
	}
	
	@Test
	public void invoiceFactoryReturnsCorrectType() {
		assertThat(bookKeeper.issuance(invoiceRequest, taxPolicy), is(instanceOf(Invoice.class)));
	}
	
	@Test
	public void clientDataRetainsCorrectValues() {
		assertThat(bookKeeper.issuance(invoiceRequest, taxPolicy).getClient().getAggregateId(), is(equalTo(clientData.getAggregateId())));
		assertThat(bookKeeper.issuance(invoiceRequest, taxPolicy).getClient().getName(), is(equalTo(clientData.getName())));
	}
}

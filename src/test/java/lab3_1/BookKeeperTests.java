package lab3_1;

import static org.hamcrest.CoreMatchers.instanceOf;
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
import pl.com.bottega.ecommerce.sales.domain.invoicing.TaxBuilder;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TaxPolicy;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
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
		// Building
		TaxBuilder taxBuilder = new TaxBuilder().withMoney(new Money(0)).withDescription("mocking");
		ProductDataBuilder productDataBuilder = new ProductDataBuilder().withPrice(new Money(123.45)).withName("Pomidorek").withType(ProductType.FOOD).withSnapshotDate(new Date());
		// Mocking
		taxPolicy = Mockito.mock(TaxPolicy.class);
		Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class)))
				.thenReturn(taxBuilder.build());
		// Generating data
		RequestItem.Builder requestItemB = new RequestItem.Builder();
		requestItemB.productData(productDataBuilder.build()).quantity(1).totalCost(new Money(10.25));
		requestItem = requestItemB.build();
		clientData = new ClientData("Zawadzki");

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
	@Test
	public void testInvoiceFactory() {
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertThat(invoice, instanceOf(Invoice.class));
		
	}

}

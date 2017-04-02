package lab3_1;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.hamcrest.Matchers.*;

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
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class Tests_3_1_1 {
	
	public TaxPolicy taxPolicy;
	public ProductData productData;
	public RequestItem requestItem;
	public BookKeeper bookKeeper;
	public InvoiceRequest invoiceRequest;
	public ClientData clientData;
	
	@Before
	public void setupTests() {
		taxPolicy = Mockito.mock(TaxPolicy.class);
		Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class),
				Mockito.any(Money.class))).thenReturn(new Tax(new Money(0), "mocking"));
		clientData = new ClientData(Id.generate(), "wujeksado");
		productData = new ProductDataBuilder()
				.withDate(new Date())
				.withName("testProductData")
				.withPrice(new Money(123))
				.withProductId(Id.generate())
				.withType(ProductType.STANDARD)
				.build();
		requestItem = new RequestItem(productData, 1, new Money(10.25));	
		bookKeeper = new BookKeeper(new InvoiceFactory());
		invoiceRequest = new InvoiceRequest(clientData);
	}
	
	@Test
	public void onePositionInvoice() {
		invoiceRequest.add(requestItem);
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertThat(invoice.getItems().size(), is(equalTo(1)));
	}
	
	@Test
	public void twoCalculateTaxCalls() {
		invoiceRequest.add(requestItem);
		invoiceRequest.add(requestItem);
		bookKeeper.issuance(invoiceRequest, taxPolicy);
		Mockito.verify(taxPolicy, Mockito.times(2)).calculateTax(Mockito.any(ProductType.class),
				Mockito.any(Money.class));
	}
	
	@Test
	public void clientDataRetainsCorrectValues() {
		assertThat(bookKeeper.issuance(invoiceRequest, taxPolicy).getClient().getAggregateId(), is(equalTo(clientData.getAggregateId())));
		assertThat(bookKeeper.issuance(invoiceRequest, taxPolicy).getClient().getName(), is(equalTo(clientData.getName())));
	}
	
	@Test
	public void invoiceFactoryCorrectReturnType() {
		assertThat(bookKeeper.issuance(invoiceRequest, taxPolicy), is(instanceOf(Invoice.class)));
	}
}

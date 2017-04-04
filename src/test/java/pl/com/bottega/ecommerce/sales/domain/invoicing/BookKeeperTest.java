package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.any;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTest {
	
	private ClientData clientData = new ClientData(Id.generate(), "Jan");
	private InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
	private ProductData productData = new ProductData(Id.generate(), new Money(100), "Apple", ProductType.FOOD, new Date(0));
	private InvoiceFactory invoiceFcatory;
	private BookKeeper bookKeeper;
	private TaxPolicy taxPolicyMocked;
	private RequestItem requestItem = new RequestItem(productData, 1, new Money(100));
	
	
	@Before
	public void setUp() {
		invoiceFcatory = new InvoiceFactory();
		 bookKeeper = new BookKeeper(invoiceFcatory);
		 taxPolicyMocked = mock(TaxPolicy.class);
		 when(taxPolicyMocked.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(new Money(100), "EUR"));
	}
	
	
	@Test
	public void invoiceWithOneItemTest() {
		invoiceRequest.add(requestItem);
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicyMocked);
		assertThat(invoice.getItems().size(), is(equalTo(1)));
	}
	
	@Test
	public void invoiceWithTwoItemsCallCalculateTaxTwoTimesTest() {
		invoiceRequest.add(requestItem);
		invoiceRequest.add(requestItem);
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicyMocked);
		
		verify(taxPolicyMocked, times(2)).calculateTax(any(ProductType.class), any(Money.class));
	}
	
	@Test
	public void invoiceWithZeroItemsTest() {
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicyMocked);
		assertThat(invoice.getItems().size(), is(equalTo(0)));
	}

	
	
}

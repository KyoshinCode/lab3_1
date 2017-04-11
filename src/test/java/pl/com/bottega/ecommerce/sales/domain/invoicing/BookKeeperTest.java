package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Invoice;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTest {

	Invoice invoice;
	InvoiceRequest invoiceRequest;
	BookKeeper bookKeeper;

	InvoiceFactory invoiceFactory;
	TaxPolicy taxPolicy;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		invoice = new InvoiceBuilder().withClient(new ClientDataBuilder().withName("Arleta").build()).build();
		invoiceRequest = new InvoiceRequest(new ClientDataBuilder().withName("Arleta").build());
		invoiceRequest.add(new RequestItemBuilder().build());

		invoiceFactory = Mockito.mock(InvoiceFactory.class);
		taxPolicy = Mockito.mock(TaxPolicy.class);

		Mockito.when(invoiceFactory.create(invoiceRequest.getClientData())).thenReturn(invoice);
		Mockito.when(taxPolicy.calculateTax(ProductType.STANDARD, new Money(1)))
				.thenReturn(new Tax(new Money(1), "default"));

		bookKeeper = new BookKeeper(invoiceFactory);
	}

	@Test
	public void testState_invoiceWithOneItem_shouldBeOneItem() {

		BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
		Invoice newInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

		assertThat(newInvoice.getItems().size(), is(1));

	}

	@Test
	public void testBehavior_invoiceWithTwoItem_shouldBeRunTwoTimes() {

		invoiceRequest.add(new RequestItemBuilder().build());
		Invoice newInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

		Mockito.verify(taxPolicy, Mockito.times(2)).calculateTax(Mockito.any(ProductType.class),
				Mockito.any(Money.class));

	}

	@Test
	public void testState_clientName_shouldBeTheSame() {

		Invoice newInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

		assertThat(newInvoice.getClient().getName(), is("Arleta"));
	}

	@Test
	public void testBehavior_createInvoice_shoudBeRunOneTime() {

		invoiceRequest.add(new RequestItemBuilder().build());
		invoiceRequest.add(new RequestItemBuilder().build());

		Invoice newInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

		Mockito.verify(invoiceFactory, Mockito.times(1)).create(invoiceRequest.getClientData());
	}

}

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

	ClientData clientData;
	Invoice invoice;
	InvoiceRequest invoiceRequest;
	ProductData productData;
	RequestItem requestItem;
	BookKeeper bookKeeper;

	InvoiceFactory invoiceFactory;
	TaxPolicy taxPolicy;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		clientData = new ClientData(Id.generate(), "Arleta");
		invoice = new Invoice(Id.generate(), clientData);
		productData = new ProductData(Id.generate(), new Money(2), "kanapka", ProductType.FOOD, new Date());
		requestItem = new RequestItem(productData, 0, new Money(2));
		invoiceRequest = new InvoiceRequest(clientData);
		invoiceRequest.add(requestItem);

		invoiceFactory = Mockito.mock(InvoiceFactory.class);
		taxPolicy = Mockito.mock(TaxPolicy.class);

		Mockito.when(invoiceFactory.create(invoiceRequest.getClientData())).thenReturn(invoice);
		Mockito.when(taxPolicy.calculateTax(ProductType.FOOD, new Money(2)))
				.thenReturn(new Tax(new Money(2), "kanapka"));

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

		invoiceRequest.add(requestItem);
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

		invoiceRequest.add(requestItem);
		invoiceRequest.add(requestItem);

		Invoice newInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

		Mockito.verify(invoiceFactory, Mockito.times(1)).create(invoiceRequest.getClientData());
	}

}

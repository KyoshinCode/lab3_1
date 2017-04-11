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

		invoiceFactory = Mockito.mock(InvoiceFactory.class);
		taxPolicy = Mockito.mock(TaxPolicy.class);
	}

	@Test
	public void testState_invoiceWithOneItem_shouldBeOneItem() {

		invoiceRequest = new InvoiceRequest(clientData);
		invoiceRequest.add(requestItem);

		Mockito.when(invoiceFactory.create(invoiceRequest.getClientData())).thenReturn(invoice);
		Mockito.when(taxPolicy.calculateTax(ProductType.FOOD, new Money(2)))
				.thenReturn(new Tax(new Money(2), "kanapka"));
		BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
		Invoice newInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

		assertThat(newInvoice.getItems().size(), is(1));

	}

	@Test
	public void testBehavior_invoiceWithTwoItem_shouldBeRunTwoTimes() {

		invoiceRequest = new InvoiceRequest(clientData);
		invoiceRequest.add(requestItem);
		invoiceRequest.add(requestItem);

		InvoiceFactory invoiceFactory = Mockito.mock(InvoiceFactory.class);
		TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);

		Mockito.when(invoiceFactory.create(invoiceRequest.getClientData())).thenReturn(invoice);
		Mockito.when(taxPolicy.calculateTax(ProductType.FOOD, new Money(2)))
				.thenReturn(new Tax(new Money(2), "kanapka"));
		BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
		Invoice newInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

		Mockito.verify(taxPolicy, Mockito.times(2)).calculateTax(Mockito.any(ProductType.class),
				Mockito.any(Money.class));

	}

	@Test
	public void testState_clientName_shouldBeTheSame() {

		invoiceRequest = new InvoiceRequest(clientData);
		invoiceRequest.add(requestItem);

		InvoiceFactory invoiceFactory = Mockito.mock(InvoiceFactory.class);
		TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);

		Mockito.when(invoiceFactory.create(invoiceRequest.getClientData())).thenReturn(invoice);
		Mockito.when(taxPolicy.calculateTax(ProductType.FOOD, new Money(2)))
				.thenReturn(new Tax(new Money(2), "kanapka"));
		BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
		Invoice newInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

		assertThat(newInvoice.getClient().getName(), is("Arleta"));
	}

	@Test
	public void testBehavior_createInvoice_shoudBeRunOneTime() {

		invoiceRequest = new InvoiceRequest(clientData);
		invoiceRequest.add(requestItem);
		invoiceRequest.add(requestItem);

		InvoiceFactory invoiceFactory = Mockito.mock(InvoiceFactory.class);
		TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);

		Mockito.when(invoiceFactory.create(invoiceRequest.getClientData())).thenReturn(invoice);
		Mockito.when(taxPolicy.calculateTax(ProductType.FOOD, new Money(2)))
				.thenReturn(new Tax(new Money(2), "kanapka"));
		BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
		Invoice newInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

		Mockito.verify(invoiceFactory, Mockito.times(1)).create(invoiceRequest.getClientData());
	}

}

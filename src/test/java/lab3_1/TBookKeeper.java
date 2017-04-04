package lab3_1;

import static org.mockito.Mockito.mock;

import java.sql.Date;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.BookKeeper;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Invoice;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceFactory;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceLine;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Tax;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TaxPolicy;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class TBookKeeper {
	TaxPolicy taxPolicy;
	ClientData client;
	InvoiceRequest invoiceRequest;
	Money money;
	Date date;
	ProductData pData;
	RequestItem item;
	Tax tax;
	BookKeeper bookKeeper;

	@Before
	public void setup() {
		taxPolicy = mock(TaxPolicy.class);

		invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Tester Jack"));

		money = new Money(5);
		date = new Date(0);

		//pData = new ProductData(Id.generate(), money, "test", ProductType.STANDARD, date);
		pData = new ProductDataBuilder().build();
		item = new RequestItem(pData, 1, money);

		tax = new Tax(money, "test");

		bookKeeper = new BookKeeper(new InvoiceFactory());
	}

	@Test
	public void testCaseForState() {
		invoiceRequest.add(item);

		Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class))).thenReturn(tax);

		Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);

		Assert.assertThat(result.getItems().size(), Matchers.equalTo(1));
	}

	@Test
	public void testCaseForBehaviour() {
		invoiceRequest.add(item);
		invoiceRequest.add(item);

		Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class))).thenReturn(tax);

		Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);

		Mockito.verify(taxPolicy, Mockito.times(2)).calculateTax(Mockito.any(ProductType.class),
				Mockito.any(Money.class));
	}

	@Test
	public void issuanceMethodDoesntChangeRequestItemsProperties() {
		invoiceRequest.add(item);

		Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class))).thenReturn(tax);

		Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);

		InvoiceLine invoiceLine = new InvoiceLine(pData, 1, money, tax);
		
		Assert.assertThat(result.getItems().get(0), Matchers.samePropertyValuesAs(invoiceLine));
	}

	@Test
	public void issuanceMethodCallsInvoiceFactoryOnce() {
		InvoiceFactory factory = mock(InvoiceFactory.class);
		Invoice invoice = new Invoice(Id.generate(), new ClientData(Id.generate(), "Tester"));

		Mockito.when(factory.create(Mockito.any(ClientData.class))).thenReturn(invoice);
		
		BookKeeper keeper = new BookKeeper(factory);
		keeper.issuance(invoiceRequest, taxPolicy);
		
		Mockito.verify(factory, Mockito.times(1)).create(Mockito.any(ClientData.class));
	}
}

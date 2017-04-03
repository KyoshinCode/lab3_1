package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.Assert.*;

import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;

public class BookKeeperTest {

	@Test
	public void testInvoiceOnePositionReturnOne() {
		InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Kuba"));

		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

		TaxPolicy taxPolicy = mock(TaxPolicy.class);
		when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(
				new Tax(new Money(1), "EUR"));
		
		RequestItem item = new RequestItem
				(new ProductData(Id.generate(), new Money(200), "Banan", ProductType.FOOD, new Date()), 1, new Money(20));

		invoiceRequest.add(item);
		
		Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);

		assertThat(result.getItems().size(), is(equalTo(1)));
	}
	
	@Test
	public void testTwoItemsCallCalculateTaxTwice() throws Exception {
		InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Kuba"));

		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());

		TaxPolicy taxPolicy = mock(TaxPolicy.class);
		when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(
				new Tax(new Money(1), "EUR"));
		
		RequestItem item = new RequestItem
				(new ProductData(Id.generate(), new Money(200), "Banan", ProductType.FOOD, new Date()), 1, new Money(20));

		invoiceRequest.add(item);
		invoiceRequest.add(item);
		
		Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);

		verify(taxPolicy, times(2)).calculateTax(ProductType.FOOD, item.getTotalCost());
	}
	
	@Test
	public void testClientDataHaveCorrectValues() {
		ClientData clientData = new ClientData(Id.generate(), "Kuba");
		InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
		
		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		
		TaxPolicy taxPolicy = mock(TaxPolicy.class);
		
		assertThat(bookKeeper.issuance(invoiceRequest, taxPolicy).getClient().getAggregateId(), is(equalTo(clientData.getAggregateId())));
		assertThat(bookKeeper.issuance(invoiceRequest, taxPolicy).getClient().getName(), is(equalTo(clientData.getName())));
	}
	
	@Test
	public void testIssuanceWithNoItemsReturnsNoItemInvoice() throws Exception {
		ClientData clientData = new ClientData(Id.generate(), "Kuba");
		InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
		
		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		
		TaxPolicy taxPolicy = mock(TaxPolicy.class);
		
		Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertThat(result.getItems().size(), is(equalTo(0)));
	}
	
	public void testInvoiceZeroItemZeroTimes() throws Exception {
		ClientData clientData = new ClientData(Id.generate(), "Kuba");
		InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
		
		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		
		TaxPolicy taxPolicy = mock(TaxPolicy.class);
		
		when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(new Money(30), "example"));
		
	}
}

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
	}

}

package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BookKeeperTest {
	
	@Test
	public void testInvoiceRequestWithOneItemShouldReturnInvoiceWithOneLine() {
		
		InvoiceFactory factory = new InvoiceFactory();
		BookKeeper bookKeeper = new BookKeeper(factory);
		
		InvoiceRequest request = mock(InvoiceRequest.class);
		RequestItem item = mock(RequestItem.class);
		request.add(item);
		List<RequestItem> items = new ArrayList<RequestItem>();
		items.add(item);
		System.out.println(request);
		when(request.getItems()).thenReturn(items);
		
		System.out.println(items);
		
		TaxPolicy taxPolicy = mock(TaxPolicy.class);
		System.out.println(taxPolicy);
		
		//Invoice invoice = bookKeeper.issuance(request, taxPolicy);
		Invoice invoice = mock(Invoice.class);
		//when(invoice.getItems()).thenReturn(value)
		Assert.assertThat(invoice.getItems().size(), is(equalTo(1)));
		
		
	}
	
	@Test
	public void testInvoiceRequestWithTwoItemsShouldInvokeCalculateTaxTwoTimes() {
		fail("Not yet implemented");
	}

}

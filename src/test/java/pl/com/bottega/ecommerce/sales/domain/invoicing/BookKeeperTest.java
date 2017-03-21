package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.Assert.*;
import org.hamcrest.Matchers;

import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;

import static org.mockito.Mockito.*;

public class BookKeeperTest {

	@Test
	public void testInvoiceOnePositionReturnOne() {
		
		BookKeeper bookKeeper = mock(BookKeeper.class);
		//Invoice mockedInvoice = mock(Invoice.class);
		//mockedInvoice.addItem(item);
		TaxPolicy tax = mock(TaxPolicy.class);
		RequestItem itemRequest = mock(RequestItem.class);
		ClientData clientData = mock(ClientData.class);
		InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
		
		invoiceRequest.add(itemRequest);
		
		bookKeeper.issuance(invoiceRequest, tax);
		
		assertThat(invoiceRequest.getItems().size(), Matchers.is(1));
	}

}

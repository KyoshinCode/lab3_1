package lab3_1;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;
import org.mockito.Mockito;

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
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTests {

	@Test
	public void testIssuance() {
		// Mocking
		TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
		Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class))).thenReturn(new Tax(new Money(0), "mocking"));
		// Generating data
		RequestItem requestItem = new RequestItem(new ProductData(Id.generate(), new Money(123.45), "Pomidorek", ProductType.FOOD,
				new Date()), 1, new Money(10.25));
		ClientData clientData = new ClientData(Id.generate(),"Zawadzki");		
		
		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
		invoiceRequest.add(requestItem);
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);		
		
		assertThat(invoice.getItems().size(), is(equalTo(1)));
	}

}

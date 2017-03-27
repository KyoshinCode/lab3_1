package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTest {
	
	private ClientData clientData = new ClientData(Id.generate(), "Jan");
	private InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
	private ProductData productData = new ProductData(Id.generate(), new Money(100), "Apple", ProductType.FOOD, Date.valueOf("2017"));
	private RequestItem requestItem = new RequestItem(productData, 0, new Money(100));

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}

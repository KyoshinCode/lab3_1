package lab3_1;

import static org.mockito.Mockito.mock;

import java.sql.Date;

import org.hamcrest.Matchers;
import org.junit.Assert;
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

public class TestsPart1 {

	@Test
	public void testCase1() {
		TaxPolicy taxPolicy = mock(TaxPolicy.class);
		ClientData client = mock(ClientData.class);

		InvoiceRequest invoiceRequest = new InvoiceRequest(client);

		Money money = new Money(5);
		Date date = new Date(0);

		ProductData pData = new ProductData(Id.generate(), money, "test", ProductType.STANDARD, date);
		RequestItem item = new RequestItem(pData, 1, money);
		invoiceRequest.add(item);

		Tax tax = new Tax(money, "test");

		Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class))).thenReturn(tax);

		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);

		Assert.assertThat(result.getItems().size(), Matchers.equalTo(1));
	}

}

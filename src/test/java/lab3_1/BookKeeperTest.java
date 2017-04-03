package lab3_1;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
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

public class BookKeeperTest {

	@Test
	public void invoiceWithOneItem() {

		ProductData productData = new ProductData(Id.generate(), new Money(100), "Product", ProductType.STANDARD, new Date());

		TaxPolicy taxPolicy = mock(TaxPolicy.class);
		Tax tax = new Tax(new Money(10), "Tax");
		Mockito.when(taxPolicy.calculateTax(productData.getType(), productData.getPrice())).thenReturn(tax);

		RequestItem requestItem = new RequestItem(productData, 1, new Money(100));
		InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "ClientName"));
		invoiceRequest.add(requestItem);

		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

		Assert.assertThat(invoice.getItems().size(), is(equalTo(1)));
	}

	@Test
	public void callCalculateTaxTwice() {

		Money money = new Money(100);
		ProductData productData = new ProductData(Id.generate(), money, "Product", ProductType.STANDARD, new Date());

		TaxPolicy taxPolicy = mock(TaxPolicy.class);
		Tax tax = new Tax(new Money(10), "Tax");
		Mockito.when(taxPolicy.calculateTax(productData.getType(), productData.getPrice())).thenReturn(tax);

		RequestItem requestItem = new RequestItem(productData, 1, money);
		InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "ClientName"));
		invoiceRequest.add(requestItem);
		invoiceRequest.add(requestItem);

		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		bookKeeper.issuance(invoiceRequest, taxPolicy);

		verify(taxPolicy, times(2)).calculateTax(ProductType.STANDARD, money);
	}
}

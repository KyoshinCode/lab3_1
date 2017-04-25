package lab3_1;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

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
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	TaxPolicy taxPolicy;

	private RequestItem requestItem;
	private InvoiceRequest invoiceRequest;
	private BookKeeper bookKeeper;
	private Money money;
	private ProductData productData;

	@Before
	public void setUp(){
		bookKeeper = new BookKeeper(new InvoiceFactory());
		money = new Money(100);
		productData = new ProductDataBuilder().setPrice(money).build();

		Tax tax = new Tax(new Money(10), "Tax");
		Mockito.when(taxPolicy.calculateTax(productData.getType(), productData.getPrice())).thenReturn(tax);

		requestItem = new RequestItem(productData, 1, money);
		invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "ClientName"));

	}

	@Test
	public void invoiceWithOneItem() {

		invoiceRequest.add(requestItem);
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

		Assert.assertThat(invoice.getItems().size(), is(equalTo(1)));
	}

	@Test
	public void callCalculateTaxTwice() {

		invoiceRequest.add(requestItem);
		invoiceRequest.add(requestItem);
		bookKeeper.issuance(invoiceRequest, taxPolicy);

		verify(taxPolicy, times(2)).calculateTax(ProductType.STANDARD, money);
	}

	@Test
	public void productOnInvoiceTest() {

		invoiceRequest.add(requestItem);
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

		Assert.assertThat(invoice.getItems().get(0).getProduct().getPrice(), is(equalTo(productData.getPrice())));
		Assert.assertThat(invoice.getItems().get(0).getProduct().getType(), is(equalTo(productData.getType())));
		Assert.assertThat(invoice.getItems().get(0).getProduct().getName(), is(equalTo(productData.getName())));
	}

	@Test
	public void issuanceReturnTypeTest() {
		Assert.assertThat(bookKeeper.issuance(invoiceRequest, taxPolicy), is(instanceOf(Invoice.class)));
	}
}

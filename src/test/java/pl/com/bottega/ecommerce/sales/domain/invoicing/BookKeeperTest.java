package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by 195086 on 21.03.2017.
 */

public class BookKeeperTest {

	private InvoiceRequest invoiceRequest;
	private BookKeeper bookKeeper;
	private TaxPolicy taxPolicy;

	@Before
    public void setUp() throws Exception {
		invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Patryk"));

		bookKeeper = new BookKeeper(new InvoiceFactory());

		taxPolicy = mock(TaxPolicy.class);
		when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(
				new Tax(new Money(1), "PLN"));
    }

    @Test
    public void issuanceWithOneItemReturnsOneItemInvoice() throws Exception {
        // given
		RequestItem item = new RequestItem(
				new ProductData(Id.generate(), new Money(200), "Kilbasa",
						ProductType.FOOD, new Date()),
				1, new Money(200)
		);

		// when
		invoiceRequest.add(item);
		Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);

		// then
		assertThat(result.getItems().size(), is(equalTo(1)));
    }

	@Test
	public void issuanceWithNoItemsReturnsEmptyInvoice() throws Exception {
		// given

		// when
		Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);

		// then
		assertThat(result.getItems().size(), is(equalTo(0)));
	}

	@Test
	public void issuanceWithTwoItemsCallsCalculateTaxTwice() throws Exception {
		// given
		RequestItem item1 = new RequestItem(
				new ProductData(Id.generate(), new Money(200), "Kielbasa",
						ProductType.FOOD, new Date()),
				1, new Money(200)
		);
		RequestItem item2 = new RequestItem(
				new ProductData(Id.generate(), new Money(200), "Szynka",
						ProductType.FOOD, new Date()),
				1, new Money(200)
		);

		// when
		invoiceRequest.add(item1);
		invoiceRequest.add(item2);
		Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);

		// then
		verify(taxPolicy, times(2)).calculateTax(any(ProductType.class), any(Money.class));
	}

	@Test
	public void issuanceWithNoItemsCallsCalculateTaxZeroTimes() throws Exception {
		// given

		// when
		Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);

		// then
		verify(taxPolicy, times(0)).calculateTax(any(ProductType.class), any(Money.class));
	}

	@Test
	public void issuanceReturnsCorrectItemsInInvoice() throws Exception {
		// given

		ProductData product = new ProductData(Id.generate(), new Money(200), "Kielbasa",
				ProductType.FOOD, new Date());
		int quantity = 2;
		Money money = new Money(200);

		RequestItem item1 = new RequestItem(product, quantity, money);

		// when
		invoiceRequest.add(item1);
		Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);

		// then
		assertThat(result.getItems().get(0).getProduct(), is(equalTo(product)));
		assertThat(result.getItems().get(0).getQuantity(), equalTo(quantity));
		assertThat(result.getItems().get(0).getNet(), is(equalTo(money)));
	}

}
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

}
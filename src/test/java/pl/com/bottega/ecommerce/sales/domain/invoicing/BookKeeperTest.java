package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

/**
 * Created by Wojciech Szczepaniak on 27.03.2017.
 */
public class BookKeeperTest {

    private BookKeeper bookKeeper;
    private TaxPolicy taxPolicy;
    private InvoiceRequest invoiceRequest;

    @Before
    public void init() {
        bookKeeper = new BookKeeper(new InvoiceFactory());

        taxPolicy = mock(TaxPolicy.class);
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class)))
                .thenReturn(new Tax(Money.ZERO, "test"));

        invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Test"));
    }

    @Test
    public void requestForAnInvoiceWithOneElementReturnInvoiceContainsOneElement() {
        // given
        ProductData productData = mock(ProductData.class);

        // when
        invoiceRequest.add(new RequestItem(productData, 1, Money.ZERO));
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        // then
        Assert.assertThat(invoice.getItems().size(), is(1));
    }
}

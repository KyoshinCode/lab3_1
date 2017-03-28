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

    @Test
    public void requestForAnInvoiceWithTwoElementsInvokeCalculateTaxMethodTwoTimes() {
        // given
        ProductData productData1 = mock(ProductData.class),
                    productData2 = mock(ProductData.class);

        // when
        invoiceRequest.add(new RequestItem(productData1, 1, Money.ZERO));
        invoiceRequest.add(new RequestItem(productData2, 1, Money.ZERO));
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        // then
        verify(taxPolicy, times(2)).calculateTax(any(ProductType.class), any(Money.class));
    }

    @Test
    public void requestForAnInvoiceWithoutElementsReturnInvoiceContainsZeroElements() {
        // given

        // when
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        // than
        Assert.assertThat(invoice.getItems().size(), is(0));
    }

    @Test
    public void requestForAnInvoiceWithoutElementsNeverInvokeCalculateTaxMethod() {
        // given

        // when
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        // than
        verify(taxPolicy, never()).calculateTax(any(ProductType.class), any(Money.class));
    }
}

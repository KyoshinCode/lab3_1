import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.sales.domain.invoicing.BookKeeper;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceFactory;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Tax;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TaxPolicy;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.mockito.Mockito.*;

/**
 * Created by Wojciech Szczepaniak on 27.03.2017.
 */
public class BookKeeperTest {

    private BookKeeper bookKeeper;

    @Before
    public void init() {
        bookKeeper = new BookKeeper(new InvoiceFactory());
    }

    @Test
    public void requestForAnInvoiceWithOneElementReturnInvoiceContainsOneElement() {
        // given
        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class)))
                .thenReturn(new Tax(Money.ZERO, "test"));
    }
}

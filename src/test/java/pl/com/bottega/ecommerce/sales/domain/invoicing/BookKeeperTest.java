package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by 194974 on 3/21/2017.
 */
public class BookKeeperTest {

    private BookKeeper bookKeeper;

    @Mock
    private TaxPolicy taxPolicy;

    @Before
    public void setUp() throws Exception {
        bookKeeper = new BookKeeper(new InvoiceFactory());
    }
}
package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by Agata on 2017-06-19.
 */
public class BookKeeperTest {
    
    private BookKeeper keeper;

    @Mock
    private TaxPolicy policy;

    @Before
    public void setUp() throws Exception {
        keeper = new BookKeeper(new InvoiceFactory());
    }
}
import org.junit.Before;
import pl.com.bottega.ecommerce.sales.domain.invoicing.BookKeeper;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceFactory;

/**
 * Created by Wojciech Szczepaniak on 27.03.2017.
 */
public class BookKeeperTest {

    private BookKeeper bookKeeper;

    @Before
    public void init() {
        bookKeeper = new BookKeeper(new InvoiceFactory());
    }
}

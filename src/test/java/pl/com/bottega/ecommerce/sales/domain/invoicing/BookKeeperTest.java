package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import static org.junit.Assert.*;


public class BookKeeperTest {
	private BookKeeper bookKeeper;
	
	@Mock
	private TaxPolicy taxPolicy;
	
	@Before 
	public void setUp() {
		bookKeeper = new BookKeeper(new InvoiceFactory());
	}
}

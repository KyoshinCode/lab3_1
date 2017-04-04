package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.*;
import pl.com.bottega.ecommerce.sales.domain.client.*;
import pl.com.bottega.ecommerce.sales.domain.payment.*;
import pl.com.bottega.ecommerce.sharedkernel.Money;


public class BookKeeperTest {
	private BookKeeper bookKeeper;
	
	@Mock
	private TaxPolicy taxPolicy;
	
	@Before 
	public void setUp() {
		bookKeeper = new BookKeeper(new InvoiceFactory());
	}
}

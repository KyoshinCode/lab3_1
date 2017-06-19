package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Agata on 2017-06-19.
 */
public class BookKeeperTest {

    private BookKeeper keeper;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private TaxPolicy policy;

    @Mock
    private ProductData dummyData;

    @Before
    public void setUp() throws Exception {
        keeper = new BookKeeper(new InvoiceFactory());
    }

    @Test
    public void testIssuanceWithOneItem() throws Exception {
        Money cost = new Money(100);
        when(policy.calculateTax(dummyData.getType(), cost)).thenReturn(new Tax(new Money(20), "DummyTax"));
        InvoiceRequest request = new InvoiceRequest(new ClientData(Id.generate(), "John Doe"));
        request.add(new RequestItem(dummyData, 1, cost));

        Invoice invoice = keeper.issuance(request, policy);

        assertThat(invoice.getItems().size(), is(equalTo(1)));
    }
}
package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by 195035 on 21.03.2017.
 */
public class BookKeeperTest {

    private BookKeeper keeper;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private TaxPolicy policy;

    @Before
    public void setUp() throws Exception {
        keeper = new BookKeeper(new InvoiceFactory());
    }

    @Test
    public void testIssuance_OneInvoice() throws Exception {
        Money cost = new Money(100);

        ProductData dummyData = new ProductData(Id.generate(), new Money(10), "stuff", ProductType.STANDARD, new Date());

        when(policy.calculateTax(dummyData.getType(), cost)).thenReturn(new Tax(new Money(20), "DummyTax"));
        InvoiceRequest request = new InvoiceRequest(new ClientData(Id.generate(), "John Doe"));
        request.add(new RequestItem(dummyData, 1, cost));

        Invoice invoice = keeper.issuance(request, policy);

        assertThat(invoice.getItems().size(), is(equalTo(1)));
    }


    @Test
    public void testIssuance_TwoRequests_CheckCallCount() throws Exception {
        Money money = new Money(15);
        ProductData[] data = new ProductData[2];
        for(int i = 0; i < 2; i++) {
            data[i] = new ProductData(Id.generate(), new Money(10 * i + 5), "stuff" + Integer.toString(i), ProductType.STANDARD, new Date());
        }
        when(policy.calculateTax(ProductType.STANDARD, money)).thenReturn(new Tax(new Money(10), "dummy tax"));
        InvoiceRequest request = new InvoiceRequest(new ClientData(Id.generate(), "Jane Doe"));
        for(int i = 0; i < 2; i++) {
            request.add(new RequestItem(data[i], i + 1, money));
        }

        keeper.issuance(request, policy);

        verify(policy, times(2)).calculateTax(ProductType.STANDARD, money);
    }

    @Test
    public void testIssuance_CorrectTax() throws Exception {
        Money money = new Money(10);
        Tax tax = new Tax(new Money(2.5), "FakeTax");

        InvoiceRequest request = new InvoiceRequest(new ClientData(Id.generate(), "John Kowalski"));
        request.add(new RequestItem(new ProductData(Id.generate(), new Money(15), "stuff", ProductType.DRUG, new Date()) , 2, money));

        when(policy.calculateTax(ProductType.DRUG, money)).thenReturn(tax);

        Invoice invoice = keeper.issuance(request, policy);

        assertThat(invoice.getItems().get(0).getTax(), is(equalTo(tax)));
    }
}
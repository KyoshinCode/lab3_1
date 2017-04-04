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
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import java.util.Date;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by pebuls on 21.03.17.
 */
public class BookKeeperTest {

    private BookKeeper bookKeeper;
    private InvoiceRequest invoiceRequest;
    private Money money;
    private ProductData productData;
    private Invoice invoice;
    private ClientData clientData;

    @Mock
    private TaxPolicy mockTaxPolicy;

    @Mock
    private InvoiceFactory mockInvoiceFactory;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void beforeRun() throws Exception    {

        bookKeeper = new BookKeeper(new InvoiceFactory());
        clientData = (new ClientData(Id.generate(), "Name"));
        invoiceRequest = new InvoiceRequest(clientData);
        money = new Money(25,"PLN");
        productData = new ProductData(Id.generate(), money, "productName", ProductType.DRUG, new Date());

    }

    @Test
    public void demandInvoiceWithOnePositionReturnInvoiceWithOnePosition()  {

        when(mockTaxPolicy.calculateTax(productData.getType(), money)).thenReturn(new Tax(new Money(5, "PLN"), "Invalid tax"));

        invoiceRequest.add(new RequestItem(productData, 1, money));

        invoice = bookKeeper.issuance(invoiceRequest, mockTaxPolicy);

        assertThat(invoice.getItems().size(), is(1));
    }

    @Test
    public void demandInvoiceWithTwoPositionCallCalculateTaxMethodTwice()   {

        when(mockTaxPolicy.calculateTax(productData.getType(), money)).thenReturn(new Tax(new Money(5, "PLN"), "Invalid tax"));

        invoiceRequest.add(new RequestItem(productData, 1, money));
        invoiceRequest.add(new RequestItem(productData, 1, money));

        bookKeeper.issuance(invoiceRequest, mockTaxPolicy);

        verify(mockTaxPolicy, times(2)).calculateTax(productData.getType(),money);
    }

    @Test
    public void demandInvoiceWithZeroPositionReturnInvoiceWithZeroPositionAndNotCallCalculateTaxMethod()  {

        when(mockTaxPolicy.calculateTax(productData.getType(), money)).thenReturn(new Tax(new Money(5, "PLN"), "Invalid tax"));

        invoice = bookKeeper.issuance(invoiceRequest, mockTaxPolicy);

        assertThat(invoice.getItems().size(), is(0));
        verify(mockTaxPolicy, times(0)).calculateTax(productData.getType(),money);
    }

    @Test
    public void demandInvoiceReturnClientDataOnInvoiceAndCallCreateOnce()  {

        bookKeeper = new BookKeeper(mockInvoiceFactory);

        productData = new ProductData(Id.generate(), money, "productName", ProductType.DRUG, new Date());

        when(mockInvoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));

        invoice = bookKeeper.issuance(invoiceRequest, mockTaxPolicy);

        verify(mockInvoiceFactory, times(1)).create(clientData);
        assertThat(invoice.getClient().getName(), is("Name"));
    }
}
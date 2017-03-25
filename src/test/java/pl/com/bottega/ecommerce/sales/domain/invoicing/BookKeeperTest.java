package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by Piotrek on 25.03.2017.
 */
public class BookKeeperTest {

    private InvoiceFactory invoiceFactory;
    private InvoiceRequest invoiceRequest;
    private RequestItem requestItem;
    private ArrayList<RequestItem> requestItemArrayList;

    @Before
    public void setUp() throws Exception{
        invoiceRequest = Mockito.mock(InvoiceRequest.class);
        Mockito.when(invoiceRequest.getClientData()).thenReturn(new ClientData(Id.generate(), "TEST_USER"));
        requestItemArrayList = new ArrayList<>();

        ProductData productData = Mockito.mock(ProductData.class);
        Mockito.when(productData.getType()).thenReturn(ProductType.STANDARD);

        requestItem = Mockito.mock(RequestItem.class);
        Mockito.when(requestItem.getProductData()).thenReturn(productData);
        Mockito.when(requestItem.getQuantity()).thenReturn(1);
        Mockito.when(requestItem.getTotalCost()).thenReturn(new Money(200));

        Mockito.when(invoiceRequest.getItems()).thenReturn(requestItemArrayList);

        ClientData clientData = invoiceRequest.getClientData();

        invoiceFactory = Mockito.mock(InvoiceFactory.class);
        Mockito.when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
    }

    @Test
    public void issuanceTestWithOnlyOneItem(){
        requestItemArrayList.add(requestItem);

        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(10), "TAX");
            }
        };

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        Assert.assertThat(invoice.getItems().size(), is(equalTo(1)));
    }
}
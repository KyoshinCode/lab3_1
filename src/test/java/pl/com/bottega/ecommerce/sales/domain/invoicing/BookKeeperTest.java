package pl.com.bottega.ecommerce.sales.domain.invoicing;

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
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Konrad Gos on 21.03.2017.
 */
public class BookKeeperTest {

    private InvoiceRequest invoiceRequest;
    private ArrayList<RequestItem> requestItemList;
    private RequestItem requestItem;
    private ProductData productData;
    private InvoiceFactory invoiceFactory;
    private BookKeeper bookKeeper;
    private TaxPolicy taxPolicy;
    
    @Before
    public void setUp() {
        requestItemList = new ArrayList<>();

        invoiceRequest = Mockito.mock(InvoiceRequest.class);
        Mockito.when(invoiceRequest.getClientData()).thenReturn(new ClientData(Id.generate(), "Konrad"));

        productData = Mockito.mock(ProductData.class);
        Mockito.when(productData.getType()).thenReturn(ProductType.FOOD);

        requestItem = Mockito.mock(RequestItem.class);
        Mockito.when(requestItem.getProductData()).thenReturn(productData);
        Mockito.when(requestItem.getQuantity()).thenReturn(1);
        Mockito.when(requestItem.getTotalCost()).thenReturn(new Money(145));

        Mockito.when(invoiceRequest.getItems()).thenReturn(requestItemList);

        ClientData clientData = invoiceRequest.getClientData();

        invoiceFactory = Mockito.mock(InvoiceFactory.class);
        Mockito.when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
    }

    @Test
    public void requestForOneInvoice() {
        requestItemList.add(requestItem);
        bookKeeper = new BookKeeper(invoiceFactory);
        taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(0.23), "VAT");
            }
        };

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertThat(invoice.getItems().size(), is(equalTo(1)));
    }

}
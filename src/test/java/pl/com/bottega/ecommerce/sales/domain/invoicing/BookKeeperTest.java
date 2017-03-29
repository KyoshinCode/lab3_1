package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;

import static org.mockito.Mockito.*;

/**
 * Created by grusz on 24.03.2017.
 */
public class BookKeeperTest {

    private InvoiceRequest invoiceRequest;
    private ArrayList<RequestItem> requestItems;
    private RequestItem requestItem;
    private InvoiceFactory invoiceFactory;
    private ProductData productData;

    @Before
    public void setUp() throws Exception {

        invoiceRequest = mock(InvoiceRequest.class);
        when(invoiceRequest.getClientData()).thenReturn(new ClientData(Id.generate(), "Andrew"));
        requestItems = new ArrayList<>();
        productData = mock(ProductData.class);
        when(productData.getType()).thenReturn(ProductType.FOOD);
        requestItem = mock(RequestItem.class);
        when(requestItem.getTotalCost()).thenReturn(new Money(100));
        when(requestItem.getProductData()).thenReturn(productData);
        when(requestItem.getQuantity()).thenReturn(1);
        when(invoiceRequest.getItems()).thenReturn(requestItems);
        ClientData clientData = invoiceRequest.getClientData();
        invoiceFactory = mock(InvoiceFactory.class);
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
    }

    @Test
    public void issueInvoice_onePosition_checkPositionsAmountOnInvoice() {

        requestItems.add(requestItem);

        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(0), "no tax");
            }
        };

        Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);
        Assert.assertThat(result.getItems().size(), is(equalTo(1)));


    }

    @Test
    public void issueInvoice_TwoPositions_callCountForTaxPolicy(){
        requestItems.add(requestItem);
        requestItems.add(requestItem);

        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(0), "no tax");
            }
        };
        TaxPolicy spy = spy(taxPolicy);
        Invoice result = bookKeeper.issuance(invoiceRequest, spy);
        verify(spy, times(2)).calculateTax(productData.getType(),requestItem.getTotalCost());
    }

    @Test
    public void issueInvoice_NoPositions_callCountForTaxPolicy(){

        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(0), "no tax");
            }
        };
        TaxPolicy spy = spy(taxPolicy);
        Invoice result = bookKeeper.issuance(invoiceRequest, spy);
        verify(spy, times(0)).calculateTax(productData.getType(),requestItem.getTotalCost());
    }

    @Test public void issueInvoice_NoPositions_checkPositionsAmountOnInvoice() {

        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        TaxPolicy taxPolicy = new TaxPolicy() {
            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(0), "no tax");
            }
        };

        Invoice result = bookKeeper.issuance(invoiceRequest, taxPolicy);
        Assert.assertThat(result.getItems().size(), is(equalTo(0)));


    }


}

package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationBuilder;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.sharedkernel.exceptions.DomainOperationException.DomainOperationException;
import pl.com.bottega.ecommerce.system.application.SystemContext;


import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import static org.mockito.Mockito.*;

/**
 * Created by grusz on 25.03.2017.
 */
public class AddProductCommandHandlerTest {


    private ProductRepository productRepository;
    private ReservationRepository reservationRepository;
    private SuggestionService suggestionService;
    private Id id;
    private Reservation spyReservation;
    private AddProductCommand addProductCommand;
    private AddProductCommandHandler addProductCommandHandler;
    private Product product;
    private Product equivalentProduct;
    private Client client;
    private SystemContext systemContext;
    private ClientRepository clientRepository;

    @Before
    public void setUp() throws Exception {

        //Setting mocks
        productRepository = mock(ProductRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        suggestionService = mock(SuggestionService.class);
        clientRepository = mock(ClientRepository.class);
        //Fakes initialization
        id = new Id("1");

        product =  ProductBuilder.
                aProduct()
                .withName("product")
                .build();

        equivalentProduct = ProductBuilder
                .aProduct()
                .withName("eqproduct")
                .build();

        Reservation reservation = ReservationBuilder
                .aReservation()
                .withStatus(Reservation.ReservationStatus.OPENED)
                .build();

        spyReservation = spy(reservation);
        client = new Client();
        systemContext = new SystemContext();
        //Tested class initialization
        addProductCommand = new AddProductCommand(id, id, 1);
        addProductCommandHandler = new AddProductCommandHandler();
        //Setting stubs behavior
        when(clientRepository.load(id)).thenReturn(client);
        when(reservationRepository.load(addProductCommand.getOrderId())).thenReturn(spyReservation);
        when(productRepository.load(addProductCommand.getProductId())).thenReturn(product);
        when(suggestionService.suggestEquivalent(product, client)).thenReturn(equivalentProduct);
        //Setting private fields of tested class to use stubs
        Whitebox.setInternalState(addProductCommandHandler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(addProductCommandHandler, "productRepository", productRepository);
        Whitebox.setInternalState(addProductCommandHandler, "suggestionService", suggestionService);
        Whitebox.setInternalState(addProductCommandHandler, "clientRepository", clientRepository);
        Whitebox.setInternalState(addProductCommandHandler, "systemContext", systemContext);



    }

    @Test
    public void addProduct_checkIfReservationSaveMethodHasBeenCalled() {

        addProductCommandHandler.handle(addProductCommand);
        verify(reservationRepository).save(spyReservation);

    }

    @Test
    public void addProduct_checkIfReservationProductAddMethodHasBeenCalled() {

        addProductCommandHandler.handle(addProductCommand);
        verify(spyReservation).add(product, 1);

    }

    @Test
    public void addProduct_checkIfEquivalentProductHasBeenAddedUponPrimaryProductUnavailability() {

        product.markAsRemoved();
        assertThat(product.isAvailable(), is(equalTo(false)));
        addProductCommandHandler.handle(addProductCommand);
        verify(spyReservation).add(equivalentProduct, 1);
    }

    @Test(expected = DomainOperationException.class)
    public void addProduct_reservationHasBeenPreviouslyClosed() {

        spyReservation.close();
        addProductCommandHandler.handle(addProductCommand);
    }

}

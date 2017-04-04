package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;;
import org.mockito.internal.util.reflection.Whitebox;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


public class AddProductCommandHandlerTest {
    private ReservationRepository reservationRepository;
    private ProductRepository productRepository;
    private SuggestionService suggestionService;
    private ClientRepository clientRepository;
    private AddProductCommandHandler addProductCommandHandler;
    private SystemContext systemContext;
    private AddProductCommand addProductCommand;
    private Reservation reservation;
    private Product product;
    @Before
    public void setUp() throws Exception {
        reservationRepository = mock(ReservationRepository.class);
        productRepository = mock(ProductRepository.class);
        suggestionService = mock(SuggestionService.class);
        clientRepository = mock(ClientRepository.class);
        addProductCommandHandler = new AddProductCommandHandler();
        addProductCommand = new AddProductCommand(Id.generate(), Id.generate(), 1);
        reservation = new ReservationBuilder().reservationStatus(Reservation.ReservationStatus.OPENED).clientData(new ClientData(Id.generate(), "Andrzej Kmicic")).date(new Date()).build();
        //(Id.generate(), Reservation.ReservationStatus.OPENED, new ClientData(Id.generate(), "Julian Ochocki"), new Date());
        product = new ProductBuilder().money(new Money(100)).name("serniczek").productType(ProductType.FOOD).build();
        //(Id.generate(), new Money(100), "serniczek", ProductType.FOOD);

        systemContext = new SystemContext();
        when(reservationRepository.load(addProductCommand.getOrderId())).thenReturn(reservation);

        Whitebox.setInternalState(addProductCommandHandler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(addProductCommandHandler, "productRepository", productRepository);
        Whitebox.setInternalState(addProductCommandHandler, "suggestionService", suggestionService);
        Whitebox.setInternalState(addProductCommandHandler, "clientRepository", clientRepository);
        Whitebox.setInternalState(addProductCommandHandler, "systemContext", systemContext);
    }

    @Test
    public void testProductIsAvailable() throws Exception {
        when(productRepository.load(any(Id.class))).thenReturn(product);
        addProductCommandHandler.handle(addProductCommand);
        assertThat(product.isAvailable(), is(equalTo(true)));
    }
    @Test
    public void testReservationIsClosed() throws Exception {
        reservation.close();
        assertThat(reservation.isClosed() ,is(true));
    }
    @Test
    public void testProductReservation() throws Exception {
        when(reservationRepository.load(addProductCommand.getOrderId())).thenReturn(reservation);
        when(productRepository.load(addProductCommand.getProductId())).thenReturn(product);
        addProductCommandHandler.handle(addProductCommand);
        verify(reservationRepository, times(1)).load(addProductCommand.getOrderId());
    }
    @Test
    public void testWithoutClient() throws Exception {
        when(reservationRepository.load(addProductCommand.getOrderId())).thenReturn(reservation);
        when(productRepository.load(addProductCommand.getProductId())).thenReturn(product);

        addProductCommandHandler.handle(addProductCommand);

        verify(clientRepository, times(0)).load(reservation.getClientData().getAggregateId());
    }

}

package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Sasho on 2017-04-02.
 */
public class AddProductCommandHandlerTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SuggestionService suggestionService;

    @Mock
    private ClientRepository clientRepository;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private AddProductCommandHandler handler;

    private AddProductCommand command;

    @Before
    public void setUp() throws Exception {
        handler = new AddProductCommandHandler();
        SystemContext systemContext = new SystemContext();
        Whitebox.setInternalState(handler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(handler, "productRepository", productRepository);
        Whitebox.setInternalState(handler, "suggestionService", suggestionService);
        Whitebox.setInternalState(handler, "clientRepository", clientRepository);
        Whitebox.setInternalState(handler, "systemContext", systemContext);
        command = new AddProductCommand(Id.generate(), Id.generate(), 1);
    }

    @Test
    public void testHandler_productAvailable_NoClientLoad() throws Exception {
        Reservation reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED, new ClientData(Id.generate(), "Pusia"), new Date());
        Product product = new Product(Id.generate(), new Money(10), "ciasto z buraka", ProductType.FOOD);

        when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
        when(productRepository.load(command.getProductId())).thenReturn(product);

        handler.handle(command);

        verify(clientRepository, never()).load(reservation.getClientData().getAggregateId());
    }

    @Test
    public void testHandler_reservationRepositoryLoadOnce() throws Exception {
        Reservation reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED, new ClientData(Id.generate(), "Pimpek"), new Date());
        Product product = new Product(Id.generate(), new Money(12, "PLN"), "wianek", ProductType.STANDARD);

        when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
        when(productRepository.load(command.getProductId())).thenReturn(product);

        handler.handle(command);

        verify(reservationRepository, times(1)).load(command.getOrderId());
    }
}
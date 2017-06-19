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
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Agata on 2017-06-19.
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
    @Mock
    private SystemContext systemContext;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private AddProductCommandHandler handler;

    private AddProductCommand command;

    @Before
    public void setUp() throws Exception {
        handler = new AddProductCommandHandler();
        systemContext = new SystemContext();
        Whitebox.setInternalState(handler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(handler, "productRepository", productRepository);
        Whitebox.setInternalState(handler, "suggestionService", suggestionService);
        Whitebox.setInternalState(handler, "clientRepository", clientRepository);
        Whitebox.setInternalState(handler, "systemContext", systemContext);

        command = new AddProductCommand(Id.generate(), Id.generate(), 5);
    }

    @Test
    public void testHandleWithCheckReservationRepositoryWithAggregateIdOrWithClient() throws Exception {
        Reservation reservation = new ReservationBuilder().withClient(new ClientData(Id.generate(), "dummy")).opened().build();
        Product product = new ProductBuilder().withAggregateId(command.getProductId()).build();

        when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
        when(productRepository.load(command.getProductId())).thenReturn(product);

        handler.handle(command);

        verify(reservationRepository, times(1)).load(command.getOrderId());
    }

    @Test
    public void testHandleWithNoClientRepositoryWithProductAvailable() throws Exception {
        Reservation reservation = new ReservationBuilder().withClient(new ClientData(Id.generate(), "dummy")).opened().build();
        Product product = new ProductBuilder().withAggregateId(command.getProductId()).build();

        when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
        when(productRepository.load(command.getProductId())).thenReturn(product);

        handler.handle(command);

        verify(clientRepository, never()).load(reservation.getClientData().getAggregateId());
    }

    @Test
    public void testHandleWithCheckSuggestionServiceWithProductUnavailable() throws Exception {
        Reservation reservation = new ReservationBuilder().withClient(new ClientData(Id.generate(), "dummy")).opened().build();
        Product product = new ProductBuilder().withAggregateId(command.getProductId()).unavailable().build();

        when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
        when(productRepository.load(command.getProductId())).thenReturn(product);

        Client client = new Client();

        when(clientRepository.load(systemContext.getSystemUser().getClientId())).thenReturn(client);
        when(suggestionService.suggestEquivalent(product, client)).thenReturn(new ProductBuilder().build());

        handler.handle(command);

        verify(suggestionService, times(1)).suggestEquivalent(product, client);
    }

    @Test
    public void testHandleWithCheckProductAddedToReservation() throws Exception {
        Reservation reservation = new ReservationBuilder().withClient(new ClientData(Id.generate(), "dummy")).opened().build();
        Product product = new ProductBuilder().withAggregateId(command.getProductId()).build();

        when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
        when(productRepository.load(command.getProductId())).thenReturn(product);

        handler.handle(command);

        assertThat(reservation.contains(product), is(true));
    }
}
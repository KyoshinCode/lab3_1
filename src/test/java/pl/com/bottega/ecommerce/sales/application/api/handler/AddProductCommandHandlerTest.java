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

import static org.mockito.Mockito.*;


/**
 * Created by pebuls on 21.03.17.
 */
public class AddProductCommandHandlerTest {

    private AddProductCommandHandler handler;
    private SystemContext systemContext;
    private AddProductCommand command;
    private Reservation reservation;
    private Product product;
    private Client client;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private SuggestionService suggestionService;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void beforeRun() throws Exception    {

        handler = new AddProductCommandHandler();
        systemContext = new SystemContext();

        Whitebox.setInternalState(handler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(handler, "productRepository", productRepository);
        Whitebox.setInternalState(handler, "clientRepository", clientRepository);
        Whitebox.setInternalState(handler, "suggestionService", suggestionService);
        Whitebox.setInternalState(handler, "systemContext", systemContext);

        command = new AddProductCommand(Id.generate(), Id.generate(), 1);
    }

    @Test
    public void CheckIfReservationRepositoryCalledProper() throws Exception {

        reservation = new ReservationBuilder().withClient(new ClientData(Id.generate(), "name")).opened().build();
        product = new ProductBuilder().withAggregateId(command.getProductId()).build();

        when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
        when(productRepository.load(command.getProductId())).thenReturn(product);

        verify(reservationRepository, times(1)).load(command.getOrderId());
    }

    @Test
    public void CheckIfClientRepositoryNotCalledWhenProductIsAvailable() throws Exception {

        reservation = new ReservationBuilder().withClient(new ClientData(Id.generate(), "name")).opened().build();
        product = new ProductBuilder().withAggregateId(command.getProductId()).build();

        when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
        when(productRepository.load(command.getProductId())).thenReturn(product);

        handler.handle(command);

        verify(clientRepository, never()).load(reservation.getClientData().getAggregateId());
    }

    @Test
    public void CheckIfSuggestionServiceCalledWhenProductIsUnavailable() throws Exception {

        reservation = new ReservationBuilder().withClient(new ClientData(Id.generate(), "name")).opened().build();
        product = new ProductBuilder().withAggregateId(command.getProductId()).unavailable().build();

        when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
        when(productRepository.load(command.getProductId())).thenReturn(product);

        client = new Client();

        when(clientRepository.load(systemContext.getSystemUser().getClientId())).thenReturn(client);
        when(suggestionService.suggestEquivalent(product, client)).thenReturn(new ProductBuilder().build());

        handler.handle(command);

        verify(suggestionService, times(1)).suggestEquivalent(product, client);
    }



}
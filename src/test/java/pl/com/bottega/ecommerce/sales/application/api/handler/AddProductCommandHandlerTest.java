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
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import java.util.Date;

import static org.hamcrest.Matchers.is;
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

    private SystemContext systemContext;

    @Before
    public void setUp() throws Exception {
        handler = new AddProductCommandHandler();
        systemContext = new SystemContext();
        Whitebox.setInternalState(handler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(handler, "productRepository", productRepository);
        Whitebox.setInternalState(handler, "suggestionService", suggestionService);
        Whitebox.setInternalState(handler, "clientRepository", clientRepository);
        Whitebox.setInternalState(handler, "systemContext", systemContext);
        command = new AddProductCommand(Id.generate(), Id.generate(), 1);
    }

    @Test
    public void testHandler_productAvailable_NoClientLoad() throws Exception {
        Reservation reservation = new ReservationBuilder().setClient(new ClientData(Id.generate(), "Pusia")).opened().build();
        Product product = new ProductBuilder().setId(command.getProductId()).setName("ciasto z buraka").build();

        when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
        when(productRepository.load(command.getProductId())).thenReturn(product);

        handler.handle(command);

        verify(clientRepository, never()).load(reservation.getClientData().getAggregateId());
    }

    @Test
    public void testHandler_reservationRepositoryLoadOnce() throws Exception {
        Reservation reservation = new ReservationBuilder().setClient(new ClientData(Id.generate(), "Pimpek")).opened().build();
        Product product = new ProductBuilder().setId(command.getProductId()).setName("wianek").setType(ProductType.STANDARD).build();

        when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
        when(productRepository.load(command.getProductId())).thenReturn(product);

        handler.handle(command);

        verify(reservationRepository, times(1)).load(command.getOrderId());
    }

    @Test
    public void testHandler_productReservation() throws Exception {
        Reservation reservation = new ReservationBuilder().setClient(new ClientData(Id.generate(), "Bzyczek")).opened().build();
        Product product = new ProductBuilder().setId(command.getProductId()).setName("Audi A8").setType(ProductType.STANDARD).build();

        when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
        when(productRepository.load(command.getProductId())).thenReturn(product);

        handler.handle(command);

        assertThat(reservation.contains(product), is(true));
    }

    @Test
    public void testHandler_suggestEquivalent() throws Exception {
        Reservation reservation = new ReservationBuilder().setClient(new ClientData(Id.generate(), "Lawenda")).opened().build();
        Product product = new ProductBuilder().setId(command.getProductId()).setName("Skrzyp polny").setType(ProductType.DRUG).unavailable().build();

        when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
        when(productRepository.load(command.getProductId())).thenReturn(product);

        Client client = new Client();

        when(clientRepository.load(systemContext.getSystemUser().getClientId())).thenReturn(client);
        when(suggestionService.suggestEquivalent(product, client)).thenReturn(new ProductBuilder().setId(command.getProductId()).setName("Bratek").setType(ProductType.DRUG).build());

        handler.handle(command);

        verify(suggestionService, times(1)).suggestEquivalent(product, client);
    }

    @Test
    public void testHandle_reserveSuggestedProduct() throws Exception {
        Reservation reservation = new ReservationBuilder().setClient(new ClientData(Id.generate(), "Anna")).opened().build();
        Product product = new ProductBuilder().setId(command.getProductId()).setName("Czolenka czerwone Badura").setType(ProductType.STANDARD).unavailable().build();

        when(reservationRepository.load(command.getOrderId())).thenReturn(reservation);
        when(productRepository.load(command.getProductId())).thenReturn(product);

        Client client = new Client();
        Product productAvailable = new ProductBuilder().setId(command.getProductId()).setName("Czolenka czerwone Lasocki").setType(ProductType.STANDARD).build();

        when(clientRepository.load(systemContext.getSystemUser().getClientId())).thenReturn(client);
        when(suggestionService.suggestEquivalent(product, client)).thenReturn(productAvailable);

        handler.handle(command);

        assertThat(reservation.contains(product), is(false));
        assertThat(reservation.contains(productAvailable), is(true));
    }
}

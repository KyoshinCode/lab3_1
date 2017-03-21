package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by 195035 on 21.03.2017.
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
        Whitebox.setInternalState(handler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(handler, "productRepository", productRepository);
        Whitebox.setInternalState(handler, "suggestionService", suggestionService);
        Whitebox.setInternalState(handler, "clientRepository", clientRepository);
        Whitebox.setInternalState(handler, "systemContext", systemContext);

        command = new AddProductCommand(Id.generate(), Id.generate(), 5);
    }

    @Test
    public void testHandle_CheckReservationRepositoryCalledCorrectly() throws Exception {
        when(reservationRepository.load(command.getOrderId())).thenReturn(new Reservation());
        when(productRepository.load(command.getProductId())).thenReturn(new Product());

        handler.handle(command);
    }
}
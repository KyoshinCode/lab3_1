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

import static org.mockito.Mockito.when;


/**
 * Created by pebuls on 21.03.17.
 */
public class AddProductCommandHandlerTest {

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

    private AddProductCommandHandler handler;
    private SystemContext systemContext;
    private AddProductCommand command;

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

        when(reservationRepository.load(command.getOrderId())).thenReturn(new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED, new ClientData(Id.generate(), "Name"), new Date()));
        when(productRepository.load(command.getProductId())).thenReturn(new Product(Id.generate(), new Money(10), "Product name", ProductType.FOOD));

        handler.handle(command);

    }

}
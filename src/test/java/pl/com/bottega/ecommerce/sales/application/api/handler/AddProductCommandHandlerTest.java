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
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Konrad Gos on 02.04.2017.
 */
public class AddProductCommandHandlerTest {

    private ProductRepository productRepository;
    private ReservationRepository reservationRepository;
    private SuggestionService suggestionService;
    private ClientRepository clientRepository;

    private Id id;
    private Reservation reservation;
    private AddProductCommand addProductCommand;
    private AddProductCommandHandler addProductCommandHandler;

    @Before
    public void setUp() throws Exception {

        productRepository = mock(ProductRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        suggestionService = mock(SuggestionService.class);
        clientRepository = mock(ClientRepository.class);

        id = Id.generate();

        addProductCommand = new AddProductCommand(id,id,1);
        addProductCommandHandler = new AddProductCommandHandler();

        ClientData clientData = new ClientData(id, "Adam");
        Product product = new Product(id,new Money(15),"Hamburger", ProductType.FOOD);
        reservation = new Reservation(id, Reservation.ReservationStatus.OPENED,clientData,new Date());
        Client client = new Client();
        SystemContext systemContext = new SystemContext();

        when(clientRepository.load(id)).thenReturn(client);
        when(reservationRepository.load(addProductCommand.getOrderId())).thenReturn(reservation);
        when(productRepository.load(addProductCommand.getProductId())).thenReturn(product);
        when(suggestionService.suggestEquivalent(product,client)).thenReturn(product);

        Whitebox.setInternalState(addProductCommandHandler,"reservationRepository",reservationRepository);
        Whitebox.setInternalState(addProductCommandHandler,"productRepository",productRepository);
        Whitebox.setInternalState(addProductCommandHandler,"suggestionService",suggestionService);
        Whitebox.setInternalState(addProductCommandHandler,"clientRepository",clientRepository);
        Whitebox.setInternalState(addProductCommandHandler,"systemContext",systemContext);
    }

    @Test
    public void checkIfReservationSaveMethodInHandleHasBeenCalled(){
        addProductCommandHandler.handle(addProductCommand);
        verify(reservationRepository).save(reservation);
    }

}
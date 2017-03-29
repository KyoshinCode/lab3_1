package pl.com.bottega.ecommerce.sales.application.api.handler;

import com.sun.xml.internal.bind.v2.model.core.ID;
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
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationItem;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.sharedkernel.exceptions.DomainOperationException.DomainOperationException;
import pl.com.bottega.ecommerce.system.application.SystemContext;
import pl.com.bottega.ecommerce.system.application.SystemUser;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

/**
 * Created by grusz on 25.03.2017.
 */
public class AddProductCommandHandlerTest {


    private ProductRepository productRepository;
    private ReservationRepository reservationRepository;
    private SuggestionService suggestionService;
    private Id id;
    private Reservation reservation;
    private AddProductCommand addProductCommand;
    private AddProductCommandHandler addProductCommandHandler;
    private Product product;
    private Product equivalentProduct;

    @Before
    public void setUp() throws Exception {

        productRepository = mock(ProductRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        suggestionService = mock(SuggestionService.class);
        ClientRepository clientRepository = mock(ClientRepository.class);
        id = new Id("1");
        addProductCommand = new AddProductCommand(id,id,1);
        addProductCommandHandler = new AddProductCommandHandler();
        ClientData clientData = new ClientData(Id.generate(), "Andrew");
        product = new Product(id,new Money(100),"product", ProductType.FOOD);
        equivalentProduct = new Product(id,new Money(100),"eqproduct",ProductType.FOOD);
        reservation = spy(new Reservation(new Id("2"), Reservation.ReservationStatus.OPENED,clientData,new Date()));
        Client client = new Client();
        SystemContext systemContext = new SystemContext();
        when(clientRepository.load(id)).thenReturn(client);
        when(reservationRepository.load(addProductCommand.getOrderId())).thenReturn(reservation);
        when(productRepository.load(addProductCommand.getProductId())).thenReturn(product);
        when(suggestionService.suggestEquivalent(product,client)).thenReturn(equivalentProduct);
        Whitebox.setInternalState(addProductCommandHandler,"reservationRepository",reservationRepository);
        Whitebox.setInternalState(addProductCommandHandler,"productRepository",productRepository);
        Whitebox.setInternalState(addProductCommandHandler,"suggestionService",suggestionService);
        Whitebox.setInternalState(addProductCommandHandler,"clientRepository",clientRepository);
        Whitebox.setInternalState(addProductCommandHandler,"systemContext",systemContext);



    }

    @Test public void addProduct_checkIfReservationSaveMethodHasBeenCalled(){

        addProductCommandHandler.handle(addProductCommand);
        verify(reservationRepository).save(reservation);

    }

    @Test public void addProduct_checkIfReservationProductAddMethodHasBeenCalled(){

        addProductCommandHandler.handle(addProductCommand);
        verify(reservation).add(product,1);

    }

    @Test public void addProduct_checkIfEquivalentProductHasBeenAddedUponPrimaryProductUnavailability(){

        product.markAsRemoved();
        assertThat(product.isAvailable(),is(equalTo(false)));
        addProductCommandHandler.handle(addProductCommand);
        verify(reservation).add(equivalentProduct,1);
    }

    @Test (expected = DomainOperationException.class)
    public void addProduct_reservationHasBeenPreviouslyClosed(){

        reservation.close();
        addProductCommandHandler.handle(addProductCommand);
    }

}

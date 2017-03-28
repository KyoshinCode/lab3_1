package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;

import static org.mockito.Mockito.mock;

/**
 * Created by Wojciech Szczepaniak on 28.03.2017.
 */
public class AddProductCommandHandlerTest {

    private AddProductCommandHandler addProductCommandHandler;
    private AddProductCommand addProductCommand;

    @Before
    public void init() {
        addProductCommandHandler = new AddProductCommandHandler();

        addProductCommand = mock(AddProductCommand.class);
    }

    @Test
    public void test() {
        // given

        // when
        addProductCommandHandler.handle(addProductCommand);

        // then

    }
}

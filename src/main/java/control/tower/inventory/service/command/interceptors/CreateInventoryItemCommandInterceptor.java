package control.tower.inventory.service.command.interceptors;

import control.tower.inventory.service.command.CreateInventoryItemCommand;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

import static control.tower.inventory.service.core.utils.Helper.isNullOrBlank;

@Component
public class CreateInventoryItemCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateInventoryItemCommandInterceptor.class);

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {

            LOGGER.info("Intercepted command: " + command.getPayloadType());

            if (CreateInventoryItemCommand.class.equals(command.getPayloadType())) {

                CreateInventoryItemCommand createInventoryItemCommand = (CreateInventoryItemCommand) command.getPayload();

                if (isNullOrBlank(createInventoryItemCommand.getProductId())) {
                    throw new IllegalArgumentException("ProductId cannot be empty");
                }
            }

            return command;
        };
    }
}

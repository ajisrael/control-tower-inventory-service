package control.tower.inventory.service.command.interceptors;

import control.tower.inventory.service.command.commands.RemoveInventoryItemFromPickListCommand;
import control.tower.inventory.service.command.commands.RemovePickListCommand;
import control.tower.inventory.service.core.data.entities.InventoryItemAssignedToPickListLookupEntity;
import control.tower.inventory.service.core.data.entities.PickListLookupEntity;
import control.tower.inventory.service.core.data.repositories.PickListLookupRepository;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

import static control.tower.core.constants.LogMessages.INTERCEPTED_COMMAND;
import static control.tower.core.utils.Helper.throwExceptionIfEntityDoesNotExist;
import static control.tower.inventory.service.core.constants.ExceptionMessages.PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST;

@Component
public class RemovePickListCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemovePickListCommandInterceptor.class);

    private final PickListLookupRepository pickListLookupRepository;
    private final CommandGateway commandGateway;

    public RemovePickListCommandInterceptor(PickListLookupRepository pickListLookupRepository,
                                            CommandGateway commandGateway) {
        this.pickListLookupRepository = pickListLookupRepository;
        this.commandGateway = commandGateway;
    }

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {

            if (RemovePickListCommand.class.equals(command.getPayloadType())) {
                LOGGER.info(String.format(INTERCEPTED_COMMAND, command.getPayloadType()));

                RemovePickListCommand removePickListCommand =
                        (RemovePickListCommand) command.getPayload();

                removePickListCommand.validate();

                String pickId = removePickListCommand.getPickId();

                PickListLookupEntity pickListLookupEntity = pickListLookupRepository.findByPickId(pickId);

                throwExceptionIfEntityDoesNotExist(pickListLookupEntity,
                        String.format(PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, pickId));

                for (InventoryItemAssignedToPickListLookupEntity inventoryItemAssignedToPickListLookupEntity:
                pickListLookupEntity.getSkuList()) {
                    RemoveInventoryItemFromPickListCommand removeInventoryItemFromPickListCommand =
                            RemoveInventoryItemFromPickListCommand.builder()
                                    .pickId(pickId)
                                    .sku(inventoryItemAssignedToPickListLookupEntity.getSku())
                                    .build();

                    commandGateway.sendAndWait(removeInventoryItemFromPickListCommand);
                }
            }

            return command;
        };
    }
}

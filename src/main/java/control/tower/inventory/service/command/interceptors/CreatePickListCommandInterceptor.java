package control.tower.inventory.service.command.interceptors;

import control.tower.inventory.service.command.commands.CreatePickListCommand;
import control.tower.inventory.service.core.data.entities.InventoryItemAssignedToPickListLookupEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemAssignedToPickListLookupRepository;
import control.tower.inventory.service.core.data.InventoryItemLookupEntity;
import control.tower.inventory.service.core.data.InventoryItemLookupRepository;
import control.tower.inventory.service.core.data.entities.PickListLookupEntity;
import control.tower.inventory.service.core.data.repositories.PickListLookupRepository;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

import static control.tower.core.constants.LogMessages.INTERCEPTED_COMMAND;
import static control.tower.core.utils.Helper.throwExceptionIfEntityDoesExist;
import static control.tower.core.utils.Helper.throwExceptionIfEntityDoesNotExist;
import static control.tower.inventory.service.core.constants.ExceptionMessages.INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST;

@Component
public class CreatePickListCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePickListCommandInterceptor.class);

    private final InventoryItemLookupRepository inventoryItemLookupRepository;
    private final PickListLookupRepository pickListLookupRepository;
    private final InventoryItemAssignedToPickListLookupRepository inventoryItemAssignedToPickListLookupRepository;

    public CreatePickListCommandInterceptor(InventoryItemLookupRepository inventoryItemLookupRepository,
                                            PickListLookupRepository pickListLookupRepository,
                                            InventoryItemAssignedToPickListLookupRepository inventoryItemAssignedToPickListLookupRepository) {
        this.inventoryItemLookupRepository = inventoryItemLookupRepository;
        this.pickListLookupRepository = pickListLookupRepository;
        this.inventoryItemAssignedToPickListLookupRepository = inventoryItemAssignedToPickListLookupRepository;
    }

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {

            if (CreatePickListCommand.class.equals(command.getPayloadType())) {
                LOGGER.info(String.format(INTERCEPTED_COMMAND, command.getPayloadType()));

                CreatePickListCommand createPickListCommand = (CreatePickListCommand) command.getPayload();

                createPickListCommand.validate();

                PickListLookupEntity pickListLookupEntity = pickListLookupRepository.findByPickId(createPickListCommand.getPickId());

                throwExceptionIfEntityDoesExist(pickListLookupEntity, "Pick list should not exist");

                for (String sku : createPickListCommand.getSkuList()) {
                    InventoryItemLookupEntity inventoryItemLookupEntity = inventoryItemLookupRepository.findBySku(sku);

                    throwExceptionIfEntityDoesNotExist(inventoryItemLookupEntity,
                            String.format(INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST, sku));

                    InventoryItemAssignedToPickListLookupEntity inventoryItemAssignedToPickListLookupEntity =
                            inventoryItemAssignedToPickListLookupRepository.findBySku(sku);

                    throwExceptionIfEntityDoesExist(inventoryItemAssignedToPickListLookupEntity, "Inventory item is already assigned to a pick list");
                }
            }

            return command;
        };
    }
}

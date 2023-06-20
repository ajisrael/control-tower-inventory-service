package control.tower.inventory.service.command.interceptors;

import control.tower.inventory.service.command.commands.AddInventoryItemToPickListCommand;
import control.tower.inventory.service.core.data.entities.InventoryItemLookupEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemLookupRepository;
import control.tower.inventory.service.core.data.entities.InventoryItemAssignedToPickListLookupEntity;
import control.tower.inventory.service.core.data.entities.PickListLookupEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemAssignedToPickListLookupRepository;
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
import static control.tower.inventory.service.core.constants.ExceptionMessages.*;

@Component
public class AddInventoryItemToPickListCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddInventoryItemToPickListCommandInterceptor.class);

    private final InventoryItemLookupRepository inventoryItemLookupRepository;
    private final PickListLookupRepository pickListLookupRepository;
    private final InventoryItemAssignedToPickListLookupRepository inventoryItemAssignedToPickListLookupRepository;

    public AddInventoryItemToPickListCommandInterceptor(InventoryItemLookupRepository inventoryItemLookupRepository,
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

            if (AddInventoryItemToPickListCommand.class.equals(command.getPayloadType())) {
                LOGGER.info(String.format(INTERCEPTED_COMMAND, command.getPayloadType()));

                AddInventoryItemToPickListCommand addInventoryItemToPickListCommand =
                        (AddInventoryItemToPickListCommand) command.getPayload();

                addInventoryItemToPickListCommand.validate();

                String sku = addInventoryItemToPickListCommand.getSku();

                InventoryItemLookupEntity inventoryItemLookupEntity = inventoryItemLookupRepository.findBySku(sku);

                throwExceptionIfEntityDoesNotExist(inventoryItemLookupEntity,
                        String.format(INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST, sku));

                String pickId = addInventoryItemToPickListCommand.getPickId();

                PickListLookupEntity pickListLookupEntity = pickListLookupRepository.findByPickId(pickId);

                throwExceptionIfEntityDoesNotExist(pickListLookupEntity,
                        String.format(PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, pickId));

                if (pickListLookupEntity.isSkuInSkuList(sku)) {
                    throw new IllegalArgumentException(
                            String.format(INVENTORY_ITEM_ALREADY_ASSIGNED_TO_PICK_LIST, sku, pickId));
                }

                InventoryItemAssignedToPickListLookupEntity inventoryItemAssignedToPickListLookupEntity =
                        inventoryItemAssignedToPickListLookupRepository.findBySku(sku);

                throwExceptionIfEntityDoesExist(inventoryItemAssignedToPickListLookupEntity,
                        String.format(INVENTORY_ITEM_IS_ASSIGNED_TO_DIFFERENT_PICK_LIST, sku,
                                inventoryItemAssignedToPickListLookupEntity.getPickListLookup().getPickId()));
            }

            return command;
        };
    }
}

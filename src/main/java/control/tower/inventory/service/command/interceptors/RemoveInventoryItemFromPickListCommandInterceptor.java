package control.tower.inventory.service.command.interceptors;

import control.tower.inventory.service.command.commands.RemoveInventoryItemFromPickListCommand;
import control.tower.inventory.service.core.data.entities.InventoryItemLookupEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemLookupRepository;
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
import static control.tower.core.utils.Helper.throwExceptionIfEntityDoesNotExist;
import static control.tower.inventory.service.core.constants.ExceptionMessages.*;

@Component
public class RemoveInventoryItemFromPickListCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveInventoryItemFromPickListCommandInterceptor.class);

    private final InventoryItemLookupRepository inventoryItemLookupRepository;
    private final PickListLookupRepository pickListLookupRepository;

    public RemoveInventoryItemFromPickListCommandInterceptor(InventoryItemLookupRepository inventoryItemLookupRepository,
                                                             PickListLookupRepository pickListLookupRepository) {
        this.inventoryItemLookupRepository = inventoryItemLookupRepository;
        this.pickListLookupRepository = pickListLookupRepository;
    }

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {

            if (RemoveInventoryItemFromPickListCommand.class.equals(command.getPayloadType())) {
                LOGGER.info(String.format(INTERCEPTED_COMMAND, command.getPayloadType()));

                RemoveInventoryItemFromPickListCommand removeInventoryItemFromPickListCommand =
                        (RemoveInventoryItemFromPickListCommand) command.getPayload();

                removeInventoryItemFromPickListCommand.validate();

                String sku = removeInventoryItemFromPickListCommand.getSku();

                if (!removeInventoryItemFromPickListCommand.isIgnoreInventoryItemValidation()) {
                    InventoryItemLookupEntity inventoryItemLookupEntity = inventoryItemLookupRepository.findBySku(sku);

                    throwExceptionIfEntityDoesNotExist(inventoryItemLookupEntity,
                            String.format(INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST, sku));
                }

                String pickId = removeInventoryItemFromPickListCommand.getPickId();

                PickListLookupEntity pickListLookupEntity = pickListLookupRepository.findByPickId(pickId);

                throwExceptionIfEntityDoesNotExist(pickListLookupEntity,
                        String.format(PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, pickId));

                if (!pickListLookupEntity.isSkuInSkuList(sku)) {
                    throw new IllegalArgumentException(String.format(SKU_IS_NOT_IN_PICK_LIST, sku, pickId));
                }
            }

            return command;
        };
    }
}

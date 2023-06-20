package control.tower.inventory.service.command.interceptors;

import control.tower.inventory.service.command.commands.CompletePickListCommand;
import control.tower.inventory.service.core.data.entities.PickItemLookupEntity;
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
import static control.tower.inventory.service.core.constants.ExceptionMessages.AT_LEAST_ONE_SKU_IS_NOT_PICKED_CANNOT_COMPLETE_PICK_LIST;
import static control.tower.inventory.service.core.constants.ExceptionMessages.PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST;

@Component
public class CompletePickListCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompletePickListCommandInterceptor.class);

    private final PickListLookupRepository pickListLookupRepository;

    public CompletePickListCommandInterceptor(PickListLookupRepository pickListLookupRepository) {
        this.pickListLookupRepository = pickListLookupRepository;
    }

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {

            if (CompletePickListCommand.class.equals(command.getPayloadType())) {
                LOGGER.info(String.format(INTERCEPTED_COMMAND, command.getPayloadType()));

                CompletePickListCommand completePickListCommand =
                        (CompletePickListCommand) command.getPayload();

                completePickListCommand.validate();

                String pickId = completePickListCommand.getPickId();

                PickListLookupEntity pickListLookupEntity = pickListLookupRepository.findByPickId(pickId);

                throwExceptionIfEntityDoesNotExist(pickListLookupEntity,
                        String.format(PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, pickId));

                boolean allSkusArePicked = true;

                for(PickItemLookupEntity pickItemLookupEntity :
                        pickListLookupEntity.getSkuList()) {
                    allSkusArePicked &= pickItemLookupEntity.isSkuPicked();
                }

                if (!allSkusArePicked) {
                    throw new IllegalStateException(AT_LEAST_ONE_SKU_IS_NOT_PICKED_CANNOT_COMPLETE_PICK_LIST);
                }
            }

            return command;
        };
    }
}

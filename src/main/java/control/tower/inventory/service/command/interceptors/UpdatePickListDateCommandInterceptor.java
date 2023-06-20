package control.tower.inventory.service.command.interceptors;

import control.tower.inventory.service.command.commands.UpdatePickListDateCommand;
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
import static control.tower.inventory.service.core.constants.ExceptionMessages.PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST;

@Component
public class UpdatePickListDateCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePickListDateCommandInterceptor.class);

    private final PickListLookupRepository pickListLookupRepository;

    public UpdatePickListDateCommandInterceptor(PickListLookupRepository pickListLookupRepository) {
        this.pickListLookupRepository = pickListLookupRepository;
    }

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {

            if (UpdatePickListDateCommand.class.equals(command.getPayloadType())) {
                LOGGER.info(String.format(INTERCEPTED_COMMAND, command.getPayloadType()));

                UpdatePickListDateCommand updatePickListDateCommand =
                        (UpdatePickListDateCommand) command.getPayload();

                updatePickListDateCommand.validate();

                String pickId = updatePickListDateCommand.getPickId();

                PickListLookupEntity pickListLookupEntity = pickListLookupRepository.findByPickId(pickId);

                throwExceptionIfEntityDoesNotExist(pickListLookupEntity,
                        String.format(PICK_LIST_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, pickId));
            }

            return command;
        };
    }
}

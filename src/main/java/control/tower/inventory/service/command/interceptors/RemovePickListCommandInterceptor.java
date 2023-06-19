package control.tower.inventory.service.command.interceptors;

import control.tower.inventory.service.command.commands.RemovePickListCommand;
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

@Component
public class RemovePickListCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemovePickListCommandInterceptor.class);

    private final PickListLookupRepository pickListLookupRepository;

    public RemovePickListCommandInterceptor(PickListLookupRepository pickListLookupRepository) {
        this.pickListLookupRepository = pickListLookupRepository;
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

                throwExceptionIfEntityDoesNotExist(pickListLookupEntity, "Pick list does not exist");

                if (!pickListLookupEntity.getSkuList().isEmpty()) {
                    throw new IllegalStateException("Cannot delete pick list, still contains skus");
                }
            }

            return command;
        };
    }
}

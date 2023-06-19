package control.tower.inventory.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import static control.tower.core.utils.Helper.throwExceptionIfParameterIsEmpty;

@Getter
@Builder
public class CompletePickListCommand {

    @TargetAggregateIdentifier
    private String pickId;

    public void validate() {
        throwExceptionIfParameterIsEmpty(this.getPickId(), "PickID cannot be empty");
    }
}

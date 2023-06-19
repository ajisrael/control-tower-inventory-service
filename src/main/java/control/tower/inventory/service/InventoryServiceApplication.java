package control.tower.inventory.service;

import control.tower.core.config.XStreamConfig;
import control.tower.inventory.service.command.interceptors.AddInventoryItemToPickListCommandInterceptor;
import control.tower.inventory.service.command.interceptors.CompletePickListCommandInterceptor;
import control.tower.inventory.service.command.interceptors.CreateInventoryItemCommandInterceptor;
import control.tower.inventory.service.command.interceptors.CreatePickListCommandInterceptor;
import control.tower.inventory.service.command.interceptors.MoveInventoryItemCommandInterceptor;
import control.tower.inventory.service.command.interceptors.PickInventoryItemCommandInterceptor;
import control.tower.inventory.service.command.interceptors.RemoveInventoryItemCommandInterceptor;
import control.tower.inventory.service.command.interceptors.RemoveInventoryItemFromPickListCommandInterceptor;
import control.tower.inventory.service.command.interceptors.RemovePickListCommandInterceptor;
import control.tower.inventory.service.command.interceptors.UpdatePickListDateCommandInterceptor;
import control.tower.inventory.service.core.errorhandling.InventoryServiceEventsErrorHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

@EnableDiscoveryClient
@SpringBootApplication
@Import({ XStreamConfig.class })
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Autowired
	public void registerInventoryItemCommandInterceptors(ApplicationContext context, CommandBus commandBus) {
		commandBus.registerDispatchInterceptor(
				context.getBean(CreateInventoryItemCommandInterceptor.class)
		);
		commandBus.registerDispatchInterceptor(
				context.getBean(MoveInventoryItemCommandInterceptor.class)
		);
		commandBus.registerDispatchInterceptor(
				context.getBean(RemoveInventoryItemCommandInterceptor.class)
		);
		commandBus.registerDispatchInterceptor(
				context.getBean(CreatePickListCommandInterceptor.class)
		);
		commandBus.registerDispatchInterceptor(
				context.getBean(PickInventoryItemCommandInterceptor.class)
		);
		commandBus.registerDispatchInterceptor(
				context.getBean(AddInventoryItemToPickListCommandInterceptor.class)
		);
		commandBus.registerDispatchInterceptor(
				context.getBean(RemoveInventoryItemFromPickListCommandInterceptor.class)
		);
		commandBus.registerDispatchInterceptor(
				context.getBean(RemovePickListCommandInterceptor.class)
		);
		commandBus.registerDispatchInterceptor(
				context.getBean(UpdatePickListDateCommandInterceptor.class)
		);
		commandBus.registerDispatchInterceptor(
				context.getBean(CompletePickListCommandInterceptor.class)
		);
	}

	@Autowired
	public void configure(EventProcessingConfigurer configurer) {
		configurer.registerListenerInvocationErrorHandler("inventory-item-group",
				configuration -> new InventoryServiceEventsErrorHandler());
	}
}

package gg.gamello.user.confirmation.domain.method;

import gg.gamello.user.confirmation.domain.secret.EmailFactory;
import gg.gamello.user.confirmation.domain.secret.SecretFactory;
import gg.gamello.user.confirmation.infrastructure.provider.Communication;
import gg.gamello.user.confirmation.infrastructure.provider.email.EmailProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;

public enum MethodType {
	EMAIL(EmailFactory.class, EmailProvider.class);

	private final Class<? extends SecretFactory> factoryClass;
	private final Class<? extends Communication> providerClass;
	private SecretFactory factory;
	private Communication provider;

	MethodType(Class<? extends SecretFactory> factoryClass, Class<? extends Communication> providerClass) {
		this.factoryClass = factoryClass;
		this.providerClass = providerClass;
	}

	public SecretFactory getFactory() {
		return factory;
	}

	public <T extends Communication> T getProvider() {
		return (T) provider;
	}

	@Component
	public static class FactoryInjector {
		@Autowired
		private ApplicationContext context;

		@PostConstruct
		public void construct(){
			Stream.of(MethodType.values())
					.forEach(type -> {
						type.factory = context.getBean(type.factoryClass);
						type.provider = context.getBean(type.providerClass);
					});
		}
	}
}

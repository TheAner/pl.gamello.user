package gg.gamello.user.confirmation.domain.method;

import gg.gamello.user.confirmation.domain.secret.EmailFactory;
import gg.gamello.user.confirmation.domain.secret.SecretFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;

public enum MethodType {
	EMAIL(EmailFactory.class);

	private final Class<? extends SecretFactory> factoryClass;
	private SecretFactory factory;

	MethodType(Class<? extends SecretFactory> factoryClass) {
		this.factoryClass = factoryClass;
	}

	public SecretFactory getFactory() {
		return factory;
	}

	@Component
	public static class FactoryInjector {
		@Autowired
		private ApplicationContext context;

		@PostConstruct
		public void construct(){
			Stream.of(MethodType.values())
					.forEach(type -> type.factory = context.getBean(type.factoryClass));
		}
	}
}

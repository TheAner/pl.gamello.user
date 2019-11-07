package gg.gamello.user.confirmation.domain.method;

import gg.gamello.user.confirmation.domain.token.EmailFactory;
import gg.gamello.user.confirmation.domain.token.TokenFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;

public enum MethodType {
	EMAIL(EmailFactory.class);

	private final Class<? extends TokenFactory> factoryClass;
	private TokenFactory factory;

	MethodType(Class<? extends TokenFactory> factoryClass) {
		this.factoryClass = factoryClass;
	}

	public TokenFactory getFactory() {
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

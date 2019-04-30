package gg.gamello.user.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

@Slf4j
@Configuration
public class JwtConfiguration {

	@Value("${jwt.certificate-path}")
	private String certificatePath;

	private Certificate certificate;

	@Bean
	public PublicKey publicKey() {
		return this.certificate.getPublicKey();
	}

	@PostConstruct
	private void loadCertificate() throws IOException, CertificateException {
		InputStream certificateStream = new ClassPathResource(certificatePath).getInputStream();
		CertificateFactory factory = CertificateFactory.getInstance("X.509");
		this.certificate = factory.generateCertificate(certificateStream);
		log.info("JWT public key has been successfully loaded");
	}
}

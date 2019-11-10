package gg.gamello.user.infrastructure.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import gg.gamello.user.infrastructure.properties.AwsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Configuration {

	private AwsProperties awsProperties;

	private AWSCredentialsProvider awsCredentialsProvider;

	public S3Configuration(AwsProperties awsProperties, AWSCredentialsProvider awsCredentialsProvider) {
		this.awsProperties = awsProperties;
		this.awsCredentialsProvider = awsCredentialsProvider;
	}

	@Bean
	public AmazonS3 awsS3Client() {
		return AmazonS3ClientBuilder.standard()
				.withCredentials(awsCredentialsProvider)
				.withRegion(awsProperties.getRegion("s3"))
				.build();
	}

	@Bean
	public TransferManager getTransferManager() {
		TransferManagerBuilder transferManagerBuilder = TransferManagerBuilder.standard();
		transferManagerBuilder.setS3Client(awsS3Client());
		return transferManagerBuilder.build();
	}
}

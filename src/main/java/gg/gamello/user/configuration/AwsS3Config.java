package gg.gamello.user.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {

    @Value("${cloud.aws.credentials.access-key}")
    private String awsKeyId;

    @Value("${cloud.aws.credentials.secret-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.region}")
    private String region;

    @Bean
    public AmazonS3 awsS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsKeyId, accessKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
    }

    @Bean
    public TransferManager getTransferManager(){
        TransferManagerBuilder transferManagerBuilder = TransferManagerBuilder.standard();
        transferManagerBuilder.setS3Client(awsS3Client());
        return transferManagerBuilder.build();
    }
}

package gg.gamello.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import gg.gamello.user.dao.User;
import gg.gamello.user.domain.avatar.Avatar;
import gg.gamello.user.domain.avatar.AvatarSize;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AvatarService {
    private final TransferManager transferManager;

    @Value("${cloud.aws.bucket-name}")
    private String bucketName;
    @Value("${avatar.location}")
    private String avatarsLocation;
    @Value("${avatar.default}")
    private String defaultAvatar;

    public AvatarService(TransferManager transferManager) {
        this.transferManager = transferManager;
    }

    String getDefaultAvatar(){
        return defaultAvatar;
    }

    String getLocation(User user) {
        String hashBase = new Date() + user.getUsername();
        String hash = DigestUtils.md5DigestAsHex(hashBase.getBytes());

        return avatarsLocation + hash.substring(0, 1) + "/" + hash.substring(1, 2) + "/" + hash;
    }

    private Avatar makeAvatar(MultipartFile image, AvatarSize avatarSize) throws IOException {
        Avatar avatar = new Avatar();
        avatar.setSize(avatarSize);

        BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
        BufferedImage resizedImage = Scalr.resize(bufferedImage,
                Scalr.Method.ULTRA_QUALITY,
                Scalr.Mode.FIT_EXACT,
                avatarSize.getSize(),
                avatarSize.getSize(),
                Scalr.OP_ANTIALIAS);

        BufferedImage completedImage = new BufferedImage(resizedImage.getWidth(), resizedImage.getHeight(), BufferedImage.TYPE_INT_BGR);
        completedImage.createGraphics().drawImage(resizedImage, 0, 0, Color.WHITE, null);

        ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
        ImageIO.write(completedImage, "jpg", imageOutputStream);

        InputStream inputStream = new ByteArrayInputStream(imageOutputStream.toByteArray());
        avatar.setInputStream(inputStream);

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(imageOutputStream.size());
        meta.setContentType("image/jpg");
        avatar.setMetadata(meta);

        imageOutputStream.flush();

        return avatar;
    }

    List<Avatar> getListOfAvatars(MultipartFile image) throws IOException {
        List<Avatar> avatars = new ArrayList<>();

        for (AvatarSize size : AvatarSize.values()) {
            avatars.add(makeAvatar(image, size));
        }

        return avatars;
    }

    void deleteCurrentAvatars(User user) {
        if(!user.getAvatarLocation().equals(defaultAvatar)){
            AmazonS3 amazonS3 = transferManager.getAmazonS3Client();
            amazonS3.listObjects(bucketName, user.getAvatarLocation())
                    .getObjectSummaries()
                    .forEach(file ->
                        amazonS3.deleteObject(bucketName, file.getKey()));
        }
    }

    void uploadListOfAvatars(List<Avatar> avatars, String location) throws InterruptedException {
        for (Avatar avatar : avatars) {
            String key = location + "_" + avatar.getSize() + ".jpg";
            Upload upload = transferManager.upload(bucketName, key, avatar.getInputStream(), avatar.getMetadata());
            UploadResult result = upload.waitForUploadResult();
            transferManager.getAmazonS3Client().setObjectAcl(bucketName, result.getKey(), CannedAccessControlList.PublicRead);
        }
    }
}

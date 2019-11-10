package gg.gamello.user.avatar;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import gg.gamello.user.avatar.domain.Avatar;
import gg.gamello.user.avatar.domain.AvatarSize;
import gg.gamello.user.avatar.exception.InvalidFileException;
import gg.gamello.user.avatar.properties.AvatarProperties;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	AvatarProperties avatarProperties;

	public AvatarService(TransferManager transferManager) {
		this.transferManager = transferManager;
	}

	public String getDefaultAvatar(){
		return avatarProperties.getDefaultAvatarLocation();
	}

	public List<Avatar> generateAvatars(MultipartFile image) throws InvalidFileException {
		var filename = image.getOriginalFilename();
		var extension = filename.substring(filename.lastIndexOf(".") + 1);
		if (!List.of("jpg", "jpeg", "png").contains(extension))
			throw new InvalidFileException("Image should be one of following types: \"jpg\", \"jpeg\", \"png\"");

		List<Avatar> avatars = new ArrayList<>();

		try {
			for (AvatarSize size : AvatarSize.values()) {
				avatars.add(makeAvatar(image, size));
			}
		} catch (IOException e) {
			throw new InvalidFileException(e.getMessage());
		}

		return avatars;
	}

	public void deleteAvatarsInLocation(String currentAvatarLocation) {
		if(!currentAvatarLocation.equals(getDefaultAvatar())){
			AmazonS3 amazonS3 = transferManager.getAmazonS3Client();
			amazonS3.listObjects(avatarProperties.getBucketName(), currentAvatarLocation)
					.getObjectSummaries()
					.forEach(file ->
							amazonS3.deleteObject(avatarProperties.getBucketName(), file.getKey()));
		}
	}

	public String uploadListOfAvatars(List<Avatar> avatars, String username) throws InterruptedException {
		var location = getLocation(username);

		for (Avatar avatar : avatars) {
			String key = location + "_" + avatar.getSize() + ".jpg";
			Upload upload = transferManager
					.upload(avatarProperties.getBucketName(), key, avatar.getInputStream(), avatar.getMetadata());
			UploadResult result = upload.waitForUploadResult();
			transferManager.getAmazonS3Client()
					.setObjectAcl(avatarProperties.getBucketName(), result.getKey(), CannedAccessControlList.PublicRead);
		}

		return location;
	}

	private String getLocation(String userName) {
		String hashBase = new Date() + userName;
		String hash = DigestUtils.md5DigestAsHex(hashBase.getBytes());

		return avatarProperties.getAvatarsLocation() + hash.substring(0, 1) + "/" + hash.substring(1, 2) + "/" + hash;
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
}

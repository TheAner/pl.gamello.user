package gg.gamello.user.command.avatar.domain;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.Data;

import java.io.InputStream;

@Data
public class Avatar {
    InputStream inputStream;
    AvatarSize size;
    ObjectMetadata metadata;
}

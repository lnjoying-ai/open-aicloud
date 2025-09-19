package com.lnjoying.justice.ims.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.lnjoying.justice.ims.harbor.model.Tag;
import lombok.Data;
import org.threeten.bp.OffsetDateTime;

import java.time.LocalDateTime;

import static com.lnjoying.justice.ims.util.ThreetenUtils.convertFrom;

/**
 * repo tag info
 *
 * @author merak
 **/

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImsRegistryRepoTag
{
    private String name;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime pushTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime pullTime;

    private String pullCommand;

    private String digest;

    public static ImsRegistryRepoTag of(Tag tag, String pullCommand, String degest)
    {
        ImsRegistryRepoTag imsRegistryRepoTag = new ImsRegistryRepoTag();
        imsRegistryRepoTag.setName(tag.getName());
        imsRegistryRepoTag.setPullTime(convertFrom(tag.getPullTime().toLocalDateTime()));
        imsRegistryRepoTag.setPushTime(convertFrom(tag.getPushTime().toLocalDateTime()));
        imsRegistryRepoTag.setPullCommand(pullCommand);
        imsRegistryRepoTag.setDigest(degest);
        return imsRegistryRepoTag;
    }
}

package hu.StudentSpace.newsMessageRead;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public class NewsMessageReadId implements Serializable {
    private String userId;
    private UUID messageId;
}

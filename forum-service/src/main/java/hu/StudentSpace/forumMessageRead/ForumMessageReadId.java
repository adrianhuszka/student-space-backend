package hu.StudentSpace.forumMessageRead;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public class ForumMessageReadId implements Serializable {
    private String userId;
    private UUID messageId;
}

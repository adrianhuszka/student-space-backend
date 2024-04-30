package hu.StudentSpace.sceneItems;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SceneReturnItems implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String name;
    private String description;
    private SceneItemType type;
    private String lastMessage;
    private Timestamp lastMessageCreatedAt;
    private int messageCount;
    private int unreadCount;
}

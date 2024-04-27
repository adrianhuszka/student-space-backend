package hu.StudentSpace.sceneItems;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SceneReturnItems implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    UUID id;
    String name;
    String description;
    SceneItemType type;
    int unreadCount;
}

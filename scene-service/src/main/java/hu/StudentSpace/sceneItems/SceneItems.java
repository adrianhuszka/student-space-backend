package hu.StudentSpace.sceneItems;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class SceneItems implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    @Enumerated(value = EnumType.STRING)
    private SceneItemType type;
}

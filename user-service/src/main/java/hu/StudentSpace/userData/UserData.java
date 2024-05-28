package hu.StudentSpace.userData;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_data", schema = "user_service")
public class UserData {
    @Id
    private String userId;

    private String profilePicture;
}

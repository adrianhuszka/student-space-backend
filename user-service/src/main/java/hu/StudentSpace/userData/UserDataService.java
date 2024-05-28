package hu.StudentSpace.userData;

import hu.StudentSpace.exception.ResourceNotFoundException;
import hu.StudentSpace.utils.JwtDecoder;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDataService {
    private final UserDataRepository userDataRepository;
    private final JwtDecoder jwtDecoder;

    public String getProfilePictureByUserId(String userId) {
        return userDataRepository.findById(userId).orElse(
                UserData.builder().build()
        ).getProfilePicture();
    }

    public void uploadProfilePicture(String token, @NotNull UserDataRequest request) {
        final var userId = jwtDecoder.decode(token).getSub();
        final var userData = UserData.builder().userId(userId).profilePicture(request.image()).build();

        userDataRepository.save(userData);
    }

    public void updateProfilePicture(String token, @NotNull UserDataRequest request) {
        final var userId = jwtDecoder.decode(token).getSub();
        final var userData = userDataRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(userId));

        userData.setProfilePicture(request.image());
        userDataRepository.save(userData);
    }
}

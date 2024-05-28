package hu.StudentSpace.userData;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/data")
public class UserDataController {
    private final UserDataService userDataService;

    @GetMapping("/getProfileImage")
    public ResponseEntity<String> getProfilePicByUserId(@RequestParam String userId) {
        return ResponseEntity.ok(userDataService.getProfilePictureByUserId(userId));
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<Void> addUserData(@RequestBody UserDataRequest request, @RequestHeader("Authorization") String token) {
        userDataService.uploadProfilePicture(token, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateImage")
    public ResponseEntity<Void> updateUserData(@RequestBody UserDataRequest request, @RequestHeader("Authorization") String token) {
        userDataService.updateProfilePicture(token, request);
        return ResponseEntity.ok().build();
    }
}

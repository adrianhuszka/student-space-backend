package hu.StudentSpace.users;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/administration/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/count")
    public ResponseEntity<Integer> countUsers() {
        return ResponseEntity.ok(userService.countUsers());
    }

    @GetMapping
    public ResponseEntity<Set<UserDTO>> listUsers(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.listUsers(search, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> createUser(@RequestBody UserRequest request) {
        var resp = userService.createUser(request);

        return ResponseEntity.status(resp.status()).body(resp.message());
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody UserRequest request) {
        userService.updateUser(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> deleteUserById(@PathVariable String id) {
        final var resp = userService.deleteUserById(id);
        return ResponseEntity.status(resp.status()).body(resp.message());
    }
}

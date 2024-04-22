package hu.StudentSpace.groups;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/administration/groups")
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/count")
    public ResponseEntity<Integer> countGroups() {
        return ResponseEntity.ok(groupService.countGroups());
    }

    @GetMapping
    public ResponseEntity<List<GroupRepresentation>> listGroups(@RequestParam(required = false) String search,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(groupService.listGroups(search, page, size));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<String>> getAllJoinedGroups(@PathVariable String userId) {
        return ResponseEntity.ok(groupService.getAllJoinedGroups(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable String id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @PostMapping
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> createGroup(@RequestBody GroupRequest request) {
        final var resp = groupService.createGroup(request);
        return ResponseEntity.status(resp.status()).body(resp.message());
    }

    @PutMapping
    public ResponseEntity<Void> updateGroup(@RequestBody GroupRequest request) {
        groupService.updateGroup(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupById(@PathVariable String id) {
        groupService.deleteGroupById(id);
        return ResponseEntity.ok().build();
    }
}

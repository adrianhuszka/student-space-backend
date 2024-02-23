package hu.StudentSpace.groups;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/administration/groups")
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<?> listGroups() {
        return ResponseEntity.ok(groupService.listGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupById(@PathVariable String id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody GroupRequest request) {
        groupService.createGroup(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> updateGroup(@RequestBody GroupRequest request) {
        groupService.updateGroup(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroupById(@PathVariable String id) {
        groupService.deleteGroupById(id);
        return ResponseEntity.ok().build();
    }
}

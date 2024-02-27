package hu.StudentSpace.groups;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/administration/groups")
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<List<GroupDTO>> listGroups() {
        return ResponseEntity.ok(groupService.listGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable String id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @PostMapping
    public ResponseEntity<Void> createGroup(@RequestBody GroupRequest request) {
        groupService.createGroup(request);
        return ResponseEntity.ok().build();
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

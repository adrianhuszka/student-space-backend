package hu.StudentSpace.roles;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/administration/roles")
public class RolesController {
    private final RolesService rolesService;

    @GetMapping
    public ResponseEntity<List<RoleRepresentation>> listRoles() {
        return ResponseEntity.ok(rolesService.listRoles());
    }

    @PutMapping("/group")
    public ResponseEntity<Void> updateGroupRoles(@RequestBody RolesRequest request) {
        rolesService.updateRolesOnGroup(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user")
    public ResponseEntity<Void> updateUserRoles(@RequestBody RolesRequest request) {
        rolesService.updateRolesOnUser(request);
        return ResponseEntity.ok().build();
    }
}

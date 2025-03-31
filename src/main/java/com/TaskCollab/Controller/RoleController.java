package com.TaskCollab.Controller;

import com.TaskCollab.dto.RoleDTO;
import com.TaskCollab.dto.RoleStatisticsDTO;
import com.TaskCollab.dto.TopUsersDTO;
import com.TaskCollab.Entity.RoleInterface;
import com.TaskCollab.Service.FetchDataService;
import com.TaskCollab.Service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    
    @Autowired
    private RoleService roleService;

    @Autowired
    private FetchDataService fetchDataService;


    @PostMapping("/search")
    public ResponseEntity<List<RoleDTO>> searchRoles(@RequestBody RoleDTO searchCriteria) { 
        Integer roleId = searchCriteria.getRoleId();
        String roleName = searchCriteria.getRoleName();
        Boolean createPermission = searchCriteria.isCreatePermission();
        Boolean readPermission = searchCriteria.isReadPermission();
        Boolean deletePermission = searchCriteria.isDeletePermission();
        Boolean updatePermission = searchCriteria.isUpdatePermission();
        String userName = searchCriteria.getUserName();
        
        List<RoleDTO> roles = roleService.searchRoles(roleId, roleName, createPermission, readPermission, deletePermission, updatePermission, userName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(roles);
    }

    // POST request to create a new role
    @PostMapping("/create")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        RoleInterface role = roleService.createRole(roleDTO);
        return ResponseEntity.ok(convertToDTO(role));
    }

    // DELETE request to delete a role by name
    @PostMapping("/delete/{roleName}")
    public ResponseEntity<String> deleteRole(@PathVariable String roleName) {
        boolean isDeleted = roleService.deleteRole(roleName);
        if (isDeleted) {
            return ResponseEntity.ok("Role deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found");
        }
    }

    /*[DATA VISUALIZATION]: Return Top 5 user with the most roles in a selected permission type.
       Visualization: A table with columns: Username, CreatePermission, ReadPermission, DeletePermission, UpdatePermission
                    User can click on which criteria to sort in descending order top 5.
       Suggestion:
        [] Add Filter: 5 categories (create, delete, update, read, total).
        [] Use Color-Coding: Highlighting the user with the highest count in each category can make it more visually intuitive.
        [] Drill-down Feature: Clicking on a username could open a detailed view showing all roles assigned to that user.
    */
    @GetMapping("/top5/{permissionType}")
    public ResponseEntity<List<TopUsersDTO>> getTop5UsersWithMostRoles(@PathVariable String permissionType) {
        List<TopUsersDTO> topUsers = fetchDataService.getTop5UsersWithPermissionCounts(permissionType);
        return ResponseEntity.ok(topUsers);
    }

    /*[DATA VISUALIZATION]: Return the total Roles in certain types and percentage.
       Suggestion:
        [] Create pie chart: Show visually visually the distribution of the Roles.
        [] Icons indicates the permission type: to show the total role of each type.
    */
    @GetMapping("/statistics")
    public ResponseEntity<List<RoleStatisticsDTO>> getRoleStatistics(){
        List<RoleStatisticsDTO> roleStatistics = fetchDataService.getRoleStatistics();
        return ResponseEntity.ok(roleStatistics);
    }

    // Helper method to convert RoleInterface to RoleDTO
    private RoleDTO convertToDTO(RoleInterface role) {
        RoleDTO dto = new RoleDTO();
        dto.setRoleId(role.getRoleId());
        dto.setRoleName(role.getRoleName());
        dto.setCreatePermission(role.isCreatePermission());
        dto.setReadPermission(role.isReadPermission());
        dto.setDeletePermission(role.isDeletePermission());
        dto.setUpdatePermission(role.isUpdatePermission());
        dto.setUserName(role.getUserName());
        return dto;
    }

    

}

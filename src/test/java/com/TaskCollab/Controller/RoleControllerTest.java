package com.TaskCollab.Controller;

import com.TaskCollab.dto.RoleDTO;
import com.TaskCollab.Entity.RoleInterface;
import com.TaskCollab.Service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// This is the RoleControllerTest class testing the RoleController class.
@WebMvcTest(controllers = RoleController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private RoleService roleService;

    @Disabled("Disabled due to JaCoCo instrumentation issues")
    @Test
    public void testSearchRoles() throws Exception {
        // Create the search criteria as a RoleDTO
        RoleDTO searchCriteria = new RoleDTO();
        searchCriteria.setRoleId(1);
        searchCriteria.setRoleName("Admin");
        searchCriteria.setCreatePermission(true);
        searchCriteria.setReadPermission(true);
        searchCriteria.setDeletePermission(true);
        searchCriteria.setUpdatePermission(true);
        searchCriteria.setUserName("admin_user");

        // Create a mock for RoleInterface
        RoleInterface role = Mockito.mock(RoleInterface.class);
        when(role.getRoleId()).thenReturn(1);
        when(role.getRoleName()).thenReturn("Admin");
        when(role.isCreatePermission()).thenReturn(true);
        when(role.isReadPermission()).thenReturn(true);
        when(role.isDeletePermission()).thenReturn(true);
        when(role.isUpdatePermission()).thenReturn(true);
        when(role.getUserName()).thenReturn("admin_user");

        // Create a list of RoleInterface objects
        List<RoleInterface> roles = Collections.singletonList(role);
        
        // Cast the list to the expected type for stubbing
        @SuppressWarnings("unchecked")
        List<RoleDTO> castedRoles = (List) roles;

        // Stub the service to return the casted list
        when(roleService.searchRoles(
                eq(searchCriteria.getRoleId()),
                eq(searchCriteria.getRoleName()),
                eq(searchCriteria.isCreatePermission()),
                eq(searchCriteria.isReadPermission()),
                eq(searchCriteria.isDeletePermission()),
                eq(searchCriteria.isUpdatePermission()),
                eq(searchCriteria.getUserName())
        )).thenReturn(castedRoles);

        // Perform the POST request and verify the response
        mockMvc.perform(post("/api/roles/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].roleId").value(1))
                .andExpect(jsonPath("$[0].roleName").value("Admin"))
                .andExpect(jsonPath("$[0].createPermission").value(true))
                .andExpect(jsonPath("$[0].readPermission").value(true))
                .andExpect(jsonPath("$[0].deletePermission").value(true))
                .andExpect(jsonPath("$[0].updatePermission").value(true))
                .andExpect(jsonPath("$[0].userName").value("admin_user"));
    }
}

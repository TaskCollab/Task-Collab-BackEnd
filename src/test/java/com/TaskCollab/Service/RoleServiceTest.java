package com.TaskCollab.Service;

import com.TaskCollab.Entity.Role;
import com.TaskCollab.Entity.RoleInterface;
import com.TaskCollab.Entity.Users;
import com.TaskCollab.dao.RoleRepository;
import com.TaskCollab.dto.RoleDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// This is the RoleServiceTest class testing the RoleService class.
@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private RoleService roleService;

    // Mocks for Criteria API used in searchRoles
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private CriteriaQuery<Object[]> criteriaQuery;
    @Mock
    private Root<Role> roleRoot;
    @Mock
    private Join<Role, Users> userJoin;
    @Mock
    private TypedQuery<Object[]> typedQuery;

    @BeforeEach
    public void setup() {
        // Use lenient stubbing for common Criteria API mocks to avoid unnecessary stubbing errors.
        lenient().when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        lenient().when(criteriaBuilder.createQuery(Object[].class)).thenReturn(criteriaQuery);
        lenient().when(criteriaQuery.from(Role.class)).thenReturn(roleRoot);
    }

    @Test
    public void testSearchRoles_byUsername() {
        String userName = "admin_user";
        // Cast userJoin to satisfy generics: first to Object then to Join<Object, Object>
        @SuppressWarnings("unchecked")
        Join<Object, Object> castedUserJoin = (Join<Object, Object>) (Object) userJoin;
        when(roleRoot.join("users", JoinType.INNER)).thenReturn(castedUserJoin);

        // Stub multiselect and where chaining.
        when(criteriaQuery.multiselect(any(), any(), any(), any(), any(), any(), any())).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate[].class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        // Prepare a fake result list for a role with username "admin_user"
        Object[] fakeResult = new Object[] { 1, "Admin", true, true, true, true, "admin_user" };
        List<Object[]> fakeResults = new ArrayList<>();
        fakeResults.add(fakeResult);
        when(typedQuery.getResultList()).thenReturn(fakeResults);

        Collection<RoleDTO> results = roleService.searchRoles(null, null, null, null, null, null, userName);
        assertNotNull(results);
        assertEquals(1, results.size());
        RoleDTO dto = results.iterator().next();
        assertEquals(1, dto.getRoleId());
        assertEquals("Admin", dto.getRoleName());
        assertTrue(dto.getCreatePermission());
        assertTrue(dto.getReadPermission());
        assertTrue(dto.getDeletePermission());
        assertTrue(dto.getUpdatePermission());
        assertEquals("admin_user", dto.getUserName());
    }

    @Test
    public void testSearchRoles_noUsername() {
        // When no username is provided, use a LEFT join.
        @SuppressWarnings("unchecked")
        Join<Object, Object> castedUserJoin = (Join<Object, Object>) (Object) userJoin;
        when(roleRoot.join("users", JoinType.LEFT)).thenReturn(castedUserJoin);

        when(criteriaQuery.multiselect(any(), any(), any(), any(), any(), any(), any())).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate[].class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        // Prepare a fake result list with a null username
        Object[] fakeResult = new Object[] { 2, "UserRole", false, true, false, true, null };
        List<Object[]> fakeResults = new ArrayList<>();
        fakeResults.add(fakeResult);
        when(typedQuery.getResultList()).thenReturn(fakeResults);

        Collection<RoleDTO> results = roleService.searchRoles(null, "UserRole", null, null, null, null, null);
        assertNotNull(results);
        assertEquals(1, results.size());
        RoleDTO dto = results.iterator().next();
        assertEquals(2, dto.getRoleId());
        assertEquals("UserRole", dto.getRoleName());
        assertFalse(dto.getCreatePermission());
        assertTrue(dto.getReadPermission());
        assertFalse(dto.getDeletePermission());
        assertTrue(dto.getUpdatePermission());
        assertNull(dto.getUserName());
    }

    @Test
    public void testCreateRole() {
        // Given a RoleDTO input, createRole should save a Role and decorate it.
        RoleDTO dto = new RoleDTO();
        dto.setRoleName("TestRole");
        dto.setCreatePermission(true);
        dto.setReadPermission(true);
        dto.setDeletePermission(false);
        dto.setUpdatePermission(true);

        // Simulate repository saving the Role by returning a Role with an ID.
        Role savedRole = new Role();
        savedRole.setRoleId(100);
        savedRole.setRoleName("TestRole");
        savedRole.setCreatePermission(true);
        savedRole.setReadPermission(true);
        savedRole.setDeletePermission(false);
        savedRole.setUpdatePermission(true);
        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

        RoleInterface result = roleService.createRole(dto);
        assertNotNull(result);
        // The result is wrapped by Logging and Validation decorators.
        assertEquals("TestRole", result.getRoleName());
        assertTrue(result.isCreatePermission());
        assertTrue(result.isReadPermission());
        assertFalse(result.isDeletePermission());
        assertTrue(result.isUpdatePermission());
    }

    @Test
    public void testDeleteRole_Found() {
        // When the role is found, deleteRole should delete it and return true.
        Role role = new Role();
        role.setRoleId(200);
        role.setRoleName("DeleteRole");
        when(roleRepository.findByRoleName("DeleteRole")).thenReturn(Optional.of(role));

        boolean result = roleService.deleteRole("DeleteRole");
        assertTrue(result);
        verify(roleRepository, times(1)).deleteById(200);
    }

    @Test
    public void testDeleteRole_NotFound() {
        // When the role is not found, deleteRole should return false.
        when(roleRepository.findByRoleName("NonExistent")).thenReturn(Optional.empty());
        boolean result = roleService.deleteRole("NonExistent");
        assertFalse(result);
        verify(roleRepository, never()).deleteById(anyInt());
    }
}

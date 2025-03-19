package com.TaskCollab.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.TaskCollab.Decorator.LoggingRoleDecorator;
import com.TaskCollab.Decorator.ValidationRoleDecorator;
import com.TaskCollab.Entity.Role;
import com.TaskCollab.Entity.RoleInterface;
import com.TaskCollab.Entity.Users;
import com.TaskCollab.dao.RoleRepository;
import com.TaskCollab.dto.RoleDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @PersistenceContext
    private EntityManager entityManager;  // Inject EntityManager
    
    public Collection<RoleDTO> searchRoles(Integer roleId, String roleName, 
            Boolean createPermission, Boolean readPermission, 
            Boolean deletePermission, Boolean updatePermission, String userName) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Role> role = cq.from(Role.class);
        
        // Use inner join if searching by username; otherwise, a left join could be used.
        Join<Role, Users> userJoin;
        if (userName != null && !userName.trim().isEmpty()) {
            userJoin = role.join("users", JoinType.INNER);
        } else {
            userJoin = role.join("users", JoinType.LEFT);
        }

        List<Predicate> predicates = new ArrayList<>();

        // Optional filters for roleId and roleName
        if (roleId != null) {
            predicates.add(cb.equal(role.get("roleId"), roleId));
        }
        if (roleName != null && !roleName.trim().isEmpty()) {
            predicates.add(cb.equal(cb.lower(role.get("roleName")), roleName.toLowerCase()));
        }

        // Optional permission filters (only added if non-null)
        if (createPermission != null) {
            predicates.add(cb.equal(role.get("createPermission"), createPermission));
        }
        if (readPermission != null) {
            predicates.add(cb.equal(role.get("readPermission"), readPermission));
        }
        if (deletePermission != null) {
            predicates.add(cb.equal(role.get("deletePermission"), deletePermission));
        }
        if (updatePermission != null) {
            predicates.add(cb.equal(role.get("updatePermission"), updatePermission));
        }

        // Add username filter if provided
        if (userName != null && !userName.trim().isEmpty()) {
            predicates.add(cb.equal(cb.lower(userJoin.get("username")), userName.toLowerCase()));
        }

        cq.multiselect(
            role.get("roleId"),
            role.get("roleName"),
            role.get("createPermission"),
            role.get("readPermission"),
            role.get("deletePermission"),
            role.get("updatePermission"),
            userJoin.get("username")
        ).where(predicates.toArray(new Predicate[0]));

        List<Object[]> results = entityManager.createQuery(cq).getResultList();

        // Map results to RoleDTO objects
        List<RoleDTO> roleDTOs = results.stream().map(result -> {
            RoleDTO dto = new RoleDTO();
            dto.setRoleId((Integer) result[0]);
            dto.setRoleName((String) result[1]);
            dto.setCreatePermission((Boolean) result[2]);
            dto.setReadPermission((Boolean) result[3]);
            dto.setDeletePermission((Boolean) result[4]);
            dto.setUpdatePermission((Boolean) result[5]);
            dto.setUserName((String) result[6]);
            return dto;
        }).collect(Collectors.toList());

        return roleDTOs;
    }

    public RoleInterface createRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setRoleName(roleDTO.getRoleName());
        role.setCreatePermission(roleDTO.isCreatePermission());
        role.setReadPermission(roleDTO.isReadPermission());
        role.setDeletePermission(roleDTO.isDeletePermission());
        role.setUpdatePermission(roleDTO.isUpdatePermission());

        Role savedRole = roleRepository.save(role);

        RoleInterface decoratedRole = (RoleInterface) savedRole;
        decoratedRole = new LoggingRoleDecorator(decoratedRole);
        decoratedRole = new ValidationRoleDecorator(decoratedRole);
        return decoratedRole;
    }

    public boolean deleteRole(String roleName) {
        if (roleRepository.findByRoleName(roleName).isPresent()) {
            // Assuming your Role entity has a getRoleId() method:
            Integer roleId = roleRepository.findByRoleName(roleName).get().getRoleId();
            roleRepository.deleteById(roleId);
            System.out.println("Deleted Role with ID: " + roleId);
            return true;
        }
        System.out.println("Role with name " + roleName + " not found");
        return false;
    }
}

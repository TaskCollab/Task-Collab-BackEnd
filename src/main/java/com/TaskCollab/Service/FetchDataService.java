package com.TaskCollab.Service;

import com.TaskCollab.dto.ActiveUserDTO;
import com.TaskCollab.dto.RoleStatisticsDTO;
import com.TaskCollab.dto.TaskStatisticsDTO;
import com.TaskCollab.dto.TopUsersDTO;
import com.TaskCollab.Entity.Role;
import com.TaskCollab.Entity.Task;
import com.TaskCollab.Entity.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FetchDataService {

    @PersistenceContext
    private EntityManager entityManager;

    /*[DATA VISUALIZATION]: Return Top 5 user with the most roles in a selected permission type.
       Visualization: A table with columns: Username, CreatePermission, ReadPermission, DeletePermission, UpdatePermission
                    User can click on which criteria to sort in descending order top 5.
       Suggestion:
        [] Add Filter: 5 categories (create, delete, update, read, total).
        [] Use Color-Coding: Highlighting the user with the highest count in each category can make it more visually intuitive.
        [] Drill-down Feature: Clicking on a username could open a detailed view showing all roles assigned to that user.
    */
    public List<TopUsersDTO> getTop5UsersWithPermissionCounts(String permissionType) {
        int limit = 5; // Limit to top 5 users

        // Validate input
        if (permissionType == null || permissionType.trim().isEmpty()) {
            throw new IllegalArgumentException("Permission type cannot be null or empty");
        }

        // Map URL parameter to entity field names for specific permissions.
        // We do not include "total" in the mapping.
        Map<String, String> permissionMapping = Map.of(
            "create", "createPermission",
            "read", "readPermission",
            "delete", "deletePermission",
            "update", "updatePermission"
        );

        // Get the corresponding entity field name; if not found (e.g. "total" or invalid),
        // we will sort by the total count.
        String permissionField = permissionMapping.get(permissionType);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Role> role = cq.from(Role.class);
        Join<Role, Users> userJoin = role.join("users", JoinType.INNER);

        // Compute counts for create permission type
        Expression<Long> createCountExpr = cb.sum(cb.selectCase()
            .when(cb.isTrue(role.get("createPermission")), cb.literal(1L))
            .otherwise(cb.literal(0L)).as(Long.class));
        // Compute counts for read permission type
        Expression<Long> readCountExpr = cb.sum(cb.selectCase()
            .when(cb.isTrue(role.get("readPermission")), cb.literal(1L))
            .otherwise(cb.literal(0L)).as(Long.class));
        // Compute counts for delete permission type
        Expression<Long> deleteCountExpr = cb.sum(cb.selectCase()
            .when(cb.isTrue(role.get("deletePermission")), cb.literal(1L))
            .otherwise(cb.literal(0L)).as(Long.class));
        // Compute counts for update permission type
        Expression<Long> updateCountExpr = cb.sum(cb.selectCase()
            .when(cb.isTrue(role.get("updatePermission")), cb.literal(1L))
            .otherwise(cb.literal(0L)).as(Long.class));
        // Compute total roles count (the sum of all permission counts)
        Expression<Long> totalCountExpr = cb.sum(
            cb.sum(createCountExpr, readCountExpr),
            cb.sum(deleteCountExpr, updateCountExpr)
        );

        /*
         * Determine sorting order:
         * If a valid specific permission type is provided, sort by that permission count (descending).
         * Otherwise (for "total" or invalid input), sort by totalCount descending.
        */
        Order sortingOrder;
        if (permissionField != null) {
            Expression<Long> permissionCountExpr = cb.sum(cb.selectCase()
                .when(cb.isTrue(role.get(permissionField)), cb.literal(1L))
                .otherwise(cb.literal(0L)).as(Long.class));
            sortingOrder = cb.desc(permissionCountExpr);
        } else {
            sortingOrder = cb.desc(totalCountExpr);
        }

        // Build the query: select username and all counts, group by username, and apply sorting.
        cq.multiselect(
            userJoin.get("username").alias("username"),
            createCountExpr.alias("createcount"),
            readCountExpr.alias("readcount"),
            deleteCountExpr.alias("deletecount"),
            updateCountExpr.alias("updatecount"),
            totalCountExpr.alias("totalcount")
        )
        .groupBy(userJoin.get("username"))
        .orderBy(sortingOrder);

        List<Tuple> results = entityManager.createQuery(cq)
                                           .setMaxResults(limit)
                                           .getResultList();

        // Map each Tuple to a TopUsersDTO
        return results.stream().map(tuple -> {
            TopUsersDTO dto = new TopUsersDTO();
            dto.setUserName(tuple.get("username", String.class));
            dto.setCreatecount(tuple.get("createcount", Long.class));
            dto.setReadcount(tuple.get("readcount", Long.class));
            dto.setDeletecount(tuple.get("deletecount", Long.class));
            dto.setUpdatecount(tuple.get("updatecount", Long.class));
            dto.setTotalcount(tuple.get("totalcount", Long.class));
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Retrieves the count of Complete, In Progress, and Pending tasks.
     * Calcuates the count of tasks based on their status and returns a list of TaskStatisticsDTO.
     */
    public List<TaskStatisticsDTO> getTaskStatistics() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery(); // Ensure type is Tuple
        Root<Task> task = cq.from(Task.class);
    
        // Use CASE expression directly for conditional summing
        Expression<Long> completedCountExpr = cb.sum(
            cb.selectCase()
                .when(cb.equal(task.get("status"), "Completed"), 1L)
                .otherwise(0L).as(Long.class)
        );
    
        Expression<Long> pendingCountExpr = cb.sum(
            cb.selectCase()
                .when(cb.equal(task.get("status"), "Pending"), 1L)
                .otherwise(0L).as(Long.class)
        );
    
        Expression<Long> inProgressCountExpr = cb.sum(
            cb.selectCase()
                .when(cb.equal(task.get("status"), "In Progress"), 1L)
                .otherwise(0L).as(Long.class)
        );
    
        cq.multiselect(
            completedCountExpr.alias("completed"),
            pendingCountExpr.alias("pending"),
            inProgressCountExpr.alias("inProgress")
        );
    
        Tuple result = entityManager.createQuery(cq).getSingleResult();
    
        Long completed = result.get("completed", Long.class);
        Long pending = result.get("pending", Long.class);
        Long inProgress = result.get("inProgress", Long.class);
    
        // Calculate total
        long total = completed + pending + inProgress;
    
        // Populate DTO
        TaskStatisticsDTO dto = new TaskStatisticsDTO();
        dto.setCompletedTaskCount(completed);
        dto.setPendingTaskCount(pending);
        dto.setInProgressTaskCount(inProgress);
        dto.setTotalTaskCount((double) total);
    
        // Calculate percentages
        dto.setCompletedPercentage(roundToNDecimalPlaces(total > 0 ? (completed * 100.0 / total) : 0.0));
        dto.setPendingPercentage(roundToNDecimalPlaces(total > 0 ? (pending * 100.0 / total) : 0.0));
        dto.setInProgressPercentage(roundToNDecimalPlaces(total > 0 ? (inProgress * 100.0 / total) : 0.0));
    
        // Return a list with the DTO
        return List.of(dto);
    }

    private double roundToNDecimalPlaces(double value){
        int decimalNumber = 2; // Setting for the number of decimal places.
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimalNumber, RoundingMode.FLOOR);
        return bd.doubleValue();
    }

    public List<RoleStatisticsDTO> getRoleStatistics() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Role> role = cq.from(Role.class);

        // Calculate the total roles of create permission.
        Expression<Long> createCount = cb.sum(cb.selectCase()
            .when(cb.isTrue(role.get("createPermission")), cb.literal(1L))
            .otherwise(cb.literal(0L)).as(Long.class));

        // Calculate the total roles of read permission.
        Expression<Long> readCount = cb.sum(cb.selectCase()
            .when(cb.isTrue(role.get("readPermission")), cb.literal(1L))
            .otherwise(cb.literal(0L)).as(Long.class));

        // Calculate the total roles of update permission.
        Expression<Long> updateCount = cb.sum(cb.selectCase()
            .when(cb.isTrue(role.get("updatePermission")), cb.literal(1L))
            .otherwise(cb.literal(0L)).as(Long.class));

        // Calculate the total roles of delete permission.
        Expression<Long> deleteCount = cb.sum(cb.selectCase()
            .when(cb.isTrue(role.get("deletePermission")), cb.literal(1L))
            .otherwise(cb.literal(0L)).as(Long.class));

        /* Multiple selection: (1) The total roles of create permissions.
                               (2) The total roles of read permissions.
                               (3) The total roles of update permissions.
                               (4) The total roles of delete permissions.
        */ 
        cq.multiselect(
            createCount.alias("createCount"),
            readCount.alias("readCount"),
            updateCount.alias("updateCount"),
            deleteCount.alias("deleteCount")
        ); 

        Tuple result = entityManager.createQuery(cq).getSingleResult();
    
        Long create = result.get("createCount", Long.class);
        Long read = result.get("readCount", Long.class);
        Long update = result.get("updateCount", Long.class);
        Long delete = result.get("deleteCount", Long.class);
    
        // Calculate total
        long total = create + read + update + delete;
        
        // Populate DTO
        RoleStatisticsDTO dto = new RoleStatisticsDTO();
        dto.setCreateRoleCount(create);
        dto.setReadRoleCount(read);
        dto.setUpdateRoleCount(update);
        dto.setDeleteRoleCount(delete);
    
        // Set the total.
        dto.setTotalRoleCount(total);
        
        // Calculate percentages
        dto.setCreateRolePercentage(roundToNDecimalPlaces(total > 0 ? (create * 100.0 / total) : 0.0));
        dto.setReadRolePercentage(roundToNDecimalPlaces(total > 0 ? (read * 100.0 / total) : 0.0));
        dto.setUpdateRolePercentage(roundToNDecimalPlaces(total > 0 ? (update * 100.0 / total) : 0.0));
        dto.setDeleteRolePercentage(roundToNDecimalPlaces(total > 0 ? (delete *100.0/total):0.0));
    
        // Return a list with the DTO
        return List.of(dto);

    }

    public List<ActiveUserDTO> getTop5UsersWithMostTasks(String filterType) {
        int limit = 5; // Limit to top 5 users
    
        // Validate input
        if (filterType == null || filterType.trim().isEmpty()) {
            throw new IllegalArgumentException("Filter type cannot be null or empty");
        }
    
        // Mapping filter types to sorting expressions
        Map<String, Expression<?>> sortOptions = new HashMap<>();
    
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        
        // Use two roots: one for Task and one for Users
        Root<Task> task = cq.from(Task.class);
        Root<Users> user = cq.from(Users.class);
    
        // Join condition: task.assigned_To equals user.username
        cq.where(cb.equal(task.get("assigned_To"), user.get("username")));
    
        // Calculate total tasks assigned to each user
        Expression<Long> totalTasks = cb.count(task.get("task_Id"));
    
        // Calculate total completed tasks
        Expression<Long> totalCompleted = cb.sum(
        cb.selectCase()
            .when(cb.equal(task.get("status"), "Completed"), 1L)
            .otherwise(0L).as(Long.class)
        );

        
        Expression<Double> completedDouble = cb.toDouble(totalCompleted);
        Expression<Double> tasksDouble = cb.toDouble(totalTasks);


        // Calculate completion percentage (avoid division by zero)
        // Calculate raw completion percentage (will be null if totalTasks is 0)
        Expression<Double> rawCompletionPercentage = cb.prod(
            cb.quot(
                completedDouble, 
                cb.nullif(tasksDouble, cb.literal(0.0))
            ),
            cb.literal(100.0)
        ).as(Double.class);

        // Round the completion percentage to two decimal places using the database's ROUND function
        Expression<Double> completionPercentage = cb.function("ROUND", Double.class, rawCompletionPercentage, cb.literal(2));
    
        // Store sorting options:
        sortOptions.put("totalCompleted", totalCompleted);
        sortOptions.put("totalTask", totalTasks);
        sortOptions.put("percentage", completionPercentage);
    
        // Default sorting if invalid filterType is provided
        Expression<?> sortBy = sortOptions.getOrDefault(filterType, totalTasks);
    
        // Select fields
        cq.multiselect(
            user.get("username").alias("username"),
            totalCompleted.alias("completedTasks"),
            totalTasks.alias("totalTasks"),
            completionPercentage.alias("completionPercentage")
        )
        .groupBy(user.get("username"))
        .orderBy(cb.desc(sortBy)); // Sorting based on the chosen filter
    
        // Execute query
        List<Tuple> results = entityManager.createQuery(cq)
            .setMaxResults(limit)
            .getResultList();
    
        // Map each Tuple to an ActiveUserDTO
        return results.stream().map(tuple -> {
            ActiveUserDTO dto = new ActiveUserDTO();
            dto.setUserName(tuple.get("username", String.class));
            dto.setCompletedCount(tuple.get("completedTasks", Long.class));
            dto.setTotalTaskCount(tuple.get("totalTasks", Long.class));
            dto.setCompletedPercentage(tuple.get("completionPercentage", Double.class));
            return dto;
        }).collect(Collectors.toList());
    }  
}

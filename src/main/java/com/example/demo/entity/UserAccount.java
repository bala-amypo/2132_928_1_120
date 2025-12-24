package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_account", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "email must be valid")
    @NotBlank(message = "email is required")
    @Column(nullable = false)package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_account", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "email must be valid")
    @NotBlank(message = "email is required")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6, message = "password must be at least 6 characters")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "role is required")
    @Column(nullable = false)
    private String role;

    // ✅ Join table referential integrity (FK constraints)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_quota_plans",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    foreignKey = @ForeignKey(name = "fk_user_quota_plans_user")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "plan_id",
                    foreignKey = @ForeignKey(name = "fk_user_quota_plans_plan")
            )
    )
    private Set<QuotaPlan> quotaPlans = new HashSet<>();

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public Set<QuotaPlan> getQuotaPlans() { return quotaPlans; }

    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setQuotaPlans(Set<QuotaPlan> quotaPlans) { this.quotaPlans = quotaPlans; }
}
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6, message = "password must be at least 6 characters")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "role is required")
    @Column(nullable = false)
    private String role;

    // ✅ Join table referential integrity (FK constraints)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_quota_plans",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    foreignKey = @ForeignKey(name = "fk_user_quota_plans_user")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "plan_id",
                    foreignKey = @ForeignKey(name = "fk_user_quota_plans_plan")
            )
    )
    private Set<QuotaPlan> quotaPlans = new HashSet<>();

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public Set<QuotaPlan> getQuotaPlans() { return quotaPlans; }

    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setQuotaPlans(Set<QuotaPlan> quotaPlans) { this.quotaPlans = quotaPlans; }
}
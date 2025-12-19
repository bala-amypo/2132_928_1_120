package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "quota_plan", uniqueConstraints = @UniqueConstraint(columnNames = "planName"))
public class QuotaPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "planName is required")
    @Column(nullable = false)
    private String planName;

    @NotNull(message = "dailyLimit is required")
    @Min(value = 1, message = "dailyLimit must be > 0")
    @Column(nullable = false)
    private Integer dailyLimit;

    private String description;

    @NotNull(message = "active is required")
    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    // ✅ NEW: One plan -> many API keys
    @OneToMany(mappedBy = "plan")
    @JsonIgnore
    private List<ApiKey> apiKeys = new ArrayList<>();

    // ✅ NEW: reverse side of UserAccount <-> QuotaPlan
    @ManyToMany(mappedBy = "quotaPlans")
    @JsonIgnore
    private Set<UserAccount> users;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getPlanName() { return planName; }
    public Integer getDailyLimit() { return dailyLimit; }
    public String getDescription() { return description; }
    public Boolean getActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // ✅ NEW getters/setters
    public List<ApiKey> getApiKeys() { return apiKeys; }
    public void setApiKeys(List<ApiKey> apiKeys) { this.apiKeys = apiKeys; }

    public Set<UserAccount> getUsers() { return users; }
    public void setUsers(Set<UserAccount> users) { this.users = users; }

    public void setId(Long id) { this.id = id; }
    public void setPlanName(String planName) { this.planName = planName; }
    public void setDailyLimit(Integer dailyLimit) { this.dailyLimit = dailyLimit; }
    public void setDescription(String description) { this.description = description; }
    public void setActive(Boolean active) { this.active = active; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
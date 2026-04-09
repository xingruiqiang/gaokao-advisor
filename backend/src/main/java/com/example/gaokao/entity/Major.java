package com.example.gaokao.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 专业实体类
 */
@Entity
@Table(name = "major")
@Data
public class Major {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "degree_type")
    private String degreeType;
    
    @Column(name = "employment_rate")
    private Double employmentRate;
    
    @Column(name = "avg_salary")
    private Double avgSalary;
    
    @Column(name = "is_barrier")
    private Boolean isBarrier;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
}

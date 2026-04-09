package com.example.gaokao.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 院校实体类
 */
@Entity
@Table(name = "university")
@Data
public class University {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "level")
    private String level;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "province")
    private String province;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "is_985")
    private Boolean is985;
    
    @Column(name = "is_211")
    private Boolean is211;
    
    @Column(name = "is_double_first_class")
    private Boolean isDoubleFirstClass;
    
    @Column(name = "employment_rate")
    private Double employmentRate;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
}

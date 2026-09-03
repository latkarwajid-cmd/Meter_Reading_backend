package com.Meter.MeterReading.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String name;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    private String email;

    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Meter> meters;}
package com.Meter.MeterReading.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer meterId;

    @Column(unique = true, nullable = false)
    private String meterNumber;

    private String meterType;

    private LocalDate installationDate;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"meters"})
    private User user;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer userId;

    public Integer getUserId() {
        return user != null ? user.getUserId() : userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
        if (this.user == null && userId != null) {
            this.user = new User();
            this.user.setUserId(userId);
        }
    }


    @OneToMany(
            mappedBy = "meter",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<MeterReading> readings;
}
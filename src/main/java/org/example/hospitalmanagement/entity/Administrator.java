package org.example.hospitalmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.hospitalmanagement.entity.abs.BaseEntity;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
public class Administrator extends BaseEntity {
    @OneToOne
    private User user;
}
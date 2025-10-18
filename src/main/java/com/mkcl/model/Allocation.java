package com.mkcl.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Allocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long registrationNumber;
    private String postName;

    @ManyToOne
    @JoinColumn(name = "center_slot_id")
    private CenterSlot centerSlot;

    private String status; // "ALLOCATED" or "PENDING"
}


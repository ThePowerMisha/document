package com.thepowermisha.document.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "creator")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Creator {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
}

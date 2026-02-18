package com.thepowermisha.document.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "author")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
}

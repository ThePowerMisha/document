package com.thepowermisha.document.entity;

import com.thepowermisha.document.type.DocumentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Entity
@Table(name = "document")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Accessors(chain = true)
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String documentNumber;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "author_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_document_author")
    )
    private Author author;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}

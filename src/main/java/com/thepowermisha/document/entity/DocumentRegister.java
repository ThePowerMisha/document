package com.thepowermisha.document.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "document_register")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class DocumentRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "document_id",
            foreignKey = @ForeignKey(name = "fk_document_history_document_register"),
            unique = true
    )
    private Document document;

    private ZonedDateTime date;
}

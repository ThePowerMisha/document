package com.thepowermisha.document.entity;

import com.thepowermisha.document.type.DocumentHistoryAction;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Accessors(chain = true)
public class DocumentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "author_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_document_author_history")
    )
    private Author author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentHistoryAction action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "document_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_document_history_document")
    )
    private Document document;

    private ZonedDateTime createdAt;
    private String commentary;
}

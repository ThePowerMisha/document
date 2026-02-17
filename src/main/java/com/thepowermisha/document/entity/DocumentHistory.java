package com.thepowermisha.document.entity;

import com.thepowermisha.document.type.DocumentHistoryAction;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "`document_history`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class DocumentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentHistoryAction action;

    @ManyToOne
    @JoinColumn(name = "document_uid", nullable = false)
    private Document document;

    private ZonedDateTime createdAt;
    private String commentary;
}

package ru.jd.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "template")
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "template_seq")
    @SequenceGenerator(name = "template_seq", sequenceName = "template_id_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private String name;

    private String logoUrl;

    private String description;

    @OneToMany( mappedBy =  "template",fetch = FetchType.LAZY)
    private List<Group> groups;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
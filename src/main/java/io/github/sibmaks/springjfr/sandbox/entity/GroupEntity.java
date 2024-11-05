package io.github.sibmaks.springjfr.sandbox.entity;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * User JPA entity
 *
 * @author sibmaks
 * @since 0.0.1
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "sandbox", name = "groups")
public class GroupEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

}

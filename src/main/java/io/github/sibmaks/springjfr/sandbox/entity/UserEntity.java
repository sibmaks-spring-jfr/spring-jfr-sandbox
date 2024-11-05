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
@Table(schema = "sandbox", name = "users")
public class UserEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "username", nullable = false)
    private String username;
    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private GroupEntity group;
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;
    @Column(name = "modified_at", nullable = false)
    private ZonedDateTime modifiedAt;

}

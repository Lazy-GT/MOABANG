package com.self.roomescape.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "user_compare")
public class Compare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ucid;
    @ManyToOne(targetEntity = User.class)
    private User user;

    @ManyToOne(targetEntity = Theme.class)
    private Theme theme;
}

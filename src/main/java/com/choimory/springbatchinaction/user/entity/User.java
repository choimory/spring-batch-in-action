package com.choimory.springbatchinaction.user.entity;

import com.choimory.springbatchinaction.common.entity.CommonDateTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends CommonDateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    @Column(unique = true)
    private String id;
    private String password;
    private String email;
    private Boolean isActivated;
    private LocalDateTime lastLoginDateTime;
}

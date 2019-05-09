package com.example.polls.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
/**
 * Created by rajeevkumarsingh on 01/08/17.
 */
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RoleName name;


    public Role(RoleName name) {
        this.name = name;
    }


}

package com.globant.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "CLIENT")
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    private String name;
    private String lastName;
    private String description;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "client_payment"), name = "PAYMENT_ID")
    private List<Payment> payments;

}

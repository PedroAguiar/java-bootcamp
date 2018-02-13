package com.globant.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "client")
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String lastName;
    private String description;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "client_payment"), name = "PAYMENT_ID")
    private List<Payment> payments;

}
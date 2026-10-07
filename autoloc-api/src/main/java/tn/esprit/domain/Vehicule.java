package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 20)
    private String immatriculation;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String marque;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String modele;

    private Integer annee;

    private Integer kilometrage;

    @Column(precision = 10, scale = 2)
    private BigDecimal prixJournalier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutVehicule statut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategorieVehicule categorie;
}
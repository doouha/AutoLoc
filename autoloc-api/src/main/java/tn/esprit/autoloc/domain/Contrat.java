package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contrat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dateSignature;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montantTotal;

    @Column(nullable = false)
    private Boolean valide;

    @OneToMany(mappedBy = "contrat", fetch = FetchType.EAGER)
    @Builder.Default
    private List<tn.esprit.autoloc.domain.Paiement> paiements = new ArrayList<>();

    @OneToOne(mappedBy = "contrat", fetch = FetchType.LAZY)
    private tn.esprit.autoloc.domain.Reservation reservation;
}

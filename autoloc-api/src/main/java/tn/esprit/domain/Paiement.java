package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Column(nullable = false)
    private LocalDateTime datePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModePaiement modePaiement;
}

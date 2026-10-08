package tn.esprit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Paiement;

public interface IPaiementRepository extends JpaRepository<Paiement, Long> {
}

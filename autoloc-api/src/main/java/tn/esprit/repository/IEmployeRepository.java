package tn.esprit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Employe;

public interface IEmployeRepository extends JpaRepository<Employe, Long> {
}

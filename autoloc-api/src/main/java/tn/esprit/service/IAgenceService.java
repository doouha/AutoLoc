package tn.esprit.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.repository.IAgenceRepository;
import tn.esprit.repository.IEmployeRepository;
import tn.esprit.repository.IVehiculeRepository;

import java.util.List;

@Service
public class IAgenceService {
    private final IAgenceRepository agenceRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IEmployeRepository employeRepository;

    public IAgenceService(
            IAgenceRepository agenceRepository,
            IVehiculeRepository vehiculeRepository,
            IEmployeRepository employeRepository) {
        this.agenceRepository = agenceRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.employeRepository = employeRepository;
    }

    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    public Agence findById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable avec l'identifiant " + id));
    }

    public Agence save(Agence agence) {
        // L'identifiant est généré par la base lors d'une création.
        agence.setId(null);
        return agenceRepository.save(agence);
    }

    public Agence update(Long id, Agence agenceModifiee) {
        Agence agenceExistante = findById(id);
        agenceExistante.setNom(agenceModifiee.getNom());
        agenceExistante.setVille(agenceModifiee.getVille());
        agenceExistante.setAdresse(agenceModifiee.getAdresse());
        agenceExistante.setTelephone(agenceModifiee.getTelephone());
        return agenceRepository.save(agenceExistante);
    }

    @Transactional
    public void delete(Long id) {
        Agence agence = findById(id);

        for (Vehicule vehicule : agence.getVehicules()) {
            vehicule.setAgence(null);
        }
        vehiculeRepository.saveAll(agence.getVehicules());

        for (Employe employe : agence.getEmployes()) {
            employe.setAgence(null);
        }
        employeRepository.saveAll(agence.getEmployes());

        agenceRepository.delete(agence);
    }
}

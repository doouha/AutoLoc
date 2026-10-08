package tn.esprit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import tn.esprit.autoloc.AutolocApiApplication;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.repository.IAgenceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = AutolocApiApplication.class)
class AgenceRepositoryTest {

    private static final AtomicInteger SEQUENCE = new AtomicInteger();

    @Autowired
    private CrudRepository<Agence, Long> basicAgenceRepository;
    @Autowired
    private IAgenceRepository fullAgenceRepository;

    @Test
    void basicAddAgence() {

         addAgence(basicAgenceRepository, "basic");
    }

    @Test
    void fullAddAgence() {addAgence(fullAgenceRepository, "full");}


    @Test
    void basicLoadAgence() {
        loadAgence(basicAgenceRepository, "CrudRepository (basic)");
    }

    @Test
    void fullLoadAgence() {
        loadAgence(fullAgenceRepository, "IAgenceRepository (full)");
    }

    @Test
    void loadSortedAgences() {
        if (fullAgenceRepository.count() == 0) {
            addAgence(fullAgenceRepository, "tri");
        }

        List<Agence> agences = fullAgenceRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        assertFalse(agences.isEmpty(), "Aucune agence trouvée pour vérifier le tri.");
        for (int i = 1; i < agences.size(); i++) {
            assertTrue(agences.get(i - 1).getId() > agences.get(i).getId(),
                    "Les agences ne sont pas triées par id décroissant.");
        }

        System.out.println("Agences triées par id décroissant :");
        agences.forEach(agence -> System.out.println(
                "id=" + agence.getId() + ", nom=" + agence.getNom()
                        + ", ville=" + agence.getVille() + ", adresse=" + agence.getAdresse()
                        + ", téléphone=" + agence.getTelephone()));
    }

    @Test
    void loadPagedAgences() {
        while (fullAgenceRepository.count() < 2) {
            addAgence(fullAgenceRepository, "pagination");
        }

        Page<Agence> page = fullAgenceRepository.findAll(
                PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id")));
        assertEquals(2, page.getSize(), "La pagination doit contenir deux agences par page.");
        assertFalse(page.getContent().isEmpty(), "La page ne contient aucune agence.");
        for (int i = 1; i < page.getContent().size(); i++) {
            assertTrue(page.getContent().get(i - 1).getId() > page.getContent().get(i).getId(),
                    "Les agences de la page ne sont pas triées par id décroissant.");
        }

        System.out.println("Nombre total de pages : " + page.getTotalPages());
        System.out.println("Page en cours : " + (page.getNumber() + 1));
        System.out.println("Agences de cette page :");
        page.getContent().forEach(agence -> System.out.println(
                "id=" + agence.getId() + ", nom=" + agence.getNom()
                        + ", ville=" + agence.getVille() + ", adresse=" + agence.getAdresse()
                        + ", téléphone=" + agence.getTelephone()));
    }

    private void addAgence(CrudRepository<Agence, Long> repository, String typeDepot) {
        int timestamp = (int) System.currentTimeMillis();
        String identifiant = Integer.toString(timestamp, 36) + "-"
                + Integer.toString(SEQUENCE.incrementAndGet(), 36) + "-"
                + UUID.randomUUID().toString().substring(0, 4);
        Vehicule vehicule = Vehicule.builder()
                    .immatriculation("TN" + identifiant)
                    .marque("Toyota")
                    .modele("Yaris")
                    .annee(2024)
                    .kilometrage(0)
                    .prixJournalier(new BigDecimal("120.00"))
                    .statut(StatutVehicule.DISPONIBLE)
                    .categorie(CategorieVehicule.ECONOMIQUE)
                .build();

        Agence agence = Agence.builder()
                    .nom("Agence " + typeDepot + " " + identifiant)
                    .ville("Tunis")
                    .adresse("Centre-ville")
                    .telephone("71000000")
                .build();
        vehicule.setAgence(agence);
        agence.getVehicules().add(vehicule);

        repository.save(agence);
        System.out.println("Ajout effectué avec " + typeDepot + " : agence et véhicule.");
    }

    private void loadAgence(CrudRepository<Agence, Long> repository, String typeDepot) {
        if (repository.count() == 0) {
            addAgence(repository, typeDepot);
        }
        List<Agence> agences = StreamSupport.stream(repository.findAll().spliterator(), false).toList();
        assertFalse(agences.isEmpty(), "Aucune agence trouvée avec " + typeDepot);
        assertTrue(agences.stream().anyMatch(agence -> !agence.getVehicules().isEmpty()),
                "Aucun véhicule associé trouvé avec " + typeDepot);

        System.out.println("Chargement avec " + typeDepot + " : " + agences.size() + " agences.");
        agences.forEach(agence -> System.out.println(
                agence.getNom() + " - " + agence.getVille() + " - "
                        + agence.getVehicules().size() + " véhicule(s)"));
    }
}

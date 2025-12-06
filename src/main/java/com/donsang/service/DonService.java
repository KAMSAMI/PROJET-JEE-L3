package com.donsang.service;

import com.donsang.dao.DonDao;
import com.donsang.dao.DonneurDao;
import com.donsang.dao.ReceveurDao;
import com.donsang.model.Don;
import com.donsang.model.Don.*;
import com.donsang.model.Donneur;
import com.donsang.model.Receveur;
import com.donsang.util.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion métier des dons
 */
public class DonService {
    
    private static DonService instance;
    private final DonDao donDao;
    private final DonneurDao donneurDao;
    private final ReceveurDao receveurDao;
    
    private DonService() {
        this.donDao = DonDao.getInstance();
        this.donneurDao = DonneurDao.getInstance();
        this.receveurDao = ReceveurDao.getInstance();
    }
    
    public static synchronized DonService getInstance() {
        if (instance == null) {
            instance = new DonService();
        }
        return instance;
    }
    
    /**
     * Crée un nouveau don avec validation
     */
    public Don creerDon(Don don) throws ValidationException {
        validerDon(don);
        
        // Vérifier que le donneur existe
        Optional<Donneur> donneur = donneurDao.findById(don.getDonneurId());
        if (!donneur.isPresent()) {
            throw new ValidationException("Donneur introuvable avec l'ID: " + don.getDonneurId());
        }
        
        // Vérifier que le donneur est actif
        if (!donneur.get().isActif()) {
            throw new ValidationException("Le donneur n'est pas actif");
        }
        
        // Vérifier si le donneur peut faire un don (délai respecté)
        if (don.getTypeDon().estDonSang()) {
            List<Don> donsRecents = donDao.findByDonneur(don.getDonneurId());
            if (!donsRecents.isEmpty()) {
                Don dernierDon = donsRecents.get(0);
                if (dernierDon.getDateProchainDonAutorise() != null &&
                    don.getDateDon().isBefore(dernierDon.getDateProchainDonAutorise())) {
                    throw new ValidationException(
                        "Le donneur ne peut pas faire de don avant le " + 
                        dernierDon.getDateProchainDonAutorise()
                    );
                }
            }
        }
        
        Don nouveauDon = donDao.create(don);
        
        // Mettre à jour l'historique du donneur
        donneur.get().ajouterDon(nouveauDon.getId());
        donneurDao.update(donneur.get());
        
        return nouveauDon;
    }
    
    /**
     * Met à jour un don existant
     */
    public Don modifierDon(Don don) throws ValidationException {
        if (don.getId() == null) {
            throw new ValidationException("L'ID du don ne peut pas être null");
        }
        
        Optional<Don> existant = donDao.findById(don.getId());
        if (!existant.isPresent()) {
            throw new ValidationException("Don introuvable avec l'ID: " + don.getId());
        }
        
        validerDon(don);
        return donDao.update(don);
    }
    
    /**
     * Annule un don
     */
    public Don annulerDon(Long id) throws ValidationException {
        Optional<Don> don = donDao.findById(id);
        if (!don.isPresent()) {
            throw new ValidationException("Don introuvable avec l'ID: " + id);
        }
        
        Don d = don.get();
        
        // Vérifier si le don peut être annulé
        if (d.getStatut() == StatutDon.UTILISE) {
            throw new ValidationException("Un don déjà utilisé ne peut pas être annulé");
        }
        
        d.annuler();
        return donDao.update(d);
    }
    
    /**
     * Attribue un don à un receveur
     */
    public Don attribuerDon(Long donId, Long receveurId) throws ValidationException {
        Optional<Don> don = donDao.findById(donId);
        if (!don.isPresent()) {
            throw new ValidationException("Don introuvable avec l'ID: " + donId);
        }
        
        Optional<Receveur> receveur = receveurDao.findById(receveurId);
        if (!receveur.isPresent()) {
            throw new ValidationException("Receveur introuvable avec l'ID: " + receveurId);
        }
        
        Don d = don.get();
        Receveur r = receveur.get();
        
        // Vérifier la compatibilité pour les dons de sang
        if (d.getTypeDon().estDonSang()) {
            Optional<Donneur> donneur = donneurDao.findById(d.getDonneurId());
            if (donneur.isPresent()) {
                if (!donneur.get().getGroupeSanguin().peutDonnerA(r.getGroupeSanguin())) {
                    throw new ValidationException(
                        "Incompatibilité sanguine: " + 
                        donneur.get().getGroupeSanguin().getDesignation() +
                        " ne peut pas donner à " + 
                        r.getGroupeSanguin().getDesignation()
                    );
                }
            }
        }
        
        // Vérifier que le don n'est pas déjà attribué
        if (d.getReceveurId() != null) {
            throw new ValidationException("Ce don est déjà attribué à un receveur");
        }
        
        // Vérifier que le don est valide
        if (!d.estValide()) {
            throw new ValidationException("Ce don n'est plus valide");
        }
        
        // Attribuer le don
        d.attribuerReceveur(receveurId);
        donDao.update(d);
        
        // Mettre à jour le receveur
        r.ajouterDonRecu(donId);
        receveurDao.update(r);
        
        return d;
    }
    
    /**
     * Marque un don comme utilisé
     */
    public Don marquerDonUtilise(Long id) throws ValidationException {
        Optional<Don> don = donDao.findById(id);
        if (!don.isPresent()) {
            throw new ValidationException("Don introuvable avec l'ID: " + id);
        }
        
        Don d = don.get();
        
        if (d.getReceveurId() == null) {
            throw new ValidationException("Le don doit être attribué avant d'être marqué comme utilisé");
        }
        
        d.marquerUtilise();
        return donDao.update(d);
    }
    
    /**
     * Récupère un don par ID
     */
    public Optional<Don> getDon(Long id) {
        return donDao.findById(id);
    }
    
    /**
     * Récupère tous les dons
     */
    public List<Don> getTousDons() {
        return donDao.findAll();
    }
    
    /**
     * Récupère les dons d'un donneur
     */
    public List<Don> getDonsParDonneur(Long donneurId) throws ValidationException {
        if (donneurId == null) {
            throw new ValidationException("L'ID du donneur ne peut pas être null");
        }
        return donDao.findByDonneur(donneurId);
    }
    
    /**
     * Récupère les dons reçus par un receveur
     */
    public List<Don> getDonsParReceveur(Long receveurId) throws ValidationException {
        if (receveurId == null) {
            throw new ValidationException("L'ID du receveur ne peut pas être null");
        }
        return donDao.findByReceveur(receveurId);
    }
    
    /**
     * Récupère les dons disponibles
     */
    public List<Don> getDonsDisponibles() {
        return donDao.findDonsSangDisponibles();
    }
    
    /**
     * Récupère les dons non attribués
     */
    public List<Don> getDonsNonAttribues() {
        return donDao.findNonAttribues();
    }
    
    /**
     * Récupère les dons par période
     */
    public List<Don> getDonsParPeriode(LocalDate debut, LocalDate fin) throws ValidationException {
        if (debut == null || fin == null) {
            throw new ValidationException("Les dates de début et fin ne peuvent pas être null");
        }
        if (debut.isAfter(fin)) {
            throw new ValidationException("La date de début doit être avant la date de fin");
        }
        return donDao.findByPeriode(debut, fin);
    }
    
    /**
     * Récupère les dons récents
     */
    public List<Don> getDonsRecents(int limite) {
        return donDao.findRecents(limite);
    }
    
    /**
     * Valide les données d'un don
     */
    private void validerDon(Don don) throws ValidationException {
        if (don == null) {
            throw new ValidationException("Le don ne peut pas être null");
        }
        
        if (don.getTypeDon() == null) {
            throw new ValidationException("Le type de don est obligatoire");
        }
        
        if (don.getDateDon() == null) {
            throw new ValidationException("La date du don est obligatoire");
        }
        
        // La date du don ne peut pas être dans le futur (sauf si programmé)
        if (don.getStatut() != StatutDon.PROGRAMME && 
            don.getDateDon().isAfter(LocalDate.now())) {
            throw new ValidationException("La date du don ne peut pas être dans le futur");
        }
        
        if (don.getDonneurId() == null) {
            throw new ValidationException("Le donneur est obligatoire");
        }
        
        // Validation spécifique pour les dons de sang
        if (don.getTypeDon().estDonSang()) {
            if (don.getQuantite() <= 0) {
                throw new ValidationException("La quantité de sang doit être supérieure à 0");
            }
            
            // Quantité maximale de sang (généralement 450-500 ml)
            if (don.getQuantite() > 500) {
                throw new ValidationException("La quantité de sang ne peut pas dépasser 500 ml");
            }
        }
        
        // Validation spécifique pour les dons d'organe
        if (don.getTypeDon().estDonOrgane()) {
            if (don.getOrgane() == null || don.getOrgane().trim().isEmpty()) {
                throw new ValidationException("Le type d'organe doit être spécifié");
            }
        }
    }
    
    /**
     * Compte le nombre total de dons
     */
    public long compterDons() {
        return donDao.count();
    }
    
    /**
     * Compte les dons du mois en cours
     */
    public long compterDonsDuMois() {
        return donDao.findDuMoisCourant().size();
    }
    
    /**
     * Calcule la quantité totale de sang disponible
     */
    public double getQuantiteSangDisponible() {
        return donDao.getQuantiteTotaleSangDisponible();
    }
}
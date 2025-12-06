package com.donsang.service;

import com.donsang.dao.ReceveurDao;
import com.donsang.model.Receveur;
import com.donsang.model.Receveur.*;
import com.donsang.model.TypeDeSang;
import com.donsang.util.ValidationException;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion métier des receveurs
 */
public class ReceveurService {
    
    private static ReceveurService instance;
    private final ReceveurDao receveurDao;
    
    private ReceveurService() {
        this.receveurDao = ReceveurDao.getInstance();
    }
    
    public static synchronized ReceveurService getInstance() {
        if (instance == null) {
            instance = new ReceveurService();
        }
        return instance;
    }
    
    /**
     * Crée un nouveau receveur avec validation
     */
    public Receveur creerReceveur(Receveur receveur) throws ValidationException {
        validerReceveur(receveur);
        
        // Vérifier si l'email existe déjà
        if (receveurDao.emailExists(receveur.getEmail())) {
            throw new ValidationException("Un receveur avec cet email existe déjà");
        }
        
        return receveurDao.create(receveur);
    }
    
    /**
     * Met à jour un receveur existant
     */
    public Receveur modifierReceveur(Receveur receveur) throws ValidationException {
        if (receveur.getId() == null) {
            throw new ValidationException("L'ID du receveur ne peut pas être null");
        }
        
        Optional<Receveur> existant = receveurDao.findById(receveur.getId());
        if (!existant.isPresent()) {
            throw new ValidationException("Receveur introuvable avec l'ID: " + receveur.getId());
        }
        
        validerReceveur(receveur);
        
        // Vérifier si le nouvel email existe déjà (sauf si c'est le même receveur)
        Optional<Receveur> avecMemeEmail = receveurDao.findByEmail(receveur.getEmail());
        if (avecMemeEmail.isPresent() && !avecMemeEmail.get().getId().equals(receveur.getId())) {
            throw new ValidationException("Un autre receveur avec cet email existe déjà");
        }
        
        return receveurDao.update(receveur);
    }
    
    /**
     * Supprime un receveur
     */
    public boolean supprimerReceveur(Long id) throws ValidationException {
        if (id == null) {
            throw new ValidationException("L'ID ne peut pas être null");
        }
        
        Optional<Receveur> receveur = receveurDao.findById(id);
        if (!receveur.isPresent()) {
            throw new ValidationException("Receveur introuvable avec l'ID: " + id);
        }
        
        // Si le receveur a reçu des dons, on archive plutôt que de supprimer
        if (receveur.get().getNombreDonsRecus() > 0) {
            receveur.get().setStatut(StatutReceveur.ARCHIVE);
            receveurDao.update(receveur.get());
            return true;
        }
        
        return receveurDao.delete(id);
    }
    
    /**
     * Récupère un receveur par ID
     */
    public Optional<Receveur> getReceveur(Long id) {
        return receveurDao.findById(id);
    }
    
    /**
     * Récupère tous les receveurs
     */
    public List<Receveur> getTousReceveurs() {
        return receveurDao.findAll();
    }
    
    /**
     * Récupère les receveurs en attente
     */
    public List<Receveur> getReceveursEnAttente() {
        return receveurDao.findEnAttente();
    }
    
    /**
     * Récupère les receveurs critiques
     */
    public List<Receveur> getReceveursCritiques() {
        return receveurDao.findCritiques();
    }
    
    /**
     * Récupère les receveurs par priorité (urgence)
     */
    public List<Receveur> getReceveursParPriorite() {
        return receveurDao.findByPriorite();
    }
    
    /**
     * Recherche des receveurs par nom
     */
    public List<Receveur> rechercherParNom(String nom) throws ValidationException {
        if (nom == null || nom.trim().isEmpty()) {
            throw new ValidationException("Le nom de recherche ne peut pas être vide");
        }
        return receveurDao.findByNom(nom);
    }
    
    /**
     * Recherche des receveurs par groupe sanguin
     */
    public List<Receveur> rechercherParGroupeSanguin(TypeDeSang groupeSanguin) throws ValidationException {
        if (groupeSanguin == null) {
            throw new ValidationException("Le groupe sanguin ne peut pas être null");
        }
        return receveurDao.findByGroupeSanguin(groupeSanguin);
    }
    
    /**
     * Change le statut d'un receveur
     */
    public Receveur changerStatut(Long id, StatutReceveur nouveauStatut) throws ValidationException {
        Optional<Receveur> receveur = receveurDao.findById(id);
        if (!receveur.isPresent()) {
            throw new ValidationException("Receveur introuvable avec l'ID: " + id);
        }
        
        if (nouveauStatut == null) {
            throw new ValidationException("Le nouveau statut ne peut pas être null");
        }
        
        Receveur r = receveur.get();
        r.setStatut(nouveauStatut);
        
        // Si le besoin est satisfait, enregistrer la date
        if (nouveauStatut == StatutReceveur.BESOIN_SATISFAIT) {
            r.setDateReceptionDon(java.time.LocalDate.now());
        }
        
        return receveurDao.update(r);
    }
    
    /**
     * Change le niveau d'urgence d'un receveur
     */
    public Receveur changerUrgence(Long id, UrgenceNiveau nouvelleUrgence) throws ValidationException {
        Optional<Receveur> receveur = receveurDao.findById(id);
        if (!receveur.isPresent()) {
            throw new ValidationException("Receveur introuvable avec l'ID: " + id);
        }
        
        if (nouvelleUrgence == null) {
            throw new ValidationException("Le niveau d'urgence ne peut pas être null");
        }
        
        Receveur r = receveur.get();
        r.setUrgence(nouvelleUrgence);
        return receveurDao.update(r);
    }
    
    /**
     * Valide les données d'un receveur
     */
    private void validerReceveur(Receveur receveur) throws ValidationException {
        if (receveur == null) {
            throw new ValidationException("Le receveur ne peut pas être null");
        }
        
        if (receveur.getNom() == null || receveur.getNom().trim().isEmpty()) {
            throw new ValidationException("Le nom est obligatoire");
        }
        
        if (receveur.getPrenom() == null || receveur.getPrenom().trim().isEmpty()) {
            throw new ValidationException("Le prénom est obligatoire");
        }
        
        if (receveur.getEmail() == null || receveur.getEmail().trim().isEmpty()) {
            throw new ValidationException("L'email est obligatoire");
        }
        
        if (!receveur.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Format d'email invalide");
        }
        
        if (receveur.getTelephone() == null || receveur.getTelephone().trim().isEmpty()) {
            throw new ValidationException("Le téléphone est obligatoire");
        }
        
        if (receveur.getGroupeSanguin() == null) {
            throw new ValidationException("Le groupe sanguin est obligatoire");
        }
        
        if (receveur.getTypeBesoin() == null) {
            throw new ValidationException("Le type de besoin est obligatoire");
        }
        
        if (receveur.getUrgence() == null) {
            throw new ValidationException("Le niveau d'urgence est obligatoire");
        }
        
        // Si le besoin concerne un organe, l'organe doit être spécifié
        if ((receveur.getTypeBesoin() == TypeBesoin.ORGANE || 
             receveur.getTypeBesoin() == TypeBesoin.SANG_ET_ORGANE) &&
            (receveur.getOrganeNecessaire() == null || 
             receveur.getOrganeNecessaire().trim().isEmpty())) {
            throw new ValidationException("L'organe nécessaire doit être spécifié");
        }
    }
    
    /**
     * Compte le nombre total de receveurs
     */
    public long compterReceveurs() {
        return receveurDao.count();
    }
    
    /**
     * Compte le nombre de receveurs en attente
     */
    public long compterReceveursEnAttente() {
        return receveurDao.findEnAttente().size();
    }
    
    /**
     * Compte le nombre de receveurs critiques
     */
    public long compterReceveursCritiques() {
        return receveurDao.findCritiques().size();
    }
}
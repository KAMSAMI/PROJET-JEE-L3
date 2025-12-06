package com.donsang.service;

import com.donsang.dao.DonneurDao;
import com.donsang.model.Donneur;
import com.donsang.model.TypeDeSang;
import com.donsang.util.ValidationException;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion métier des donneurs
 */
public class DonneurService {
    
    private static DonneurService instance;
    private final DonneurDao donneurDao;
    
    private DonneurService() {
        this.donneurDao = DonneurDao.getInstance();
    }
    
    public static synchronized DonneurService getInstance() {
        if (instance == null) {
            instance = new DonneurService();
        }
        return instance;
    }
    
    /**
     * Crée un nouveau donneur avec validation
     */
    public Donneur creerDonneur(Donneur donneur) throws ValidationException {
        validerDonneur(donneur);
        
        // Vérifier si l'email existe déjà
        if (donneurDao.emailExists(donneur.getEmail())) {
            throw new ValidationException("Un donneur avec cet email existe déjà");
        }
        
        return donneurDao.create(donneur);
    }
    
    /**
     * Met à jour un donneur existant
     */
    public Donneur modifierDonneur(Donneur donneur) throws ValidationException {
        if (donneur.getId() == null) {
            throw new ValidationException("L'ID du donneur ne peut pas être null");
        }
        
        Optional<Donneur> existant = donneurDao.findById(donneur.getId());
        if (!existant.isPresent()) {
            throw new ValidationException("Donneur introuvable avec l'ID: " + donneur.getId());
        }
        
        validerDonneur(donneur);
        
        // Vérifier si le nouvel email existe déjà (sauf si c'est le même donneur)
        Optional<Donneur> avecMemeEmail = donneurDao.findByEmail(donneur.getEmail());
        if (avecMemeEmail.isPresent() && !avecMemeEmail.get().getId().equals(donneur.getId())) {
            throw new ValidationException("Un autre donneur avec cet email existe déjà");
        }
        
        return donneurDao.update(donneur);
    }
    
    /**
     * Supprime un donneur
     */
    public boolean supprimerDonneur(Long id) throws ValidationException {
        if (id == null) {
            throw new ValidationException("L'ID ne peut pas être null");
        }
        
        Optional<Donneur> donneur = donneurDao.findById(id);
        if (!donneur.isPresent()) {
            throw new ValidationException("Donneur introuvable avec l'ID: " + id);
        }
        
        // Vérifier si le donneur a des dons
        if (donneur.get().getNombreDons() > 0) {
            // Plutôt que de supprimer, on désactive
            donneur.get().setActif(false);
            donneurDao.update(donneur.get());
            return true;
        }
        
        return donneurDao.delete(id);
    }
    
    /**
     * Récupère un donneur par ID
     */
    public Optional<Donneur> getDonneur(Long id) {
        return donneurDao.findById(id);
    }
    
    /**
     * Récupère tous les donneurs
     */
    public List<Donneur> getTousDonneurs() {
        return donneurDao.findAll();
    }
    
    /**
     * Récupère les donneurs actifs
     */
    public List<Donneur> getDonneursActifs() {
        return donneurDao.findActifs();
    }
    
    /**
     * Recherche des donneurs par nom
     */
    public List<Donneur> rechercherParNom(String nom) throws ValidationException {
        if (nom == null || nom.trim().isEmpty()) {
            throw new ValidationException("Le nom de recherche ne peut pas être vide");
        }
        return donneurDao.findByNom(nom);
    }
    
    /**
     * Recherche des donneurs par groupe sanguin
     */
    public List<Donneur> rechercherParGroupeSanguin(TypeDeSang groupeSanguin) throws ValidationException {
        if (groupeSanguin == null) {
            throw new ValidationException("Le groupe sanguin ne peut pas être null");
        }
        return donneurDao.findByGroupeSanguin(groupeSanguin);
    }
    
    /**
     * Récupère les donneurs les plus actifs
     */
    public List<Donneur> getTopDonneurs(int limite) {
        return donneurDao.findTopDonneurs(limite);
    }
    
    /**
     * Active ou désactive un donneur
     */
    public Donneur changerStatutActif(Long id, boolean actif) throws ValidationException {
        Optional<Donneur> donneur = donneurDao.findById(id);
        if (!donneur.isPresent()) {
            throw new ValidationException("Donneur introuvable avec l'ID: " + id);
        }
        
        Donneur d = donneur.get();
        d.setActif(actif);
        return donneurDao.update(d);
    }
    
    /**
     * Valide les données d'un donneur
     */
    private void validerDonneur(Donneur donneur) throws ValidationException {
        if (donneur == null) {
            throw new ValidationException("Le donneur ne peut pas être null");
        }
        
        if (donneur.getNom() == null || donneur.getNom().trim().isEmpty()) {
            throw new ValidationException("Le nom est obligatoire");
        }
        
        if (donneur.getPrenom() == null || donneur.getPrenom().trim().isEmpty()) {
            throw new ValidationException("Le prénom est obligatoire");
        }
        
        if (donneur.getEmail() == null || donneur.getEmail().trim().isEmpty()) {
            throw new ValidationException("L'email est obligatoire");
        }
        
        if (!donneur.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Format d'email invalide");
        }
        
        if (donneur.getTelephone() == null || donneur.getTelephone().trim().isEmpty()) {
            throw new ValidationException("Le téléphone est obligatoire");
        }
        
        if (donneur.getGroupeSanguin() == null) {
            throw new ValidationException("Le groupe sanguin est obligatoire");
        }
        
        if (donneur.getDateNaissance() == null) {
            throw new ValidationException("La date de naissance est obligatoire");
        }
        
        // Vérifier l'âge minimum (18 ans)
        if (donneur.getAge() < 18) {
            throw new ValidationException("Le donneur doit avoir au moins 18 ans");
        }
        
        // Vérifier l'âge maximum (70 ans pour les nouveaux donneurs)
        if (donneur.getId() == null && donneur.getAge() > 70) {
            throw new ValidationException("L'âge maximum pour un nouveau donneur est de 70 ans");
        }
    }
    
    /**
     * Compte le nombre total de donneurs
     */
    public long compterDonneurs() {
        return donneurDao.count();
    }
    
    /**
     * Compte le nombre de donneurs actifs
     */
    public long compterDonneursActifs() {
        return donneurDao.findActifs().size();
    }
}
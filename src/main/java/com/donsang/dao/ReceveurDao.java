package com.donsang.dao;

import com.donsang.model.Receveur;
import com.donsang.model.Receveur.StatutReceveur;
import com.donsang.model.Receveur.UrgenceNiveau;
import com.donsang.model.Receveur.TypeBesoin;
import com.donsang.model.TypeDeSang;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * DAO pour la gestion des receveurs (implémentation en mémoire)
 */
public class ReceveurDao implements IDao<Receveur> {
    
    private static ReceveurDao instance;
    private final Map<Long, Receveur> receveurs;
    private final AtomicLong idGenerator;
    
    private ReceveurDao() {
        this.receveurs = new ConcurrentHashMap<>();
        this.idGenerator = new AtomicLong(1);
    }
    
    /**
     * Pattern Singleton
     */
    public static synchronized ReceveurDao getInstance() {
        if (instance == null) {
            instance = new ReceveurDao();
        }
        return instance;
    }
    
    @Override
    public Receveur create(Receveur receveur) {
        if (receveur == null) {
            throw new IllegalArgumentException("Le receveur ne peut pas être null");
        }
        
        Long id = idGenerator.getAndIncrement();
        receveur.setId(id);
        receveurs.put(id, receveur);
        return receveur;
    }
    
    @Override
    public Optional<Receveur> findById(Long id) {
        return Optional.ofNullable(receveurs.get(id));
    }
    
    @Override
    public List<Receveur> findAll() {
        return new ArrayList<>(receveurs.values());
    }
    
    @Override
    public Receveur update(Receveur receveur) {
        if (receveur == null || receveur.getId() == null) {
            throw new IllegalArgumentException("Le receveur et son ID ne peuvent pas être null");
        }
        
        if (!receveurs.containsKey(receveur.getId())) {
            throw new IllegalArgumentException("Receveur avec l'ID " + receveur.getId() + " n'existe pas");
        }
        
        receveurs.put(receveur.getId(), receveur);
        return receveur;
    }
    
    @Override
    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }
        return receveurs.remove(id) != null;
    }
    
    @Override
    public long count() {
        return receveurs.size();
    }
    
    /**
     * Recherche des receveurs par nom
     */
    public List<Receveur> findByNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String nomRecherche = nom.toLowerCase().trim();
        return receveurs.values().stream()
                .filter(r -> r.getNom().toLowerCase().contains(nomRecherche) ||
                            r.getPrenom().toLowerCase().contains(nomRecherche))
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche des receveurs par groupe sanguin
     */
    public List<Receveur> findByGroupeSanguin(TypeDeSang groupeSanguin) {
        if (groupeSanguin == null) {
            return new ArrayList<>();
        }
        
        return receveurs.values().stream()
                .filter(r -> r.getGroupeSanguin() == groupeSanguin)
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche des receveurs par statut
     */
    public List<Receveur> findByStatut(StatutReceveur statut) {
        if (statut == null) {
            return new ArrayList<>();
        }
        
        return receveurs.values().stream()
                .filter(r -> r.getStatut() == statut)
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche des receveurs en attente
     */
    public List<Receveur> findEnAttente() {
        return findByStatut(StatutReceveur.EN_ATTENTE);
    }
    
    /**
     * Recherche des receveurs par niveau d'urgence
     */
    public List<Receveur> findByUrgence(UrgenceNiveau urgence) {
        if (urgence == null) {
            return new ArrayList<>();
        }
        
        return receveurs.values().stream()
                .filter(r -> r.getUrgence() == urgence)
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche des receveurs critiques (urgence critique)
     */
    public List<Receveur> findCritiques() {
        return findByUrgence(UrgenceNiveau.CRITIQUE);
    }
    
    /**
     * Recherche des receveurs par type de besoin
     */
    public List<Receveur> findByTypeBesoin(TypeBesoin typeBesoin) {
        if (typeBesoin == null) {
            return new ArrayList<>();
        }
        
        return receveurs.values().stream()
                .filter(r -> r.getTypeBesoin() == typeBesoin)
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche des receveurs nécessitant du sang
     */
    public List<Receveur> findNecessitantSang() {
        return receveurs.values().stream()
                .filter(r -> r.getTypeBesoin() == TypeBesoin.SANG || 
                            r.getTypeBesoin() == TypeBesoin.SANG_ET_ORGANE)
                .filter(r -> r.getStatut() == StatutReceveur.EN_ATTENTE)
                .collect(Collectors.toList());
    }
    
    /**
     * Recherche des receveurs nécessitant un organe
     */
    public List<Receveur> findNecessitantOrgane() {
        return receveurs.values().stream()
                .filter(r -> r.getTypeBesoin() == TypeBesoin.ORGANE || 
                            r.getTypeBesoin() == TypeBesoin.SANG_ET_ORGANE)
                .filter(r -> r.getStatut() == StatutReceveur.EN_ATTENTE)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère les receveurs triés par priorité (urgence décroissante)
     */
    public List<Receveur> findByPriorite() {
        return receveurs.values().stream()
                .filter(r -> r.getStatut() == StatutReceveur.EN_ATTENTE)
                .sorted((r1, r2) -> Integer.compare(
                    r2.getUrgence().getPriorite(), 
                    r1.getUrgence().getPriorite()))
                .collect(Collectors.toList());
    }
    
    /**
     * Compte les receveurs par statut
     */
    public Map<StatutReceveur, Long> countByStatut() {
        return receveurs.values().stream()
                .collect(Collectors.groupingBy(
                    Receveur::getStatut,
                    Collectors.counting()
                ));
    }
    
    /**
     * Compte les receveurs par niveau d'urgence
     */
    public Map<UrgenceNiveau, Long> countByUrgence() {
        return receveurs.values().stream()
                .filter(r -> r.getStatut() == StatutReceveur.EN_ATTENTE)
                .collect(Collectors.groupingBy(
                    Receveur::getUrgence,
                    Collectors.counting()
                ));
    }
    
    /**
     * Recherche un receveur par email
     */
    public Optional<Receveur> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return receveurs.values().stream()
                .filter(r -> email.equalsIgnoreCase(r.getEmail()))
                .findFirst();
    }
    
    /**
     * Vérifie si un email existe déjà
     */
    public boolean emailExists(String email) {
        return findByEmail(email).isPresent();
    }
    
    /**
     * Nettoie toutes les données
     */
    public void clear() {
        receveurs.clear();
        idGenerator.set(1);
    }
}
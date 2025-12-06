package com.donsang.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe représentant un receveur de sang ou d'organe
 */
public class Receveur implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private TypeDeSang groupeSanguin;
    private LocalDate dateNaissance;
    private String adresse;
    private String ville;
    private String codePostal;
    private TypeBesoin typeBesoin;
    private String organeNecessaire; // Si besoin d'organe
    private UrgenceNiveau urgence;
    private String detailsMedicaux;
    private StatutReceveur statut;
    private LocalDate dateInscription;
    private LocalDate dateReceptionDon;
    private List<Long> donsRecusIds; // IDs des dons reçus
    private String observations;
    
    // Énumération pour le type de besoin
    public enum TypeBesoin {
        SANG("Sang"),
        ORGANE("Organe"),
        SANG_ET_ORGANE("Sang et Organe");
        
        private final String libelle;
        
        TypeBesoin(String libelle) {
            this.libelle = libelle;
        }
        
        public String getLibelle() {
            return libelle;
        }
    }
    
    // Énumération pour le niveau d'urgence
    public enum UrgenceNiveau {
        FAIBLE("Faible", 1),
        MOYEN("Moyen", 2),
        ELEVE("Élevé", 3),
        CRITIQUE("Critique", 4);
        
        private final String libelle;
        private final int priorite;
        
        UrgenceNiveau(String libelle, int priorite) {
            this.libelle = libelle;
            this.priorite = priorite;
        }
        
        public String getLibelle() {
            return libelle;
        }
        
        public int getPriorite() {
            return priorite;
        }
    }
    
    // Énumération pour le statut du receveur
    public enum StatutReceveur {
        EN_ATTENTE("En attente"),
        BESOIN_SATISFAIT("Besoin satisfait"),
        EN_TRAITEMENT("En traitement"),
        TRANSFERE("Transféré"),
        ARCHIVE("Archivé");
        
        private final String libelle;
        
        StatutReceveur(String libelle) {
            this.libelle = libelle;
        }
        
        public String getLibelle() {
            return libelle;
        }
    }
    
    // Constructeur par défaut
    public Receveur() {
        this.donsRecusIds = new ArrayList<>();
        this.dateInscription = LocalDate.now();
        this.statut = StatutReceveur.EN_ATTENTE;
        this.urgence = UrgenceNiveau.MOYEN;
    }
    
    // Constructeur avec paramètres essentiels
    public Receveur(String nom, String prenom, String email, String telephone,
                    TypeDeSang groupeSanguin, TypeBesoin typeBesoin, UrgenceNiveau urgence) {
        this();
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.groupeSanguin = groupeSanguin;
        this.typeBesoin = typeBesoin;
        this.urgence = urgence;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public TypeDeSang getGroupeSanguin() {
        return groupeSanguin;
    }
    
    public void setGroupeSanguin(TypeDeSang groupeSanguin) {
        this.groupeSanguin = groupeSanguin;
    }
    
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }
    
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public String getVille() {
        return ville;
    }
    
    public void setVille(String ville) {
        this.ville = ville;
    }
    
    public String getCodePostal() {
        return codePostal;
    }
    
    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }
    
    public TypeBesoin getTypeBesoin() {
        return typeBesoin;
    }
    
    public void setTypeBesoin(TypeBesoin typeBesoin) {
        this.typeBesoin = typeBesoin;
    }
    
    public String getOrganeNecessaire() {
        return organeNecessaire;
    }
    
    public void setOrganeNecessaire(String organeNecessaire) {
        this.organeNecessaire = organeNecessaire;
    }
    
    public UrgenceNiveau getUrgence() {
        return urgence;
    }
    
    public void setUrgence(UrgenceNiveau urgence) {
        this.urgence = urgence;
    }
    
    public String getDetailsMedicaux() {
        return detailsMedicaux;
    }
    
    public void setDetailsMedicaux(String detailsMedicaux) {
        this.detailsMedicaux = detailsMedicaux;
    }
    
    public StatutReceveur getStatut() {
        return statut;
    }
    
    public void setStatut(StatutReceveur statut) {
        this.statut = statut;
    }
    
    public LocalDate getDateInscription() {
        return dateInscription;
    }
    
    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }
    
    public LocalDate getDateReceptionDon() {
        return dateReceptionDon;
    }
    
    public void setDateReceptionDon(LocalDate dateReceptionDon) {
        this.dateReceptionDon = dateReceptionDon;
    }
    
    public List<Long> getDonsRecusIds() {
        return donsRecusIds;
    }
    
    public void setDonsRecusIds(List<Long> donsRecusIds) {
        this.donsRecusIds = donsRecusIds;
    }
    
    public String getObservations() {
        return observations;
    }
    
    public void setObservations(String observations) {
        this.observations = observations;
    }
    
    // Méthodes utilitaires
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    public int getAge() {
        if (dateNaissance != null) {
            return LocalDate.now().getYear() - dateNaissance.getYear();
        }
        return 0;
    }
    
    public void ajouterDonRecu(Long donId) {
        if (!donsRecusIds.contains(donId)) {
            donsRecusIds.add(donId);
        }
    }
    
    public boolean estBesoinSatisfait() {
        return statut == StatutReceveur.BESOIN_SATISFAIT;
    }
    
    public int getNombreDonsRecus() {
        return donsRecusIds.size();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receveur receveur = (Receveur) o;
        return Objects.equals(id, receveur.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Receveur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", groupeSanguin=" + groupeSanguin +
                ", typeBesoin=" + typeBesoin +
                ", urgence=" + urgence +
                ", statut=" + statut +
                '}';
    }
}
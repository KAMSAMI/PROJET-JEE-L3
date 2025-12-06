package com.donsang.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Classe représentant un don de sang ou d'organe
 */
public class Don implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private TypeDon typeDon;
    private LocalDate dateDon;
    private LocalDateTime heurePrelevee;
    private Long donneurId;
    private Long receveurId; // Peut être null si pas encore attribué
    private String lieuDon;
    private StatutDon statut;
    private double quantite; // En ml pour le sang, non applicable pour organe
    private String organe; // Type d'organe si applicable
    private String observations;
    private LocalDate dateProchainDonAutorise; // Pour les donneurs de sang
    private String numeroReference;
    private boolean compatible;
    
    // Énumération pour le type de don
    public enum TypeDon {
        SANG_TOTAL("Sang Total"),
        PLASMA("Plasma"),
        PLAQUETTES("Plaquettes"),
        ORGANE_REIN("Organe - Rein"),
        ORGANE_FOIE("Organe - Foie"),
        ORGANE_COEUR("Organe - Cœur"),
        ORGANE_POUMON("Organe - Poumon"),
        ORGANE_PANCREAS("Organe - Pancréas"),
        ORGANE_CORNEE("Organe - Cornée"),
        ORGANE_AUTRE("Organe - Autre");
        
        private final String libelle;
        
        TypeDon(String libelle) {
            this.libelle = libelle;
        }
        
        public String getLibelle() {
            return libelle;
        }
        
        public boolean estDonSang() {
            return this == SANG_TOTAL || this == PLASMA || this == PLAQUETTES;
        }
        
        public boolean estDonOrgane() {
            return !estDonSang();
        }
    }
    
    // Énumération pour le statut du don
    public enum StatutDon {
        PROGRAMME("Programmé"),
        EN_COURS("En cours"),
        COMPLETE("Complété"),
        ATTRIBUE("Attribué à un receveur"),
        UTILISE("Utilisé"),
        ANNULE("Annulé"),
        EXPIRE("Expiré"),
        EN_STOCK("En stock");
        
        private final String libelle;
        
        StatutDon(String libelle) {
            this.libelle = libelle;
        }
        
        public String getLibelle() {
            return libelle;
        }
    }
    
    // Constructeur par défaut
    public Don() {
        this.statut = StatutDon.PROGRAMME;
        this.compatible = false;
    }
    
    // Constructeur avec paramètres essentiels
    public Don(TypeDon typeDon, LocalDate dateDon, Long donneurId) {
        this();
        this.typeDon = typeDon;
        this.dateDon = dateDon;
        this.donneurId = donneurId;
        this.numeroReference = genererNumeroReference();
        
        // Calculer la date du prochain don autorisé pour le sang
        if (typeDon.estDonSang()) {
            calculerProchainDonAutorise();
        }
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public TypeDon getTypeDon() {
        return typeDon;
    }
    
    public void setTypeDon(TypeDon typeDon) {
        this.typeDon = typeDon;
    }
    
    public LocalDate getDateDon() {
        return dateDon;
    }
    
    public void setDateDon(LocalDate dateDon) {
        this.dateDon = dateDon;
    }
    
    public LocalDateTime getHeurePrelevee() {
        return heurePrelevee;
    }
    
    public void setHeurePrelevee(LocalDateTime heurePrelevee) {
        this.heurePrelevee = heurePrelevee;
    }
    
    public Long getDonneurId() {
        return donneurId;
    }
    
    public void setDonneurId(Long donneurId) {
        this.donneurId = donneurId;
    }
    
    public Long getReceveurId() {
        return receveurId;
    }
    
    public void setReceveurId(Long receveurId) {
        this.receveurId = receveurId;
    }
    
    public String getLieuDon() {
        return lieuDon;
    }
    
    public void setLieuDon(String lieuDon) {
        this.lieuDon = lieuDon;
    }
    
    public StatutDon getStatut() {
        return statut;
    }
    
    public void setStatut(StatutDon statut) {
        this.statut = statut;
    }
    
    public double getQuantite() {
        return quantite;
    }
    
    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }
    
    public String getOrgane() {
        return organe;
    }
    
    public void setOrgane(String organe) {
        this.organe = organe;
    }
    
    public String getObservations() {
        return observations;
    }
    
    public void setObservations(String observations) {
        this.observations = observations;
    }
    
    public LocalDate getDateProchainDonAutorise() {
        return dateProchainDonAutorise;
    }
    
    public void setDateProchainDonAutorise(LocalDate dateProchainDonAutorise) {
        this.dateProchainDonAutorise = dateProchainDonAutorise;
    }
    
    public String getNumeroReference() {
        return numeroReference;
    }
    
    public void setNumeroReference(String numeroReference) {
        this.numeroReference = numeroReference;
    }
    
    public boolean isCompatible() {
        return compatible;
    }
    
    public void setCompatible(boolean compatible) {
        this.compatible = compatible;
    }
    
    // Méthodes utilitaires
    private String genererNumeroReference() {
        return "DON-" + System.currentTimeMillis();
    }
    
    /**
     * Calcule la date du prochain don autorisé selon le type de don
     */
    private void calculerProchainDonAutorise() {
        if (dateDon != null && typeDon != null) {
            switch (typeDon) {
                case SANG_TOTAL:
                    // 8 semaines pour les hommes, 12 pour les femmes (on prend 12 par défaut)
                    dateProchainDonAutorise = dateDon.plusWeeks(12);
                    break;
                case PLASMA:
                    // 2 semaines minimum
                    dateProchainDonAutorise = dateDon.plusWeeks(2);
                    break;
                case PLAQUETTES:
                    // 4 semaines minimum
                    dateProchainDonAutorise = dateDon.plusWeeks(4);
                    break;
                default:
                    dateProchainDonAutorise = null;
            }
        }
    }
    
    /**
     * Vérifie si le don est encore valide (non expiré)
     */
    public boolean estValide() {
        if (statut == StatutDon.EXPIRE || statut == StatutDon.ANNULE) {
            return false;
        }
        
        if (typeDon.estDonSang() && dateDon != null) {
            // Le sang a une durée de conservation limitée
            LocalDate dateExpiration;
            switch (typeDon) {
                case SANG_TOTAL:
                    dateExpiration = dateDon.plusDays(42); // 42 jours
                    break;
                case PLASMA:
                    dateExpiration = dateDon.plusYears(1); // 1 an congelé
                    break;
                case PLAQUETTES:
                    dateExpiration = dateDon.plusDays(5); // 5 jours
                    break;
                default:
                    return true;
            }
            return LocalDate.now().isBefore(dateExpiration);
        }
        
        return true;
    }
    
    /**
     * Attribue le don à un receveur
     */
    public void attribuerReceveur(Long receveurId) {
        this.receveurId = receveurId;
        this.statut = StatutDon.ATTRIBUE;
    }
    
    /**
     * Marque le don comme utilisé
     */
    public void marquerUtilise() {
        this.statut = StatutDon.UTILISE;
    }
    
    /**
     * Annule le don
     */
    public void annuler() {
        this.statut = StatutDon.ANNULE;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Don don = (Don) o;
        return Objects.equals(id, don.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Don{" +
                "id=" + id +
                ", typeDon=" + typeDon +
                ", dateDon=" + dateDon +
                ", donneurId=" + donneurId +
                ", receveurId=" + receveurId +
                ", statut=" + statut +
                ", numeroReference='" + numeroReference + '\'' +
                '}';
    }
}
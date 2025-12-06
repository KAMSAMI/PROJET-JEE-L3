package com.donsang.servlet;

import com.donsang.model.Donneur;
import com.donsang.model.TypeDeSang;
import com.donsang.service.DonneurService;
import com.donsang.util.ValidationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servlet pour gérer les opérations CRUD sur les donneurs
 */
public class DonneurServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private DonneurService donneurService;
    
    @Override
    public void init() throws ServletException {
        donneurService = DonneurService.getInstance();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        try {
            switch (action) {
                case "list":
                    listerDonneurs(request, response);
                    break;
                case "new":
                    afficherFormulaireAjout(request, response);
                    break;
                case "edit":
                    afficherFormulaireModification(request, response);
                    break;
                case "delete":
                    supprimerDonneur(request, response);
                    break;
                case "view":
                    afficherDonneur(request, response);
                    break;
                case "search":
                    rechercherDonneurs(request, response);
                    break;
                default:
                    listerDonneurs(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/pages/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            if ("insert".equals(action)) {
                ajouterDonneur(request, response);
            } else if ("update".equals(action)) {
                modifierDonneur(request, response);
            }
        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("donneur", construireDonneurDepuisRequete(request));
            request.getRequestDispatcher("/pages/donneur/form.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Erreur: " + e.getMessage());
            request.getRequestDispatcher("/pages/error.jsp").forward(request, response);
        }
    }
    
    private void listerDonneurs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Donneur> donneurs = donneurService.getTousDonneurs();
        request.setAttribute("donneurs", donneurs);
        request.setAttribute("totalDonneurs", donneurService.compterDonneurs());
        request.setAttribute("donneursActifs", donneurService.compterDonneursActifs());
        request.getRequestDispatcher("/pages/donneur/list.jsp").forward(request, response);
    }
    
    private void afficherFormulaireAjout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("typesSanguins", TypeDeSang.values());
        request.getRequestDispatcher("/pages/donneur/form.jsp").forward(request, response);
    }
    
    private void afficherFormulaireModification(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Optional<Donneur> donneur = donneurService.getDonneur(id);
        
        if (donneur.isPresent()) {
            request.setAttribute("donneur", donneur.get());
            request.setAttribute("typesSanguins", TypeDeSang.values());
            request.getRequestDispatcher("/pages/donneur/form.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Donneur introuvable");
            listerDonneurs(request, response);
        }
    }
    
    private void afficherDonneur(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Optional<Donneur> donneur = donneurService.getDonneur(id);
        
        if (donneur.isPresent()) {
            request.setAttribute("donneur", donneur.get());
            request.getRequestDispatcher("/pages/donneur/view.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Donneur introuvable");
            listerDonneurs(request, response);
        }
    }
    
    private void ajouterDonneur(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ValidationException {
        Donneur donneur = construireDonneurDepuisRequete(request);
        donneurService.creerDonneur(donneur);
        response.sendRedirect(request.getContextPath() + "/donneur?action=list&success=create");
    }
    
    private void modifierDonneur(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ValidationException {
        Donneur donneur = construireDonneurDepuisRequete(request);
        Long id = Long.parseLong(request.getParameter("id"));
        donneur.setId(id);
        donneurService.modifierDonneur(donneur);
        response.sendRedirect(request.getContextPath() + "/donneur?action=list&success=update");
    }
    
    private void supprimerDonneur(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ValidationException {
        Long id = Long.parseLong(request.getParameter("id"));
        donneurService.supprimerDonneur(id);
        response.sendRedirect(request.getContextPath() + "/donneur?action=list&success=delete");
    }
    
    private void rechercherDonneurs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String typeRecherche = request.getParameter("searchType");
        String valeurRecherche = request.getParameter("searchValue");
        
        List<Donneur> donneurs;
        
        try {
            if ("nom".equals(typeRecherche)) {
                donneurs = donneurService.rechercherParNom(valeurRecherche);
            } else if ("groupeSanguin".equals(typeRecherche)) {
                TypeDeSang groupeSanguin = TypeDeSang.fromDesignation(valeurRecherche);
                donneurs = donneurService.rechercherParGroupeSanguin(groupeSanguin);
            } else {
                donneurs = donneurService.getTousDonneurs();
            }
            
            request.setAttribute("donneurs", donneurs);
            request.setAttribute("searchPerformed", true);
            request.getRequestDispatcher("/pages/donneur/list.jsp").forward(request, response);
        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            listerDonneurs(request, response);
        }
    }
    
    private Donneur construireDonneurDepuisRequete(HttpServletRequest request) {
        Donneur donneur = new Donneur();
        
        donneur.setNom(request.getParameter("nom"));
        donneur.setPrenom(request.getParameter("prenom"));
        donneur.setEmail(request.getParameter("email"));
        donneur.setTelephone(request.getParameter("telephone"));
        
        String groupeSanguinStr = request.getParameter("groupeSanguin");
        if (groupeSanguinStr != null && !groupeSanguinStr.isEmpty()) {
            donneur.setGroupeSanguin(TypeDeSang.fromDesignation(groupeSanguinStr));
        }
        
        String dateNaissanceStr = request.getParameter("dateNaissance");
        if (dateNaissanceStr != null && !dateNaissanceStr.isEmpty()) {
            donneur.setDateNaissance(LocalDate.parse(dateNaissanceStr));
        }
        
        donneur.setAdresse(request.getParameter("adresse"));
        donneur.setVille(request.getParameter("ville"));
        donneur.setCodePostal(request.getParameter("codePostal"));
        donneur.setObservations(request.getParameter("observations"));
        
        String actifStr = request.getParameter("actif");
        donneur.setActif(actifStr != null && "on".equals(actifStr));
        
        return donneur;
    }
}
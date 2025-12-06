package com.donsang.servlet;

import com.donsang.model.Don;
import com.donsang.model.Don.*;
import com.donsang.service.DonService;
import com.donsang.service.DonneurService;
import com.donsang.service.ReceveurService;
import com.donsang.util.ValidationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class DonServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DonService donService;
    private DonneurService donneurService;
    private ReceveurService receveurService;
    
    @Override
    public void init() throws ServletException {
        donService = DonService.getInstance();
        donneurService = DonneurService.getInstance();
        receveurService = ReceveurService.getInstance();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";
        
        try {
            switch (action) {
                case "list": listerDons(request, response); break;
                case "new": afficherFormulaireAjout(request, response); break;
                case "edit": afficherFormulaireModification(request, response); break;
                case "delete": annulerDon(request, response); break;
                case "view": afficherDon(request, response); break;
                case "attribuer": afficherFormulaireAttribution(request, response); break;
                default: listerDons(request, response);
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
                ajouterDon(request, response);
            } else if ("update".equals(action)) {
                modifierDon(request, response);
            } else if ("attribuer".equals(action)) {
                attribuerDon(request, response);
            } else if ("marquerUtilise".equals(action)) {
                marquerUtilise(request, response);
            }
        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("don", construireDonDepuisRequete(request));
            request.getRequestDispatcher("/pages/don/form.jsp").forward(request, response);
        }
    }
    
    private void listerDons(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Don> dons = donService.getTousDons();
        request.setAttribute("dons", dons);
        request.setAttribute("totalDons", donService.compterDons());
        request.setAttribute("donsMoisCourant", donService.compterDonsDuMois());
        request.setAttribute("quantiteSangDisponible", donService.getQuantiteSangDisponible());
        request.getRequestDispatcher("/pages/don/list.jsp").forward(request, response);
    }
    
    private void afficherFormulaireAjout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("typesDon", TypeDon.values());
        request.setAttribute("statutsDon", StatutDon.values());
        request.setAttribute("donneurs", donneurService.getDonneursActifs());
        request.getRequestDispatcher("/pages/don/form.jsp").forward(request, response);
    }
    
    private void afficherFormulaireModification(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Optional<Don> don = donService.getDon(id);
        
        if (don.isPresent()) {
            request.setAttribute("don", don.get());
            request.setAttribute("typesDon", TypeDon.values());
            request.setAttribute("statutsDon", StatutDon.values());
            request.setAttribute("donneurs", donneurService.getTousDonneurs());
            request.getRequestDispatcher("/pages/don/form.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Don introuvable");
            listerDons(request, response);
        }
    }
    
    private void afficherFormulaireAttribution(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Optional<Don> don = donService.getDon(id);
        
        if (don.isPresent()) {
            request.setAttribute("don", don.get());
            request.setAttribute("receveurs", receveurService.getReceveursParPriorite());
            request.getRequestDispatcher("/pages/don/attribuer.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Don introuvable");
            listerDons(request, response);
        }
    }
    
    private void afficherDon(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Optional<Don> don = donService.getDon(id);
        
        if (don.isPresent()) {
            request.setAttribute("don", don.get());
            
            // Charger le donneur
            donneurService.getDonneur(don.get().getDonneurId())
                    .ifPresent(d -> request.setAttribute("donneur", d));
            
            // Charger le receveur si attribué
            if (don.get().getReceveurId() != null) {
                receveurService.getReceveur(don.get().getReceveurId())
                        .ifPresent(r -> request.setAttribute("receveur", r));
            }
            
            request.getRequestDispatcher("/pages/don/view.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Don introuvable");
            listerDons(request, response);
        }
    }
    
    private void ajouterDon(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ValidationException {
        Don don = construireDonDepuisRequete(request);
        donService.creerDon(don);
        response.sendRedirect(request.getContextPath() + "/don?action=list&success=create");
    }
    
    private void modifierDon(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ValidationException {
        Don don = construireDonDepuisRequete(request);
        Long id = Long.parseLong(request.getParameter("id"));
        don.setId(id);
        donService.modifierDon(don);
        response.sendRedirect(request.getContextPath() + "/don?action=list&success=update");
    }
    
    private void annulerDon(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ValidationException {
        Long id = Long.parseLong(request.getParameter("id"));
        donService.annulerDon(id);
        response.sendRedirect(request.getContextPath() + "/don?action=list&success=cancel");
    }
    
    private void attribuerDon(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ValidationException {
        Long donId = Long.parseLong(request.getParameter("donId"));
        Long receveurId = Long.parseLong(request.getParameter("receveurId"));
        donService.attribuerDon(donId, receveurId);
        response.sendRedirect(request.getContextPath() + "/don?action=view&id=" + donId + "&success=attribue");
    }
    
    private void marquerUtilise(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ValidationException {
        Long id = Long.parseLong(request.getParameter("id"));
        donService.marquerDonUtilise(id);
        response.sendRedirect(request.getContextPath() + "/don?action=view&id=" + id + "&success=utilise");
    }
    
    private Don construireDonDepuisRequete(HttpServletRequest request) {
        Don don = new Don();
        
        String typeDonStr = request.getParameter("typeDon");
        if (typeDonStr != null && !typeDonStr.isEmpty()) {
            don.setTypeDon(TypeDon.valueOf(typeDonStr));
        }
        
        String dateDonStr = request.getParameter("dateDon");
        if (dateDonStr != null && !dateDonStr.isEmpty()) {
            don.setDateDon(LocalDate.parse(dateDonStr));
        }
        
        String donneurIdStr = request.getParameter("donneurId");
        if (donneurIdStr != null && !donneurIdStr.isEmpty()) {
            don.setDonneurId(Long.parseLong(donneurIdStr));
        }
        
        String statutStr = request.getParameter("statut");
        if (statutStr != null && !statutStr.isEmpty()) {
            don.setStatut(StatutDon.valueOf(statutStr));
        }
        
        String quantiteStr = request.getParameter("quantite");
        if (quantiteStr != null && !quantiteStr.isEmpty()) {
            don.setQuantite(Double.parseDouble(quantiteStr));
        }
        
        don.setLieuDon(request.getParameter("lieuDon"));
        don.setOrgane(request.getParameter("organe"));
        don.setObservations(request.getParameter("observations"));
        
        return don;
    }
}
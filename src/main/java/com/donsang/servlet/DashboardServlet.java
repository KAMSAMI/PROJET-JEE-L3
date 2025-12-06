package com.donsang.servlet;

import com.donsang.service.DonService;
import com.donsang.service.DonneurService;
import com.donsang.service.ReceveurService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DashboardServlet extends HttpServlet {
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
        
        // Statistiques générales
        request.setAttribute("totalDonneurs", donneurService.compterDonneurs());
        request.setAttribute("donneursActifs", donneurService.compterDonneursActifs());
        request.setAttribute("totalReceveurs", receveurService.compterReceveurs());
        request.setAttribute("receveursEnAttente", receveurService.compterReceveursEnAttente());
        request.setAttribute("receveursCritiques", receveurService.compterReceveursCritiques());
        request.setAttribute("totalDons", donService.compterDons());
        request.setAttribute("donsMoisCourant", donService.compterDonsDuMois());
        request.setAttribute("quantiteSangDisponible", donService.getQuantiteSangDisponible());
        
        // Listes récentes
        request.setAttribute("donsRecents", donService.getDonsRecents(5));
        request.setAttribute("topDonneurs", donneurService.getTopDonneurs(5));
        request.setAttribute("receveursUrgents", receveurService.getReceveursCritiques());
        request.setAttribute("donsDisponibles", donService.getDonsDisponibles().size());
        
        request.getRequestDispatcher("/pages/dashboard.jsp").forward(request, response);
    }
}
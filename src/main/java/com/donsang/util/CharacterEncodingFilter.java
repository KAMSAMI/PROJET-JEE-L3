package com.donsang.util;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filtre pour forcer l'encodage UTF-8
 */
public class CharacterEncodingFilter implements Filter {
    
    private String encoding = "UTF-8";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String encodingParam = filterConfig.getInitParameter("encoding");
        if (encodingParam != null) {
            this.encoding = encodingParam;
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Nettoyage si nécessaire
    }
}
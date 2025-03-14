package br.edu.unifio.banco;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@WebFilter("/banco/*")
public class FiltroAutenticacao implements Filter {
    private static final String USER = "user";
    private static final String PASS = "pass";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] values = credentials.split(":", 2);
            if (values.length == 2 && USER.equals(values[0]) && PASS.equals(values[1])) {
                chain.doFilter(request, response);
                return;
            }
        }

        httpResponse.setHeader("WWW-Authenticate", "Basic realm=\"BancoServlet\"");
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Autenticação necessária");
    }
}
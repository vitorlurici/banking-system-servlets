package br.edu.unifio.banco;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/banco/*")
public class BancoServlet extends HttpServlet {
    private static Map<Integer, Conta> contas = new HashMap<>();

    static {
        contas.put(1001, new Conta(1001, 1000.00));
        contas.put(2002, new Conta(2002, 1000.00));
        contas.put(3003, new Conta(3003, 1000.00));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        int numeroConta = Integer.parseInt(req.getParameter("conta"));

        Conta conta = contas.get(numeroConta);
        if (conta == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Conta não encontrada");
            return;
        }

        switch (pathInfo) {
            case "/sacar":
                try {
                    double valorSaque = Double.parseDouble(req.getParameter("valor"));
                    conta.sacar(valorSaque);
                    resp.getWriter().write("Saque realizado com sucesso. Saldo atual: " + conta.getSaldo());
                } catch (IllegalArgumentException e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                }
                break;
            case "/depositar":
                try {
                    double valorDeposito = Double.parseDouble(req.getParameter("valor"));
                    conta.depositar(valorDeposito);
                    resp.getWriter().write("Depósito realizado com sucesso. Saldo atual: " + conta.getSaldo());
                } catch (IllegalArgumentException e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                }
                break;
            case "/consultar":
                resp.getWriter().write("Saldo atual: " + conta.getSaldo());
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint não encontrado");
        }
    }
}
package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        pageVariables.put("message", "Hello world");
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", pageVariables));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        resp.setStatus(200);
        pageVariables.put("message", "Client not add");
        String name = req.getParameter("name");
        String pass = req.getParameter("password");
        String moneyStr = req.getParameter("money");
        if (!name.equals("")&& !pass.equals("") && !moneyStr.equals("")) {
            long money = Long.parseLong(moneyStr);
            BankClient bankClient = new BankClient(name, pass, money);
            BankClientService service = new BankClientService();
            try {
                if (!service.clientExist(name)) {
                    service.addClient(bankClient);
                    resp.setStatus(200);
                    pageVariables.put("message", "Add client successful");
                }
            } catch (DBException e) {
                e.getStackTrace();
            }
        }
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        return pageVariables;
    }
}

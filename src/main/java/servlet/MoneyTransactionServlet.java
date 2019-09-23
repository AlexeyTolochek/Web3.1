package servlet;

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

public class MoneyTransactionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        pageVariables.put("message", "Hell o Yea");
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", pageVariables));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        String senderName = req.getParameter("senderName");
        String senderPass = req.getParameter("senderPass");
        String countStr = req.getParameter("count");
        String nameTo = req.getParameter("nameTo");
        resp.setStatus(200);
        pageVariables.put("message", "transaction rejected");

        if (!senderName.equals("") && !senderPass.equals("") && !countStr.equals("") && !nameTo.equals("")) {
            long count = Long.parseLong(countStr);
            BankClientService service = new BankClientService();
            BankClient client = service.getClientByName(senderName);
            if (service.clientExist(senderName,senderPass) && count > 0 && service.sendMoneyToClient(client, nameTo, count)) {
                resp.setStatus(200);
                pageVariables.put("message", "The transaction was successful");
            }
        }
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("name", request.getParameter("name"));
        pageVariables.put("password", request.getParameter("password"));
        pageVariables.put("money", request.getParameter("money"));
        pageVariables.put("senderName", request.getParameter("senderName"));
        pageVariables.put("senderPass", request.getParameter("senderPass"));
        pageVariables.put("count", request.getParameter("count"));
        pageVariables.put("nameTo", request.getParameter("nameTo"));
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        return pageVariables;
    }
}

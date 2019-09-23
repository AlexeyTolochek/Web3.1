package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BankClientService {


    public BankClient getClientById(long id) throws DBException {
        try {
            return getBankClientDAO().getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public BankClient getClientByName(String name) {
        BankClient client = null;
        try {
            client = getBankClientDAO().getClientByName(name);
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return client;
    }

    public List<BankClient> getAllClient() {
        List<BankClient> list = null;
        try {
            list = getBankClientDAO().getAllBankClient();
        }
        catch (SQLException e) {
            e.getStackTrace();
        }
        return list;
    }

    public boolean deleteClient(String name) {

        return false;
    }
    public boolean clientExist(String name) {
            if (getClientByName(name)!= null) {
                return true;
            }
        return false;
    }

    public boolean clientExist(String name, String password) {
            if (getBankClientDAO().validateClient(name, password)) {
                return true;
            }
        return false;
    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) {
        BankClientDAO dao = getBankClientDAO();
        String senderName = sender.getName();
        String senderPass = sender.getPassword();
        if (clientExist(senderName, senderPass) && value > 0) {
            BankClient clientTo = null;
            clientTo = getClientByName(name);
            if (clientTo != null && isClientHasSum(senderName, value)) {
                try {
                    dao.updateClientsMoney(senderName, senderPass, -value);
                    dao.updateClientsMoney(name, clientTo.getPassword(), value);
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
    public void createTable() throws DBException{
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver)Class.forName("com.mysql.cj.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("web3?").                //db name
                    append("user=root&").          //login
                    append("password=root").         //password
                    append("&serverTimezone=UTC");   //setup server time

            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }

    public void addClient(BankClient client) throws DBException{
        if (!clientExist(client.getName())) {
            try {
                getBankClientDAO().addClient(client);
            } catch (SQLException e) {
                throw new DBException(e);
            }
        }
    }

    public boolean isClientHasSum(String name, Long money) {
        try {
           return getBankClientDAO().isClientHasSum(name, money);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

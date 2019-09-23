package dao;

import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        List<BankClient> list = new ArrayList<>();
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client");
        ResultSet result = stmt.getResultSet();
        while (result.next()) {
            long id = result.getLong(1);
            String name = result.getNString(2);
            String pass = result.getNString(3);
            long money = result.getLong(4);
            BankClient client = new BankClient(id, name, pass, money);
            list.add(client);
        }
        result.close();
        stmt.close();
        return list;
    }

    public boolean validateClient(String name, String password) {
        try {
            BankClient client = getClientByName(name);
            if (client.getPassword().equals(password)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        if (validateClient(name, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from bank_client where name=?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Long money = resultSet.getLong(4);
            money += transactValue;
            preparedStatement = connection.prepareStatement("UPDATE bank_client SET money = ? WHERE name LIKE ?");
            preparedStatement.setLong(1, money);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
            resultSet.close();;
            preparedStatement.close();
        }

    }

    public BankClient getClientById(long id) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where id='" + id + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        String name = result.getNString(2);
        String pass = result.getNString(3);
        Long money = result.getLong(4);
        BankClient client = new BankClient(name, pass, money);
        result.close();
        stmt.close();
        return client;
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long money = result.getLong(4);
        result.close();
        stmt.close();
        return expectedSum <= money;
    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public BankClient getClientByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        String nameclient = result.getNString(2);
        String pass = result.getNString(3);
        Long money = result.getLong(4);
        BankClient client = new BankClient(nameclient, pass, money);
        result.close();
        stmt.close();
        return client;
    }

    public void addClient(BankClient client) throws SQLException {
        String sql = "INSERT INTO bank_client(name, password, money) values (?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, client.getName());
        statement.setString(2, client.getPassword());
        statement.setLong(3, client.getMoney());
        statement.executeUpdate();
        statement.close();
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
            stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }


}

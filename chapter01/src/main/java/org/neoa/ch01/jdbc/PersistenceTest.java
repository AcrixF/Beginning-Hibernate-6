package org.neoa.ch01.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.neoa.ch01.pojo.Message;
import org.neoa.ch01.pojo.MessageEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class PersistenceTest {

    Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", "root", "Neoa2912");
    }

    @BeforeClass
    public void setup() {

        final String DROP = "DROP TABLE IF EXISTS messages";
        final String CREATE = "CREATE TABLE messages ("
                + "id int NOT NULL AUTO_INCREMENT,"
                + "text VARCHAR(256),"
                + "PRIMARY KEY(id))";

        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(DROP)) {
                ps.execute();
            }

            try (PreparedStatement ps = connection.prepareStatement(CREATE)) {
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public MessageEntity saveMessage(String text) {
        final String INSERT = "INSERT INTO messages(text) values (?)";
        MessageEntity message = null;

        try(Connection connection = getConnection()) {
            try(PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, text);
                ps.execute();
                try(ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("No Generated Keys");
                    }
                    message = new MessageEntity(keys.getLong(1), text);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return message;
    }

    @Test
    public void readMessage() {
        final String text = "Hello, World!";

        MessageEntity message = saveMessage(text);

        final String SELECT = "SELECT id, text FROM messages";

        List<MessageEntity> list = new ArrayList<>();

        try(Connection connection = getConnection()) {
            try(PreparedStatement ps = connection.prepareStatement(SELECT)) {
                try(ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        MessageEntity newMessage = new MessageEntity();
                        newMessage.setId(rs.getLong(1));
                        newMessage.setText(rs.getString(2));
                        list.add(message);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        assertEquals(list.size(), 1);
        for (MessageEntity m: list) {
            System.out.println(m);
        }
        assertEquals(list.get(0), message);
    }

}

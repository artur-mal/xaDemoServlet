package com.example.demoservlet;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.Transactional;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try {
            executeUpdates();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    @Transactional
    public void executeUpdates() throws SQLException, NamingException {
        localUpdate();
        remoteUpdate();
    }

    private void remoteUpdate() throws NamingException {

        InitialContext initialContext = null;

            initialContext = new InitialContext();
            DataSource dataSourceRemote = (DataSource) initialContext.lookup("My_Sql_Remote");

            Connection connectionRemote = null;

            PreparedStatement statementRemote;
        try {
            connectionRemote = dataSourceRemote.getConnection();



        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());


         //   statementRemote = connectionRemote.prepareStatement("UPDATE PERSON " +
            statementRemote = connectionRemote.prepareStatement("UPDATE PERSON " +
                    "SET NAME = ? " +
                    "WHERE ID = ?");
            statementRemote.setString(1, timeStamp);
            statementRemote.setLong(2, 1L);
            statementRemote.executeUpdate();

            statementRemote.close();

            connectionRemote.close();

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    }
    private void localUpdate() throws  NamingException {

            InitialContext initialContext = null;

                initialContext = new InitialContext();
                DataSource dataSourceLocal = (DataSource) initialContext.lookup("My_Sql");


                Connection connectionLocal = null;

                PreparedStatement statementLocal;
        try {
            connectionLocal = dataSourceLocal.getConnection();


        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());


                statementLocal = connectionLocal.prepareStatement("UPDATE PERSON " +
                        "SET NAME = ? " +
                        "WHERE ID = ?");
                statementLocal.setString(1, timeStamp);
                statementLocal.setLong(2, 1L);
                statementLocal.executeUpdate();

                statementLocal.close();

                connectionLocal.close();

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

}

    public void destroy() {
    }
}
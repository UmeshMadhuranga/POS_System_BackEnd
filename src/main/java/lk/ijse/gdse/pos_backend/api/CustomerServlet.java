package lk.ijse.gdse.pos_backend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.gdse.pos_backend.convertor.Convertor;
import lk.ijse.gdse.pos_backend.dao.DaoFactory;
import lk.ijse.gdse.pos_backend.dao.DaoType;
import lk.ijse.gdse.pos_backend.dao.custom.CustomerDAO;
import lk.ijse.gdse.pos_backend.dto.CustomerDTO;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerServlet extends HttpServlet {
    private Connection connection;
    private CustomerDAO customerDAO;
    private Convertor convertor;

    @Override
    public void init() throws ServletException {
        try {

            InitialContext initialContext = new InitialContext();
            DataSource pool = (DataSource) initialContext.lookup("java:comp/env/jdbc/pos");
            this.connection = pool.getConnection();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.convertor = new Convertor();
        this.customerDAO = DaoFactory.getInstance().getDao(DaoType.CUSTOMER, connection);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");

        Jsonb jsonb = JsonbBuilder.create();

        try {

            List<CustomerDTO> list = customerDAO.getAll().stream().map(customer -> convertor.fromCustomer(customer)).collect(Collectors.toList());
            response.setStatus(HttpServletResponse.SC_ACCEPTED);

            jsonb.toJson(new Response(200, "Done", list), response.getWriter());

        } catch (Exception e) {
            jsonb.toJson(new Response(400, "Error", e.getLocalizedMessage()), response.getWriter());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getContentType() == null || !request.getContentType().toLowerCase().startsWith("application/json")) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

        response.setContentType("application/json");

        Jsonb jsonb = JsonbBuilder.create();
        CustomerDTO customerDTO = jsonb.fromJson(request.getReader(), CustomerDTO.class);

        try {

            if (customerDAO.save(convertor.toCustomer(customerDTO)) != null) {
                jsonb.toJson(new Response(200, "Successfully added..", ""), response.getWriter());
            }

        } catch (Exception exception) {
            jsonb.toJson(new Response(400, "Error !", exception.getLocalizedMessage()), response.getWriter());
            exception.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getContentType() == null || !request.getContentType().toLowerCase().startsWith("application/json")) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

        response.setContentType("application/json");

        Jsonb jsonb = JsonbBuilder.create();
        CustomerDTO customerDTO = jsonb.fromJson(request.getReader(), CustomerDTO.class);

        try {

            if (customerDAO.update(convertor.toCustomer(customerDTO)) != null) {
                jsonb.toJson(new Response(200, "Successfully update..", ""), response.getWriter());
            }

        } catch (Exception e) {
            jsonb.toJson(new Response(400, "Error !", e.getLocalizedMessage()), response.getWriter());
            e.printStackTrace();
        }


    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getContentType() == null || !request.getContentType().toLowerCase().startsWith("application/json")) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

        response.setContentType("application/json");

        Jsonb jsonb = JsonbBuilder.create();
        String customerId = request.getParameter("customerId");

        try {
            customerDAO.delete(customerId);
            jsonb.toJson(new Response(200, "Successfully deleted..", ""), response.getWriter());

        } catch (SQLException e) {
            jsonb.toJson(new Response(400, "Error !", e.getLocalizedMessage()), response.getWriter());
            e.printStackTrace();
        }
    }
}

package lk.ijse.gdse.pos_backend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.gdse.pos_backend.convertor.Convertor;
import lk.ijse.gdse.pos_backend.dao.DaoFactory;
import lk.ijse.gdse.pos_backend.dao.DaoType;
import lk.ijse.gdse.pos_backend.dao.custom.ItemDAO;
import lk.ijse.gdse.pos_backend.dto.ItemDTO;

import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ItemServlet extends HttpServlet {

    private Connection connection;
    private ItemDAO itemDAO;
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
        this.itemDAO = DaoFactory.getInstance().getDao(DaoType.ITEM, connection);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");

        Jsonb jsonb = JsonbBuilder.create();

        try {

            List<ItemDTO> list = itemDAO.getAll().stream().map(item -> convertor.formItem(item)).collect(Collectors.toList());
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            jsonb.toJson(new Response(200, "Done", list), response.getWriter());

        } catch (SQLException e) {

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
        ItemDTO itemDTO = jsonb.fromJson(request.getReader(), ItemDTO.class);

        System.out.println(itemDTO);

        try {

            if (itemDAO.save(convertor.toItem(itemDTO)) != null) {
                jsonb.toJson(new Response(200, "Successfully added..", ""), response.getWriter());
            }

        } catch (SQLException e) {

            jsonb.toJson(new Response(400, "Error..", e.getLocalizedMessage()), response.getWriter());
            e.printStackTrace();

        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

        resp.setContentType("application/json");

        Jsonb jsonb = JsonbBuilder.create();
        ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

        try {

            if (itemDAO.update(convertor.toItem(itemDTO)) != null) {
                jsonb.toJson(new Response(200, "Successfully update..", ""), resp.getWriter());
            }

        } catch (SQLException e) {

            jsonb.toJson(new Response(400, "Error !", e.getLocalizedMessage()), resp.getWriter());
            e.printStackTrace();

        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

        resp.setContentType("application/json");

        Jsonb jsonb = JsonbBuilder.create();
        String code = req.getParameter("code");super.doDelete(req, resp);

        try {

            itemDAO.delete(code);
            jsonb.toJson(new Response(200, "Successfully deleted !", ""), resp.getWriter());

        } catch (SQLException e) {

            jsonb.toJson(new Response(400, "Error !", e.getLocalizedMessage()), resp.getWriter());
            e.printStackTrace();

        }

    }
}

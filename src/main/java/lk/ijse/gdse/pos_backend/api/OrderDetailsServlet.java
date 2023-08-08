package lk.ijse.gdse.pos_backend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.gdse.pos_backend.convertor.Convertor;
import lk.ijse.gdse.pos_backend.dao.DaoFactory;
import lk.ijse.gdse.pos_backend.dao.DaoType;
import lk.ijse.gdse.pos_backend.dao.custom.ItemDAO;
import lk.ijse.gdse.pos_backend.dao.custom.OrderDetailsDAO;
import lk.ijse.gdse.pos_backend.dao.exception.ConstrainViolationException;
import lk.ijse.gdse.pos_backend.dto.ItemDTO;
import lk.ijse.gdse.pos_backend.dto.OrderDetailsDTO;

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

public class OrderDetailsServlet extends HttpServlet {

    private Connection connection;
    private OrderDetailsDAO orderDetailsDAO;
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

        this.orderDetailsDAO = DaoFactory.getInstance().getDao(DaoType.ORDERDETAILS, connection);
        this.itemDAO = DaoFactory.getInstance().getDao(DaoType.ITEM, connection);
        this.convertor = new Convertor();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");

        Jsonb jsonb = JsonbBuilder.create();

        try {

            List<OrderDetailsDTO> list = orderDetailsDAO.getAll().stream().map(orderDetails -> convertor.fromOrderDetails(orderDetails)).collect(Collectors.toList());
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            jsonb.toJson(new Response(200, "Done", list), response.getWriter());

        } catch (Exception exception) {
            jsonb.toJson(new Response(400, "Error", exception.getLocalizedMessage()), response.getWriter());
            exception.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getContentType() == null || !request.getContentType().toLowerCase().startsWith("application/json")) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

        response.setContentType("application/json");

        Jsonb jsonb = JsonbBuilder.create();
        OrderDetailsDTO orderDetailsDTO = jsonb.fromJson(request.getReader(), OrderDetailsDTO.class);

        try {

            connection.setAutoCommit(false);

            if (orderDetailsDAO.save(convertor.toOrderDetails(orderDetailsDTO)) == null) {
                throw new ConstrainViolationException("Failed to save order..");
            }

            ItemDTO item = itemDAO.getItem(orderDetailsDTO.getCode());

            if (item != null) {
                int newQty = item.getQty() - orderDetailsDTO.getQty();

                if (!itemDAO.updateQty(orderDetailsDTO.getCode(), newQty)) {
                    throw new ConstrainViolationException("Failed to update item qty..");
                }

            }

            connection.commit();
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            jsonb.toJson(new Response(200, "Successfully order placed..", ""), response.getWriter());

        } catch (SQLException t){
            try {

                connection.rollback();
                jsonb.toJson(new Response(400, "Error !", t.getLocalizedMessage()), response.getWriter());
                t.printStackTrace();

            }catch (SQLException e){

                jsonb.toJson(new Response(400, "Error !", e.getLocalizedMessage()), response.getWriter());
                t.printStackTrace();

            }finally {

                try {

                    connection.setAutoCommit(true);

                }catch (SQLException e){

                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}

package lk.ijse.gdse.pos_backend.convertor;

import lk.ijse.gdse.pos_backend.dto.CustomerDTO;
import lk.ijse.gdse.pos_backend.dto.ItemDTO;
import lk.ijse.gdse.pos_backend.dto.OrderDetailsDTO;
import lk.ijse.gdse.pos_backend.entity.Customer;
import lk.ijse.gdse.pos_backend.entity.Item;
import lk.ijse.gdse.pos_backend.entity.OrderDetails;

public class Convertor {

    public CustomerDTO fromCustomer(Customer customer){
        return new CustomerDTO(customer.getCustomerID(), customer.getName(), customer.getAddress(), customer.getContact());
    }

    public Customer toCustomer(CustomerDTO customerDTO){
        return new Customer(customerDTO.getCustomerId(), customerDTO.getName(), customerDTO.getAddress(), customerDTO.getContact());
    }

    public ItemDTO formItem(Item item){
        return new ItemDTO(item.getCode(), item.getName(), item.getPrice(), item.getQty());
    }

    public Item toItem(ItemDTO itemDTO){
        return new Item(itemDTO.getCode(), itemDTO.getName(), itemDTO.getPrice(), itemDTO.getQty());
    }

    public OrderDetailsDTO fromOrderDetails(OrderDetails orderDetails){
        return new OrderDetailsDTO(
                orderDetails.getOrderId(),
                orderDetails.getCustomerId(),
                orderDetails.getCode(),
                orderDetails.getName(),
                orderDetails.getPrice(),
                orderDetails.getQty(),
                orderDetails.getTotal()
        );
    }

    public OrderDetails toOrderDetails(OrderDetailsDTO orderDetailsDTO){
        return new OrderDetails(
                orderDetailsDTO.getOrderId(),
                orderDetailsDTO.getCustomerId(),
                orderDetailsDTO.getCode(),
                orderDetailsDTO.getName(),
                orderDetailsDTO.getPrice(),
                orderDetailsDTO.getQty(),
                orderDetailsDTO.getTotal()
        );
    }

}

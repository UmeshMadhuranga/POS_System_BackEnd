package lk.ijse.gdse.pos_backend.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetails implements SuperEntity{
    private String orderId;
    private String customerId;
    private String code;
    private String name;
    private double price;
    private int qty;
    private double total;
}

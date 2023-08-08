package lk.ijse.gdse.pos_backend.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetailsDTO {
    private String orderId;
    private String customerId;
    private String code;
    private String name;
    private double price;
    private int qty;
    private double total;
}

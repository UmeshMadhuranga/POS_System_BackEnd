package lk.ijse.gdse.pos_backend.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemDTO {
    private String code;
    private String name;
    private double price;
    private int qty;
}

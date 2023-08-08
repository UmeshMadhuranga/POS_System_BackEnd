package lk.ijse.gdse.pos_backend.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Item implements SuperEntity{
    private String code;
    private String name;
    private double price;
    private int qty;
}

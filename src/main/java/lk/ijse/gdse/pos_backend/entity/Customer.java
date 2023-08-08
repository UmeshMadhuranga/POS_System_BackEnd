package lk.ijse.gdse.pos_backend.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Customer implements SuperEntity{
    private String customerID;
    private String name;
    private String address;
    private String contact;
}

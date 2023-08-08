package lk.ijse.gdse.pos_backend.api;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Response {
    private int status;
    private String massage;
    private Object data;
}

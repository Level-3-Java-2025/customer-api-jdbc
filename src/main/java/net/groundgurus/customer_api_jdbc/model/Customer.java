package net.groundgurus.customer_api_jdbc.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Customer {
    @Id
    private long id;
    private String firstName;
    private String lastName;
}

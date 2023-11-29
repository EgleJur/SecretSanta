package Secret.Santa.Secret.Santa.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "group_table")
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "group_id", nullable = false)
    private Integer groupId;
    private String name;
    //TODO date
    private double budget;

    //TODO user_id with manyToOne
}

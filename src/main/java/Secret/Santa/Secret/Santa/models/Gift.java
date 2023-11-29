package Secret.Santa.Secret.Santa.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "gift_table")
@AllArgsConstructor
@NoArgsConstructor
public class Gift {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "gift_id", nullable = false)
    private Integer giftId;
    private String name;
    private String description;
    private String link;
    private double price;
    private int created_by;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}

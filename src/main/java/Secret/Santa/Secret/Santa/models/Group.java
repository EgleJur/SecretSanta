package Secret.Santa.Secret.Santa.models;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "group_table")
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Setter(value = AccessLevel.NONE)
    private Integer groupId;
    @Column(name = "Name")
    private String name;
    @Column(name = "Date")
    private LocalDate eventDate;
    @Column(name = "Budget")
    private double budget;

    @ManyToMany(fetch = FetchType.EAGER)//, cascade = CascadeType.ALL)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
    @JoinTable(
            name = "users_in_groups",
            joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    )
    private List<User> user;


    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Gift> gifts;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
//    @JsonIgnore
    private List<GenerateSanta> generatedSanta;

    @ManyToOne
    @JoinColumn(
            name = "owner_id",
            referencedColumnName = "user_id")
    private User owner;

}

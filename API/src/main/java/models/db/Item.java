package models.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "entities")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Boolean verified = false;

    @ElementCollection
    @CollectionTable(name = "entity_numbers", joinColumns = @JoinColumn(name = "entity_id"))
    @Column(name = "number")
    private List<Integer> importantNumbers;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "addition_id")
    private Addition addition;

    public void setAddition(Addition addition) {
        this.addition = addition;
    }
}

package models.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "additions")
public class Addition {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "additional_info")
    private String additionalInfo;

    @Column(name = "additional_number")
    private Integer additionalNumber;

    @OneToOne(mappedBy = "addition", fetch = FetchType.LAZY)
    private Item item;
}

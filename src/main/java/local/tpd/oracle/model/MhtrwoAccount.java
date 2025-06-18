package local.tpd.oracle.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tech.ailef.snapadmin.external.annotations.DisplayFormat;
import tech.ailef.snapadmin.external.annotations.Filterable;

@Data
@Entity
@Table(name = "MHTRWO_ACCOUNT", catalog = "", schema = "DTPD")
public class MhtrwoAccount {

    @Id
    @NotNull
    @Column(name = "ID")
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mhtrwo_account_generator")
    @SequenceGenerator(
            name = "mhtrwo_account_generator",
            sequenceName = "mhtrwo_account_seq",
            schema = "DTPD",
            allocationSize = 1
    )
    private long id;

    @ManyToOne
    @Filterable
    @JoinColumn(name = "MHTRWO_ID")
    private Mhtrwo mhtrwo;

    @Filterable
    @Column(name = "BALANCE")
    @DisplayFormat(format = "€%.2f")
    private Double balance;

    @Override
    public String toString() {
        return id +
                "," + mhtrwo.getId() +
                "," + balance;
    }

}

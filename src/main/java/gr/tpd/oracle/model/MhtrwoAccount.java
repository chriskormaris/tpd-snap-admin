package gr.tpd.oracle.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "MHTRWO_ACCOUNT", catalog = "", schema = "DTPD")
public class MhtrwoAccount {

    @Id
    @NotNull
    @Column(name = "ID")
    @Basic(optional = false)
    private long id;

    @NotNull
    @Basic(optional = false)
    @Column(name = "MHTRWO_ID")
    private long mhtrwo_id;

    @Column(name = "BALANCE")
    private Double balance;

    public MhtrwoAccount() {
    }

    public MhtrwoAccount(long id) {
        this.id = id;
    }

    //    @Override
//    public String toString() {
//        return "mhtrwo.entity.MhtrwoAccount[ id=" + id + " ]";
//    }

    public static String getHeader() {
        return "id,mhtrwo_id,balance";
    }

    @Override
    public String toString() {
        return id +
                "," + mhtrwo_id +
                "," + balance;
    }

}

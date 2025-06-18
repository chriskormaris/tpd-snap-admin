package local.tpd.oracle.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "MHTRWO", catalog = "", schema = "DTPD")
public class Mhtrwo {

    @Id
    @NotNull
    @Column(name = "ID")
    @Basic(optional = false)
    private long id;

    @Size(max = 128)
    @Column(name = "ONOMA")
    private String onoma;

    @Size(max = 128)
    @Column(name = "EPONYMO")
    private String eponymo;

    @Column(name = "AFM")
    private Long afm;

    @Column(name = "HMNIA")
    private LocalDateTime hmnia;

    @Column(name = "SALARY")
    private Double salary;

    public Mhtrwo() {
    }

    public Mhtrwo(long id) {
        this.id = id;
    }

    //    @Override
//    public String toString() {
//        return "mhtrwo.entity.Mhtrwo[ id=" + id + " ]";
//    }

    public static String getHeader() {
        return "id,onoma,eponymo,afm,hmnia,salary";
    }

    @Override
    public String toString() {
        return id +
                ",\"" + onoma + "\"" +
                ",\"" + eponymo + "\"" +
                "," + afm + "\"" +
                ",\"" + hmnia + "\"" +
                "," + salary;
    }

}

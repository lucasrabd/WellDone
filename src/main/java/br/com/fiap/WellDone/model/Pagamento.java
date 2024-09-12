package br.com.fiap.WellDone.model;

import java.math.BigDecimal;
import org.springframework.hateoas.RepresentationModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagamento")
public class Pagamento extends RepresentationModel<Pagamento> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pagamento_id")
    private Long pagamento_id;

    @Column(name = "pagamento_metodo", nullable = false)
    private String pagamento_metodo;

    @Column(name = "pagamento_vlr_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal pagamento_vlr_total;

    public Pagamento() {}

    public Pagamento(Long pagamento_id, String pagamento_metodo, BigDecimal pagamento_vlr_total) {
        super();
        this.pagamento_id = pagamento_id;
        this.pagamento_metodo = pagamento_metodo;
        this.pagamento_vlr_total = pagamento_vlr_total;
    }

    public Long getPagamento_id() {
        return pagamento_id;
    }

    public void setPagamento_id(Long pagamento_id) {
        this.pagamento_id = pagamento_id;
    }

    public String getPagamento_metodo() {
        return pagamento_metodo;
    }

    public void setPagamento_metodo(String pagamento_metodo) {
        this.pagamento_metodo = pagamento_metodo;
    }

    public BigDecimal getPagamento_vlr_total() {
        return pagamento_vlr_total;
    }

    public void setPagamento_vlr_total(BigDecimal pagamento_vlr_total) {
        this.pagamento_vlr_total = pagamento_vlr_total;
    }
}

package track.log.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "awbs")
public class AWB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**Dados obtidos internamente */
    private boolean recebida;
    private LocalDateTime dataDeRecebimento;
    private String colaborador;

    /**Dados obtídos através dos e-mail */
    private String numeroOperacional;
    @OneToMany(mappedBy = "awb", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Pedido> pedidos = new ArrayList<>();

    public AWB() {
    }

    /**Caso o PedidoService tente adcionar um pedido a uma Awb inexistente,
     *utilizar este construtor com o numeroOperacional do pedido*/
    public AWB(String numeroOperacional) {
        this.numeroOperacional = numeroOperacional;
    }

    public Long getId() {
        return id;
    }

    public String getNumeroOperacional() {
        return numeroOperacional;
    }

    public void setNumeroOperacional(String numeroOperacional) {
        this.numeroOperacional = numeroOperacional;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public boolean isRecebida() {
        return recebida;
    }

    public void setRecebida(boolean recebida) {
        this.recebida = recebida;
    }

    public LocalDateTime getDataDeRecebimento() {
        return dataDeRecebimento;
    }

    public void setDataDeRecebimento(LocalDateTime dataDeRecebimento) {
        this.dataDeRecebimento = dataDeRecebimento;
    }

    public String getColaborador() {
        return colaborador;
    }

    public void setColaborador(String colaborador) {
        this.colaborador = colaborador;
    }
}

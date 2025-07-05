package track.log.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Representa um pedido extraído de e-mails.
 * Contém informações logísticas como nota fiscal, CTe, destino e status de entrega.
 */
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Dados extraídos do conteúdo dos e-mails*/
    private String cte;
    private String notaFiscal;
    private String destinatario;
    private String cidadeOrigem;
    private String cidadeDestino;
    private String numeroOperacional;
    private String tipoDeProduto;
    private double peso;
    private int volume;
    private String embalagem;

    /** Dados internos atribuídos no momento da entrega*/
    private LocalDateTime dataDaEntrega;
    private String colaborador;
    private boolean entregue;

    @ManyToOne
    @JoinColumn(name = "awb_id")
    @JsonBackReference
    private AWB awb;

    public Pedido() {}

    public Pedido(String cte, String notaFiscal, String destinatario, String cidadeOrigem, String cidadeDestino,
                  String numeroOperacional, String tipoDeProduto, double peso, int volume, String embalagem,
                  LocalDateTime dataRecebimento, String colaborador) {
        this.cte = cte;
        this.notaFiscal = notaFiscal;
        this.destinatario = destinatario;
        this.cidadeOrigem = cidadeOrigem;
        this.cidadeDestino = cidadeDestino;
        this.numeroOperacional = numeroOperacional;
        this.tipoDeProduto = tipoDeProduto;
        this.peso = peso;
        this.volume = volume;
        this.embalagem = embalagem;
        this.dataDaEntrega = dataRecebimento;
        this.colaborador = colaborador;
    }

    // Getters e setters manuais

    public Long getId() {
        return id;
    }

    public boolean isEntregue() {
        return entregue;
    }

    public void setEntregue(boolean entregue) {
        this.entregue = entregue;
    }

    public String getCte() {
        return cte;
    }

    public void setCte(String cte) {
        this.cte = cte;
    }

    public String getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getCidadeOrigem() {
        return cidadeOrigem;
    }

    public void setCidadeOrigem(String cidadeOrigem) {
        this.cidadeOrigem = cidadeOrigem;
    }

    public String getCidadeDestino() {
        return cidadeDestino;
    }

    public void setCidadeDestino(String cidadeDestino) {
        this.cidadeDestino = cidadeDestino;
    }

    public String getNumeroOperacional() {
        return numeroOperacional;
    }

    public void setNumeroOperacional(String numeroOperacional) {
        this.numeroOperacional = numeroOperacional;
    }

    public String getTipoDeProduto() {
        return tipoDeProduto;
    }

    public void setTipoDeProduto(String tipoDeProduto) {
        this.tipoDeProduto = tipoDeProduto;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getEmbalagem() {
        return embalagem;
    }

    public void setEmbalagem(String embalagem) {
        this.embalagem = embalagem;
    }

    public LocalDateTime getDataDaEntrega() {
        return dataDaEntrega;
    }

    public void setDataDaEntrega(LocalDateTime dataDaEntrega) {
        this.dataDaEntrega = dataDaEntrega;
    }

    public AWB getAwb() {
        return awb;
    }

    public void setAwb(AWB awb) {
        this.awb = awb;
    }

    public String getColaborador() {
        return colaborador;
    }

    public void setColaborador(String colaborador) {
        this.colaborador = colaborador;
    }
}

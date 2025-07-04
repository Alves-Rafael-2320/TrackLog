package track.log.demo.service;

import jakarta.mail.Session;

import jakarta.mail.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import track.log.demo.model.Pedido;
import track.log.demo.repository.PedidoRepository;


import java.util.*;

@Service
public class EmailScannerService {

    private final PedidoService pedidoService;
    private final PedidoRepository pedidoRepository;

    @Value("${email.imap.username}")
    private String username;
    @Value("${email.imap.password}")
    private String password;

    private final String host = "imap.gmail.com";


    public EmailScannerService(PedidoService pedidoService, PedidoRepository pedidoRepository) {
        this.pedidoService = pedidoService;
        this.pedidoRepository = pedidoRepository;
    }


    /** Inicia a sessão em G-mail, acessa o INBOX e armazena todas as mensagens não lidas
     * em uma array para serem analisadas. */
    public void lerInbox(){
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imap");
            props.put("mail.imap.host", host);
            props.put("mail.imap.port", "993");
            props.put("mail.imap.ssl.enable", "true");

            Session session = Session.getInstance(props);
            Store store = session.getStore("imap");
            store.connect(username, password);

            Folder inbox = store.getFolder("INBOX");
            Folder processedFolder = store.getFolder("TrackLog/Processados");

            if (!processedFolder.exists()){
                processedFolder.create(Folder.HOLDS_MESSAGES);
            }

            inbox.open(Folder.READ_WRITE);
            processedFolder.open(Folder.READ_WRITE);




            Message[] mensagens = inbox.getMessages();

            for (Message mensagem : mensagens) {
                if (mensagem.isMimeType("text/html") || mensagem.isMimeType("multipart/*")) {
                    String html = extrairHtml(mensagem);

                    if (html != null && !html.isBlank()) {
                        List<Pedido> pedidos = extrairPedidosDeHtml(html);

                        for (Pedido pedido : pedidos) {
                            pedidoService.salvarPedido(pedido);
                            System.out.println("PED " + pedido.getNotaFiscal() + " Salvo em AWB " + pedido.getNumeroOperacional());
                        }

                        if (!pedidos.isEmpty()) {
                            // 👉 Somente agora movemos o e-mail para o label
                            mensagem.setFlag(Flags.Flag.SEEN, true);
                            inbox.copyMessages(new Message[]{mensagem}, processedFolder);
                            mensagem.setFlag(Flags.Flag.DELETED, true);
                        } else {
                            System.out.println("Nenhum pedido válido extraído do e-mail.");
                        }
                    }
                }
            }
            inbox.close(true);
            processedFolder.close(false);
            store.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /** Testa se as mensagens armazenadas no array se text/html as retorna
     * se multipart/* a converte para String e então a retorna. */
    private String extrairHtml(Message mensagem) throws Exception {
        if (mensagem.isMimeType("text/html")) {
            return mensagem.getContent().toString();
        } else if (mensagem.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) mensagem.getContent();

            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart parte = multipart.getBodyPart(i);
                if (parte.isMimeType("text/html")) {
                    return (String) parte.getContent();
                }
            }
        }
        return null;
    }

    /** Analisa o conteúdo do html e extraí os valores em suas tabelas em seguida os usa para construir
     *  os pedidos. */
    public List<Pedido> extrairPedidosDeHtml(String html) {
        List<Pedido> pedidos = new ArrayList<>();

        Document doc = Jsoup.parse(html);
        Element table = doc.selectFirst("table");
        if (table == null) return pedidos;

        // Mapeamento de colunas
        List<String> cabecalhos = new ArrayList<>();
        Element thead = table.selectFirst("thead");
        if (thead == null) return pedidos;

        Elements ths = thead.select("th");
        for (Element th : ths) {
            cabecalhos.add(th.text().trim());
        }

        /** Valida se o html tem todos os dados necessarios para compor um pedido, se algum estiver
         * faltando será retornado uma array de pedidos vazia, se não, cada table row irá compor um pedido. */
        List<String> obrigatorios = List.of(
                "CT-e",
                "Notas Fiscais",
                "Destinatário",
                "Cidade Origem",
                "Cidade Destino",
                "Número Operacional",
                "Tipo de Produto",
                "Peso",
                "Volume",
                "Embalagem"
        );
        if (!cabecalhos.containsAll(obrigatorios)) {
            System.out.println("Tabela ignorada - colunas obrigatórias ausentes.");
            return pedidos;
        }

        // Processa cada linha do tbody
        Element tbody = table.selectFirst("tbody");
        if (tbody == null) return pedidos;

        for (Element row : tbody.select("tr")) {
            Elements tds = row.select("td");
            if (tds.size() != cabecalhos.size()) continue;

            Map<String, String> dados = new HashMap<>();
            for (int i = 0; i < tds.size(); i++) {
                dados.put(cabecalhos.get(i), tds.get(i).text().trim());
            }

            try {
                String cte = dados.get("CT-e");
                String notaFiscal = dados.get("Notas Fiscais");

                Optional<Pedido> existente = pedidoRepository
                        .findByCteAndNotaFiscal(cte, notaFiscal);

                if (existente.isPresent()) {
                    continue; // pula este pedido (já existe)
                }
                Pedido pedido = new Pedido();
                pedido.setCte(dados.get("CT-e"));
                pedido.setNotaFiscal(dados.get("Notas Fiscais"));
                pedido.setDestinatario(dados.get("Destinatário"));
                pedido.setCidadeOrigem(dados.get("Cidade Origem"));
                pedido.setCidadeDestino(dados.get("Cidade Destino"));
                pedido.setNumeroOperacional(dados.get("Número Operacional"));
                pedido.setTipoDeProduto(dados.get("Tipo de Produto"));
                pedido.setPeso(Double.parseDouble(dados.get("Peso").replace(",", ".")));
                pedido.setVolume(Integer.parseInt(dados.get("Volume")));
                pedido.setEmbalagem(dados.get("Embalagem"));
                pedido.setDataDaEntrega(null);// será preenchida depois
                pedido.setColaborador(null);// será preenchido depois
                pedido.setEntregue(false);

                pedidos.add(pedido);
            } catch (Exception e) {
                System.out.println("Erro ao processar linha de pedido. Ignorando.");
            }
        }

        return pedidos;
    }



}

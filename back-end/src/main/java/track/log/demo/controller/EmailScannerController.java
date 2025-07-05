package track.log.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import track.log.demo.service.EmailScannerService;

@RestController
@RequestMapping("/api/emailscanner")
public class EmailScannerController {

    private final EmailScannerService emailScannerService;


    public EmailScannerController(EmailScannerService emailScannerService) {
        this.emailScannerService = emailScannerService;
    }

    /**
     * Endpoint para iniciar a leitura da caixa de entrada do Gmail.
     *
     * Varre os e-mails da pasta INBOX, extrai dados de pedidos a partir do conteúdo HTML
     * e salva as entidades válidas. E-mails com pedidos processados com sucesso
     * são movidos para a pasta "TrackLog/Processados".
     *
     * @return mensagem indicando que a varredura foi concluída
     */
    @GetMapping("/scan")
    public ResponseEntity<String> escanearEmails(){
        emailScannerService.lerInbox();
        return ResponseEntity.ok("Varredura concluída");
    }
}

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

    @GetMapping("/scan")
    public ResponseEntity<String> escanearEmails(){
        emailScannerService.lerInbox();
        return ResponseEntity.ok("Varredura concluída");
    }
}

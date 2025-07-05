package track.log.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import track.log.demo.dto.AuthRequest;
import track.log.demo.dto.AuthResponse;
import track.log.demo.model.User;
import track.log.demo.security.JwtUtil;
import org.springframework.security.core.Authentication;
import track.log.demo.service.UserService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Realiza a autenticação do usuário e gera um token JWT em caso de sucesso.
     *
     * @param request contém username e password para autenticação
     * @return token JWT encapsulado em {@link AuthResponse}
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param request contém username e password do novo usuário
     * @return mensagem de sucesso se o cadastro for realizado
     */
    @PostMapping("/register")
    public ResponseEntity<String> cadastrar(@RequestBody AuthRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        userService.save(user);
        return ResponseEntity.ok("Usuário registrado com sucesso");
    }
}

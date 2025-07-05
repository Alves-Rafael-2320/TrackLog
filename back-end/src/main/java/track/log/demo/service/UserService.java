package track.log.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import track.log.demo.model.User;
import track.log.demo.repository.UserRepository;

import java.util.Optional;

/**Serviço para operações relacionadas ao usuário, como salvar e buscar por username.*/
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Salva um usuário codificando sua senha antes da persistência.
     * @param user Usuário a ser salvo
     * @return Usuário salvo
     */
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Busca um usuário pelo username.
     * @param username Nome do usuário
     * @return Optional com o usuário encontrado ou vazio
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

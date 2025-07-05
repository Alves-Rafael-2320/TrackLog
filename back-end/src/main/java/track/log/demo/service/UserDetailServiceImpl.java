package track.log.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import track.log.demo.model.User;
import track.log.demo.repository.UserRepository;

/**
 * Implementação de UserDetailsService para carregar detalhes do usuário
 * a partir do banco de dados usando o UserRepository.
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * Busca usuário pelo username e retorna um UserDetails para autenticação.
     * @param username Nome do usuário
     * @return UserDetails com dados do usuário e roles
     * @throws UsernameNotFoundException Caso usuário não seja encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER") // Ajustar caso múltiplos roles estejam disponíveis
                .build();
    }
}

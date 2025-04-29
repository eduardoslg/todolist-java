package br.com.eduardoslg.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.eduardoslg.todolist.users.IUsersRepository;
import br.com.eduardoslg.todolist.users.UsersModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUsersRepository usersRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

            var servletPath = request.getServletPath();

            if (servletPath.equals("/v1/tasks")) {
                // Pegar a autenticação (usuário e senha)
                String authorization = request.getHeader("Authorization");

                if (authorization == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                String authEncoded = authorization.substring("Basic ".length()).trim();

                byte[] authDecode = Base64.getDecoder().decode(authEncoded);
                String authString = new String(authDecode);

                String[] credentials = authString.split(":");

                String username = credentials[0];
                String password = credentials[1];

                // Validar usuário

                UsersModel user = this.usersRepository.findByUsername(username);
                if (user == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                // Validar senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    filterChain.doFilter(request, response);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else {
                filterChain.doFilter(request, response);
            }
    }
}

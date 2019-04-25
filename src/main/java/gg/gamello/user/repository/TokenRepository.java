package gg.gamello.user.repository;

import gg.gamello.user.dao.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
    Optional<Token> findByUserIdAndValue(@NotBlank UUID userId, @NotBlank String value);

    void deleteAllByUserId(@NotNull UUID userId);
}

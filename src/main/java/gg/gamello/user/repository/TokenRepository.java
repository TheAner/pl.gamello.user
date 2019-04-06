package gg.gamello.user.repository;

import gg.gamello.user.dao.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
    Optional<Token> findByUserIdAndValue(@NotBlank Long userId, @NotBlank String value);

    void deleteAllByUserId(@NotNull Long userId);
}

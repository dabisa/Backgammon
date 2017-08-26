package com.dkelava.backgammon.websrv.repo;

import com.dkelava.backgammon.websrv.domain.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;

/**
 * Created by Dabisa on 11/08/2017.
 */
@RepositoryRestResource(exported = false)
public interface GameRepository extends CrudRepository<Game, Integer> {

    @Override
    @PostAuthorize("returnObject.whitePlayer.name == authentication.name or returnObject.blackPlayer.name == authentication.name")
    Game findOne(Integer integer);
}

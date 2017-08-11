package com.dkelava.backgammon.websrv.repo;

import com.dkelava.backgammon.websrv.domain.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Dabisa on 11/08/2017.
 */
public interface PlayerRepository extends CrudRepository<Player, String> {
    Player findByName(@Param("name") String name);
}

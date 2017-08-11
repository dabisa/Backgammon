package com.dkelava.backgammon.websrv.repo;

import com.dkelava.backgammon.websrv.domain.Game;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Dabisa on 11/08/2017.
 */
public interface GameRepository extends CrudRepository<Game, Integer> {
}

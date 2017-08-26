package com.dkelava.backgammon.websrv.repo;

import com.dkelava.backgammon.websrv.domain.GameRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface GameRequestRepository extends CrudRepository<GameRequest, Integer> {
}

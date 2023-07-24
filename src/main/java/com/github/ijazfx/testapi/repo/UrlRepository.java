package com.github.ijazfx.testapi.repo;

import org.springframework.stereotype.Repository;

import com.github.ijazfx.testapi.model.Url;

import io.graphenee.core.model.jpa.GxJpaRepository;

@Repository
public interface UrlRepository extends GxJpaRepository<Url, Long> {

	Url findOneByAlias(String alias);

}

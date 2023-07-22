package com.github.ijazfx.urlproxy.repo;

import org.springframework.stereotype.Repository;

import com.github.ijazfx.urlproxy.model.Url;

import io.graphenee.core.model.jpa.GxJpaRepository;

@Repository
public interface UrlRepository extends GxJpaRepository<Url, Long> {

	Url findOneByAlias(String alias);

}

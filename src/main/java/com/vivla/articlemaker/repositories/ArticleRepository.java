package com.vivla.articlemaker.repositories;

import com.vivla.articlemaker.entities.ArticleEntity;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<ArticleEntity, Long> {
}

package br.com.lsena.despesas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractRepository<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T>{
}

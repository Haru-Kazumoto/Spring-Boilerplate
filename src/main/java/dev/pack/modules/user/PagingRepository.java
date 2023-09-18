package dev.pack.modules.user;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagingRepository extends PagingAndSortingRepository<UserModel, Integer> {
}

package edu.corvinus.HTBeadando2.repository;

import edu.corvinus.HTBeadando2.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}

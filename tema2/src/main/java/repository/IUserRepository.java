package repository;

import domain.User;

public interface IUserRepository extends IRepository<User> {
    boolean findOne(String username, String password);
}

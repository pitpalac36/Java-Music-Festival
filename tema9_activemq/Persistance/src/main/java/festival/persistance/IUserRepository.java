package festival.persistance;

import festival.domain.models.User;

public interface IUserRepository extends IRepository<User> {
    boolean findOne(String username, String password);
}
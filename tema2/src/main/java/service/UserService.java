package service;

import repository.IUserRepository;

public class UserService {
    private IUserRepository repo;

    public UserService(IUserRepository repo) {
        this.repo = repo;
    }

    public boolean login(String username, String password) {
        return repo.findOne(username, password);
    }
}

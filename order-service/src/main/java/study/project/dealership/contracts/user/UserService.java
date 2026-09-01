package study.project.dealership.contracts.user;

import study.project.dealership.contracts.user.model.CreatedUserCredentials;
import study.project.dealership.contracts.user.request.*;
import study.project.dealership.domain.user.User;

import java.util.List;

public interface UserService {
    CreatedUserCredentials createClient(RequestCreateClient request);

    CreatedUserCredentials createManager(RequestCreateManager request);

    CreatedUserCredentials createSystemAdmin(RequestCreateSystemAdmin request);

    CreatedUserCredentials createWarehouseAdmin(RequestCreateWarehouseAdmin request);

    User findUser(RequestFindUser request);

    List<User> getAllUsers();

    void removeUser(RequestRemoveUser request);

    User updateUser(RequestUpdateUser request);
}

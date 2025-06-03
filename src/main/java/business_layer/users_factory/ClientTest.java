package business_layer.users_factory;

import com.sure.enums.UserType;

public class ClientTest {
    public static void main(String[] args) {
        // create UserFactory Object
        UserFactory userFactory = new UserFactory();

        // create Admin User Type
        User admin =  userFactory.getUser(UserType.ADMIN);
        System.out.println("User Type = "+admin.createUser());

        // create Guest User Type
        User guest = userFactory.getUser(UserType.GUEST);
        System.out.println("User_Type = "+guest.createUser());

        // create Normal User Type
        User normal = userFactory.getUser(UserType.NORMAL);
        System.out.println("User_Type = "+normal.createUser());

    }
}

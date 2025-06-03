package business_layer.users_factory;

public class AdminUser implements User{
    @Override
    public String createUser() {

        return "Admin User";
    }
}

package business_layer.users_factory;

public class GuestUser implements User{
    @Override
    public String createUser() {
        return "Guest User";
    }
}

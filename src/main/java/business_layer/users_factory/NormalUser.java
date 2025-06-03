package business_layer.users_factory;

public class NormalUser implements User{


    @Override
    public String createUser() {
        return "Normal User";
    }
}

package business_layer.users_factory;

import com.sure.enums.UserType;

public class UserFactory {



    public User getUser(UserType userType){

        if (userType == null)
            return null;

        switch (userType){
            case ADMIN-> { return new AdminUser(); }

            case GUEST-> { return new GuestUser(); }

            case NORMAL-> { return new NormalUser(); }

            default->
                throw new IllegalArgumentException("Unknown userType "+userType);
        }
    }




}

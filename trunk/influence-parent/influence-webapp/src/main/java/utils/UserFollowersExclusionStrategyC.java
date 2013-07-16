package utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import it.cybion.model.twitter.User;


public class UserFollowersExclusionStrategyC implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {

        boolean isUser = f.getDeclaringClass() == User.class;
        boolean followersField = (isUser && f.getName().equals("followers"));
        boolean friendsField = (isUser && f.getName().equals("friends"));

        return followersField || friendsField;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {

        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

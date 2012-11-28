package eu.granatum.importer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class UserPOJO implements User, Serializable {

    public int id;

    public String name;

    private List<User> socialNetwork = new ArrayList<User>();

    public UserPOJO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<User> getSocialNetwork() {
        return socialNetwork;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPOJO userPOJO = (UserPOJO) o;

        if (id != userPOJO.id) return false;
        if (name != null ? !name.equals(userPOJO.name) : userPOJO.name != null)
            return false;
        if (socialNetwork != null ? !socialNetwork.equals(userPOJO.socialNetwork) : userPOJO.socialNetwork != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (socialNetwork != null ? socialNetwork.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserPOJO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", socialNetwork=" + socialNetwork +
                '}';
    }
}

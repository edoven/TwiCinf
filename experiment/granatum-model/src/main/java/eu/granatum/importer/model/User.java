package eu.granatum.importer.model;

import java.util.List;

public interface User {

    public int getId();

    public String getName();

    public List<User> getSocialNetwork();

}
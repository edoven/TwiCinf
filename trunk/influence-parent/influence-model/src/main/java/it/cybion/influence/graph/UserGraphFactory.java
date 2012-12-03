package it.cybion.influence.graph;

import it.cybion.influence.model.User;

import java.util.List;

import com.tinkerpop.blueprints.Graph;

public interface UserGraphFactory {

	public abstract void addUsersToGraph(List<User> users)
			throws GraphCreationException;

	public abstract Graph getGraph();

}
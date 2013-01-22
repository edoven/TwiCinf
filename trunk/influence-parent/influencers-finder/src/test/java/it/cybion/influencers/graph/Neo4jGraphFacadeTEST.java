package it.cybion.influencers.graph;

import it.cybion.influencers.graph.Neo4jGraphFacade;
import it.cybion.influencers.graph.UserVertexNotPresent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import org.testng.Assert;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;


public class Neo4jGraphFacadeTEST {
	
	
	private static final Logger logger = Logger.getLogger(Neo4jGraphFacadeTEST.class);
	
	
	public static void main(String[] args) throws IOException {
		String graphDirPath = "src/test/resources/graphs/massiveInsertionsTEST";
		delete(new File(graphDirPath));
		
		Neo4jGraphFacade graphFacade = new Neo4jGraphFacade(graphDirPath);
		
		for (long i=0; i<800000; i++) {
			logger.info(i+
						" (free memory= "+Runtime.getRuntime().freeMemory()/(1024*1024)+" MB");
			graphFacade.addUser(i);
			
		}
	}
	
	@Test(enabled=false)
	public void massiveInsertionsTEST() throws IOException, UserVertexNotPresent {
		String graphDirPath = "src/test/resources/graphs/massiveInsertionsTEST";
		delete(new File(graphDirPath));
		
		Neo4jGraphFacade graphFacade = new Neo4jGraphFacade(graphDirPath);
		
		for (long i=0; i<800000; i++) {

			graphFacade.addUser(i);
			if (i%100 == 0) {
				logger.info("before - "+i+
						" (free memory= "+Runtime.getRuntime().freeMemory()/(1024*1024)+" MB");
				Runtime.getRuntime().gc();
				logger.info("after - "+i+
						" (free memory= "+Runtime.getRuntime().freeMemory()/(1024*1024)+" MB");
			}
		}
		
	}
	
	
	@Test(enabled=false)
	public void insertAndRetrieveTEST() throws IOException, UserVertexNotPresent {
		String graphDirPath = "src/test/resources/graphs/addUserTEST";
		delete(new File(graphDirPath));
		
		Neo4jGraphFacade graphFacade = new Neo4jGraphFacade(graphDirPath);
		
		Long userId = 1111l;
		graphFacade.addUser(userId);
		Assert.assertEquals(graphFacade.getVerticesCount() , 1);
		
		Vertex vertex = graphFacade.getUserVertex(userId);
		Assert.assertNotNull(vertex);
		
		delete(new File(graphDirPath));
	}

	
	@Test(enabled=false)
	public void addUsersTEST() throws IOException, UserVertexNotPresent {
		String graphDirPath = "src/test/resources/graphs/addUsersTESTgraph";
		delete(new File(graphDirPath));
		
		Neo4jGraphFacade graphFacade = new Neo4jGraphFacade(graphDirPath);
		
		List<Long> usersIds = new ArrayList<Long>();
		usersIds.add(111l);
		usersIds.add(222l);
		usersIds.add(333l);
		usersIds.add(4l);
		
		graphFacade.addUsers(usersIds);
		
		for (Long userId : usersIds) {
			Vertex vertex = graphFacade.getUserVertex(userId);
			Assert.assertEquals( userId , vertex.getProperty("userId") );
		}
		delete(new File(graphDirPath));
	}
	
	@Test(enabled=false)
	public void addFollowersTEST() throws IOException, UserVertexNotPresent {
		String graphDirPath = "src/test/resources/graphs/addFollowersTEST";
		delete(new File(graphDirPath));
		
		Neo4jGraphFacade graphFacade = new Neo4jGraphFacade(graphDirPath);
		
		long userId = 111;	
		graphFacade.addUser(userId);
		Assert.assertTrue( graphFacade.getUserVertex(userId) != null);
		
		List<Long> followersIds = new ArrayList<Long>();
		followersIds.add(222l);	
		followersIds.add(333l);
		followersIds.add(444l);
		graphFacade.addFollowers(userId, followersIds);
		
		Assert.assertEquals(graphFacade.getVerticesCount(), (1+followersIds.size()) );
		
		Vertex userVertex = graphFacade.getUserVertex(userId);
		Iterator<Vertex> followersIterator = userVertex.getVertices(Direction.OUT, "follows").iterator();
		while (followersIterator.hasNext()) {
			Vertex followerVertex = followersIterator.next();
			Long followerId = (Long) followerVertex.getProperty("userId");
			logger.info(followerId);
			Assert.assertTrue( followersIds.contains(followerId) );
		}
		
		delete(new File(graphDirPath));
	}
	
	
	@Test(enabled=false)
	public void addFriendsTEST() throws IOException, UserVertexNotPresent {
		String graphDirPath = "src/test/resources/graphs/addFriendsTEST";
		delete(new File(graphDirPath));
		
		Neo4jGraphFacade graphFacade = new Neo4jGraphFacade(graphDirPath);
		
		long userId = 111;	
		graphFacade.addUser(userId);
		Assert.assertTrue( graphFacade.getUserVertex(userId) != null);
		
		List<Long> friendsIds = new ArrayList<Long>();
		friendsIds.add(222l);	
		friendsIds.add(333l);
		friendsIds.add(444l);
		graphFacade.addFriends(userId, friendsIds);
		
		Assert.assertEquals(graphFacade.getVerticesCount(), (1+friendsIds.size()) );
		
		Vertex userVertex = graphFacade.getUserVertex(userId);
		Iterator<Vertex> friendsIterator = userVertex.getVertices(Direction.IN, "follows").iterator();
		while (friendsIterator.hasNext()) {
			Vertex friendVertex = friendsIterator.next();
			Long friendId = (Long) friendVertex.getProperty("userId");
			logger.info(friendId);
			Assert.assertTrue( friendsIds.contains(friendId) );
		}
		
		delete(new File(graphDirPath));
	}
	
		
		
	public static void delete(File file) throws IOException{
		if(file.isDirectory()){
			//directory is empty, then delete it
	    	if(file.list().length==0){	 
	    		   file.delete();
	    		   logger.info("Directory is deleted : " 
	                                                 + file.getAbsolutePath());
	    	}
	    	else{
	    		//list all the directory contents
	    		String files[] = file.list();
	 
	    		for (String temp : files) {
	    			//construct the file structure
	    			File fileDelete = new File(file, temp);
	    			//recursive delete
	    			delete(fileDelete);
	    		}
	    		//check the directory again, if empty then delete it
	        	if(file.list().length==0){
	        		file.delete();
	        	    logger.info("Directory is deleted : " + file.getAbsolutePath());
	        	   }
	    		}
	 
	    	}else{
	    		//if file, then delete it
	    		file.delete();
	    		logger.info("File is deleted : " + file.getAbsolutePath());
	    	}
	    }
}
	
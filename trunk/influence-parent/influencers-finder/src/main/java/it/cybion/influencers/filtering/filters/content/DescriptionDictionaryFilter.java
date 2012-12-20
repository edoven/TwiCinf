package it.cybion.influencers.filtering.filters.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import it.cybion.influencers.filtering.filters.Filter;

public class DescriptionDictionaryFilter implements Filter{

	List<String> dictionary;
	Map<Long,String> user2description;
	
	public DescriptionDictionaryFilter(List<String> dictionary,
									   Map<Long,String> user2description) {
		super();
		this.dictionary = dictionary;	
		this.user2description = user2description;
	}
	

	public void setDescriptions(Map<Long,String> user2description) {
		this.user2description = user2description;
	}

	@Override
	public List<Long> filter() {
		List<Long> goodUsers = new ArrayList<Long>();
		for (Entry<Long, String> mapEntry : user2description.entrySet()) {
			String description = mapEntry.getValue().toLowerCase();
			Long userId = mapEntry.getKey();
			for (String keyWord : dictionary)
				if (description.contains(keyWord)) {
					goodUsers.add(userId);
					break;
				}					
		}
		return goodUsers;
	}

}

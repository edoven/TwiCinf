package servlets.model;

import it.cybion.influencers.ranking.RankedUser;
import it.cybion.model.twitter.User;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class InfluenceUser {

    private RankedUser rankedUser;

    private User twitterUser;

    public InfluenceUser() {

    }

    public InfluenceUser(RankedUser rankedUser, User twitterUser) {

        this.rankedUser = rankedUser;
        this.twitterUser = twitterUser;
    }

    public RankedUser getRankedUser() {

        return rankedUser;
    }

    public void setRankedUser(RankedUser rankedUser) {

        this.rankedUser = rankedUser;
    }

    public User getTwitterUser() {

        return twitterUser;
    }

    public void setTwitterUser(User twitterUser) {

        this.twitterUser = twitterUser;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InfluenceUser that = (InfluenceUser) o;

        if (rankedUser != null ? !rankedUser.equals(that.rankedUser) : that.rankedUser != null) {
            return false;
        }
        if (twitterUser != null ? !twitterUser.equals(that.twitterUser) :
                that.twitterUser != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {

        int result = rankedUser != null ? rankedUser.hashCode() : 0;
        result = 31 * result + (twitterUser != null ? twitterUser.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        return "InfluenceUser{" +
               "rankedUser=" + rankedUser +
               ", twitterUser=" + twitterUser +
               '}';
    }
}

package com.emis.emismobile.knowledge;

import com.emis.emismobile.knowledge.persistence.ArticleVoteLocalRepository;
import com.emis.emismobile.knowledge.persistence.ArticleVoteLocalRepository.VoteType;
import com.emis.emismobile.knowledge.web.rest.ArticleRestRepository;


public class ArticleVoteService {
    private ArticleVoteLocalRepository voteRepository;
    private ArticleRestRepository articleRestRepository;

    public ArticleVoteService(ArticleVoteLocalRepository voteRepository,
                              ArticleRestRepository articleRestRepository) {
        this.voteRepository = voteRepository;
        this.articleRestRepository = articleRestRepository;
    }

    public VoteType getVote(String articleId) {
        return voteRepository.getVote(articleId);
    }

    public void upvote(String articleId) {
        if (currentlyUpvoted(articleId)) {
            // todo Vote(1, 0)
            removeVote(articleId);
        } else if (currentlyDownvoted(articleId)) {
            // todo Vote(-1, 1)
            voteRepository.addVote(VoteType.UPVOTE, articleId);
        } else {
            // todo Vote(0, 1)
        }
    }

    public void downvote(String articleId) {
        Vote vote;
        if (currentlyDownvoted(articleId)) {
            // todo Vote(-1, 0)
            vote = new Vote(articleId, -1, 0);
            removeVote(articleId);
        } else {
            if (currentlyUpvoted(articleId)) {
                vote = new Vote(articleId, 1, -1);
            }
            vote = new Vote(articleId, 0, -1);

            articleRestRepository.postVote(vote);

            voteRepository.addVote(VoteType.DOWNVOTE, articleId);
        }
    }

    /**
     * Returns whether the article is upvoted in the local storage.
     *
     * @param articleId the ID of the article
     * @return true if the article is upvoted, false if it's empty or downvoted
     */
    public boolean currentlyUpvoted(String articleId) {
        return VoteType.UPVOTE.equals(voteRepository.getVote(articleId));
    }

    /**
     * Returns whether the article is downvoted in the local storage.
     *
     * @param articleId the ID of the article
     * @return true if the article is downvoted, false if it's empty or upvoted
     */
    public boolean currentlyDownvoted(String articleId) {
        return VoteType.DOWNVOTE.equals(voteRepository.getVote(articleId));
    }

    private void removeVote(String articleId) {
        // todo make request to server

        voteRepository.removeVote(articleId);
    }
}

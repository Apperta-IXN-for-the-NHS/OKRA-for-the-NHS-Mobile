package com.emis.emismobile.knowledge;

import android.content.Context;
import android.content.ContextWrapper;

import com.emis.emismobile.knowledge.persistence.ArticleVoteLocalRepository;
import com.emis.emismobile.knowledge.persistence.ArticleVoteLocalRepository.VoteType;
import com.emis.emismobile.knowledge.web.rest.ArticleRestRepository;


public class ArticleVoteService extends ContextWrapper {
    private ArticleVoteLocalRepository voteRepository;
    private ArticleRestRepository articleRestRepository;

    public ArticleVoteService(Context base) {
        super(base);
        voteRepository = new ArticleVoteLocalRepository(this);
        articleRestRepository = ArticleRestRepository.getInstance();
    }

    public VoteType getVote(String articleId) {
        return voteRepository.getVote(articleId);
    }

    public void upvote(String articleId) {
        // todo make request to server

        if (alreadyUpvoted(articleId)) {
            removeVote(articleId);
        } else {
            voteRepository.addVote(VoteType.UPVOTE, articleId);
        }
    }

    public void downvote(String articleId) {
        // todo make request to server

        if (alreadyDownvoted(articleId)) {
            removeVote(articleId);
        } else {
            voteRepository.addVote(VoteType.DOWNVOTE, articleId);
        }
    }

    /**
     * Returns whether the article is upvoted in the local storage.
     *
     * @param articleId the ID of the article
     * @return true if the article is upvoted, false if it's empty or downvoted
     */
    public boolean alreadyUpvoted(String articleId) {
        return VoteType.UPVOTE.equals(voteRepository.getVote(articleId));
    }

    /**
     * Returns whether the article is downvoted in the local storage.
     *
     * @param articleId the ID of the article
     * @return true if the article is downvoted, false if it's empty or upvoted
     */
    public boolean alreadyDownvoted(String articleId) {
        return VoteType.DOWNVOTE.equals(voteRepository.getVote(articleId));
    }

    private void removeVote(String articleId) {
        // todo make request to server

        voteRepository.removeVote(articleId);
    }
}

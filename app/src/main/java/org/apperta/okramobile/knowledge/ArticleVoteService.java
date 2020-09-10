package org.apperta.okramobile.knowledge;

import android.util.Log;

import org.apperta.okramobile.knowledge.persistence.ArticleVoteLocalRepository;
import org.apperta.okramobile.knowledge.web.rest.ArticleRestRepository;

public class ArticleVoteService {
    private static final String TAG = "ArticleVoteService";

    private ArticleVoteLocalRepository localRepository;
    private ArticleRestRepository articleRestRepository;

    public ArticleVoteService(ArticleVoteLocalRepository localRepository,
                              ArticleRestRepository articleRestRepository) {
        this.localRepository = localRepository;
        this.articleRestRepository = articleRestRepository;
    }

    public ArticleVoteLocalRepository.VoteType getVote(String articleId) {
        return localRepository.getVote(articleId);
    }

    /**
     * Makes an upvote request to the backend. If the backend call succeeds,
     * persists the vote to the local Android storage. If the article is already
     * upvoted, removes the vote.
     *
     * @param articleId - the ID of the article to be upvoted
     */
    public void upvote(String articleId) {
        Vote vote;
        if (currentlyUpvoted(articleId)) {
            vote = new Vote(articleId, 1, 0);
        } else if (currentlyDownvoted(articleId)) {
            vote = new Vote(articleId, -1, 1);
        } else {
            vote = new Vote(articleId, 0, 1);
        }

        postVoteAndPersistLocally(vote);
    }

    /**
     * Makes an downvote request to the backend. If the backend call succeeds,
     * persists the vote to the local Android storage. If the article is already
     * downvoted, removes the vote.
     *
     * @param articleId - the ID of the article to be downvoted
     */
    public void downvote(String articleId) {
        Vote vote;
        if (currentlyDownvoted(articleId)) {
            vote = new Vote(articleId, -1, 0);
        } else if (currentlyUpvoted(articleId)) {
            vote = new Vote(articleId, 1, -1);
        } else {
            vote = new Vote(articleId, 0, -1);
        }

        postVoteAndPersistLocally(vote);
    }

    private void postVoteAndPersistLocally(Vote vote) {
        articleRestRepository.postVoteWithCallback(vote, new VoidCallback() {
            @Override
            public void onSuccess() {
                logSuccess(vote);
                persistLocally(vote);
            }

            @Override
            public void onFailure() {
                logError(vote);
            }
        });
    }

    private void persistLocally(Vote vote) {
        switch (vote.getCurrent()) {
            case 0:
                localRepository.removeVote(vote.getArticleId());
                break;
            case 1:
                localRepository.addVote(ArticleVoteLocalRepository.VoteType.UPVOTE, vote.getArticleId());
                break;
            case -1:
                localRepository.addVote(ArticleVoteLocalRepository.VoteType.DOWNVOTE, vote.getArticleId());
                break;
        }
    }

    /**
     * Returns whether the article is upvoted in the local storage.
     *
     * @param articleId the ID of the article
     * @return true if the article is upvoted, false if it's empty or downvoted
     */
    public boolean currentlyUpvoted(String articleId) {
        return ArticleVoteLocalRepository.VoteType.UPVOTE.equals(localRepository.getVote(articleId));
    }

    /**
     * Returns whether the article is downvoted in the local storage.
     *
     * @param articleId the ID of the article
     * @return true if the article is downvoted, false if it's empty or upvoted
     */
    public boolean currentlyDownvoted(String articleId) {
        return ArticleVoteLocalRepository.VoteType.DOWNVOTE.equals(localRepository.getVote(articleId));
    }

    private static void logSuccess(Vote vote) {
        Log.i(TAG, String.format("%s executed successfully.", vote));
    }

    private static void logError(Vote vote) {
        Log.e(TAG, String.format("%s failed to execute.", vote));
    }
}

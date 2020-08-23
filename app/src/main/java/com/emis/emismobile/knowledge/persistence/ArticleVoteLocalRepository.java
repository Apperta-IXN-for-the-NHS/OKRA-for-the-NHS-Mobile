package com.emis.emismobile.knowledge.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.emis.emismobile.R;

public class ArticleVoteLocalRepository {
    public enum VoteType {UPVOTE, DOWNVOTE}

    private SharedPreferences sharedPreferences;

    public ArticleVoteLocalRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    /**
     * Get the vote type for an article.
     *
     * @param articleId the ID of the article
     * @return the {@link VoteType} of the article or null if it doesn't exist
     */
    public VoteType getVote(String articleId) {
        final String NO_VOTE = "";

        String voteType = sharedPreferences.getString(articleId, NO_VOTE);

        if (NO_VOTE.equals(voteType)) {
            return null;
        }

        return VoteType.valueOf(voteType);
    }

    public void addVote(VoteType voteType, String articleId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(articleId, voteType.toString());
        editor.apply();
    }

    public void removeVote(String articleId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(articleId);
        editor.apply();
    }
}

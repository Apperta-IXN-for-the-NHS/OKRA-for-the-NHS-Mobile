package com.emis.emismobile.knowledge.persistence;

import android.content.SharedPreferences;

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

    /**
     * Saves the vote type for the specified article ID. Updates any existing previous entry for the articleId.
     *
     * @param voteType UPVOTE or DOWNVOTE
     * @param articleId the ID of the article that is to be added or updates
     */
    public void addVote(VoteType voteType, String articleId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(articleId, voteType.toString());
        editor.apply();
    }

    /**
     * Removes the vote for the article ID from the local data store.
     */
    public void removeVote(String articleId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(articleId);
        editor.apply();
    }
}

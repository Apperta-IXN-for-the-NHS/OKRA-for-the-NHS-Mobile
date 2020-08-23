package com.emis.emismobile.knowledge.persistence;

import android.content.SharedPreferences;

import com.emis.emismobile.knowledge.persistence.ArticleVoteLocalRepository.VoteType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ArticleVoteLocalRepositoryTest {
    private SharedPreferences sharedPreferences;
    private ArticleVoteLocalRepository localRepository;

    @Before
    public void setUp() {
        sharedPreferences = mock(SharedPreferences.class);
        localRepository = new ArticleVoteLocalRepository(sharedPreferences);
    }

    @Test
    public void when_vote_does_not_exist_in_db_then_return_null() {
        String articleId = "some_id";
        String noVote = "";

        when(sharedPreferences.getString(articleId, noVote)).thenReturn(noVote);

        assertNull(localRepository.getVote(articleId));
    }

    @Test
    public void when_vote_exists_in_db_then_return_vote_type() {
        String articleId = "some_id";
        String noVote = "";
        String voteType = VoteType.UPVOTE.toString();

        when(sharedPreferences.getString(articleId, noVote)).thenReturn(voteType);

        assertEquals(VoteType.UPVOTE, localRepository.getVote(articleId));
    }

    @Test
    public void when_add_vote_then_put_in_db_and_apply() {
        String articleId = "some_id";
        VoteType voteType = VoteType.UPVOTE;

        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        localRepository.addVote(voteType, articleId);

        verify(editor).putString(articleId, voteType.toString());
        verify(editor).apply();
    }

    @Test
    public void when_remove_vote_then_remove_from_db_and_apply() {
        String articleId = "some_id";

        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        localRepository.removeVote(articleId);

        verify(editor).remove(articleId);
        verify(editor).apply();
    }
}
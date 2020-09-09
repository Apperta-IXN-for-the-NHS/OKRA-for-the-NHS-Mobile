package org.apperta.okramobile.knowledge;

import org.apperta.okramobile.knowledge.persistence.ArticleVoteLocalRepository;
import org.apperta.okramobile.knowledge.persistence.ArticleVoteLocalRepository.VoteType;
import org.apperta.okramobile.knowledge.web.rest.ArticleRestRepository;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ArticleVoteServiceTest {
    private ArticleVoteService voteService;
    private ArticleVoteLocalRepository voteLocalRepository;
    private ArticleRestRepository restRepository;

    @Before
    public void setUp() {
        voteLocalRepository = mock(ArticleVoteLocalRepository.class);
        restRepository = mock(ArticleRestRepository.class);
        voteService = new ArticleVoteService(voteLocalRepository, restRepository);
    }

    @Test
    public void when_get_vote_verify_get_from_db() {
        String articleId = "some_id";

        voteService.getVote(articleId);

        verify(voteLocalRepository).getVote(articleId);
    }

    @Test
    public void given_article_upvoted_in_db_then_return_already_upvoted() {
        String articleId = "some_id";
        when(voteLocalRepository.getVote(articleId)).thenReturn(VoteType.UPVOTE);

        assertTrue(voteService.currentlyUpvoted(articleId));
    }

    @Test
    public void given_article_downvoted_in_db_then_return_already_downvoted() {
        String articleId = "some_id";
        when(voteLocalRepository.getVote(articleId)).thenReturn(VoteType.DOWNVOTE);

        assertTrue(voteService.currentlyDownvoted(articleId));
    }

}
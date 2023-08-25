package com.io.realworldjpa.domain.article.service;

import com.io.realworldjpa.IntegrationTest;
import com.io.realworldjpa.domain.article.entity.*;
import com.io.realworldjpa.domain.article.model.ArticlePostRequest;
import com.io.realworldjpa.domain.article.model.ArticlePutRequest;
import com.io.realworldjpa.domain.article.model.CommentPostRequest;
import com.io.realworldjpa.domain.article.model.MultipleArticleRequest;
import com.io.realworldjpa.domain.user.entity.Email;
import com.io.realworldjpa.domain.user.entity.Password;
import com.io.realworldjpa.domain.user.entity.Profile;
import com.io.realworldjpa.domain.user.entity.User;
import com.io.realworldjpa.domain.user.service.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
@DisplayName("ArticleService Test")
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User shimki;
    private User bangki;
    private User tangki;
    private Article preTestArticle;
    private String testSlug;
    private String testComment;

    @BeforeEach
    void setupUsers() throws Exception {
        shimki = new User.Builder()
                .email(new Email("shimki@example.com"))
                .profile(Profile.of("shimki", "shimki's bio", "shimki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();

        bangki = new User.Builder()
                .email(new Email("bangki@example.com"))
                .profile(Profile.of("bangki", "bangki's bio", "bangki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();

        tangki = new User.Builder()
                .email(new Email("tangki@example.com"))
                .profile(Profile.of("tangki", "tangki's bio", "tangki.jpg"))
                .password(Password.of("testPassword", passwordEncoder))
                .build();

        userRepository.save(shimki);
        userRepository.save(bangki);
        userRepository.save(tangki);

        preTestArticle = new Article.Builder()
                .title("How to train your dragon")
                .description("Ever wonder how?")
                .body("Very carefully.")
                .author(shimki)
                .build();

        Tag tag1 = new Tag("training");
        Tag tag2 = new Tag("dragons");

        preTestArticle.addTag(tag1);
        preTestArticle.addTag(tag2);

        articleRepository.save(preTestArticle);

        for(int i = 1; i < 8; i++) {
            Article testArticle = new Article.Builder()
                    .title("How to get your dragon " + i)
                    .description("Ever wonder how?")
                    .body("Very carefully.")
                    .author(bangki)
                    .build();

            articleRepository.save(testArticle);
        }

        for(int i = 8; i < 13; i++) {
            Article testArticle = new Article.Builder()
                    .title("How to get your dragon " + i)
                    .description("Ever wonder how?")
                    .body("Very carefully.")
                    .author(tangki)
                    .build();

            articleRepository.save(testArticle);
        }

        testSlug = "how-to-train-your-dragon";
        testComment = "Comments for Test";
    }

    @Test
    @DisplayName("Get_Single_Article")
    void Get_Single_Article() throws Exception {
        // when
        ArticleDto articleDto = articleService.getArticle(shimki, testSlug);

        // then
        assertThat(articleDto.title()).isEqualTo("How to train your dragon");
        assertThat(articleDto.slug()).isEqualTo("how-to-train-your-dragon");
        assertThat(articleDto.description()).isEqualTo("Ever wonder how?");
        assertThat(articleDto.body()).isEqualTo("Very carefully.");
        assertThat(articleDto.tagList()).contains("training");
        assertThat(articleDto.tagList()).contains("dragons");
    }

    @Test
    @DisplayName("Get_Not_Exist_Article_Expect_Error")
    void Get_Not_Exist_Article_Expect_Error() throws Exception {
        // when
        assertThatThrownBy(() -> articleService.getArticle(shimki, "not-exist-slug"))

        // then
                .isInstanceOf(NoSuchElementException.class).hasMessageContaining("존재하지 않습니다.");
    }

    @Test
    @DisplayName("Get_Multi_Articles")
    void Get_Multi_Articles() throws Exception {
        // given
        MultipleArticleRequest request =
                new MultipleArticleRequest(null, tangki.getProfile().getUsername(), null, null, null);

        // when
        List<ArticleDto> articleDtos = articleService.getArticles(shimki, request);

        // then
        assertThat(articleDtos.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("Get_Feed_Articles_1")
    void Get_Feed_Articles_1() throws Exception {
        // given
        MultipleArticleRequest request =
                new MultipleArticleRequest(null, null, null, null, null);

        // when
        shimki.followUser(bangki);
        List<ArticleDto> articleDtos = articleService.getFeedArticles(shimki, request);

        // then
        assertThat(articleDtos.size()).isEqualTo(7);
    }

    @Test
    @DisplayName("Get_Feed_Articles_2")
    void Get_Feed_Articles_2() throws Exception {
        // given
        MultipleArticleRequest request =
                new MultipleArticleRequest(null, null, null, null, null);

        // when
        shimki.followUser(tangki);
        List<ArticleDto> articleDtos = articleService.getFeedArticles(shimki, request);

        // then
        assertThat(articleDtos.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("Get_Feed_Articles_3")
    void Get_Feed_Articles_3() throws Exception {
        // given
        MultipleArticleRequest request =
                new MultipleArticleRequest(null, null, null, null, null);

        // when
        shimki.followUser(bangki);
        shimki.followUser(tangki);
        List<ArticleDto> articleDtos = articleService.getFeedArticles(shimki, request);

        // then
        assertThat(articleDtos.size()).isEqualTo(12);
    }

    @Test
    @DisplayName("Post_Article")
    void Post_Article() throws Exception {
        // given
        ArticlePostRequest testRequest =
                new ArticlePostRequest("test Article","test description", "test body of content", List.of("testTag1", "dragons"));

        // when
        ArticleDto articleDto = articleService.createArticle(shimki, testRequest);

        // then
        assertThat(testRequest.title()).isEqualTo(articleDto.title());
        assertThat(testRequest.description()).isEqualTo(articleDto.description());
        assertThat(testRequest.body()).isEqualTo(articleDto.body());
        assertThat(testRequest.tagList()).contains(articleDto.tagList());
        assertThat(shimki.getProfile().getUsername()).isEqualTo(articleDto.author().username());
    }

    @Test
    @DisplayName("Post_Article_With_Exist_Tag_Expect_OK")
    void Post_Article_With_Exist_Tag_Expect_OK() throws Exception {
        // given
        ArticlePostRequest testRequest =
                new ArticlePostRequest("test Article","test description", "test body of content", List.of("testTag1", "dragons"));

        // when
        ArticleDto articleDto = articleService.createArticle(shimki, testRequest);

        // then
        assertThat(testRequest.title()).isEqualTo(articleDto.title());
        assertThat(testRequest.description()).isEqualTo(articleDto.description());
        assertThat(testRequest.body()).isEqualTo(articleDto.body());
        assertThat(testRequest.tagList()).contains(articleDto.tagList());
        assertThat(shimki.getProfile().getUsername()).isEqualTo(articleDto.author().username());
    }

    @Test
    @DisplayName("Post_Article_With_Exist_Title_Expect_Error")
    void Post_Article_With_Exist_Title_Expect_Error() throws Exception {
        // given
        ArticlePostRequest testRequest =
                new ArticlePostRequest("How to train your dragon","test description", "test body of content", List.of("testTag1", "dragons"));

        // when
        assertThatThrownBy(() -> articleService.createArticle(shimki, testRequest))

        // then
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Update_Article")
    void Update_Article() throws Exception {
        // given
        ArticlePutRequest testRequest
                = new ArticlePutRequest("test update title", "test description", "With two hands");

        // when
        articleService.updateArticle(shimki, testSlug, testRequest);

        // then
        assertThat(preTestArticle.getTitle()).isEqualTo("test update title");
        assertThat(preTestArticle.getSlug()).isEqualTo("test-update-title");
        assertThat(preTestArticle.getDescription()).isEqualTo("test description");
        assertThat(preTestArticle.getBody()).isEqualTo("With two hands");
    }

    @Test
    @DisplayName("Update_Not_Exist_Article_Expect_Error")
    void Update_Not_Exist_Article_Expect_Error() throws Exception {
        // given
        ArticlePutRequest testRequest
                = new ArticlePutRequest("test update title", "test description", "With two hands");

        // when
        assertThatThrownBy(() -> articleService.updateArticle(shimki, "not-exist-slug", testRequest))

        // then
                .isInstanceOf(NoSuchElementException.class).hasMessageContaining("존재하지 않습니다.");
    }

    @Test
    @DisplayName("Update_Not_My_Article_Expect_Error")
    void Update_Not_My_Article_Expect_Error() throws Exception {
        // given
        ArticlePutRequest testRequest
                = new ArticlePutRequest("test update title", "test description", "With two hands");

        // when
        assertThatThrownBy(() -> articleService.updateArticle(bangki, testSlug, testRequest))

        // then
                .isInstanceOf(IllegalArgumentException.class).hasMessage("수정 권한이 없습니다.");
        assertThat(preTestArticle.getAuthor()).isEqualTo(shimki);
        assertThat(preTestArticle.isNotPostByMe(shimki)).isFalse();
        assertThat(preTestArticle.isNotPostByMe(bangki)).isTrue();
    }

    @Test
    @DisplayName("Delete_Article")
    void Delete_Article() throws Exception {
        // when
        articleService.deleteArticle(shimki, testSlug);

        // then
        assertThat(articleRepository.existsBySlug(testSlug)).isFalse();
    }

    @Test
    @DisplayName("Delete_Article_Not_Exist_Slug")
    void Delete_Article_Not_Exist_Slug() throws Exception {
        // when
        assertThatThrownBy(() -> articleService.deleteArticle(shimki, "not-exist-slug"))

        // then
                .isInstanceOf(NoSuchElementException.class).hasMessageContaining("존재하지 않습니다.");
    }

    @Test
    @DisplayName("Delete_Article_Not_Author_Expect_Error")
    void Delete_Article_Not_Author_Expect_Error() throws Exception {
        // when
        assertThatThrownBy(() -> articleService.deleteArticle(bangki, testSlug))

        // then
                .isInstanceOf(IllegalArgumentException.class).hasMessage("삭제 권한이 없습니다.");
    }

    @Test
    @DisplayName("Post_Comment")
    void Post_Comment() throws Exception {
        // given
        CommentPostRequest testRequest = new CommentPostRequest(testComment);

        // when
        CommentDto comment = articleService.createComment(shimki, testSlug, testRequest);

        // then
        assertThat(comment.body()).isEqualTo("Comments for Test");
        assertThat(comment.author().username()).isEqualTo("shimki");
        assertThat(comment.author().following()).isEqualTo(false);
    }

    @Test
    @DisplayName("Post_Comment_Not_Exist_Article_Expect_Error")
    void Post_Comment_Not_Exist_Article_Expect_Error() throws Exception {
        // given
        CommentPostRequest testRequest = new CommentPostRequest(testComment);

        // when
        assertThatThrownBy(() -> articleService.createComment(shimki, "not-exist-article", testRequest))

                // then
                .isInstanceOf(NoSuchElementException.class).hasMessageContaining("존재하지 않습니다.");
    }

    @Test
    @DisplayName("Get_Comments_Article")
    void Get_All_Comments_Article() throws Exception {
        // given
        CommentPostRequest testComment1 = new CommentPostRequest(testComment);
        CommentPostRequest testComment2 = new CommentPostRequest("bangki Test Comment");
        CommentPostRequest testComment3 = new CommentPostRequest("tangki Test Comment");
        for (int i = 0; i < 3; i++) {
            articleService.createComment(shimki, testSlug, testComment1);
            articleService.createComment(bangki, testSlug, testComment2);
            articleService.createComment(tangki, testSlug, testComment3);
        }

        // when
        List<CommentDto> articleComments = articleService.getArticleComments(shimki, testSlug);

        // then
        assertThat(articleComments.size()).isEqualTo(9);
        assertThat(articleComments.get(0).author().username()).isEqualTo("tangki");
        assertThat(articleComments.get(0).author().following()).isEqualTo(false);
        assertThat(articleComments.get(0).body()).isEqualTo("tangki Test Comment");
    }

    @Test
    @DisplayName("Get_All_Comments_Article_With_Follow_User")
    void Get_All_Comments_Article_With_Follow_User() throws Exception {
        // given
        CommentPostRequest testComment1 = new CommentPostRequest(testComment);
        CommentPostRequest testComment2 = new CommentPostRequest("bangki Test Comment");
        CommentPostRequest testComment3 = new CommentPostRequest("tangki Test Comment");
        for (int i = 0; i < 3; i++) {
            articleService.createComment(shimki, testSlug, testComment1);
            articleService.createComment(bangki, testSlug, testComment2);
            articleService.createComment(tangki, testSlug, testComment3);
        }

        shimki.followUser(bangki);

        // when
        List<CommentDto> articleComments = articleService.getArticleComments(shimki, testSlug);

        // then
        assertThat(articleComments.size()).isEqualTo(9);
        assertThat(articleComments.get(1).author().username()).isEqualTo("bangki");
        assertThat(articleComments.get(1).author().following()).isEqualTo(true);
        assertThat(articleComments.get(1).body()).isEqualTo("bangki Test Comment");
    }

    @Test
    @DisplayName("Get_All_Comments_Article_With_UnFollow_User")
    void Get_All_Comments_Article_With_UnFollow_User() throws Exception {
        // given
        CommentPostRequest testComment1 = new CommentPostRequest(testComment);
        CommentPostRequest testComment2 = new CommentPostRequest("bangki Test Comment");
        CommentPostRequest testComment3 = new CommentPostRequest("tangki Test Comment");
        for (int i = 0; i < 3; i++) {
            articleService.createComment(shimki, testSlug, testComment1);
            articleService.createComment(bangki, testSlug, testComment2);
            articleService.createComment(tangki, testSlug, testComment3);
        }

        shimki.followUser(bangki);

        // when
        List<CommentDto> articleComments = articleService.getArticleComments(tangki, testSlug);

        // then
        assertThat(articleComments.size()).isEqualTo(9);
        assertThat(articleComments.get(1).author().username()).isEqualTo("bangki");
        assertThat(articleComments.get(1).author().following()).isEqualTo(false);
        assertThat(articleComments.get(1).body()).isEqualTo("bangki Test Comment");
    }

    @Test
    @DisplayName("Get_All_Comments_Article_With_Anonymous_User")
    void Get_All_Comments_Article_With_Anonymous_User() throws Exception {
        // given
        CommentPostRequest testComment1 = new CommentPostRequest(testComment);
        CommentPostRequest testComment2 = new CommentPostRequest("bangki Test Comment");
        CommentPostRequest testComment3 = new CommentPostRequest("tangki Test Comment");
        for (int i = 0; i < 3; i++) {
            articleService.createComment(shimki, testSlug, testComment1);
            articleService.createComment(bangki, testSlug, testComment2);
            articleService.createComment(tangki, testSlug, testComment3);
        }

        User anonymous = User.anonymous();

        // when
        List<CommentDto> articleComments = articleService.getArticleComments(anonymous, testSlug);

        // then
        assertThat(articleComments.size()).isEqualTo(9);
        assertThat(articleComments.get(0).author().username()).isEqualTo("tangki");
        assertThat(articleComments.get(0).author().following()).isEqualTo(false);
        assertThat(articleComments.get(0).body()).isEqualTo("tangki Test Comment");

        assertThat(articleComments.get(1).author().username()).isEqualTo("bangki");
        assertThat(articleComments.get(1).author().following()).isEqualTo(false);
        assertThat(articleComments.get(1).body()).isEqualTo("bangki Test Comment");

        assertThat(articleComments.get(2).author().username()).isEqualTo("shimki");
        assertThat(articleComments.get(2).author().following()).isEqualTo(false);
        assertThat(articleComments.get(2).body()).isEqualTo("Comments for Test");
    }

    @Test
    @DisplayName("Delete_Comment")
    void Delete_Comment() throws Exception {
        // given
        Comment comment = testCreateComment(shimki);
        commentRepository.saveAndFlush(comment);

        // when
        articleService.deleteComment(shimki, comment.getId());

        // then
        assertThat(commentRepository.existsById(comment.getId())).isEqualTo(false);
    }

    @Test
    @DisplayName("Delete_Comment_With_Wrong_CommentId_Expect_Error")
    void Delete_Comment_With_Wrong_CommentId_Expect_Error() throws Exception {
        // given
        Comment comment1 = testCreateComment(shimki);
        Comment comment2 = testCreateComment(bangki);
        Comment comment3 = testCreateComment(tangki);
        commentRepository.saveAndFlush(comment1);
        commentRepository.saveAndFlush(comment2);
        commentRepository.saveAndFlush(comment3);

        // when
        assertThatThrownBy(() -> articleService.deleteComment(shimki, comment2.getId()+10L))
        // then
                .isInstanceOf(NoSuchElementException.class).hasMessageContaining("존재하지 않습니다.");
        ;
    }

    @Test
    @DisplayName("Delete_Comment_With_Wrong_AuthorId_Expect_Error")
    void Delete_Comment_With_Wrong_AuthorId_Expect_Error() throws Exception {
        // given
        Comment comment1 = testCreateComment(shimki);
        Comment comment2 = testCreateComment(bangki);
        Comment comment3 = testCreateComment(tangki);
        commentRepository.saveAndFlush(comment1);
        commentRepository.saveAndFlush(comment2);
        commentRepository.saveAndFlush(comment3);

        // when
        assertThatThrownBy(() -> articleService.deleteComment(shimki, comment2.getId()))

        // then
                .isInstanceOf(IllegalArgumentException.class).hasMessage("댓글 삭제 권한이 없습니다.");
        ;
    }

    private Comment testCreateComment(User author) {
        return new Comment.Builder()
                .article(preTestArticle)
                .author(author)
                .body("%s Test Comment".formatted(author.getProfile().getUsername()))
                .build();
    }
}
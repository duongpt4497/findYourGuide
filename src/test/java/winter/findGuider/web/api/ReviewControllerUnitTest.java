package winter.findGuider.web.api;

import entities.Review;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import services.Review.ReviewService;

import java.util.List;

import static org.mockito.Mockito.when;

public class ReviewControllerUnitTest {
    @InjectMocks
    ReviewController reviewController;

    @Mock
    ReviewService reviewService;

    @Rule
    public ExpectedException thrown= ExpectedException.none();
    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindReviewByGuiderId(){
        ResponseEntity<List<Review>>  result = reviewController.findReviewByGuiderId(1);
        Assert.assertEquals(200,result.getStatusCodeValue());
    }

    @Test(expected = AssertionError.class)
    public void testFindReviewByGuiderIdWithException() throws Exception {
        thrown.expect(AssertionError.class);
        when(reviewService.findReviewsByGuiderId(1)).thenThrow(Exception.class);
        ResponseEntity<List<Review>>  result = reviewController.findReviewByGuiderId(1);
        Assert.assertEquals(404,result.getStatusCodeValue());
    }

    @Test
    public void testFindReviewByPostId(){
        ResponseEntity<List<Review>>  result = reviewController.findReviewByPostId(1);
        Assert.assertEquals(200,result.getStatusCodeValue());
    }

    @Test(expected = AssertionError.class)
    public void testFindReviewByPostIdWithException() throws Exception {
        thrown.expect(AssertionError.class);
        when(reviewService.findReviewsByPostId(1)).thenThrow(Exception.class);
        ResponseEntity<List<Review>>  result = reviewController.findReviewByPostId(1);
        Assert.assertEquals(404,result.getStatusCodeValue());
    }

    @Test
    public void testCreateReview(){
        Review review = Mockito.mock(Review.class);
        ResponseEntity<Boolean>  result = reviewController.createReview(review);
        Assert.assertEquals(200,result.getStatusCodeValue());
    }

    @Test(expected = AssertionError.class)
    public void testCreateReviewWithException() throws Exception {
        thrown.expect(AssertionError.class);
        Review review = Mockito.mock(Review.class);
        when(reviewService.createReview(review)).thenThrow(Exception.class);
        ResponseEntity<Boolean>  result = reviewController.createReview(review);
        Assert.assertEquals(404,result.getStatusCodeValue());
    }

    @Test
    public void testCheckReviewExist() throws Exception {
        when(reviewService.checkReviewExist(1)).thenReturn(true);
        ResponseEntity<List<Review>>  result = reviewController.checkReviewExist(1);
        Assert.assertEquals(200,result.getStatusCodeValue());
    }

    @Test
    public void testCheckReviewExist2() throws Exception {
        when(reviewService.checkReviewExist(1)).thenReturn(false);
        ResponseEntity<List<Review>>  result = reviewController.checkReviewExist(1);
        Assert.assertEquals(404,result.getStatusCodeValue());
    }

    @Test(expected = AssertionError.class)
    public void testCheckREviewExistWithException() throws Exception {
        thrown.expect(AssertionError.class);
        when(reviewService.checkReviewExist(1)).thenThrow(Exception.class);
        ResponseEntity<List<Review>>  result = reviewController.checkReviewExist(1);
        Assert.assertEquals(404,result.getStatusCodeValue());
    }
}

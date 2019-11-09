package services.Feedback;

import entities.Feedback;

public interface FeedbackService {
    public boolean sendFeedback(Feedback feedback);

    public boolean createFeedback(Feedback newFeedback);
}

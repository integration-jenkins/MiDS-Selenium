import React, { useState, useEffect } from 'react';
import { FaPaperPlane, FaStar, FaRegStar, FaSmile, FaMeh, FaFrown, FaCheckCircle } from 'react-icons/fa';
import Layout from '../components/Layout';
import '../css/FeedbackPage.css';

const FeedbackPage = () => {
  const [feedback, setFeedback] = useState({
    name: '',
    email: '',
    subject: '',
    message: '',
    rating: 0,
    mood: '',
    isAnonymous: false
  });
  const [hoverRating, setHoverRating] = useState(0);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isSubmitted, setIsSubmitted] = useState(false);
  const [errors, setErrors] = useState({});
  const maxMessageLength = 500;

  const validateForm = () => {
    const newErrors = {};
    if (!feedback.subject.trim()) newErrors.subject = 'Subject is required';
    if (!feedback.message.trim()) newErrors.message = 'Feedback message is required';
    if (feedback.message.length > maxMessageLength) newErrors.message = `Message cannot exceed ${maxMessageLength} characters`;
    if (!feedback.isAnonymous && feedback.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(feedback.email)) {
      newErrors.email = 'Invalid email format';
    }
    if (!feedback.rating) newErrors.rating = 'Please provide a rating';
    if (!feedback.mood) newErrors.mood = 'Please select a mood';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFeedback(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
    // Clear error for the field being edited
    setErrors(prev => ({ ...prev, [name]: '' }));
  };

  const handleRating = (rating) => {
    setFeedback(prev => ({ ...prev, rating }));
    setErrors(prev => ({ ...prev, rating: '' }));
  };

  const handleMood = (mood) => {
    setFeedback(prev => ({ ...prev, mood }));
    setErrors(prev => ({ ...prev, mood: '' }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validateForm()) return;
    setIsSubmitting(true);
    
    // Simulate API submission
    setTimeout(() => {
      setIsSubmitting(false);
      setIsSubmitted(true);
      setFeedback({
        name: '',
        email: '',
        subject: '',
        message: '',
        rating: 0,
        mood: '',
        isAnonymous: false
      });
      setErrors({});
      setHoverRating(0);
      setTimeout(() => setIsSubmitted(false), 3000);
    }, 1500);
  };

  const MoodIcon = ({ type, selected, onClick }) => {
    const Icon = type === 'happy' ? FaSmile : type === 'neutral' ? FaMeh : FaFrown;
    const color = type === 'happy' ? '#10b981' : type === 'neutral' ? '#eab308' : '#ef4444';
    return (
      <div 
        className={`feedback-mood-option${selected ? ' feedback-mood-selected' : ''}`}
        onClick={() => onClick(type)}
        role="button"
        tabIndex={0}
        onKeyPress={(e) => e.key === 'Enter' && onClick(type)}
        aria-label={`Select ${type} mood`}
      >
        <Icon 
          size={36} 
          color={color} 
          className="feedback-mood-icon"
        />
        <span className="feedback-mood-label">
          {type.charAt(0).toUpperCase() + type.slice(1)}
        </span>
      </div>
    );
  };

  return (
    <Layout title="Feedback">
      <div className="feedback-container">
        <div className="feedback-header">
          <p>We value your thoughts and suggestions to improve our platform</p>
        </div>
        
        {isSubmitted ? (
          <div className="feedback-success-card">
            <FaCheckCircle className="feedback-success-icon" />
            <h2>Thank You for Your Feedback!</h2>
            <p>Your insights help us create a better experience for everyone.</p>
            <button 
              className="feedback-back-btn" 
              onClick={() => setIsSubmitted(false)}
              aria-label="Submit another feedback"
            >
              Submit Another Feedback
            </button>
          </div>
        ) : (
          <form className="feedback-form" onSubmit={handleSubmit}>
            <div className="feedback-card">
              <div className="feedback-section">
                <h3>How would you rate your experience?</h3>
                <div className="feedback-stars-container">
                  {[...Array(5)].map((_, i) => {
                    const ratingValue = i + 1;
                    return (
                      <label key={i} className="feedback-star-label">
                        <input
                          type="radio"
                          name="rating"
                          value={ratingValue}
                          onClick={() => handleRating(ratingValue)}
                          style={{ display: 'none' }}
                          aria-label={`${ratingValue} star rating`}
                        />
                        {ratingValue <= (hoverRating || feedback.rating) ? (
                          <FaStar
                            className="feedback-star"
                            color="#FFD700"
                            size={36}
                            onMouseEnter={() => setHoverRating(ratingValue)}
                            onMouseLeave={() => setHoverRating(0)}
                          />
                        ) : (
                          <FaRegStar
                            className="feedback-star"
                            color="#cbd5e1"
                            size={36}
                            onMouseEnter={() => setHoverRating(ratingValue)}
                            onMouseLeave={() => setHoverRating(0)}
                          />
                        )}
                      </label>
                    );
                  })}
                </div>
                {errors.rating && <span className="feedback-error">{errors.rating}</span>}
                <div className="feedback-rating-label">
                  {feedback.rating === 0 ? 'Select your rating' : `Rated: ${feedback.rating} star${feedback.rating > 1 ? 's' : ''}`}
                </div>
              </div>

              <div className="feedback-section">
                <h3>How do you feel about our service?</h3>
                <div className="feedback-mood-container">
                  <MoodIcon 
                    type="happy" 
                    selected={feedback.mood === 'happy'} 
                    onClick={handleMood} 
                  />
                  <MoodIcon 
                    type="neutral" 
                    selected={feedback.mood === 'neutral'} 
                    onClick={handleMood} 
                  />
                  <MoodIcon 
                    type="unhappy" 
                    selected={feedback.mood === 'unhappy'} 
                    onClick={handleMood} 
                  />
                </div>
                {errors.mood && <span className="feedback-error">{errors.mood}</span>}
              </div>
            </div>

            <div className="feedback-card">
              <div className="feedback-form-group">
                <label>Subject</label>
                <input
                  className={`feedback-input${errors.subject ? ' feedback-input-error' : ''}`}
                  type="text"
                  name="subject"
                  value={feedback.subject}
                  onChange={handleChange}
                  placeholder="What is your feedback about?"
                  required
                  aria-describedby={errors.subject ? 'subject-error' : undefined}
                />
                {errors.subject && <span id="subject-error" className="feedback-error">{errors.subject}</span>}
              </div>

              <div className="feedback-form-group">
                <label>Your Feedback</label>
                <textarea
                  className={`feedback-textarea${errors.message ? ' feedback-input-error' : ''}`}
                  name="message"
                  value={feedback.message}
                  onChange={handleChange}
                  placeholder="Please share your detailed feedback..."
                  rows="5"
                  required
                  maxLength={maxMessageLength}
                  aria-describedby={errors.message ? 'message-error' : undefined}
                ></textarea>
                <div className="feedback-char-counter">
                  {feedback.message.length}/{maxMessageLength}
                  {feedback.message.length > maxMessageLength * 0.9 && (
                    <span className="feedback-char-warning"> Approaching limit</span>
                  )}
                </div>
                {errors.message && <span id="message-error" className="feedback-error">{errors.message}</span>}
              </div>

              <div className="feedback-form-grid">
                <div className="feedback-form-group">
                  <label>Name</label>
                  <input
                    className="feedback-input"
                    type="text"
                    name="name"
                    value={feedback.name}
                    onChange={handleChange}
                    placeholder="Your name (optional)"
                    disabled={feedback.isAnonymous}
                    aria-disabled={feedback.isAnonymous}
                  />
                </div>

                <div className="feedback-form-group">
                  <label>Email</label>
                  <input
                    className={`feedback-input${errors.email ? ' feedback-input-error' : ''}`}
                    type="email"
                    name="email"
                    value={feedback.email}
                    onChange={handleChange}
                    placeholder="Your email (optional)"
                    disabled={feedback.isAnonymous}
                    aria-disabled={feedback.isAnonymous}
                    aria-describedby={errors.email ? 'email-error' : undefined}
                  />
                  {errors.email && <span id="email-error" className="feedback-error">{errors.email}</span>}
                </div>
              </div>

              <div className="feedback-anonymous-option">
                <label className="feedback-checkbox-label">
                  <input
                    type="checkbox"
                    name="isAnonymous"
                    checked={feedback.isAnonymous}
                    onChange={handleChange}
                    aria-label="Submit feedback anonymously"
                  />
                  <span className="feedback-checkmark"></span>
                  Submit anonymously
                </label>
              </div>
            </div>

            <button 
              type="submit" 
              className="feedback-submit-btn"
              disabled={isSubmitting || Object.keys(errors).length > 0}
              aria-label="Submit feedback"
            >
              {isSubmitting ? (
                <>
                  <span className="feedback-spinner"></span> Sending...
                </>
              ) : (
                <>
                  <FaPaperPlane /> Submit Feedback
                </>
              )}
            </button>
          </form>
        )}
      </div>
    </Layout>
  );
};

export default FeedbackPage;
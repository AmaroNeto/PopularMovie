package com.amaro.popularmovies.data.review;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ReviewDao {

    @Query("SELECT * FROM review WHERE id_movie = :id")
    LiveData<List<ReviewModel>> loadAllReviewsByMovieId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReview(ReviewModel review);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReviews(List<ReviewModel> reviews);

    @Delete
    void deleteReview(List<ReviewModel> reviews);
}

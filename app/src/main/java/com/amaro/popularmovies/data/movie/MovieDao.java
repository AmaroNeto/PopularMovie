package com.amaro.popularmovies.data.movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY id")
    LiveData<List<MovieModel>> loadAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(MovieModel movieModel);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieModel movieModel);

    @Delete
    void deleteMovie(MovieModel movieModel);

    @Query("SELECT * FROM movie WHERE id = :id ")
    MovieModel findMovieById(int id);
}

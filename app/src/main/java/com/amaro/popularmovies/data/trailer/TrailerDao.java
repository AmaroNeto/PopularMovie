package com.amaro.popularmovies.data.trailer;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TrailerDao {

    @Query("SELECT * FROM trailer WHERE id_movie = :id")
    LiveData<List<TrailerModel>> loadAllTrailersByMovieId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrailer(TrailerModel trailer);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrailers(List<TrailerModel> trailer);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTrailer(TrailerModel trailer);

    @Delete
    void deleteTrailer(TrailerModel trailer);

    @Delete
    void deleteAllTrailers(List<TrailerModel> trailer);
}

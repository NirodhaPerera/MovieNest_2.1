package com.example.movienest_2_1.Databse

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface MovieDAO {
    @Insert
    suspend fun insertAll(movies: List<Movie>)

    @Insert()
    suspend fun insertMovie(movie: Movie)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<Movie>


    @Query("delete from movies")
    suspend fun deleteAll()

    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: Int)

    @Query("SELECT MAX(id) FROM movies")
    suspend fun getLastMovieId(): Int?

    @Query("SELECT * FROM movies WHERE LOWER(actors) LIKE '%' || LOWER(:actor) || '%'")
    suspend fun searchMoviesByActor(actor: String): List<Movie>


}

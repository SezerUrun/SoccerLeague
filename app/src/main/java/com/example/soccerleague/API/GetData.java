package com.example.soccerleague.API;


import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GetData {
    @Headers("X-Auth-Token: 8668a181729c4735a6234d76296de838")
    @GET("competitions/2000/teams")
    Call<Example> getTeams();

    @DELETE("/api/team/remove/id")
    Call<String> deleteTeam(@Query("id") int id);

    @DELETE("/api/teams/remove")
    Call<String> deleteTeams();
}

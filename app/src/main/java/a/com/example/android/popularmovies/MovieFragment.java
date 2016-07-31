package a.com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {
    public static final String LOG_TAG_FRAG = MovieFragment.class.getSimpleName();
    public static final String MOVIE_KEY = "movie_information";
    private MovieArrayAdapter movieArrayAdapter; // Custom ArrayAdapter to show images in GridView
    private ArrayList<Movies> movies; // ArrayList to hold our movies and pass to Adapter
    private  String movieEndPoint = "/movie/popular?"; // Our modifiable movie endpoint to choose between popular movies and top rated

    public MovieFragment() {
    }

    // Override onCreate and setHasOptionsMenu to true so that we can create our Sort
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIE_KEY)){
            new MovieDatabaseRequest().execute();
        }else{
            movies = savedInstanceState.getParcelableArrayList(MOVIE_KEY);
        }
    }


    @Override
    public void onStart(){
        super.onStart();
        updateMovies();
    }

    private void updateMovies(){
        movieEndPoint = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(
                getString(R.string.key_movie_endpoints), getString(R.string.default_endpoint));
        new MovieDatabaseRequest().execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.movie_poster_gridview);
        movieArrayAdapter = new MovieArrayAdapter(getActivity(), R.layout.fragment_main, R.id.movie_poster);
        movieArrayAdapter.setData(movies);
        gridView.setAdapter(movieArrayAdapter);

        // Handle clicks on Movie Posters
        AdapterView.OnItemClickListener messageClickedHandler = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent movieDetail = new Intent(getActivity(), MovieDetailActivity.class);
                movieDetail.putExtra(MOVIE_KEY, movieArrayAdapter.getItem(i));
                startActivity(movieDetail);
            }
        };
        // Pass the OnItemClickListener object we created to our GridView
        gridView.setOnItemClickListener(messageClickedHandler);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList(MOVIE_KEY, movies);
        super.onSaveInstanceState(outState);
    }

    /**
     *  MovieDatabaseRequest queries tmdb.org for it's list of popular movies or top rated movies
     *  based on the value of the global field "movieEndPoint", which can point to either the popular
     *  movies endpoint ("/movie/popular?") or top rated movies endpoint ("/move/top_rated?"). The
     *  default value for "movieEndPoint" will point to the popular movies endpoint. The values are
     *  toggled in onOptionsItemSelected so that they can be controlled by the user.
     *
     *  The request is done on a separate thread using AsyncTask. It takes in a Movies object, and
     *  returns an ArrayList of Movies objects to pass to our custom MovieArrayAdapter for
     *  presentation.
     */
    public class MovieDatabaseRequest extends AsyncTask<Movies, Void, ArrayList<Movies>>{
        // Log tag for this class
        public final String LOG_TAG_MDR = MovieDatabaseRequest.class.getSimpleName();
        private String jsonStr;

        /*
            ----- JSON PARSING CODE------------
         */
        private ArrayList<Movies> getMovieInfo(String jsonStr) throws JSONException{
            // Fields to get from JSON
            final String POSTER_PATH = "poster_path";
            final String TITLE = "original_title";
            final String SYNOPSIS = "overview";
            final String RATING = "vote_average";
            final String RELEASE_DATE = "release_date";

            // Take the jsonStr from our network call and convert it to JSON object
            JSONObject movieList = new JSONObject(jsonStr);
            // Get the array called "results" holding the poster path
            JSONArray results = movieList.getJSONArray("results");
            // Empty array to hold our poster paths
            movies = new ArrayList<Movies>(results.length());
            // Loop through the JSON data to pull and store necessary info
            for (int i = 0; i < results.length();i++){

                Movies movie = new Movies();
                // Get poster path from JSON
                String posterPath = results.getJSONObject(i).getString(POSTER_PATH);
                movie.setPosterPath(posterPath);
                // Get title from JSON
                String title = results.getJSONObject(i).getString(TITLE);
                movie.setTitle(title);
                // Get synopsis from JSON
                String synopsis = results.getJSONObject(i).getString(SYNOPSIS);
                movie.setSynopsis(synopsis);
                // Get rating from JSON
                Double rating = results.getJSONObject(i).getDouble(RATING);
                movie.setRating(rating);
                // Get release date from JSON and set to our movie
                String releaseDate = results.getJSONObject(i).getString(RELEASE_DATE);
                movie.setReleaseDate(releaseDate);
                // Add the movie to our ArrayList
                movies.add (movie);

            }

            return movies;
        }
        /*
            --- END JSON PARSING CODE-----------
         */

        @Override
        protected ArrayList<Movies> doInBackground(Movies...movie){
            // Start creating the network connection
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            BufferedReader reader = null;
            final String BASE_URL = "http://api.themoviedb.org/3";
            try{
                /*
                    Creating the URL and passing it to our connection object to start connection
                 */

                URL tmdbUrl = new URL(BASE_URL + movieEndPoint + "api_key=" + BuildConfig.TMDb_API_KEY);
                httpURLConnection = (HttpURLConnection) tmdbUrl.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                inputStream = httpURLConnection.getInputStream();
                // Check if input stream is null. If so, no point in continuing
                if (inputStream == null) jsonStr = null;

                StringBuffer data = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null){
                    data.append(line +"\n");
                }

                if (data.length() == 0) jsonStr = null;

                jsonStr = data.toString();



            }catch(IOException e){
                jsonStr = null;
                Log.v(LOG_TAG_MDR, "Network Error: "+ e.getMessage());
                return null;
            } finally{
                if (httpURLConnection != null){
                    httpURLConnection.disconnect();
                }
                if (inputStream != null){
                    try{
                        inputStream.close();
                    }catch (IOException e){
                        Log.e(LOG_TAG_MDR, e.getMessage());
                    }
                }
            }

            try{
                movies = getMovieInfo(jsonStr);
                return movies;
            }catch (JSONException e){
                Log.v(LOG_TAG_MDR, e.getMessage());
            }

            return null;
        }

        @Override
        public void onPostExecute(ArrayList<Movies> movieInfo){
            if (movieInfo != null && movieInfo.size() > 0){
                movieArrayAdapter.clear();
                for(Movies movie: movieInfo){
                    movieArrayAdapter.add(movie);
                }
                movieArrayAdapter.notifyDataSetChanged();
            }
        }
    }

}

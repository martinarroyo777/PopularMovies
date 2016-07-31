package a.com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A Fragment which will display our detail view for the selected movie
 */
public class MovieDetailFragment extends Fragment {
    private Bundle receivedIntent;
    private Movies movieDetail;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            receivedIntent = getActivity().getIntent().getExtras();
            movieDetail = (Movies) receivedIntent.getParcelable(MovieFragment.MOVIE_KEY);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
            // Receiving the intent from MovieFragment


            String movieTitleText = movieDetail.getTitle();
            if (movieTitleText != null) Log.v("Movie Title: ", movieDetail.getTitle());

            // Set the details from the Movies object to our layout

            // Get the poster and set it to our image view
            ImageView posterImage = (ImageView) rootView.findViewById(R.id.detail_movie_poster);
            Picasso.with(getContext()).load(movieDetail.getPosterPath()).into(posterImage);

            /*
                Populate the TextView fields
             */

            // Movie Title
            TextView movieTitle = (TextView) rootView.findViewById(R.id.detail_movie_title);
            movieTitle.setText(movieDetail.getTitle());

            // Release Date
            TextView releaseDate = (TextView) rootView.findViewById(R.id.detail_release_date);
            releaseDate.setText(getString(R.string.release_date) + " " + movieDetail.getReleaseDate());
            // Rating
            TextView movieRating = (TextView) rootView.findViewById(R.id.detail_rating);
            movieRating.setText(getString(R.string.rating) + " " + Double.toString(movieDetail.getRating()) + "/10");
            // Synopsis
            TextView synopsis = (TextView) rootView.findViewById(R.id.detail_synopsis);
            synopsis.setText(movieDetail.getSynopsis());

            return rootView;
        }
    }


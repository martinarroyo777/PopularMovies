package a.com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by martinarroyo on 7/31/16.
 *
 * A separate activity to display the details of a movie that the user selects from the main activity
 */
public class MovieDetailActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) { // Restarts the fragment if there is no saved data
            getSupportFragmentManager().beginTransaction().add(R.id.container, new MovieDetailFragment()).commit();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.container, new MovieDetailFragment()).commit();
    }

}

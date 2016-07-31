package a.com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Custom Adapter to display our Movie Posters in the GridView layout
 *
 * Created by martinarroyo on 7/18/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movies> {
    // Fields
    private ArrayList<Movies> moviesArrayList;

    // Constructors
    public MovieArrayAdapter(Context context, int resource, int layout, ArrayList<Movies> moviesArrayList){
        super(context, resource, layout, moviesArrayList);

    }
    public MovieArrayAdapter(Context context, int resource, int layout){
        super(context, resource, layout);

    }

    public void setData(ArrayList <Movies> moviesArrayList){
        this.moviesArrayList = moviesArrayList;
    }


    //Implement our own getView() method
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_poster_view, parent, false);
        }
        Movies movie = getItem(position);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_poster);

        String moviePosterPath = movie.getPosterPath();
        Picasso.with(getContext()).load(moviePosterPath).error(R.drawable.loading_placeholder).into(imageView);

        return imageView;
    }

}

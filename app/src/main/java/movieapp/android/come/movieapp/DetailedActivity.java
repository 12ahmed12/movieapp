package movieapp.android.come.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailedFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailedFragment extends Fragment {

        private final static String LOG_TAG=DetailedFragment.class.getSimpleName();
        private final static String  MOVIE_SHARE=" #MovieAPP";
       // private final static String  shareString ="look that good movie its title ..";
        private String MmoviesTitle;

        public DetailedFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
                      // Inflate the menu; this adds items to the action bar if it is present.
                                inflater.inflate(R.menu.detailedfragment, menu);
                                // Retrieve the share menu item
                                       MenuItem menuItem = menu.findItem(R.id.action_share);

                    // Get the provider and hold onto it to set/change the share intent.
                                      ShareActionProvider mShareActionProvider =
                                       (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

                             // Attach an intent to this ShareActionProvider.  You can update this at any time,
                                     // like when the user selects a new piece of data they might like to share.
                                              if (mShareActionProvider != null ) {
                               mShareActionProvider.setShareIntent(CreateShareMovieIntent());
                           } else {
                               Log.d(LOG_TAG, "Share Action Provider is null?");}
                    }






        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detailed, container, false);
            TextView Title= (TextView) rootView.findViewById(R.id.DetailedTitle);
            ImageView Imag= (ImageView) rootView.findViewById(R.id.DetailedImage);

            TextView Popularity= (TextView) rootView.findViewById(R.id.DetailedPoularity);
            TextView ReleaseDate= (TextView) rootView.findViewById(R.id.ReleaseDate);
            TextView OverView= (TextView) rootView.findViewById(R.id.DetailedOverview);

            Intent intent=getActivity().getIntent();
            if(intent!=null)
            {
                if(intent.hasExtra("img"))
                {
                  String imgString=intent.getStringExtra("img");
                    String IMG_URL="http://image.tmdb.org/t/p/w780"+imgString;
                    String title= intent.getStringExtra("title");
                    MmoviesTitle=title;
                    String overview=intent.getStringExtra("overview");
                    String date=intent.getStringExtra("Rdate");
                    String popularity=intent.getStringExtra("popularity");


                    Picasso.with(getActivity()).load(imgString).into(Imag);
                    Title.setText(title);
                    Popularity.setText("popularity:" + "\n" + popularity);
                    OverView.setText("OverView" + "\n" + overview);
                    ReleaseDate.setText("Date:"+"\n"+date);

                }

            }

            return rootView;
        }

        private Intent CreateShareMovieIntent()
        {
            Intent ShareIntent =new Intent(Intent.ACTION_SEND);
            ShareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            ShareIntent.setType("text/pain");

            ShareIntent.putExtra(Intent.EXTRA_TEXT,MmoviesTitle+MOVIE_SHARE);
            return  ShareIntent;
        }
    }
}
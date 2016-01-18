package movieapp.android.come.movieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

        private detailsAdapter mDetailAdapter;
        private ArrayList<reviewModel> mReviewData;
        private ArrayList<trailerModel> mTrailerData;
        private ListView detailsList;


        private ArrayList<String> trailersData;
        private ArrayAdapter adperTrailer;

        private String T_id="";

        private ArrayList<String> reviewsData;
        private ArrayAdapter adapterReviews;
        private ListView listReivews;



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
            container.removeAllViews();

            View rootView = inflater.inflate(R.layout.fragment_detailed, container, false);
            int layoutId = -1;
            mReviewData = new ArrayList<>();
            mTrailerData = new ArrayList<>();
            detailsList = (ListView)rootView. findViewById(R.id.list_details);


            View header = inflater.inflate(R.layout.details_header, null, false);

            TextView Title= (TextView) header.findViewById(R.id.DetailedTitle);
            ImageView Imag= (ImageView) header.findViewById(R.id.DetailedImage);

            TextView Popularity= (TextView) header.findViewById(R.id.DetailedPoularity);
            TextView ReleaseDate= (TextView) header.findViewById(R.id.ReleaseDate);
            TextView OverView= (TextView) header.findViewById(R.id.DetailedOverview);






            Intent intent=getActivity().getIntent();
            if(intent!=null)
            {
                if(intent.hasExtra("img"))
                {
                    String imgString=intent.getStringExtra("img");
                    String IMG_URL="http://image.tmdb.org/t/p/w180"+imgString;
                    String title= intent.getStringExtra("title");
                    MmoviesTitle=title;
                    String overview=intent.getStringExtra("overview");
                    String date=intent.getStringExtra("Rdate");
                    String popularity=intent.getStringExtra("popularity");
                     T_id=intent.getStringExtra("ID");
                    //new fetchTrailers().execute("T_id");


                    Picasso.with(getContext()).load(imgString).into(Imag);
                    Title.setText(title);
                    Popularity.setText("popularity:"  + popularity);
                    OverView.setText("OverView" +"\n"+ overview);
                    ReleaseDate.setText("Date:"+date);

                    detailsList.addHeaderView(header, null, false);
                }
            }
            new fetchTrailers().execute(T_id);
            new fetchReviews().execute(T_id);

            return rootView;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);





           // new fetchTrailers().execute(T_id);
        }

        @Override
        public void onStart() {
            super.onStart();

        }


        //classe for athynic task fr handle reterive data
        public class fetchTrailers extends AsyncTask<String,Void,Integer>
        {
            Integer result =0;
            private final String LOG_TAG=MainActivityFragment.class.getSimpleName();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


            }

            @Override
            protected Integer doInBackground(String... params) {


                HttpURLConnection urlConnection=null;
                BufferedReader reader=null;
                String MovieJasonStr=null;


                try
                {
                    final String API_KEY="api_key";
                    String api_key="f1d1772fa7a2e66b446efb99a1512dcc";

                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https")
                            .authority("api.themoviedb.org")
                            .appendPath("3")
                            .appendPath("movie")
                            .appendPath(params[0])
                            .appendPath("videos")
                            .appendQueryParameter(API_KEY, api_key).build();

                    URL url=new URL(builder.toString());
                    Log.v(LOG_TAG,"BUILD URL"+builder.toString());


                    urlConnection= (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    //read input string into string
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer=new StringBuffer();
                    if(inputStream==null)
                    {
                        return null;

                    }

                    reader =new BufferedReader(new InputStreamReader(inputStream));

                    String line;

                    while ((line=reader.readLine())!=null)
                    {
                        buffer.append(line+"\n");
                    }

                    if(buffer.length()==0)
                    {
                        //Stream empty
                        return null;
                    }

                    MovieJasonStr=buffer.toString();
                    GetMoviesDataFromJson(MovieJasonStr);
                    result=1;
                    // Log.v(LOG_TAG,"Movies String "+MovieJasonStr);
                }catch(Exception e)
                {
                    Log.e(LOG_TAG,"Error"+e);
                }finally {
                    if(urlConnection!=null)
                    {
                        urlConnection.disconnect();
                    }

                    if(reader!=null)
                    {
                        try{
                            reader.close();
                        }catch (final IOException IO)
                        {

                            Log.e(LOG_TAG,"error Closing Stream"+IO);
                        }
                    }
                }


                return result;
            }


            private void GetMoviesDataFromJson(String MovieJasonStr)
                    throws JSONException

            {
                try {
                    // These are the names of the JSON objects that need to be extracted.
                    final String LIST = "results";
                    final String KEY = "key";
                    final String NAME = "name";
                    final String ID="id";





                    JSONObject JsonObj = new JSONObject(MovieJasonStr);
                    JSONArray MoviesArray = JsonObj.getJSONArray(LIST);
                    trailerModel item;
                    String ImgExtra ;


                    for (int i = 0; i < MoviesArray.length(); i++) {
                        item = new trailerModel();
                        JSONObject dataObj = MoviesArray.getJSONObject(i);

                        String key = dataObj.getString(KEY);
                        String trailer_name = dataObj.getString(NAME);
                        String id=dataObj.getString(ID);


                        item.setId(id);
                        item.setKey(key);
                        item.setType("trailer");
                        item.setName(trailer_name);
                        mTrailerData.add(item);

                    }
                }catch (JSONException  e)
                {
                    e.printStackTrace();
                }



            }



            @Override
            protected void onPostExecute(Integer result) {

                // Download complete. Lets update UI

                if (result == 1) {

                    //prgDialog.cancel();
                   // mDetailAdapter = new detailsAdapter(getActivity(),0, mDetailData);
                   // detailsList.setAdapter(mDetailAdapter);
                    //mDetailData.clear();
                    //mDetailAdapter.setGridData(mDetailData);

                } else {
                    Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
                }

                //Hide progressbar
                //mProgressBar.setVisibility(View.GONE);
            }



        }




        //classe for athynic task fr handle reterive data
        public class fetchReviews extends AsyncTask<String,Void,Integer>
        {
            Integer result =0;
            private final String LOG_TAG=MainActivityFragment.class.getSimpleName();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Integer doInBackground(String... params) {


                HttpURLConnection urlConnection=null;
                BufferedReader reader=null;
                String MovieJasonStr=null;


                try
                {
                    final String API_KEY="api_key";
                    String api_key="f1d1772fa7a2e66b446efb99a1512dcc";

                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https")
                            .authority("api.themoviedb.org")
                            .appendPath("3")
                            .appendPath("movie")
                            .appendPath(params[0])
                            .appendPath("reviews")
                            .appendQueryParameter(API_KEY, api_key).build();

                    URL url=new URL(builder.toString());
                    Log.v(LOG_TAG,"BUILD URL"+builder.toString());


                    urlConnection= (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    //read input string into string
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer=new StringBuffer();
                    if(inputStream==null)
                    {
                        return null;

                    }

                    reader =new BufferedReader(new InputStreamReader(inputStream));

                    String line;

                    while ((line=reader.readLine())!=null)
                    {
                        buffer.append(line+"\n");
                    }

                    if(buffer.length()==0)
                    {
                        //Stream empty
                        return null;
                    }

                    MovieJasonStr=buffer.toString();
                    GetMoviesDataFromJson(MovieJasonStr);
                    result=1;
                    // Log.v(LOG_TAG,"Movies String "+MovieJasonStr);
                }catch(Exception e)
                {
                    Log.e(LOG_TAG,"Error"+e);
                }finally {
                    if(urlConnection!=null)
                    {
                        urlConnection.disconnect();
                    }

                    if(reader!=null)
                    {
                        try{
                            reader.close();
                        }catch (final IOException IO)
                        {

                            Log.e(LOG_TAG,"error Closing Stream"+IO);
                        }
                    }
                }


                return result;
            }


            private void GetMoviesDataFromJson(String MovieJasonStr)
                    throws JSONException

            {
                try {
                    // These are the names of the JSON objects that need to be extracted.
                    final String LIST = "results";
                    final String AUTHOR = "author";
                    final String CONTENT = "content";
                    final String ID="id";


                    JSONObject JsonObj = new JSONObject(MovieJasonStr);
                    JSONArray MoviesArray = JsonObj.getJSONArray(LIST);
                    reviewModel item;
                    String ImgExtra ;


                    for (int i = 0; i < MoviesArray.length(); i++) {
                        item = new reviewModel();
                        JSONObject dataObj = MoviesArray.getJSONObject(i);

                        String author = dataObj.getString(AUTHOR);
                        String content = dataObj.getString(CONTENT);
                        String id=dataObj.getString(ID);

                        item.setContent(content);
                        item.setAuthor(author);
                        item.setType("reveiw");
                        mReviewData.add(item);

                    }
                }catch (JSONException  e)
                {
                    e.printStackTrace();
                }



            }



            @Override
            protected void onPostExecute(Integer result) {

                // Download complete. Lets update UI

                if (result == 1) {
                    for (trailerModel obj:mTrailerData) {

                        Log.i("Object data name   ",obj.getName()+" object key   "+obj.getKey());


                    }

                    for (reviewModel obj:mReviewData) {

                        Log.i("Review author name   ",obj.getAuthor());


                    }

                    if(mDetailAdapter ==null) {
                        detailsList.setAdapter(new detailsAdapter(getActivity(), mTrailerData, mReviewData));
                    }
                    // mDetailData.clear();
                    //prgDialog.cancel();
                    //mDetailAdapter.setGridData(mDetailData);

                } else {
                    Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
                }

                //Hide progressbar
                //mProgressBar.setVisibility(View.GONE);
            }



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
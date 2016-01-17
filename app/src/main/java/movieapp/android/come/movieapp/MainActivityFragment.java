package movieapp.android.come.movieapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
public class MainActivityFragment extends Fragment {
    GridView grid;

    private GridView mGridView;
    private ProgressBar mProgressBar;
    private ProgressDialog prgDialog;


    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;

   public  MainActivityFragment()
   {

   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        prgDialog=new ProgressDialog(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.movie, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.action_refresh)
        {
            UpdateMovie();

            return true;
        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView)rootView. findViewById(R.id.gridView);
        mProgressBar = (ProgressBar)rootView. findViewById(R.id.progressBar);


        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.gridview_item, mGridData);
        mGridView.setAdapter(mGridAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridItem item= (GridItem) parent.getItemAtPosition(position);
                GridItem SelectedItem=mGridAdapter.getItem(position);
                Intent intent=new Intent(getActivity(),DetailedActivity.class);

                intent.putExtra("title",SelectedItem.getTitle() );
                intent.putExtra("img",SelectedItem.getImage());
                intent.putExtra("overview",SelectedItem.getOverview());
                intent.putExtra("Rdate",SelectedItem.getReleaseDate());
                intent.putExtra("popularity",SelectedItem.getPopularity());
                intent.putExtra("ID",SelectedItem.getId());
                startActivity(intent);

            }
        });



        return  rootView;
    }


    public void UpdateMovie()
    {
        FetchMovies MoviesTask= new FetchMovies();

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort=prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_avarege));
        MoviesTask.execute(sort);
        prgDialog.cancel();
       // mProgressBar.setVisibility(View.VISIBLE);

    }


    @Override
    public void onResume() {
        super.onResume();
        UpdateMovie();
    }

    @Override
    public void onStart() {

        super.onStart();

        UpdateMovie();

    }



    //classe for athynic task fr handle reterive data
    public class FetchMovies extends AsyncTask<String,Void,Integer>
    {
        Integer result =0;
        private final String LOG_TAG=MainActivityFragment.class.getSimpleName();

        @Override
        protected void onPreExecute() {

            prgDialog.setMessage("Loading....");
            prgDialog.show();


            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {


            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;
            String MovieJasonStr=null;

             String format="json";
             String sort_by="vote_average.desc";
             String include_video="true";
             String api_key="f1d1772fa7a2e66b446efb99a1512dcc";


            try
            {
                final  String MOVIE_URL="http://api.themoviedb.org/3/discover/movie?";
                final String QUERY_PARAM="sort_by";
                final String FORMAT_PARAM="format";
                final String SORT_PARAM="sort_by";
                final  String INCLUDE_PARAM="include_video";
                final String API_KEY="api_key";

                Uri buildUri = Uri.parse(MOVIE_URL).buildUpon().
                        appendQueryParameter(QUERY_PARAM,params[0]).
                        appendQueryParameter(FORMAT_PARAM, format).
                        appendQueryParameter(INCLUDE_PARAM,include_video).
                        appendQueryParameter(API_KEY,api_key).build();

                URL url=new URL(buildUri.toString());
                Log.v(LOG_TAG,"BUILD URL"+buildUri.toString());


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
              final String IMGPATH = "poster_path";
              final String OVERVIEW = "overview";
              final String TITLE = "title";
              final String POPULATRIY = "popularity";
              final String AVERAGE = "vote_average";
              final String RELEASE_DATE="release_date";
              final String ID="id";


              JSONObject JsonObj = new JSONObject(MovieJasonStr);
              JSONArray MoviesArray = JsonObj.getJSONArray(LIST);
              GridItem item;
              String ImgExtra ;


              for (int i = 0; i < MoviesArray.length(); i++) {
                  item = new GridItem();
                  JSONObject dataObj = MoviesArray.getJSONObject(i);

                  String title = dataObj.getString(TITLE);
                  String OverView = dataObj.getString(OVERVIEW);
                  String Popularity = dataObj.getString(POPULATRIY);
                  String average=dataObj.getString(AVERAGE);
                  String date=dataObj.getString(RELEASE_DATE);
                  String Img = dataObj.getString(IMGPATH);
                  String id=dataObj.getString(ID);

                  item.setTitle(title);
                  item.setImage("http://image.tmdb.org/t/p/w185" + Img);
                  item.setOverview(OverView);
                  item.setPopularity(Popularity);
                  item.setReleaseDate(date);
                  item.setAverage(average);
                  item.setId(id);
                  mGridData.add(item);

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
                    mGridAdapter.setGridData(mGridData);
                } else {
                    Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
                }

                //Hide progressbar
                //mProgressBar.setVisibility(View.GONE);
            }



        }



    }



package movieapp.android.come.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
   // private boolean mTwoPane;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private String mSort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSort = Utility.getPreferredSort(this);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.Movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            //mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                MainActivityFragment.towwplay=true;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Movie_detail_container, new DetailedFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
           // mTwoPane = false;
            getSupportActionBar().setElevation(0f);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_container, new MainActivityFragment(), DETAILFRAGMENT_TAG)
                    .commit();
            MainActivityFragment.towwplay=false;

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;

}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.action_settings)
        {
            Intent intent= new Intent(this,SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        String sort = Utility.getPreferredSort(this);
        // update the location in our second pane using the fragment manager
        if (sort != null && !sort.equals(mSort)) {
            MainActivityFragment ff = (MainActivityFragment)getSupportFragmentManager().findFragmentById(R.id.movie_container);
            if ( null != ff ) {
                ff.onLocationChanged();
            }
            DetailedFragment df = (DetailedFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if ( null != df ) {
               // df.onLocationChanged(sort);
            }
            mSort = sort;
        }
    }
}

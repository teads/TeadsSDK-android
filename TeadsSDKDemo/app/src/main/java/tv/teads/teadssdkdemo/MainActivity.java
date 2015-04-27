package tv.teads.teadssdkdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.teads.teadssdkdemo.format.InFlowFragment;
import tv.teads.teadssdkdemo.format.InSwipeViewPagerFragment;
import tv.teads.teadssdkdemo.format.inboard.InBoardListViewFragment;
import tv.teads.teadssdkdemo.format.inboard.InBoardScrollViewFragment;
import tv.teads.teadssdkdemo.format.inboard.InBoardWebViewFragment;
import tv.teads.teadssdkdemo.format.inread.InReadListViewFragment;
import tv.teads.teadssdkdemo.format.inread.InReadScrollViewFragment;
import tv.teads.teadssdkdemo.format.inread.InReadWebViewFragment;


public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MainActivity";

    public static final String SHAREDPREF_PID = "sp_pid";
    public static final String SHAREDPREF_PID_DEFAULT = "27695";

    private Toolbar                     mToolbar;
    private ActionBarDrawerToggle       mDrawerToggle;
    private DrawerLayout                mDrawerLayout;

    /**
     * Prevent fragment that the navigation drawer has been opened or closed
     */
    public DrawerLayout.DrawerListener  mDrawerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        Window window = getWindow();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.primaryDarkDef));

        } else if(android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(false);
            tintManager.setTintColor(getResources().getColor(R.color.primaryDef));
        }

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MainFragment fragment = new MainFragment();
            transaction.replace(R.id.fragment_container, fragment, MainFragment.class.getSimpleName());
            transaction.commit();
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,  R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                if(mDrawerListener != null){
                    mDrawerListener.onDrawerClosed(mDrawerLayout);
                }

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if(mDrawerListener != null){
                    mDrawerListener.onDrawerOpened(mDrawerLayout);
                }

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    /**
     * Return the pid, if not one is set, the default one
     * @param context current context
     * @return pid
     */
    public String getPid(Context context){
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(
                    SHAREDPREF_PID, SHAREDPREF_PID_DEFAULT);
    }

    private void changeFragment(Fragment frag){

        String backStateName =  ((Object)frag).getClass().getName();

        try {
            FragmentManager manager = getSupportFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

            if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null){
                //fragment not in back stack, create it.
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, frag, ((Object) frag).getClass().getName());
                transaction.addToBackStack(backStateName);
                transaction.commit();
                // Close drawer
                mDrawerLayout.closeDrawer(Gravity.START);
            } else {
                // custom effect if fragment is already instanciateddrawer
                mDrawerLayout.closeDrawer(Gravity.START);
            }
        } catch(IllegalStateException exception){
            Log.e(LOG_TAG, "Unable to commit fragment, could be activity as been killed in background. " + exception.toString());
        }
    }

    public DrawerLayout.DrawerListener getDrawerListener() {
        return mDrawerListener;
    }

    public void setDrawerListener(DrawerLayout.DrawerListener drawerListener) {
        mDrawerListener = drawerListener;
    }

    /*----------------------------------------
    * NavigationDrawer button listener
    */

    @OnClick(R.id.inread_scrollview)
    public void inReadScrollView() {
        changeFragment(new InReadScrollViewFragment());
    }

    @OnClick(R.id.inread_listview)
    public void inReadListView() {
        changeFragment(new InReadListViewFragment());

    }
    @OnClick(R.id.inread_webview)
    public void inReadWebView() {
        changeFragment(new InReadWebViewFragment());
    }

    @OnClick(R.id.inboard_scrollview)
    public void inBoardScrollView() {
        changeFragment(new InBoardScrollViewFragment());

    }
    @OnClick(R.id.inboard_listview)
    public void inBoardListView() {
        changeFragment(new InBoardListViewFragment());

    }
    @OnClick(R.id.inboard_webview)
    public void inBoardWebView() {
        changeFragment(new InBoardWebViewFragment());

    }

    @OnClick(R.id.inswipe_viewpager)
    public void inSwipeViewPager() {
        changeFragment(new InSwipeViewPagerFragment());

    }

    @OnClick(R.id.inflow)
    public void inFlowBasic() {
        changeFragment(new InFlowFragment());

    }


    @OnClick(R.id.action_pid)
    public void changePidDialog() {
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setText(getPid(this));

        new AlertDialog.Builder(this)
                .setTitle("Pid")
                .setMessage("Change saved pid")
                .setView(input)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String pidString = input.getText().toString();
                        if (pidString == null || pidString.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Setting default pid", Toast.LENGTH_SHORT).show();
                            pidString = SHAREDPREF_PID_DEFAULT;
                        }
                        PreferenceManager
                                .getDefaultSharedPreferences(MainActivity.this)
                                .edit()
                                .putString(
                                        SHAREDPREF_PID,
                                        pidString)
                                .apply();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();

    }

}

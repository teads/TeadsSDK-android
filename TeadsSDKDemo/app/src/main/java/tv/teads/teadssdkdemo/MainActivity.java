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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.otto.Subscribe;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.teads.sdk.publisher.TeadsLog;
import tv.teads.teadssdkdemo.format.inread.InReadListViewFragment;
import tv.teads.teadssdkdemo.format.inread.InReadRecyclerViewFragment;
import tv.teads.teadssdkdemo.format.inread.InReadScrollViewFragment;
import tv.teads.teadssdkdemo.format.inread.InReadWebViewFragment;
import tv.teads.teadssdkdemo.format.inreadTop.InReadTopListViewFragment;
import tv.teads.teadssdkdemo.format.inreadTop.InReadTopRecyclerViewFragment;
import tv.teads.teadssdkdemo.format.inreadTop.InReadTopScrollViewFragment;
import tv.teads.teadssdkdemo.format.inreadTop.InReadTopWebViewFragment;
import tv.teads.teadssdkdemo.utils.AdViewSampleChooserFragment;
import tv.teads.teadssdkdemo.utils.BusProvider;
import tv.teads.teadssdkdemo.utils.ReloadEvent;
import tv.teads.teadssdkdemo.utils.event.ChangeFragmentEvent;


public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public static final String LOG_TAG = "MainActivity";

    public static final String SHAREDPREF_PID             = "sp_pid";
    public static final String SHAREDPREF_WEBVIEWURL      = "sp_wvurl";
    public static final String SHAREDPREF_ENDSCREEN_MODE  = "sp_endscreen";
    public static final String SHAREDPREF_PID_DEFAULT     = "54934";
    public static final String SHAREDPREF_WEBVIEW_DEFAULT = "https://fr.m.wikipedia.org/wiki/Mer_Noire";

    private DrawerLayout                mDrawerLayout;
    private DrawerLayout.DrawerListener mDrawerListener;

    @Bind(R.id.action_endscreen_mode)
    protected SwitchCompat mEndScreenModeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Window window = getWindow();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.primaryDarkDef));

        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                if (mDrawerListener != null) {
                    mDrawerListener.onDrawerClosed(mDrawerLayout);
                }

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (mDrawerListener != null) {
                    mDrawerListener.onDrawerOpened(mDrawerLayout);
                }

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // Preload one Ad from AdFactory
        TeadsLog.setLogLevel(TeadsLog.LogLevel.verbose);
        mEndScreenModeSwitch.setChecked(isEndScreenLightMode(this));
        mEndScreenModeSwitch.setOnCheckedChangeListener(this);
    }

    /**
     * Return the pid, if not one is set, the default one
     *
     * @param context current context
     * @return pid
     */
    public String getPid(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(
                        SHAREDPREF_PID, SHAREDPREF_PID_DEFAULT);
    }

    /**
     * Return the Webview url, if not one is set, the default one
     *
     * @param context current context
     * @return an url
     */
    public String getWebViewUrl(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(
                        SHAREDPREF_WEBVIEWURL, SHAREDPREF_WEBVIEW_DEFAULT);
    }

    /**
     * Return the end screen mode
     *
     * @param context current context
     * @return true if the end screen is in {@link tv.teads.sdk.publisher.TeadsConfiguration#LIGHT_MODE},
     * false if the end screen is in {@link tv.teads.sdk.publisher.TeadsConfiguration#DARK_MODE}
     */
    public boolean isEndScreenLightMode(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(
                        SHAREDPREF_ENDSCREEN_MODE, false);
    }

    private void changeFragment(Fragment frag) {

        String backStateName = ((Object) frag).getClass().getName();

        try {
            FragmentManager manager = getSupportFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

            if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
                //fragment not in back stack, create it.
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, frag, ((Object) frag).getClass().getName());
                transaction.addToBackStack(backStateName);
                transaction.commit();
                // Close drawer
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                // custom effect if fragment is already instanciated
                mDrawerLayout.closeDrawer(GravityCompat.START);
                EventBus.getDefault().post(new ReloadEvent());

            }
        } catch (IllegalStateException exception) {
            Log.e(LOG_TAG, "Unable to commit fragment, could be activity as been killed in background. " +
                    exception.toString());
        }
    }

    public void setDrawerListener(DrawerLayout.DrawerListener drawerListener) {
        mDrawerListener = drawerListener;
    }

    @Override
    public void onResume() {
        super.onResume();

        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onPostFragmentChangeEvent(ChangeFragmentEvent event) {
        changeFragment(event.mFragment);
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

    @OnClick(R.id.inread_recyclerview)
    public void inReadRecyclerView() {
        changeFragment(new InReadRecyclerViewFragment());
    }

    @OnClick(R.id.inread_webview)
    public void inReadWebView() {
        changeFragment(new InReadWebViewFragment());
    }


    @OnClick(R.id.inreadtop_scrollview)
    public void inReadTopScrollView() {
        changeFragment(new InReadTopScrollViewFragment());
    }

    @OnClick(R.id.inreadtop_listview)
    public void inReadTopListView() {
        changeFragment(new InReadTopListViewFragment());
    }

    @OnClick(R.id.inreadtop_recyclerview)
    public void inReadTopRecyclerView() {
        changeFragment(new InReadTopRecyclerViewFragment());
    }

    @OnClick(R.id.inreadtop_webview)
    public void inReadTopWebView() {
        changeFragment(new InReadTopWebViewFragment());
    }

    @OnClick(R.id.adview)
    public void videoViewChooser() {
        changeFragment(new AdViewSampleChooserFragment());
    }

    @OnClick(R.id.action_pid)
    public void changePidDialog() {
        // Set an EditText view to get user input
        View view = getLayoutInflater().inflate(R.layout.dialog_pid_content, null);
        final EditText input = (EditText) view.findViewById(R.id.pid_input);
        input.setText(getPid(this));
        input.setLines(1);
        input.setSingleLine(true);

        new AlertDialog.Builder(this)
                .setTitle("Pid")
                .setMessage("Change saved pid")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String pidString = input.getText().toString();
                        if (pidString.isEmpty()) {
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

    @OnClick(R.id.action_webviewurl)
    public void changeWebviewUrlDialog() {
        // Set an EditText view to get user input
        View view = getLayoutInflater().inflate(R.layout.dialog_pid_content, null);
        final EditText input = (EditText) view.findViewById(R.id.pid_input);
        input.setText(getWebViewUrl(this));

        new AlertDialog.Builder(this)
                .setTitle("WebView Url")
                .setMessage("Change WebView url")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String pidString = input.getText().toString();
                        if (pidString.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Setting default webview url", Toast.LENGTH_SHORT).show();
                            pidString = SHAREDPREF_WEBVIEW_DEFAULT;
                        }
                        PreferenceManager
                                .getDefaultSharedPreferences(MainActivity.this)
                                .edit()
                                .putString(
                                        SHAREDPREF_WEBVIEWURL,
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        PreferenceManager
                .getDefaultSharedPreferences(MainActivity.this)
                .edit()
                .putBoolean(
                        SHAREDPREF_ENDSCREEN_MODE,
                        isChecked)
                .apply();
    }
}

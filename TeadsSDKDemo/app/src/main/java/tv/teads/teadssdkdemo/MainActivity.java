package tv.teads.teadssdkdemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.teads.teadssdkdemo.format.custom.CustomAdRecyclerViewFragment;
import tv.teads.teadssdkdemo.format.custom.CustomAdScrollViewFragment;
import tv.teads.teadssdkdemo.format.custom.CustomAdWebViewFragment;
import tv.teads.teadssdkdemo.format.example.ExampleFragment;
import tv.teads.teadssdkdemo.format.inread.InReadRecyclerViewFragment;
import tv.teads.teadssdkdemo.format.inread.InReadRepeatableRecyclerViewFragment;
import tv.teads.teadssdkdemo.format.inread.InReadScrollViewFragment;
import tv.teads.teadssdkdemo.format.inread.InReadWebViewFragment;
import tv.teads.teadssdkdemo.utils.ReloadEvent;
import tv.teads.teadssdkdemo.utils.event.ChangeFragmentEvent;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    private static final String INTENT_EXTRA_PID           = "ext_pid";
    private static final String SHAREDPREF_PID             = "sp_pid";
    private static final String SHAREDPREF_WEBVIEWURL      = "sp_wvurl";
    private static final int    SHAREDPREF_PID_DEFAULT     = 84242;
    private static final String SHAREDPREF_WEBVIEW_DEFAULT = "http://sample.teads.net/demo/sdk/demo.html";

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onNewIntent(Intent intent) {
        if (!TextUtils.isEmpty(intent.getStringExtra(INTENT_EXTRA_PID))){
            PreferenceManager
                    .getDefaultSharedPreferences(MainActivity.this)
                    .edit()
                    .putInt(
                            SHAREDPREF_PID,
                            Integer.parseInt(intent.getStringExtra(INTENT_EXTRA_PID)))
                    .apply();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (!TextUtils.isEmpty(getIntent().getStringExtra(INTENT_EXTRA_PID))){
            PreferenceManager
                    .getDefaultSharedPreferences(MainActivity.this)
                    .edit()
                    .putInt(
                            SHAREDPREF_PID,
                            Integer.parseInt(getIntent().getStringExtra(INTENT_EXTRA_PID)))
                    .apply();
        }

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MainFragment        fragment    = new MainFragment();
            transaction.replace(R.id.fragment_container, fragment, MainFragment.class.getSimpleName());
            transaction.commit();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.accent, null));
        } else {
            //noinspection deprecation
            toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        }
        mDrawerLayout = findViewById(R.id.drawer_layout);

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

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    /**
     * Return the pid, if not one is set, the default one
     *
     * @param context current context
     * @return pid
     */
    public int getPid(Context context) {
        return PreferenceManager
                       .getDefaultSharedPreferences(context)
                       .getInt(
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


    private void changeFragment(Fragment frag) {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass() == frag.getClass()) {
            closeDrawerAndReloadAd();
            return;
        }

        String backStateName = ((Object) frag).getClass().getName();

        try {
            FragmentManager manager = getSupportFragmentManager();
            //fragment not in back stack, create it.
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container, frag, ((Object) frag).getClass().getName());
            transaction.addToBackStack(backStateName);
            transaction.commit();
            // Close drawer
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } catch (IllegalStateException exception) {
            Log.e(LOG_TAG, "Unable to commit fragment, could be activity as been killed in background. " +
                                   exception.toString());
        }
    }

    private void closeDrawerAndReloadAd() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        EventBus.getDefault().post(new ReloadEvent());
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    @SuppressWarnings("unused")
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

    @OnClick(R.id.inread_recyclerview)
    public void inReadRecyclerView() {
        changeFragment(new InReadRecyclerViewFragment());
    }

    @OnClick(R.id.inread_repeatable_recyclerview)
    public void inReadRepeatableRecyclerView() {
        changeFragment(new InReadRepeatableRecyclerViewFragment());
    }

    @OnClick(R.id.inread_webview)
    public void inReadWebView() {
        changeFragment(new InReadWebViewFragment());
    }

    @OnClick(R.id.custom_scrollview)
    public void inReadTopScrollView() {
        changeFragment(new CustomAdScrollViewFragment());
    }

    @OnClick(R.id.custom_recyclerview)
    public void inReadTopRecyclerView() {
        changeFragment(new CustomAdRecyclerViewFragment());
    }

    @OnClick(R.id.custom_webview)
    public void inReadTopWebView() {
        changeFragment(new CustomAdWebViewFragment());
    }

    @OnClick(R.id.exampleButton)
    public void example() {
        changeFragment(new ExampleFragment());
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.action_pid)
    public void changePidDialog() {
        // Set an EditText view to get user input
        @SuppressLint("InflateParams") View view  = getLayoutInflater().inflate(R.layout.dialog_pid_content, null);
        final EditText                      input = view.findViewById(R.id.pidEditText);
        input.setText(Integer.toString(getPid(this)));
        input.setLines(1);
        input.setSingleLine(true);

        new AlertDialog.Builder(this)
                .setTitle("Pid")
                .setMessage("Change saved pid")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String pidString = input.getText().toString();
                        int    pid;
                        if (pidString.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Setting default pid", Toast.LENGTH_SHORT).show();
                            pid = SHAREDPREF_PID_DEFAULT;
                        } else {
                            pid = Integer.parseInt(pidString);
                        }
                        PreferenceManager
                                .getDefaultSharedPreferences(MainActivity.this)
                                .edit()
                                .putInt(
                                        SHAREDPREF_PID,
                                        pid)
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
        @SuppressLint("InflateParams") View view  = getLayoutInflater().inflate(R.layout.dialog_webview_content, null);
        final EditText                      input = view.findViewById(R.id.webViewEditText);
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
}

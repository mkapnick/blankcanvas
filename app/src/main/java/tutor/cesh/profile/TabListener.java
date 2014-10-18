// TabListenr class for managing user interaction with the ActionBar tabs. The
// application context is passed in pass it in constructor, needed for the
// toast.

package tutor.cesh.profile;

//need to use the android.support.v4 libraries !!
import android.R;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

class TabListener implements ActionBar.TabListener {
    public Fragment fragment;
    public Context context;
    public String tabName;

    public TabListener(Fragment fragment, Context context, String tabName) {
        this.fragment   = fragment;
        this.context    = context;
        this.tabName    = tabName;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        Toast.makeText(context, "SELECTED", Toast.LENGTH_SHORT).show();
        ft.replace(R.id.content, fragment); //not sure what this does
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        Toast.makeText(context, "Unselected!", Toast.LENGTH_SHORT).show();
        ft.remove(fragment);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        Toast.makeText(context, "Reselected!", Toast.LENGTH_SHORT).show();

    }
}
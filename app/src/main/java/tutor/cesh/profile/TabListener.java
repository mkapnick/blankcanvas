// TabListenr class for managing user interaction with the ActionBar tabs. The
// application context is passed in pass it in constructor, needed for the
// toast.

package tutor.cesh.profile;

//need to use the android.support.v4 libraries !!

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;

import tutor.cesh.R;

class TabListener implements ActionBar.TabListener
{
    public Fragment     fragment;
    public Context      context;
    public String       tabName;
    public View         actionBarCustomView;

    public TabListener(Fragment fragment, Context context, String tabName, View view) {
        this.fragment               = fragment;
        this.context                = context;
        this.tabName                = tabName;
        this.actionBarCustomView    = view;

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
    {
        TextView textView;
        ft.replace(android.R.id.content, fragment); //not sure what this does

        textView = (TextView) this.actionBarCustomView.findViewById(R.id.actionBarTitle);

        if(this.tabName.equals("student")){
            tab.setIcon(tutor.cesh.R.drawable.student_dark);
            textView.setText("Student Profile");
        }
        else if (this.tabName.equals("tutor")){
            tab.setIcon(tutor.cesh.R.drawable.tutor_dark);
            textView.setText("Tutor Profile");
        }

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.remove(fragment);
        if(this.tabName.equals("student")){
            tab.setIcon(tutor.cesh.R.drawable.student_light);
        }
        else if (this.tabName.equals("tutor")){
            tab.setIcon(tutor.cesh.R.drawable.tutor_light);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
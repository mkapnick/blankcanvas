package tutor.cesh.profile.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import tutor.cesh.R;

/**
 * Created by michaelk18 on 10/12/14.
 */
public class StudentProfileFragment extends Fragment
{

    public static ImageView profileImageView, coverImageView;
    private EditText name, major, year, about, classes;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //view containing the fragments UI
        return inflater.inflate(R.layout.activity_student_profile, container, false);
    }

}

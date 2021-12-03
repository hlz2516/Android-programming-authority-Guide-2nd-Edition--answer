package top.normal_hu.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import top.normal_hu.criminalintent.CrimeListFragment;
import top.normal_hu.criminalintent.R;

public class TCrimeListActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcrime_list);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container1);

        if (fragment == null){
            fragment = new CrimeListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container1,fragment)
                    .commit();
        }
    }
}